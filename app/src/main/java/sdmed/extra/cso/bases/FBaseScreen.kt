package sdmed.extra.cso.bases

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import sdmed.extra.cso.interfaces.command.IAsyncEventListener
import sdmed.extra.cso.models.command.AsyncRelayCommand
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType

@Composable
inline fun <reified T: FBaseViewModel> fBaseScreen(crossinline setLayoutCommand: (Any?, T) -> Unit = { x, y -> },
                                                   noinline content: @Composable ((T) -> Unit)? = null,
                                                   windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                                                   navigationType: NavigationType = NavigationType.BOTTOM,
                                                   twoPane: @Composable (T) -> Unit = { },
                                                   phone: @Composable (T) -> Unit = { },
                                                   tablet: @Composable (T) -> Unit = { }): T {
    val dataContext: T = viewModel(T::class)
    dataContext.relayCommand = AsyncRelayCommand()
    dataContext.addEventListener(object: IAsyncEventListener {
        override suspend fun onEvent(data: Any?) {
            setLayoutCommand(data, dataContext)
        }
    })
    if (content != null) {
        content(dataContext)
        return dataContext
    }

    when (windowPanelType) {
        WindowPanelType.SINGLE_PANE -> {
            if (navigationType == NavigationType.BOTTOM) {
                phone(dataContext)
            } else {
                tablet(dataContext)
            }
        }
        WindowPanelType.DUAL_PANE -> twoPane(dataContext)
    }
    return dataContext
}