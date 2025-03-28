package sdmed.extra.cso.views.main.landing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.models.menu.MenuItem
import sdmed.extra.cso.models.menu.MenuList
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType

@Composable
fun landingScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                  displayFeatures: List<DisplayFeature> = emptyList(),
                  navigationType: NavigationType = NavigationType.BOTTOM,
                  navigate: (MenuItem, Boolean) -> Unit) {
    val dataContext = fBaseScreen<LandingScreenVM>({ data, dataContext -> setLayoutCommand(data, dataContext) },
        { dataContext -> landingScreenDetail(dataContext, navigate) },
        windowPanelType, navigationType)
}

private fun setLayoutCommand(data: Any?, dataContext: LandingScreenVM) {
    setThisCommand(data, dataContext)
}
private fun setThisCommand(data: Any?, dataContext: LandingScreenVM) {
    val eventName = data as? LandingScreenVM.ClickEvent ?: return
    when (eventName) {
        LandingScreenVM.ClickEvent.START -> start(dataContext)
    }
}
private fun start(dataContext: LandingScreenVM) {
    dataContext.loginClick.value = true
}