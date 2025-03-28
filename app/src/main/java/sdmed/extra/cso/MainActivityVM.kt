package sdmed.extra.cso

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.common.VersionCheckModel
import sdmed.extra.cso.models.services.FMqttService
import sdmed.extra.cso.utils.FDI

class MainActivityVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    val mqttService: FMqttService by FDI.di(applicationContext).instance(FMqttService::class)
    val updateVisible = MutableStateFlow(false)
    val updateApp = MutableStateFlow(false)

    suspend fun versionCheck(): RestResultT<List<VersionCheckModel>> {
        return commonRepository.versionCheck()
    }
}