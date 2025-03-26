package sdmed.extra.cso.views.landing

import androidx.lifecycle.viewModelScope
import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.users.UserMultiLoginModel

class LoginScreenVM: FBaseViewModel() {
    val id = MutableStateFlow("")
    val pw = MutableStateFlow("")
    val fillDataState = combine(id, pw) { id, pw ->
        id.length >= 3 && pw.length >= 4
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)
    val multiSignItems = MutableStateFlow(mutableListOf<UserMultiLoginModel>())

    suspend fun signIn(): RestResultT<String> {
        val ret = commonRepository.signIn(id.value, pw.value)
        return ret
    }

    enum class ClickEvent(var index: Int) {
        FORGOT_ID(0),
        FORGOT_PW(1),
        SIGN_IN(2),
        MULTI_LOGIN(3)
    }
}