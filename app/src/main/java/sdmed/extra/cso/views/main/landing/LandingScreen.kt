package sdmed.extra.cso.views.main.landing

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.views.dialog.message.MessageDialogVM

@Composable
fun landingScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                  displayFeatures: List<DisplayFeature> = emptyList(),
                  navigationType: NavigationType = NavigationType.BOTTOM) {
    fBaseScreen<LandingScreenVM>({ data, dataContext ->
            setThisCommand(data, dataContext)
            setUpDateCommand(data, dataContext)
        },
        { dataContext ->
            val context = LocalContext.current
            val loginVisible = dataContext.loginVisible.collectAsState()
            if (loginVisible.value) {
                loginScreen()
            } else {
                landingScreenDetail(dataContext)
            }

            LaunchedEffect(dataContext.loginVisible) {
                if (dataContext.updateApp.value) {
                    context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                        this.data = "market://details?id=${context.packageName}".toUri()
                    })
                    dataContext.updateApp.value = true
                }
            }
        },
        windowPanelType, navigationType)
}

private fun setThisCommand(data: Any?, dataContext: LandingScreenVM) {
    val eventName = data as? LandingScreenVM.ClickEvent ?: return
    when (eventName) {
        LandingScreenVM.ClickEvent.START -> start(dataContext)
    }
}
private fun setUpDateCommand(data: Any?, dataContext: LandingScreenVM) {
    val eventName = data as? MessageDialogVM.ClickEvent ?: return
    when (eventName) {
        MessageDialogVM.ClickEvent.CLOSE -> dialogClose(dataContext)
        MessageDialogVM.ClickEvent.LEFT -> dialogLeft(dataContext)
        MessageDialogVM.ClickEvent.RIGHT -> dialogRight(dataContext)
    }
}
private fun start(dataContext: LandingScreenVM) {
    dataContext.loginVisible.value = true
}
private fun dialogClose(dataContext: LandingScreenVM) {
    dataContext.updateVisible.value = false
}
private fun dialogLeft(dataContext: LandingScreenVM) {
    dataContext.updateApp.value = true
}
private fun dialogRight(dataContext: LandingScreenVM) {
    dataContext.updateApp.value = true
}