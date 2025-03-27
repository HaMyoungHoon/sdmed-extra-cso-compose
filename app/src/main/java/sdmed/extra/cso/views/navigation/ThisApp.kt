package sdmed.extra.cso.views.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import sdmed.extra.cso.models.menu.BookPosture
import sdmed.extra.cso.models.menu.IDevicePosture
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.Route
import sdmed.extra.cso.models.menu.Separating
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.models.menu.WindowStateUtils.isBookPosture
import sdmed.extra.cso.models.menu.WindowStateUtils.isSeparating
import sdmed.extra.cso.utils.FComposableDI
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun thisApp(
    windowPanelType: WindowPanelType,
    displayFeatures: List<DisplayFeature>,
    startDest: Route = Route.EDI()) {
    val context = LocalContext.current
    val navHostController = rememberNavController()
    val navigationActions = remember(navHostController) {
        NavigationAction(navHostController)
    }
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val dest = navBackStackEntry?.destination
    val navVisible = FComposableDI.uiStateService(context).isNavigationVisible.collectAsState()
    val color = FThemeUtil.safeColor()

    Surface(Modifier.background(color.background)) {
        if (navVisible.value) {
            navigationWrapper(dest, navigationActions::navigateTo) {
                appNavHost(navHostController, windowPanelType, displayFeatures,
                    navSuiteType.toNavType(),
                    startDestination = startDest)
            }
        }
    }
}

fun getFoldingDevicePosture(displayFeatures: List<DisplayFeature>): IDevicePosture {
    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()
    return when {
        isBookPosture(foldingFeature) -> BookPosture(foldingFeature.bounds)
        isSeparating(foldingFeature) -> Separating(foldingFeature.bounds, foldingFeature.orientation)
        else -> IDevicePosture.NormalPosture
    }
}
fun getWindowPaneType(windowSize: WindowSizeClass, foldingDevicePosture: IDevicePosture) = when (windowSize.widthSizeClass) {
    WindowWidthSizeClass.Compact -> WindowPanelType.SINGLE_PANE
    WindowWidthSizeClass.Medium -> if (foldingDevicePosture != IDevicePosture.NormalPosture) {
        WindowPanelType.DUAL_PANE
    } else {
        WindowPanelType.SINGLE_PANE
    }
    WindowWidthSizeClass.Expanded -> WindowPanelType.DUAL_PANE
    else -> WindowPanelType.SINGLE_PANE
}

private fun NavigationSuiteType.toNavType() = when (this) {
    NavigationSuiteType.NavigationBar -> NavigationType.BOTTOM
    NavigationSuiteType.NavigationRail -> NavigationType.RAIL
    NavigationSuiteType.NavigationDrawer -> NavigationType.DRAWER
    else -> NavigationType.BOTTOM
}