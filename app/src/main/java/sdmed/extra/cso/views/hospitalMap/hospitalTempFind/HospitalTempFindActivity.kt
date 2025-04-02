package sdmed.extra.cso.views.hospitalMap.hospitalTempFind

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.android.gms.maps.model.LatLng
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.utils.FLocationUtil
import sdmed.extra.cso.utils.FStorage.putParcelable
import sdmed.extra.cso.views.theme.FThemeUtil
import java.util.ArrayList

class HospitalTempFindActivity: FBaseActivity<HospitalTempFindActivityVM>(), FLocationUtil.ILocationListener {
    override val dataContext: HospitalTempFindActivityVM by viewModels()
    private var locationUtil: FLocationUtil? = null
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        initCommand()
        setContent {
            FThemeUtil.thisTheme {
                setToast()
                setLoading()
                val windowSize = calculateWindowSizeClass(this)
                val displayFeatures = calculateDisplayFeatures(this)
                dataContext.permissionService.requestLocation {
                    if (it) {
                        locationUtil = FLocationUtil(this, true, this)
                        locationUtil?.getLocation()
                        dataContext.isMyLocationEnabled.value = true
                    }
                }
                val color = FThemeUtil.safeColorC()
                Box(Modifier.windowInsetsPadding(WindowInsets.statusBars.only(WindowInsetsSides.Top))
                    .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom))
                    .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Horizontal))
                    .fillMaxSize().background(color.background)) {
                    calcScreen(windowSize, displayFeatures)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationUtil = null
    }
    override fun onLocationEvent(latitude: Double, longitude: Double) {
        loading(false)
        dataContext.currentLatLng.value = LatLng(latitude, longitude)
        dataContext.nearbyAble.value = true
    }
    override fun onLocationFail(exception: String) {
        loading(false)
        toast(exception)
    }
    override fun onLocationFail(exception: Exception) {
        loading(false)
        toast(exception.message)
    }

    @Composable
    override fun twoPane(dataContext: HospitalTempFindActivityVM, displayFeatures: List<DisplayFeature>) {
        hospitalTempFindScreenTwoPane(dataContext, displayFeatures)
    }
    @Composable
    override fun phone(dataContext: HospitalTempFindActivityVM) {
        hospitalTempFindScreenPhone(dataContext)
    }
    @Composable
    override fun tablet(dataContext: HospitalTempFindActivityVM) {
        hospitalTempFindScreenTablet(dataContext)
    }

    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
        setHospitalCommand(data)
    }
    private fun setThisCommand(data: Any?) {
        val eventName = data as? HospitalTempFindActivityVM.ClickEvent ?: return
        when (eventName) {
            HospitalTempFindActivityVM.ClickEvent.SELECT -> selectHospitalFinish()
            HospitalTempFindActivityVM.ClickEvent.CLOSE -> closeThis()
            HospitalTempFindActivityVM.ClickEvent.MAP_TOGGLE -> mapToggle()
            HospitalTempFindActivityVM.ClickEvent.NEARBY -> nearBy()
        }
    }
    private fun setHospitalCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? HospitalTempModel.ClickEvent ?: return
        val dataBuff = data[1] as? HospitalTempModel ?: return
        when (eventName) {
            HospitalTempModel.ClickEvent.THIS -> selectHospital(dataBuff)
            HospitalTempModel.ClickEvent.WEB_SITE -> openWebsite(dataBuff)
            HospitalTempModel.ClickEvent.PHONE_NUMBER -> openTelephony(dataBuff)
        }
    }
    private fun selectHospitalFinish() {
        val item = dataContext.selectedHospitalTemp.value ?: return
        setResult(RESULT_OK, Intent().apply {
            putParcelable(FConstants.HOSPITAL_TEMP, item)
        })
        finish()
    }
    private fun closeThis() {
        finish()
    }
    private fun mapToggle() {
        dataContext.mapVisible.value = !dataContext.mapVisible.value
    }
    private fun nearBy() {
        dataContext.currentLatLng.value?.let { latLng ->
            loading()
            FCoroutineUtil.coroutineScope({
                val ret = dataContext.getNearby(latLng.latitude, latLng.longitude)
                loading(false)
                if (ret.result != true) {
                    toast(ret.msg)
                }
            })
        }
    }
    private fun selectHospital(data: HospitalTempModel) {
        dataContext.selectHospital(data)
    }
    private fun openWebsite(data: HospitalTempModel) {
        val url = data.websiteUrl
        if (url.isEmpty()) {
            return
        }

        val buff = if (!url.startsWith("http://") && !url.startsWith("https://")) {
            "https://$url"
        } else {
            url
        }
        startActivity(Intent(Intent.ACTION_VIEW, buff.toUri()))
    }
    private fun openTelephony(data: HospitalTempModel) {
        val phoneNumber = data.phoneNumber
        val buff = FExtensions.regexNumberReplace(phoneNumber)
        if (buff.isNullOrBlank()) {
            return
        }

        startActivity(Intent(Intent.ACTION_DIAL, "tel:$buff".toUri()))
    }
}