package sdmed.extra.cso.views.main.landing

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.eventbus.EventList
import sdmed.extra.cso.models.retrofit.users.UserMultiLoginModel
import sdmed.extra.cso.utils.FEventBus
import sdmed.extra.cso.utils.FStorage

class LoginScreenVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    private val multiLoginChannel = FEventBus.createEventChannel<EventList.MultiLoginEvent>()
    val id = MutableStateFlow("")
    val pw = MutableStateFlow("")
    val loginEnd = MutableStateFlow(false)
    val fillDataState = combine(id, pw) { id, pw ->
        id.length >= 3 && pw.length >= 4
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)
    val multiSignItems = MutableStateFlow(mutableListOf<UserMultiLoginModel>())
    val multiSignInSelect = MutableStateFlow(false)
    init {
        viewModelScope.launch {
            for (event in multiLoginChannel) {
                loading(false)
                loginEnd.value = true
            }
        }
    }

    fun gatheringMultiSign() {
        FStorage.getMultiLoginData(context)?.let {
            multiSignItems.value = it.toMutableList()
        }
    }
    fun reSet() {
        id.value = ""
        pw.value = ""
        loginEnd.value = false
        gatheringMultiSign()
    }
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