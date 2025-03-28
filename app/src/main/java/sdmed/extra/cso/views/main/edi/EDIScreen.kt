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
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.models.menu.MenuItem
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun ediScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
              displayFeatures: List<DisplayFeature> = emptyList(),
              navigationType: NavigationType = NavigationType.BOTTOM,
              navigate: (MenuItem, Boolean) -> Unit) {
    val dataContext = fBaseScreen<EDIScreenVM>( { data, dataContext -> setLayoutCommand(data, dataContext) },
        null,
        windowPanelType, navigationType,
        { dataContext -> twoPaneScreen(displayFeatures, dataContext) },
        { dataContext -> phoneScreen(dataContext) },
        { dataContext -> tabletScreen(dataContext) })
}
private fun setLayoutCommand(data: Any?, dataContext: EDIScreenVM) {

}
@Composable
private fun twoPaneScreen(displayFeatures: List<DisplayFeature>, dataContext: EDIScreenVM) {
    TwoPane({
    }, {

    },
        HorizontalTwoPaneStrategy(0.5F, 16.dp),
        displayFeatures)
}
@Composable
private fun phoneScreen(dataContext: EDIScreenVM) {
    val lazyListState = rememberLazyListState()
    val color = FThemeUtil.safeColorC()
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
@Composable
private fun tabletScreen(dataContext: EDIScreenVM) {
    val lazyListState = rememberLazyListState()
    val color = FThemeUtil.safeColorC()
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