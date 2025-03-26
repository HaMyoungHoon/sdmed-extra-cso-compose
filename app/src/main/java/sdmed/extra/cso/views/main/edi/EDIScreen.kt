package sdmed.extra.cso.views.main.edi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import sdmed.extra.cso.interfaces.command.IAsyncEventListener
import sdmed.extra.cso.models.command.AsyncRelayCommand
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun ediScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
              displayFeatures: List<DisplayFeature> = emptyList(),
              dataContext: EDIScreenVM = viewModel()) {
    dataContext.relayCommand = AsyncRelayCommand()
    dataContext.addEventListener(object: IAsyncEventListener {
        override suspend fun onEvent(data: Any?) {
            setThisCommand(data, dataContext)
        }
    })
    when (windowPanelType) {
        WindowPanelType.DUAL_PANE -> dualScreen(displayFeatures, dataContext)
        WindowPanelType.SINGLE_PANE -> singleScreen(dataContext)
    }
}
private fun setThisCommand(data: Any?, dataContext: EDIScreenVM) {

}
@Composable
private fun dualScreen(displayFeatures: List<DisplayFeature>, dataContext: EDIScreenVM) {
    TwoPane({
    }, {

    },
        HorizontalTwoPaneStrategy(0.5F, 16.dp),
        displayFeatures)
}
@Composable
private fun singleScreen(dataContext: EDIScreenVM) {
    val lazyListState = rememberLazyListState()
    val color = FThemeUtil.safeColor()
    Box(Modifier.background(color.background)) {
        Column(Modifier.fillMaxWidth()) {
            Row {
                ediListScreenTopContainer(dataContext.startDate.value,
                    dataContext.endDate.value,
                    dataContext.relayCommand)
            }
            Row(Modifier.weight(1F)) {
                ediListScreenEdiList(dataContext.items.value,
                    lazyListState,
                    dataContext.relayCommand)
            }
        }
    }
}