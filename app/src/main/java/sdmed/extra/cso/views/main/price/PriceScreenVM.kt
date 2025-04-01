package sdmed.extra.cso.views.main.price

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IMedicinePriceListRepository
import sdmed.extra.cso.models.RestPage
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.common.PaginationModel
import sdmed.extra.cso.models.retrofit.medicines.MedicineModel
import sdmed.extra.cso.utils.FDI

class PriceScreenVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    private val priceListRepository: IMedicinePriceListRepository by FDI.di(applicationContext).instance(IMedicinePriceListRepository::class)
    val searchLoading = MutableStateFlow(false)
    var searchString = ""
    val searchBuff = MutableStateFlow<String?>(null)
    val previousPage = MutableStateFlow(0)
    val page = MutableStateFlow(0)
    val size = MutableStateFlow(20)
    val medicineModel = MutableStateFlow(mutableListOf<MedicineModel>())
    val paginationModel = MutableStateFlow(PaginationModel())

    suspend fun getList(): RestResultT<RestPage<MutableList<MedicineModel>>> {
        page.value = 0
        val ret = priceListRepository.getList(page.value, size.value)
        if (ret.result == true) {
            medicineModel.value = ret.data?.content ?: mutableListOf()
            paginationModel.value = PaginationModel().init(ret.data)
        }
        return ret
    }
    suspend fun getLike(): RestResultT<RestPage<MutableList<MedicineModel>>> {
        page.value = 0
        val ret = priceListRepository.getLike(searchString, page.value, size.value)
        if (ret.result == true) {
            medicineModel.value = ret.data?.content ?: mutableListOf()
            paginationModel.value = PaginationModel().init(ret.data)
        }
        return ret
    }
    suspend fun addList(): RestResultT<RestPage<MutableList<MedicineModel>>> {
        val ret = priceListRepository.getList(page.value, size.value)
        if (ret.result == true) {
            medicineModel.value = ret.data?.content ?: mutableListOf()
//            val medicineBuff = medicineModel.value.toMutableList()
//            medicineBuff.addAll(ret.data?.content ?: mutableListOf())
//            medicineBuff.distinctBy { it.thisPK }
//            medicineModel.value = medicineBuff
        }
        return ret
    }
    suspend fun addLike(): RestResultT<RestPage<MutableList<MedicineModel>>> {
        val ret = priceListRepository.getLike(searchString, page.value, size.value)
        if (ret.result == true) {
            medicineModel.value = ret.data?.content ?: mutableListOf()
//            val medicineBuff = medicineModel.value.toMutableList()
//            medicineBuff.addAll(ret.data?.content ?: mutableListOf())
//            medicineBuff.distinctBy { it.thisPK }
//            medicineModel.value = medicineBuff
        }
        return ret
    }
}