package sdmed.extra.cso.views.main.qna

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.kodein.di.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IQnAListRepository
import sdmed.extra.cso.models.RestPage
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.common.PaginationModel
import sdmed.extra.cso.models.eventbus.EventList
import sdmed.extra.cso.models.retrofit.qna.QnAHeaderModel
import sdmed.extra.cso.utils.FDI
import sdmed.extra.cso.utils.FEventBus

class QnaScreenVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    private val qnaListRepository: IQnAListRepository by FDI.di(applicationContext).instance(IQnAListRepository::class)
    private val eventChannel = FEventBus.createEventChannel<EventList.QnAUploadEvent>()
    val searchLoading = MutableStateFlow(false)
    var searchString = ""
    val searchBuff = MutableStateFlow<String?>(null)
    val previousPage = MutableStateFlow(0)
    val page = MutableStateFlow(0)
    val size = MutableStateFlow(20)
    val qnaModel = MutableStateFlow(mutableListOf<QnAHeaderModel>())
    val paginationModel = MutableStateFlow(PaginationModel())
    val addQnA = MutableStateFlow(false)
    val replyQnA: MutableStateFlow<Pair<String?, String?>> = MutableStateFlow(Pair(null, null))
    val selectItem = MutableStateFlow<QnAHeaderModel?>(null)

    init {
        viewModelScope.launch {
            for (event in eventChannel) {
                loading(false)
                getList()
            }
        }
    }

    suspend fun getList(): RestResultT<RestPage<MutableList<QnAHeaderModel>>> {
        page.value = 0
        val ret = qnaListRepository.getList(page.value, size.value)
        if (ret.result == true) {
            qnaModel.value = ret.data?.content ?: mutableListOf()
            paginationModel.value = PaginationModel().init(ret.data)
        }
        return ret
    }
    suspend fun getLike(): RestResultT<RestPage<MutableList<QnAHeaderModel>>> {
        page.value = 0
        val ret = qnaListRepository.getLike(searchString, page.value, size.value)
        if (ret.result == true) {
            qnaModel.value = ret.data?.content ?: mutableListOf()
            paginationModel.value = PaginationModel().init(ret.data)
        }
        return ret
    }
    suspend fun addList(): RestResultT<RestPage<MutableList<QnAHeaderModel>>> {
        val ret = qnaListRepository.getList(page.value, size.value)
        if (ret.result == true) {
            qnaModel.value = ret.data?.content ?: mutableListOf()
        }
        return ret
    }
    suspend fun addLike(): RestResultT<RestPage<MutableList<QnAHeaderModel>>> {
        val ret = qnaListRepository.getLike(searchString, page.value, size.value)
        if (ret.result == true) {
            qnaModel.value = ret.data?.content ?: mutableListOf()
        }
        return ret
    }

    enum class ClickEvent(var index: Int) {
        ADD(0)
    }
}