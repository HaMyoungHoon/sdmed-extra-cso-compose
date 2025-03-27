package sdmed.extra.cso.views.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import sdmed.extra.cso.models.menu.MenuItem

class NavigationAction(private val navController: NavHostController) {
    fun navigateTo(menu: MenuItem) {
        navController.navigate(menu.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}