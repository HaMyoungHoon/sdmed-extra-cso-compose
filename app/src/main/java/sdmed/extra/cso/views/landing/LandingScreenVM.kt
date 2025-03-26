package sdmed.extra.cso.views.landing

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.common.VersionCheckModel

class LandingScreenVM: FBaseViewModel() {
    val startVisible = MutableStateFlow(false)
    val updateVisible = MutableStateFlow(false)
    val loginVisible = MutableStateFlow(false)
    val updateApp = MutableStateFlow(false)
    var checking = false
    suspend fun versionCheck(): RestResultT<List<VersionCheckModel>> {
        return commonRepository.versionCheck()
    }

    enum class ClickEvent(var index: Int) {
        START(0)
    }
}