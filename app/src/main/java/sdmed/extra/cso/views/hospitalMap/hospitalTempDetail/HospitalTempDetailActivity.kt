package sdmed.extra.cso.views.hospitalMap.hospitalTempDetail

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
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.retrofit.hospitals.PharmacyTempModel
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.views.theme.FThemeUtil
import java.util.ArrayList

class HospitalTempDetailActivity: FBaseActivity<HospitalTempDetailActivityVM>() {
    override val dataContext: HospitalTempDetailActivityVM by viewModels()
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        initCommand()
        dataContext.hospitalPK = intent.getStringExtra(FConstants.HOSPITAL_PK) ?: ""
        getData()
        setContent {
            FThemeUtil.thisTheme {
                setToast()
                setLoading()
                val windowSize = calculateWindowSizeClass(this)
                val displayFeatures = calculateDisplayFeatures(this)
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

    @Composable
    override fun twoPane(dataContext: HospitalTempDetailActivityVM, displayFeatures: List<DisplayFeature>) {
        hospitalTempDetailTwoPane(dataContext, displayFeatures)
    }
    @Composable
    override fun phone(dataContext: HospitalTempDetailActivityVM) {
        hospitalTempDetailPhone(dataContext)
    }
    @Composable
    override fun tablet(dataContext: HospitalTempDetailActivityVM) {
        hospitalTempDetailTablet(dataContext)
    }

    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
        setPharmacyCommand(data)
    }
    private fun setThisCommand(data: Any?) {
        val eventName = data as? HospitalTempDetailActivityVM.ClickEvent ?: return
        when (eventName) {
            HospitalTempDetailActivityVM.ClickEvent.CLOSE -> closeThis()
            HospitalTempDetailActivityVM.ClickEvent.MAP_TOGGLE -> mapToggle()
            HospitalTempDetailActivityVM.ClickEvent.PHARMACY_TOGGLE -> pharmacyToggle()
        }
    }
    private fun setPharmacyCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? PharmacyTempModel.ClickEvent ?: return
        val dataBuff = data[1] as? PharmacyTempModel ?: return
        when (eventName) {
            PharmacyTempModel.ClickEvent.THIS -> selectPharmacy(dataBuff)
            PharmacyTempModel.ClickEvent.PHONE_NUMBER -> openTelephony(dataBuff)
        }
    }
    private fun getData() {
        if (dataContext.hospitalPK.isEmpty()) {
            return
        }
        loading()
        FCoroutineUtil.coroutineScope({
            val ret = dataContext.getData()
            if (ret.result != true) {
                toast(ret.msg)
                loading(false)
                return@coroutineScope
            }
            getNearby()
        })
    }
    private fun getNearby() {
        if (dataContext.hospitalTempItem.value.thisPK.isEmpty()) {
            return
        }
        loading()
        FCoroutineUtil.coroutineScope({
            val ret = dataContext.getNearby()
            loading(false)
            if (ret.result != true) {
                toast(ret.msg)
            }
        })
    }
    private fun closeThis() {
        finish()
    }
    private fun mapToggle() {
        dataContext.mapVisible.value = !dataContext.mapVisible.value
    }
    private fun pharmacyToggle() {
        dataContext.pharmacyToggle.value = !dataContext.pharmacyToggle.value
    }
    private fun selectPharmacy(data: PharmacyTempModel) {
        dataContext.selectPharmacy(data)
    }
    private fun openTelephony(data: PharmacyTempModel) {
        val phoneNumber = data.phoneNumber
        val buff = FExtensions.regexNumberReplace(phoneNumber)
        if (buff.isNullOrBlank()) {
            return
        }

        startActivity(Intent(Intent.ACTION_DIAL, "tel:$buff".toUri()))
    }
}