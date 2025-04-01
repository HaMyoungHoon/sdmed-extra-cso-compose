package sdmed.extra.cso.views.main.qna

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.kodein.di.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IQnAListRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.common.MediaViewParcelModel
import sdmed.extra.cso.models.eventbus.EventList
import sdmed.extra.cso.models.retrofit.qna.QnAContentModel
import sdmed.extra.cso.models.retrofit.qna.QnAHeaderModel
import sdmed.extra.cso.models.retrofit.qna.QnAReplyFileModel
import sdmed.extra.cso.utils.FDI
import sdmed.extra.cso.utils.FEventBus

class QnAScreenDetailVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    private val qnaListRepository: IQnAListRepository by FDI.di(applicationContext).instance(IQnAListRepository::class)
    private val eventChannel = FEventBus.createEventChannel<EventList.QnAUploadEvent>()
    val thisPK = MutableStateFlow("")
    val headerModel = MutableStateFlow(QnAHeaderModel())
    val contentModel = MutableStateFlow(QnAContentModel())
    val collapseContent = MutableStateFlow(true)
    val addQnATitle = MutableStateFlow<String?>(null)
    init {
        viewModelScope.launch {
            for (event in eventChannel) {
                loading(false)
                getData()
            }
        }
    }
    fun reSet() {
        thisPK.value = ""
        headerModel.value = QnAHeaderModel()
        contentModel.value = QnAContentModel()
    }
    suspend fun getData(): RestResultT<QnAHeaderModel> {
        if (thisPK.value.isEmpty()) {
            return RestResultT<QnAHeaderModel>().emptyResult()
        }
        val ret = qnaListRepository.getHeaderData(thisPK.value)
        if (ret.result == true) {
            headerModel.value = ret.data ?: QnAHeaderModel()
            val contentRet = qnaListRepository.getContentData(thisPK.value)
            if (contentRet.result == true) {
                val contentBuff = contentRet.data ?: QnAContentModel()
                contentModel.value = contentBuff
            } else {
                ret.result = contentRet.result
                ret.msg = contentRet.msg
            }
        }
        return ret
    }
    suspend fun postData(): RestResultT<QnAHeaderModel> {
        val ret = qnaListRepository.putData(thisPK.value)
        if (ret.result == true) {
            headerModel.value = ret.data ?: QnAHeaderModel()
        }
        return ret
    }

    fun getMediaViewQnAFiles(): ArrayList<MediaViewParcelModel> {
        val ret = arrayListOf<MediaViewParcelModel>()
        contentModel.value.fileList.forEach { x ->
            ret.add(MediaViewParcelModel().parse(x))
        }
        return ret
    }
    fun getMediaViewQnAReplyFiles(item: QnAReplyFileModel): ArrayList<MediaViewParcelModel> {
        val reply = contentModel.value.replyList.find { x -> x.fileList.find { y -> y.thisPK == item.thisPK } != null } ?: return arrayListOf<MediaViewParcelModel>()
        val ret = arrayListOf<MediaViewParcelModel>()
        reply.fileList.forEach { x ->
            ret.add(MediaViewParcelModel().parse(x))
        }
        return ret
    }

    enum class ClickEvent(var index: Int) {
        CLOSE(0),
        COLLAPSE(1),
        ADD(2),
        COMP(3),
    }
}