package sdmed.extra.cso.views.hospitalMap.hospitalTempDetail

import android.content.Context
import com.google.maps.android.clustering.Cluster
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IHospitalTempRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel
import sdmed.extra.cso.models.retrofit.hospitals.PharmacyTempModel
import sdmed.extra.cso.utils.FDI
import sdmed.extra.cso.views.component.googleMap.MarkerClusterDataModel

class HospitalTempDetailActivityVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    private val hospitalTempRepository: IHospitalTempRepository by FDI.di(applicationContext).instance(IHospitalTempRepository::class)
    var hospitalPK: String = ""
    val mapVisible = MutableStateFlow(true)
    val pharmacyToggle = MutableStateFlow(true)
    val hospitalTempItem = MutableStateFlow(HospitalTempModel())
    val pharmacyTempItems = MutableStateFlow(mutableListOf<PharmacyTempModel>())
    val selectPharmacyTemp = MutableStateFlow<PharmacyTempModel?>(null)
    val singleCluster = MutableStateFlow<MarkerClusterDataModel?>(null)
    val listCluster = MutableStateFlow<Cluster<MarkerClusterDataModel>?>(null)

    suspend fun getData(): RestResultT<HospitalTempModel> {
        val ret = hospitalTempRepository.getData(hospitalPK)
        if (ret.result == true) {
            hospitalTempItem.value = ret.data ?: HospitalTempModel()
        }
        return ret
    }
    suspend fun getNearby(): RestResultT<MutableList<PharmacyTempModel>> {
        val ret = hospitalTempRepository.getPharmacyListNearby(hospitalTempItem.value.latitude, hospitalTempItem.value.longitude)
        if (ret.result == true) {
            pharmacyTempItems.value = ret.data ?: mutableListOf()
        }
        return ret
    }
    fun selectPharmacy(data: PharmacyTempModel) {
        val buff = pharmacyTempItems.value.toMutableList()
        buff.filter { it.isSelect.value }.forEach { it.isSelect.value = false }
        if (selectPharmacyTemp.value?.thisPK == data.thisPK) {
            selectPharmacyTemp.value = null
        } else {
            buff.find { it.thisPK == data.thisPK }?.isSelect?.value = true
            selectPharmacyTemp.value = data
        }
        pharmacyTempItems.value = buff
    }

    enum class ClickEvent(var index: Int) {
        CLOSE(0),
        MAP_TOGGLE(1),
        PHARMACY_TOGGLE(2),
    }
}