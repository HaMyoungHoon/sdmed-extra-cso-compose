package sdmed.extra.cso.views.main.landing

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel

class LandingScreenVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    val startVisible = MutableStateFlow(false)
    val loginClick = MutableStateFlow(false)
    val tokenCheck = MutableStateFlow(false)

    fun reSet() {
        startVisible.value = false
        loginClick.value = false
        tokenCheck.value = false
    }

    enum class ClickEvent(var index: Int) {
        START(0)
    }
}