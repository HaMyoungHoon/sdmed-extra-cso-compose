package sdmed.extra.cso.views.main.price

import androidx.compose.runtime.Composable
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType

@Composable
fun priceScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                displayFeatures: List<DisplayFeature> = emptyList(),
                navigationType: NavigationType = NavigationType.BOTTOM) {
    fBaseScreen<PriceScreenVM>({ data, dataContext ->
            setThisCommand(data, dataContext)
        },
        null,
        windowPanelType, navigationType)
}
private fun setThisCommand(data: Any?, dataContext: PriceScreenVM) {

}