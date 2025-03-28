package sdmed.extra.cso.views.main.landing

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.common.VersionCheckModel

class LandingScreenVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    val startVisible = MutableStateFlow(false)
    val loginVisible = MutableStateFlow(false)
    val tokenCheck = MutableStateFlow(false)
    var checking = false

    enum class ClickEvent(var index: Int) {
        START(0)
    }
}