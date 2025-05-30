package sdmed.extra.cso.views.dialog.multiLogin

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.users.UserMultiLoginModel
import sdmed.extra.cso.utils.FCoroutineUtil

class MultiLoginDialogVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    val items = MutableStateFlow(mutableListOf<UserMultiLoginModel>())
    val isAddVisible = MutableStateFlow(false)
    val addLogin = MutableStateFlow(false)

    suspend fun multiSign(token: String): RestResultT<String> {
        return commonRepository.multiSign(token)
    }
    fun mqttReInit() {
        FCoroutineUtil.coroutineScope({
            mqttService.mqttDisconnect()
            mqttService.mqttInit()
        })
    }

    enum class ClickEvent(var index: Int) {
        CLOSE(0),
        ADD(1)
    }
}