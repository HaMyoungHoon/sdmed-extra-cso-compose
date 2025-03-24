package sdmed.extra.cso.models.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import org.greenrobot.eventbus.EventBus
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.DIContext
import org.kodein.di.diContext
import org.kodein.di.instance
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseService
import sdmed.extra.cso.bases.FMainApplication
import sdmed.extra.cso.interfaces.repository.IAzureBlobRepository
import sdmed.extra.cso.interfaces.repository.ICommonRepository
import sdmed.extra.cso.interfaces.repository.IQnAListRepository
import sdmed.extra.cso.models.common.NotifyIndex
import sdmed.extra.cso.models.common.QnAAzureQueueModel
import sdmed.extra.cso.models.common.QnAResultQueueModel
import sdmed.extra.cso.models.common.QueueLockModel
import sdmed.extra.cso.models.common.QnASASKeyQueueModel
import sdmed.extra.cso.models.eventbus.QnAUploadEvent
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.utils.FImageUtils
import java.util.UUID

class FBackgroundQnAUpload(applicationContext: Context): FBaseService(applicationContext) {
    val mqttService: FMqttService by di.instance(FMqttService::class)
    val notificationService: FNotificationService by di.instance(FNotificationService::class)
    val commonRepository: ICommonRepository by di.instance(ICommonRepository::class)
    val azureBlobRepository: IAzureBlobRepository by di.instance(IAzureBlobRepository::class)
    val qnaListRepository: IQnAListRepository by di.instance(IQnAListRepository::class)

    private val sasKeyQ = QueueLockModel<QnASASKeyQueueModel>("sasQ ${FExtensions.getToday().toString("yyyyMMdd_HHmmss")} ${UUID.randomUUID()}")
    private val azureQ = QueueLockModel<QnAAzureQueueModel>("azureQ ${FExtensions.getToday().toString("yyyyMMdd_HHmmss")} ${UUID.randomUUID()}")
    private val resultQ = QueueLockModel<QnAResultQueueModel>("resultQ ${FExtensions.getToday().toString("yyyyMMdd_HHmmss")} ${UUID.randomUUID()}")

    private var resultQRun = false

    fun sasKeyEnqueue(data: QnASASKeyQueueModel) = sasKeyQ.enqueue(data, true, { sasKeyThreadStart() })
    private fun azureEnqueue(data: QnAAzureQueueModel) = azureQ.enqueue(data, true, { azureThreadStart() })
    private fun resultEnqueue(data: QnAResultQueueModel) {
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
    private fun resultDequeue(): QnAResultQueueModel {
        resultQ.locking()
        val ret: QnAResultQueueModel
        val retBuff = resultQ.findQ(false, { it.readyToSend() })
        if (retBuff == null) {
            ret = QnAResultQueueModel("-1")
        } else {
            ret = retBuff
            resultQ.removeQ(ret, false)
        }
        resultQ.unlocking()
        return ret
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

    private fun checkSASKeyQ(data: QnASASKeyQueueModel) {
        FCoroutineUtil.coroutineScope({
            val blobName = data.blobName(context)
            val ret = commonRepository.postGenerateSasList(blobName.map { it.second })
            if (ret.result != true || ret.data == null) {
                notificationService.sendNotify(context, NotifyIndex.QNA_UPLOAD, context.getString(R.string.qna_upload_fail), ret.msg ?: "")
                return@coroutineScope
            }
            val uuid = UUID.randomUUID().toString()
            resultEnqueue(QnAResultQueueModel(uuid, data.qnaPK, itemIndex = -1, itemCount = data.medias.size, title = data.title, content = data.content))
            ret.data?.forEachIndexed { index, x ->
                val queue = QnAAzureQueueModel(uuid, data.qnaPK, mediaIndex = index).parse(data, blobName, x) ?: return@forEachIndexed
                azureEnqueue(queue)
            }
            progressNotificationCall(uuid)
        })
    }
    private fun checkAzureQ(data: QnAAzureQueueModel) {
        FCoroutineUtil.coroutineScope({
            data.media.mediaPath?.let { uri ->
                try {
                    val cachedFile = FImageUtils.uriToFile(context, uri, data.media.mediaName)
                    val ret = azureBlobRepository.upload(data.qnaFileModel.blobUrlKey(), cachedFile, data.qnaFileModel.mimeType)
                    FImageUtils.fileDelete(context, cachedFile)
                    if (ret.isSuccessful) {
                        resultEnqueue(QnAResultQueueModel(data.uuid, data.qnaPK, data.qnaFileModel, data.mediaIndex))
                    } else {
                        progressNotificationCall(data.uuid, true)
                        notificationCall(context.getString(R.string.qna_upload_fail))
                    }
                } catch (_: Exception) {
                    progressNotificationCall(data.uuid, true)
                    notificationCall(context.getString(R.string.qna_upload_fail))
                }
            }
        })
    }
    private fun postResultData(data: QnAResultQueueModel) {
        if (data.uuid == "-1") {
            return
        }
        FCoroutineUtil.coroutineScope({
            if (data.qnaPK.isNotBlank()) {
                postReply(data)
            } else {
                postData(data)
            }
        })
    }
    private suspend fun postData(data: QnAResultQueueModel) {
        val ret = qnaListRepository.postData(data.title, data.parsePostData())
        if (ret.result == true) {
            val qnaPK = ret.data?.thisPK ?: ""
            notificationCall(context.getString(R.string.qna_upload_comp), qnaPK = qnaPK)
            mqttService.mqttQnA(qnaPK, data.title)
        } else {
            notificationCall(context.getString(R.string.qna_upload_fail), ret.msg)
        }
        progressNotificationCall(data.uuid, true)
    }
    private suspend fun postReply(data: QnAResultQueueModel) {
        val ret = qnaListRepository.postReply(data.qnaPK, data.parsePostReply())
        if (ret.result == true) {
            notificationCall(context.getString(R.string.qna_upload_comp), qnaPK = data.qnaPK)
            mqttService.mqttQnA(data.qnaPK, data.title)
        } else {
            notificationCall(context.getString(R.string.qna_upload_fail), ret.msg)
        }
        progressNotificationCall(data.uuid, true)
    }
    private fun notificationCall(title: String, message: String? = null, qnaPK: String = "") {
        notificationService.sendNotify(context, NotifyIndex.QNA_UPLOAD, title, message, FNotificationService.NotifyType.WITH_VIBRATE, true, qnaPK)
        EventBus.getDefault().post(QnAUploadEvent(qnaPK))
    }
    private fun progressNotificationCall(uuid: String, isCancel: Boolean = false) {
        if (isCancel) {
            notificationService.progressUpdate(context, uuid, isCancel = true)
        } else {
            val title = context.getString(R.string.qna_upload)
            notificationService.makeProgressNotify(context, uuid, title)
        }
    }
}