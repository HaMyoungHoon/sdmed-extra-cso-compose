package sdmed.extra.cso.views.main.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.models.menu.MenuItem
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText

@Composable
fun homeEDIRequestScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                         displayFeatures: List<DisplayFeature> = emptyList(),
                         navigationType: NavigationType = NavigationType.BOTTOM,
                         navigate: (MenuItem, Boolean) -> Unit) {
    val dataContext = fBaseScreen<HomeEDIRequestScreenVM>( { data, dataContext -> setLayoutCommand(data, dataContext, navigate) },
        null,
        windowPanelType, navigationType)
    Box(Modifier.fillMaxSize()) {
        customText(CustomTextData().apply {
            text = "home edi request"
            modifier = Modifier.align(Alignment.Center)
        })
    }
}

private fun setLayoutCommand(data: Any?, dataContext: HomeEDIRequestScreenVM, navigate: (MenuItem, Boolean) -> Unit) {

}