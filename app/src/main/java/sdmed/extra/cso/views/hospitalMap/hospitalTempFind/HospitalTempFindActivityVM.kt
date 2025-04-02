package sdmed.extra.cso.views.hospitalMap.hospitalTempFind

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.Cluster
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.instance
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IHospitalTempRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel
import sdmed.extra.cso.utils.FDI
import sdmed.extra.cso.views.component.googleMap.MarkerClusterDataModel

class HospitalTempFindActivityVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    private val hospitalTempRepository: IHospitalTempRepository by FDI.di(applicationContext).instance(IHospitalTempRepository::class)
    val searchLoading = MutableStateFlow(false)
    var searchString = ""
    val searchBuff = MutableStateFlow<String?>(null)
    val mapVisible = MutableStateFlow(true)
    val nearbyAble = MutableStateFlow(false)
    val hospitalTempItems = MutableStateFlow(mutableListOf<HospitalTempModel>())
    val selectedHospitalTemp: MutableStateFlow<HospitalTempModel?> = MutableStateFlow(null)
    val currentLatLng = MutableStateFlow<LatLng?>(null)
    val isMyLocationEnabled = MutableStateFlow(false)

    val singleCluster = MutableStateFlow<MarkerClusterDataModel?>(null)
    val listCluster = MutableStateFlow<Cluster<MarkerClusterDataModel>?>(null)

    suspend fun getSearch(): RestResultT<MutableList<HospitalTempModel>> {
        if (searchString.length < 3) {
            return RestResultT<MutableList<HospitalTempModel>>().setFail(-1, context.getString(R.string.search_length_too_low))
        }
        val ret = hospitalTempRepository.getListSearch(searchString)
        if (ret.result == true) {
            hospitalTempItems.value = ret.data ?: mutableListOf()
        }
        return ret
    }
    suspend fun getNearby(latitude: Double, longitude: Double): RestResultT<MutableList<HospitalTempModel>> {
        val ret = hospitalTempRepository.getListNearby(latitude, longitude)
        if (ret.result == true) {
            hospitalTempItems.value = ret.data ?: mutableListOf()
        }
        return ret
    }
    fun selectHospital(data: HospitalTempModel) {
        val buff = hospitalTempItems.value.toMutableList()
        buff.filter { it.isSelect.value }.forEach { it.isSelect.value = false }
        if (selectedHospitalTemp.value?.thisPK == data.thisPK) {
            selectedHospitalTemp.value = null
        } else {
            buff.find { it.thisPK == data.thisPK }?.isSelect?.value = true
            selectedHospitalTemp.value = data
        }
        hospitalTempItems.value = buff
    }

    enum class ClickEvent(var index: Int) {
        SELECT(0),
        CLOSE(1),
        MAP_TOGGLE(2),
        NEARBY(3)
    }
}