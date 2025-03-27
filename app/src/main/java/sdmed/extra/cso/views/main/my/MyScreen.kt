package sdmed.extra.cso.views.main.my

import androidx.compose.runtime.Composable
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType

@Composable
fun myScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
             displayFeatures: List<DisplayFeature> = emptyList(),
             navigationType: NavigationType = NavigationType.BOTTOM) {
    fBaseScreen<MyScreenVM>({ data, dataContext ->
            setThisCommand(data, dataContext)
        },
        null,
        windowPanelType, navigationType)
}
private fun setThisCommand(data: Any?, dataContext: MyScreenVM) {

}