package sdmed.extra.cso.views.main.landing

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.models.menu.MenuItem
import sdmed.extra.cso.models.menu.MenuList
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.views.dialog.message.MessageDialogVM

@Composable
fun landingScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                  displayFeatures: List<DisplayFeature> = emptyList(),
                  navigationType: NavigationType = NavigationType.BOTTOM,
                  navigate: (MenuItem, Boolean) -> Unit) {
    fBaseScreen<LandingScreenVM>({ data, dataContext -> setLayoutCommand(data, dataContext) },
        { dataContext ->
            val context = LocalContext.current
            val loginVisible by dataContext.loginVisible.collectAsState()
            landingScreenDetail(dataContext, navigate)
            if (loginVisible) {
                navigate(MenuList.menuLogin(), false)
            }
        },
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
    dataContext.loginVisible.value = true
}