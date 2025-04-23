package sdmed.extra.cso.views.dialog.loginDialog

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions

class LoginDialogVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    val id = MutableStateFlow("")
    val pw = MutableStateFlow("")
    val fillDataState = FExtensions.stateIn(combine(id, pw) { id, pw ->
        id.length >= 3 && pw.length >= 4
    }, false)

    suspend fun signIn(): RestResultT<String> {
        val ret = commonRepository.signIn(id.value, pw.value)
        return ret
    }
    fun mqttReInit() {
        FCoroutineUtil.coroutineScope({
            mqttService.mqttDisconnect()
            mqttService.mqttInit()
        })
    }

    enum class ClickEvent(var index: Int) {
        SIGN_IN(0)
    }
}