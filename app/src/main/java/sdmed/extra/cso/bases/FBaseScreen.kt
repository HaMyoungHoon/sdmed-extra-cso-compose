package sdmed.extra.cso.bases

import androidx.compose.runtime.Composable
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.interfaces.command.IAsyncEventListener
import sdmed.extra.cso.models.command.AsyncRelayCommand
import sdmed.extra.cso.models.menu.WindowPanelType

abstract class FBaseScreen<T: FBaseViewModel>: IAsyncEventListener {

    abstract val dataContext: T
    @Composable
    fun screen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
               displayFeatures: List<DisplayFeature> = emptyList()) {
        dataContext.relayCommand = AsyncRelayCommand()
        dataContext.addEventListener(this)
        view(windowPanelType, displayFeatures)
    }

    @Composable
    open fun view(windowPanelType: WindowPanelType,
                  displayFeatures: List<DisplayFeature>) {
    }


    override suspend fun onEvent(data: Any?) {
        setLayoutCommand(data)
    }
    open fun setLayoutCommand(data: Any?) {

    }
}