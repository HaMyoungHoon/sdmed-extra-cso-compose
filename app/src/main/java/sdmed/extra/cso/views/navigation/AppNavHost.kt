package sdmed.extra.cso.views.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.models.menu.MenuItem
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.Route
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.views.main.edi.ediScreen
import sdmed.extra.cso.views.main.home.homeScreen
import sdmed.extra.cso.views.main.landing.landingScreen
import sdmed.extra.cso.views.main.landing.loginScreen
import sdmed.extra.cso.views.main.my.myScreen
import sdmed.extra.cso.views.main.price.priceScreen
import sdmed.extra.cso.views.main.qna.qnaScreen

@Composable
fun appNavHost(navController: NavHostController,
               windowPanelType: WindowPanelType,
               displayFeatures: List<DisplayFeature>,
               navigationType: NavigationType,
               navigate: (MenuItem, Boolean) -> Unit,
               startDestination: String = Route.LANDING.Main.data.path) {
    NavHost(navController, startDestination) {
        composable(Route.LANDING.Main.data.path) { landingScreen(windowPanelType, displayFeatures, navigationType, navigate) }
        composable(Route.LOGIN.Main.data.path) { loginScreen(windowPanelType, displayFeatures, navigationType, navigate) }
        composable(Route.EDI.Main.data.path) { ediScreen(windowPanelType, displayFeatures, navigationType, navigate) }
        composable(Route.PRICE.Main.data.path) { priceScreen(windowPanelType, displayFeatures, navigationType, navigate) }
        composable(Route.HOME.Main.data.path) { homeScreen(windowPanelType, displayFeatures, navigationType, navigate) }
        composable(Route.QNA.Main.data.path) { qnaScreen(windowPanelType, displayFeatures, navigationType, navigate) }
        composable(Route.MY.Main.data.path) { myScreen(windowPanelType, displayFeatures, navigationType, navigate) }
    }
}