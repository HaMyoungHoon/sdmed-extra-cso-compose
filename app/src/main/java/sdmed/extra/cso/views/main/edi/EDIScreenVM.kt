package sdmed.extra.cso.views.main.edi

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.kodein.di.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.bases.FMainApplication
import sdmed.extra.cso.interfaces.repository.IEDIListRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.eventbus.EventList
import sdmed.extra.cso.models.retrofit.edi.EDIState
import sdmed.extra.cso.models.retrofit.edi.EDIType
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel
import sdmed.extra.cso.utils.FDI
import sdmed.extra.cso.utils.FEventBus
import sdmed.extra.cso.utils.FExtensions
import java.util.UUID
import kotlin.getValue

class EDIScreenVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    private val ediListRepository: IEDIListRepository by FDI.di(applicationContext).instance(IEDIListRepository::class)
    private val eventChannel = FEventBus.createEventChannel<EventList.EDIUploadEvent>()

    val items = MutableStateFlow(mutableListOf<EDIUploadModel>())
    val startDate = MutableStateFlow(FExtensions.getToday().addMonth(-1).toString("yyyy-MM-dd"))
    val endDate = MutableStateFlow(FExtensions.getTodayString())
    val startDateSelect = MutableStateFlow(false)
    val endDateSelect = MutableStateFlow(false)
    val selectItem = MutableStateFlow<EDIUploadModel?>(null)

    init {
        viewModelScope.launch {
            for (event in eventChannel) {
                loading(false)
                getList()
            }
        }
    }

    suspend fun getList(): RestResultT<List<EDIUploadModel>> {
        val ret = ediListRepository.getList(startDate.value, endDate.value)
        if (ret.result == true) {
            items.value = ret.data?.toMutableList() ?: mutableListOf()
        }
        return ret
    }

    override fun fakeInit() {
        val list = mutableListOf<EDIUploadModel>()
        list.add(EDIUploadModel().apply {
            thisPK = UUID.randomUUID().toString()
            regDate = "2025-01-02"
            ediState = EDIState.Reject
            orgName = "신규 병원"
            tempOrgName = "ABC 병원"
            ediType = EDIType.NEW
            isSelected.value = true
        })
        list.add(EDIUploadModel().apply {
            thisPK = UUID.randomUUID().toString()
            regDate = "2025-02-03"
            ediState = EDIState.Pending
            orgName = "이관 병원"
            tempOrgName = "ABC 병원 asdfajsldk fdalksdfjd al"
            ediType = EDIType.TRANSFER
            isSelected.value = false
        })
        list.add(EDIUploadModel().apply {
            thisPK = UUID.randomUUID().toString()
            regDate = "2025-03-04"
            ediState = EDIState.OK
            orgName = "기냥 병원"
            tempOrgName = "ABC 병원"
            ediType = EDIType.DEFAULT
            isSelected.value = false
        })
        items.value = list
    }

    enum class ClickEvent(var index: Int) {
        START_DATE(0),
        END_DATE(1),
        SEARCH(2),
    }
}