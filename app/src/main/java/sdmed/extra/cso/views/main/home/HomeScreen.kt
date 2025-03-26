package sdmed.extra.cso.views.main.home

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.interfaces.command.IAsyncEventListener
import sdmed.extra.cso.models.command.AsyncRelayCommand
import sdmed.extra.cso.models.menu.WindowPanelType

@Composable
fun homeScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
               displayFeatures: List<DisplayFeature> = emptyList(),
               dataContext: HomeScreenVM = viewModel()) {
    dataContext.relayCommand = AsyncRelayCommand()
    dataContext.addEventListener(object: IAsyncEventListener {
        override suspend fun onEvent(data: Any?) {
            setThisCommand(data, dataContext)
        }
    })
}
private fun setThisCommand(data: Any?, dataContext: HomeScreenVM) {

}