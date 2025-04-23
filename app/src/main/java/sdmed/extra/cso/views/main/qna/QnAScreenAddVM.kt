package sdmed.extra.cso.views.main.qna

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.kodein.di.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.common.MediaFileType
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.common.QnASASKeyQueueModel
import sdmed.extra.cso.models.eventbus.EventList
import sdmed.extra.cso.models.retrofit.qna.QnAHeaderModel
import sdmed.extra.cso.models.services.FBackgroundQnAUpload
import sdmed.extra.cso.utils.FDI
import sdmed.extra.cso.utils.FEventBus
import kotlin.collections.forEach

class QnAScreenAddVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    private val backgroundService: FBackgroundQnAUpload by FDI.di(applicationContext).instance(FBackgroundQnAUpload::class)
    private val eventChannel = FEventBus.createEventChannel<EventList.QnAUploadEvent>()
    var thisPK = MutableStateFlow<String>("")
    val title = MutableStateFlow<String>("")
    val postTitle = MutableStateFlow<String>("")
    val content = MutableStateFlow<String>("")
    val imageSelect = MutableStateFlow(false)
    val uploadItems = MutableStateFlow(mutableListOf<MediaPickerSourceModel>())
    val isSavable = MutableStateFlow(false)
    val closeAble = MutableStateFlow(true)
    val dismissRequest = MutableStateFlow(false)
    init {
        viewModelScope.launch {
            for (event in eventChannel) {
                loading(false)
                closeAble.value = true
                reSet()
                dismissRequest.value = true
            }
        }
    }

    fun reSet() {
        thisPK.value = ""
        title.value = ""
        postTitle.value = ""
        content.value = ""
        uploadItems.value = mutableListOf()
    }
    fun getMediaItems() = ArrayList(uploadItems.value.toMutableList())
    fun removeImage(data: MediaPickerSourceModel?) {
        uploadItems.value = uploadItems.value.filter { it != data }.toMutableList()
    }
    fun addImage(uri: Uri?, name: String, fileType: MediaFileType, mimeType: String) {
        uri ?: return
        try {
            val imageBuff = uploadItems.value.toMutableList()
            imageBuff.add(MediaPickerSourceModel().apply {
                mediaUri = uri
                mediaName = name
                mediaFileType = fileType
                mediaMimeType = mimeType
            })
            uploadItems.value = imageBuff
        } catch (_: Exception) {
        }
    }
    fun addImage(mediaPickerSource: MediaPickerSourceModel) {
        try {
            val imageBuff = uploadItems.value.toMutableList()
            imageBuff.add(mediaPickerSource)
            uploadItems.value = imageBuff
        } catch (_: Exception) {
        }
    }
    fun reSetImage(mediaList: ArrayList<MediaPickerSourceModel>?) {
        uploadItems.value = mutableListOf()
        mediaList?.forEach { x ->
            addImage(x)
        }
    }

    fun startBackgroundService() {
        closeAble.value = false
        val uploadFile = this.uploadItems.value.toMutableList()
        val data = QnASASKeyQueueModel(title = postTitle.value, content = content.value).apply {
            qnaPK = thisPK.value
            medias = uploadFile
        }
        this.uploadItems.value = mutableListOf()
        backgroundService.sasKeyEnqueue(data)
    }

    enum class ClickEvent(var index: Int) {
        CLOSE(0),
        ADD(1),
        SAVE(2)
    }
}