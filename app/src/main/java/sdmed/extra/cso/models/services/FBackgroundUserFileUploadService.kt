package sdmed.extra.cso.models.services

import android.content.Context
import org.kodein.di.instance
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseService
import sdmed.extra.cso.interfaces.repository.IAzureBlobRepository
import sdmed.extra.cso.interfaces.repository.ICommonRepository
import sdmed.extra.cso.interfaces.repository.IMyInfoRepository
import sdmed.extra.cso.models.common.NotifyIndex
import sdmed.extra.cso.models.common.QueueLockModel
import sdmed.extra.cso.models.common.UserFileAzureQueueModel
import sdmed.extra.cso.models.common.UserFileResultQueueModel
import sdmed.extra.cso.models.common.UserFileSASKeyQueueModel
import sdmed.extra.cso.models.eventbus.EventList
import sdmed.extra.cso.models.eventbus.UserFileUploadEvent
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FEventBus
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.utils.FImageUtils
import java.util.UUID

class FBackgroundUserFileUploadService(applicationContext: Context): FBaseService(applicationContext) {
    val notificationService: FNotificationService by di.instance(FNotificationService::class)
    val commonRepository: ICommonRepository by di.instance(ICommonRepository::class)
    val azureBlobRepository: IAzureBlobRepository by di.instance(IAzureBlobRepository::class)
    val myInfoRepository: IMyInfoRepository by di.instance(IMyInfoRepository::class)

    private val sasKeyQ = QueueLockModel<UserFileSASKeyQueueModel>("sasQ ${FExtensions.getToday().toString("yyyyMMdd_HHmmss")} ${UUID.randomUUID()}")
    private val azureQ = QueueLockModel<UserFileAzureQueueModel>("sasQ ${FExtensions.getToday().toString("yyyyMMdd_HHmmss")} ${UUID.randomUUID()}")
    private val resultQ = QueueLockModel<UserFileResultQueueModel>("sasQ ${FExtensions.getToday().toString("yyyyMMdd_HHmmss")} ${UUID.randomUUID()}")

    private var resultQRun = false
    fun sasKeyEnqueue(data: UserFileSASKeyQueueModel) = sasKeyQ.enqueue(data, true, { sasKeyThreadStart() })
    private fun azureEnqueue(data: UserFileAzureQueueModel) = azureQ.enqueue(data, true, { azureThreadStart() })
    private fun resultEnqueue(data: UserFileResultQueueModel) {
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
    private fun resultDequeue(): UserFileResultQueueModel {
        resultQ.locking()
        val ret: UserFileResultQueueModel
        val retBuff = resultQ.findQ(false, { it.readyToSend() })
        if (retBuff == null) {
            ret = UserFileResultQueueModel("-1")
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

    private fun checkSASKeyQ(data: UserFileSASKeyQueueModel) {
        FCoroutineUtil.coroutineScope({
            val blobName = data.blobName(context)
            val ret = commonRepository.postGenerateSasList(blobName.map { it.second })
            if (ret.result != true || ret.data == null) {
                notificationCall(context.getString(R.string.qna_upload_fail), ret.msg)
                return@coroutineScope
            }
            val uuid = UUID.randomUUID().toString()
            resultEnqueue(UserFileResultQueueModel(uuid, itemIndex = -1, itemCount = data.medias.size, mediaTypeIndex = data.mediaTypeIndex))
            ret.data?.forEachIndexed { index, x ->
                val queue = UserFileAzureQueueModel(uuid, mediaTypeIndex = index).parse(data, blobName, x) ?: return@forEachIndexed
                azureEnqueue(queue)
            }
            progressNotificationCall(uuid)
        })
    }
    private fun checkAzureQ(data: UserFileAzureQueueModel) {
        FCoroutineUtil.coroutineScope({
            data.media.mediaPath?.let { uri ->
                try {
                    val cachedFile = FImageUtils.uriToFile(context, uri, data.media.mediaName)
                    val ret = azureBlobRepository.upload(data.userFileModel.blobUrlKey(), cachedFile, data.userFileModel.mimeType)
                    FImageUtils.fileDelete(context, cachedFile)
                    if (ret.isSuccessful) {
                        resultEnqueue(UserFileResultQueueModel(data.uuid, data.userFileModel, data.mediaTypeIndex, mediaTypeIndex = data.mediaTypeIndex))
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
    private fun postResultData(data: UserFileResultQueueModel) {
        if (data.uuid == "-1") {
            return
        }
        FCoroutineUtil.coroutineScope({
            postData(data)
        })
    }
    private suspend fun postData(data: UserFileResultQueueModel) {
        val thisPK = FAmhohwa.getThisPK(context)
        val ret = myInfoRepository.putUserFileImageUrl(thisPK, data.parseBlobUploadModel(), data.userFileType())
        if (ret.result == true) {
            notificationCall(context.getString(R.string.user_file_upload_comp), thisPK = thisPK)
        } else {
            notificationCall(context.getString(R.string.user_file_upload_fail), ret.msg)
        }
        progressNotificationCall(data.uuid, true)
    }
    private suspend fun notificationCall(title: String, message: String? = null, thisPK: String = "") {
        notificationService.sendNotify(context, NotifyIndex.USER_FILE_UPLOAD, title, message, FNotificationService.NotifyType.WITH_VIBRATE, true, thisPK)
        FEventBus.emit(EventList.UserFileUploadEvent(thisPK))
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