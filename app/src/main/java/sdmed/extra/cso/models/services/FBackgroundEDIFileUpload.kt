package sdmed.extra.cso.models.services

import android.content.Context
import org.kodein.di.instance
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseService
import sdmed.extra.cso.interfaces.repository.IAzureBlobRepository
import sdmed.extra.cso.interfaces.repository.ICommonRepository
import sdmed.extra.cso.interfaces.repository.IEDIListRepository
import sdmed.extra.cso.models.common.EDIAzureQueueModel
import sdmed.extra.cso.models.common.NotifyIndex
import sdmed.extra.cso.models.common.QueueLockModel
import sdmed.extra.cso.models.common.EDIFileResultQueueModel
import sdmed.extra.cso.models.common.EDISASKeyQueueModel
import sdmed.extra.cso.models.eventbus.EDIUploadEvent
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.utils.FImageUtils
import java.util.UUID

class FBackgroundEDIFileUpload(applicationContext: Context): FBaseService(applicationContext)  {
    val mqttService: FMqttService by di.instance(FMqttService::class)
    val notificationService: FNotificationService by di.instance(FNotificationService::class)
    val commonRepository: ICommonRepository by di.instance(ICommonRepository::class)
    val azureBlobRepository: IAzureBlobRepository by di.instance(IAzureBlobRepository::class)
    val ediListRepository: IEDIListRepository by di.instance(IEDIListRepository::class)
    private val sasKeyQ = QueueLockModel<EDISASKeyQueueModel>("sasQ ${FExtensions.getToday().toString("yyyyMMdd_HHmmss")} ${UUID.randomUUID()}")
    private val azureQ = QueueLockModel<EDIAzureQueueModel>("azureQ ${FExtensions.getToday().toString("yyyyMMdd_HHmmss")} ${UUID.randomUUID()}")
    private val resultQ = QueueLockModel<EDIFileResultQueueModel>("resultQ ${FExtensions.getToday().toString("yyyyMMdd_HHmmss")} ${UUID.randomUUID()}")
    private var resultQRun = false
    fun sasKeyEnqueue(data: EDISASKeyQueueModel) = sasKeyQ.enqueue(data, true, { sasKeyThreadStart() })
    private fun azureEnqueue(data: EDIAzureQueueModel) = azureQ.enqueue(data, true, { azureThreadStart() })
    private fun resultEnqueue(data: EDIFileResultQueueModel) {
        resultQ.locking()
        val findBuff = resultQ.findQ(false, { it.uuid == data.uuid })
        if (findBuff == null) {
            resultQ.enqueue(data, false)
        } else {
            findBuff.appendItemPath(data.currentMedia, data.itemIndex)
        }
        resultThreadStart(resultQRun)
        resultQRun = true
        resultQ.unlocking()
    }
    private fun resultDequeue(): EDIFileResultQueueModel {
        resultQ.locking()
        val ret: EDIFileResultQueueModel
        val retBuff = resultQ.findQ(false, { it.readyToSend() })
        if (retBuff == null) {
            ret = EDIFileResultQueueModel("-1")
        } else {
            ret = retBuff
            resultQ.removeQ(ret, false)
        }
        resultQ.unlocking()
        return ret
    }
    private fun resultBreak(uuid: String) {
        resultQ.locking()
        val retBuff = resultQ.findQ(false, { it.uuid == uuid})
        if (retBuff == null) {
            resultQ.unlocking()
            return
        }
        resultQ.removeQ(retBuff, false)
        resultQ.unlocking()
    }

    private fun sasKeyThreadStart() = sasKeyQ.threadStart {
        checkSASKeyQ(sasKeyQ.dequeue())
        Thread.sleep(100)
    }
    private fun azureThreadStart() = azureQ.threadStart {
        checkAzureQ(azureQ.dequeue())
        Thread.sleep(100)
    }
    private fun resultThreadStart(resultQRun: Boolean) = resultQ.threadStart({
        while (resultQ.isNotEmpty()) {
            postResultData(resultDequeue())
            Thread.sleep(100)
        }
        this.resultQRun = false
    }, resultQRun)

    private fun checkSASKeyQ(data: EDISASKeyQueueModel) {
        FCoroutineUtil.coroutineScope({
            for (pharma in data.ediUploadModel.pharmaList) {
                val uuid = UUID.randomUUID().toString()
                val blobName = data.blobName(context, pharma)
                val ret = commonRepository.postGenerateSasList(blobName.map { it.second })
                if (ret.result != true || ret.data == null) {
                    notificationService.sendNotify(context, NotifyIndex.EDI_FILE_UPLOAD, context.getString(R.string.edi_file_upload_fail), ret.msg ?: "")
                    notificationCall(context.getString(R.string.edi_file_upload_fail), ret.msg)
                    resultBreak(uuid)
                    return@coroutineScope
                }
                resultEnqueue(EDIFileResultQueueModel(uuid, pharma.ediPK, pharma.thisPK, itemIndex = -1, itemCount = pharma.uploadItems.value.size, ediUploadModel = data.ediUploadModel))
                ret.data?.forEachIndexed { index, x ->
                    val queue = EDIAzureQueueModel(uuid, pharma.ediPK, pharma.thisPK, mediaIndex = index).parse(pharma, blobName, x) ?: return@forEachIndexed
                    azureEnqueue(queue)
                }
                progressNotificationCall(uuid)
            }
        })
    }
    private fun checkAzureQ(data: EDIAzureQueueModel) {
        FCoroutineUtil.coroutineScope({
            data.media.mediaPath?.let { uri ->
                try {
                    val cachedFile = FImageUtils.uriToFile(context, uri, data.media.mediaName)
                    val ret = azureBlobRepository.upload(data.ediPharmaFileUploadModel.blobUrlKey(), cachedFile, data.ediPharmaFileUploadModel.mimeType)
                    FImageUtils.fileDelete(context, cachedFile)
                    if (ret.isSuccessful) {
                        resultEnqueue(EDIFileResultQueueModel(data.uuid, data.ediPK, data.ediPharmaPK, data.ediPharmaFileUploadModel, data.mediaIndex))
                    } else {
                        progressNotificationCall(data.uuid, true)
                        notificationCall(context.getString(R.string.edi_file_upload_fail))
                    }
                } catch (_: Exception) {
                    notificationCall(context.getString(R.string.edi_file_upload_fail))
                }
            }
        })
    }
    private fun postResultData(data: EDIFileResultQueueModel) {
        if (data.uuid == "-1") {
            return
        }
        FCoroutineUtil.coroutineScope({
            val ret = ediListRepository.postPharmaFile(data.ediPK, data.ediPharmaPK, data.medias)
            if (ret.result == true) {
                notificationCall(context.getString(R.string.edi_file_upload_comp), ediPK = data.ediPK)
                mqttService.mqttEDIFileAdd(data.ediPK, data.ediUploadModel.orgName)
            } else {
                notificationCall(context.getString(R.string.edi_file_upload_fail), ret.msg)
            }
            progressNotificationCall(data.uuid, true)
        })
    }

    private fun notificationCall(title: String, message: String? = null, ediPK: String = "") {
        notificationService.sendNotify(context, NotifyIndex.EDI_FILE_UPLOAD, title, message, FNotificationService.NotifyType.WITH_VIBRATE, true, ediPK)
    }
    private fun progressNotificationCall(uuid: String, isCancel: Boolean = false) {
        if (isCancel) {
            notificationService.progressUpdate(context, uuid, isCancel = true)
        } else {
            val title = context.getString(R.string.edi_file_upload)
            notificationService.makeProgressNotify(context, uuid, title)
        }
    }
}