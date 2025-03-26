package sdmed.extra.cso.views.main.price

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.interfaces.command.IAsyncEventListener
import sdmed.extra.cso.models.command.AsyncRelayCommand
import sdmed.extra.cso.models.menu.WindowPanelType

@Composable
fun priceScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                displayFeatures: List<DisplayFeature> = emptyList(),
                dataContext: PriceScreenVM = viewModel()) {
    dataContext.relayCommand = AsyncRelayCommand()
    dataContext.addEventListener(object: IAsyncEventListener {
        override suspend fun onEvent(data: Any?) {
            setThisCommand(data, dataContext)
        }
    })
}
private fun setThisCommand(data: Any?, dataContext: PriceScreenVM) {

}