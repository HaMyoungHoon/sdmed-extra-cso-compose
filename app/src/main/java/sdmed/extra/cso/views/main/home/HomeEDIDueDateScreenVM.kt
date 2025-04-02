package sdmed.extra.cso.views.main.home

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IEDIDueDateRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaDueDateModel
import sdmed.extra.cso.utils.FDI
import sdmed.extra.cso.utils.FExtensions

class HomeEDIDueDateScreenVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    private val ediDueDateRepository: IEDIDueDateRepository by FDI.di(applicationContext).instance(IEDIDueDateRepository::class)
    val startDate = MutableStateFlow(FExtensions.getTodayString())
    val endDate = MutableStateFlow(FExtensions.getToday().getMonthOfLastDay().toString("yyyy-MM-dd"))
    val startDateSelect = MutableStateFlow(false)
    val endDateSelect = MutableStateFlow(false)
    val items = MutableStateFlow(mutableListOf<EDIPharmaDueDateModel>())

    suspend fun getList(): RestResultT<List<EDIPharmaDueDateModel>> {
        val ret = ediDueDateRepository.getListRange(startDate.value, endDate.value)
        if (ret.result == true) {
            items.value = ret.data?.toMutableList() ?: mutableListOf()
        }
        return ret
    }

    enum class ClickEvent(var index: Int) {
        START_DATE(0),
        END_DATE(1),
        SEARCH(2),
    }
}