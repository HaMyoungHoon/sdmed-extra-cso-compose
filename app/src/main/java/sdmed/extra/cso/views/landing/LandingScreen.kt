package sdmed.extra.cso.views.landing

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.interfaces.command.IAsyncEventListener
import sdmed.extra.cso.models.command.AsyncRelayCommand
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.views.dialog.message.MessageDialog

@Composable
fun landingScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                  displayFeatures: List<DisplayFeature> = emptyList(),
                  dataContext: LandingScreenVM = viewModel()) {
    dataContext.relayCommand = AsyncRelayCommand()
    dataContext.addEventListener(object: IAsyncEventListener {
        override suspend fun onEvent(data: Any?) {
            setThisCommand(data, dataContext)
            setUpDateCommand(data, dataContext)
        }
    })
    val context = LocalContext.current
    val loginVisible = dataContext.loginVisible.collectAsState()
    if (loginVisible.value) {
        LoginScreen().screen()
    } else {
        LandingScreenDetail().screen(dataContext)
    }

    LaunchedEffect(dataContext.loginVisible) {
        if (dataContext.updateApp.value) {
            context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                this.data = "market://details?id=${context.packageName}".toUri()
            })
            dataContext.updateApp.value = true
        }
    }
}

private fun setThisCommand(data: Any?, dataContext: LandingScreenVM) {
    val eventName = data as? LandingScreenVM.ClickEvent ?: return
    when (eventName) {
        LandingScreenVM.ClickEvent.START -> start(dataContext)
    }
}
private fun setUpDateCommand(data: Any?, dataContext: LandingScreenVM) {
    val eventName = data as? MessageDialog.ClickEvent ?: return
    when (eventName) {
        MessageDialog.ClickEvent.CLOSE -> dialogClose(dataContext)
        MessageDialog.ClickEvent.LEFT -> dialogLeft(dataContext)
        MessageDialog.ClickEvent.RIGHT -> dialogRight(dataContext)
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