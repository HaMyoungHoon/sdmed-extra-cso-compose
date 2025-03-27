package sdmed.extra.cso

import android.content.Context
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.services.FMqttService
import sdmed.extra.cso.utils.FDI

class MainActivityVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    val mqttService: FMqttService by lazy {
        FDI.mqttService()
    }
}