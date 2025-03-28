package sdmed.extra.cso.views.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavDestination
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import kotlinx.coroutines.launch
import sdmed.extra.cso.models.menu.MenuItem
import sdmed.extra.cso.models.menu.NavigationContentType
import sdmed.extra.cso.models.menu.RouteParser
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun navigationWrapper(navDestination: NavDestination?,
                      navigate: (MenuItem, Boolean) -> Unit,
                      content: @Composable NavSuiteScope.() -> Unit) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val windowSize = with(LocalDensity.current) { currentWindowSize().toSize().toDpSize() }
    val navLayoutType = when {
        adaptiveInfo.windowPosture.isTabletop -> NavigationSuiteType.NavigationBar
        adaptiveInfo.windowSizeClass.isCompact() -> NavigationSuiteType.NavigationBar
        adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED &&
                windowSize.width >= 1200.dp -> NavigationSuiteType.NavigationDrawer
        else -> NavigationSuiteType.NavigationRail
    }
    val navContentPosition = when (adaptiveInfo.windowSizeClass.windowHeightSizeClass) {
        WindowHeightSizeClass.COMPACT -> NavigationContentType.TOP
        WindowHeightSizeClass.MEDIUM,
        WindowHeightSizeClass.EXPANDED -> NavigationContentType.CENTER
        else -> NavigationContentType.TOP
    }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val gesturesEnabled = drawerState.isOpen || navLayoutType == NavigationSuiteType.NavigationRail
    val color = FThemeUtil.safeColorC()

    BackHandler(drawerState.isOpen) {
        coroutineScope.launch {
            drawerState.close()
        }
    }

    val navigation = RouteParser.routeToClass(navDestination?.route).data.navigation
    ModalNavigationDrawer({
        modalDrawerNavigationBar(navDestination, navContentPosition, navigate, {
            coroutineScope.launch {
                drawerState.close()
            }
        })},
        Modifier.background(color.background), drawerState, gesturesEnabled) {
        NavigationSuiteScaffoldLayout({
            if (navigation) {
                when (navLayoutType) {
                    NavigationSuiteType.NavigationBar -> bottomNavigationBar(navDestination, navigate)
                    NavigationSuiteType.NavigationRail -> railNavigationBar(navDestination, navContentPosition, navigate, {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    })
                    NavigationSuiteType.NavigationDrawer -> railNavigationBar(navDestination, navContentPosition, navigate, {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    })
                }
            }
        }, navLayoutType) {
            NavSuiteScope(navLayoutType).content()
        }
    }
}

class NavSuiteScope(val navSuiteType: NavigationSuiteType)
fun NavDestination?.hasRoute(dest: MenuItem) = dest.route.data.thisMyRoute(this?.route)
fun WindowSizeClass.isCompact() = windowWidthSizeClass == WindowWidthSizeClass.COMPACT || windowHeightSizeClass == WindowHeightSizeClass.COMPACT
