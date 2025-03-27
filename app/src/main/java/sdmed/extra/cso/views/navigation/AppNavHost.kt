package sdmed.extra.cso.views.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.Route
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.views.main.edi.ediScreen
import sdmed.extra.cso.views.main.home.homeScreen
import sdmed.extra.cso.views.main.my.myScreen
import sdmed.extra.cso.views.main.price.priceScreen
import sdmed.extra.cso.views.main.qna.qnaScreen

@Composable
fun appNavHost(navController: NavHostController,
               windowPanelType: WindowPanelType,
               displayFeatures: List<DisplayFeature>,
               navigationType: NavigationType,
               modifier: Modifier = Modifier,
               startDestination: Route = Route.EDI()) {
    NavHost(navController, startDestination, modifier) {
        composable<Route.EDI> {
            ediScreen(windowPanelType, displayFeatures, navigationType)
        }
        composable<Route.PRICE> {
            priceScreen(windowPanelType, displayFeatures, navigationType)
        }
        composable<Route.HOME> {
            homeScreen(windowPanelType, displayFeatures, navigationType)
        }
        composable<Route.QNA> {
            qnaScreen(windowPanelType, displayFeatures, navigationType)
        }
        composable<Route.MY> {
            myScreen(windowPanelType, displayFeatures, navigationType)
        }
    }
}