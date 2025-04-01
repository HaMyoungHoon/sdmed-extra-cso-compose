package sdmed.extra.cso.views.dialog.loginDialog

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.RestResultT

class LoginDialogVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    val id = MutableStateFlow("")
    val pw = MutableStateFlow("")
    val fillDataState = combine(id, pw) { id, pw ->
        id.length >= 3 && pw.length >= 4
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    suspend fun signIn(): RestResultT<String> {
        val ret = commonRepository.signIn(id.value, pw.value)
        return ret
    }

    enum class ClickEvent(var index: Int) {
        SIGN_IN(0)
    }
}