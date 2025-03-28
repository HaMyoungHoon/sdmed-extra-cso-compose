package sdmed.extra.cso.views.navigation

import androidx.navigation.NavHostController
import sdmed.extra.cso.models.menu.MenuItem

class NavigationAction(private val navController: NavHostController) {
    fun navigateTo(menu: MenuItem, clearAll: Boolean = false) {
        val mother = menu.route.data.mother
        val current = navController.currentDestination?.route
        navController.navigate(menu.route.data.path) {
            if (clearAll) {
                current?.let {
                    popUpTo(it) {
                        inclusive = true
                    }
                    restoreState = false
                }
            } else if (!mother.isNullOrBlank()) {
                popUpTo(mother) {
                    saveState = true
                }
                restoreState = true
            }
            launchSingleTop = true
        }
    }
}