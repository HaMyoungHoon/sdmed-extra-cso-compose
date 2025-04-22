package sdmed.extra.cso

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.common.VersionCheckModel
import sdmed.extra.cso.models.services.FMqttService
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FDI

class MainActivityVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    val updateVisible = MutableStateFlow(false)
    val updateApp = MutableStateFlow(false)
    val tokenExpired = MutableStateFlow(false)

    fun mqttInit() {
        FCoroutineUtil.coroutineScope({
            mqttService.mqttInit()
        })
    }
    fun mqttDisconnect() {
        mqttService.mqttDisconnect()
    }
    fun mqttReInit() {
        FCoroutineUtil.coroutineScope({
            mqttService.mqttDisconnect()
            mqttService.mqttInit()
        })
    }
    suspend fun versionCheck(): RestResultT<List<VersionCheckModel>> {
        return commonRepository.versionCheck()
    }
}