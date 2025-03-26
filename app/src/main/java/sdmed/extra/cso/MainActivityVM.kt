package sdmed.extra.cso

import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.services.FMqttService
import sdmed.extra.cso.utils.FDI

class MainActivityVM: FBaseViewModel() {
    val mqttService: FMqttService by lazy {
        FDI.mqttService()
    }
}