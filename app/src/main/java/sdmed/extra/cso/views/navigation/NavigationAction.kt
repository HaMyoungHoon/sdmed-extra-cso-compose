package sdmed.extra.cso.views.navigation

import androidx.navigation.NavHostController
import sdmed.extra.cso.models.menu.MenuItem
import sdmed.extra.cso.utils.FLog

class NavigationAction(private val navController: NavHostController) {
    fun navigateTo(menu: MenuItem, clearAll: Boolean = false) {
        val mother = menu.route.data.mother
        if (clearAll) {
            var popback = navController.popBackStack()
            while (popback) {
                popback = navController.popBackStack()
            }
            navController.navigate(menu.route.data.path)
            return
        }
        navController.navigate(menu.route.data.path) {
            if (!mother.isNullOrBlank()) {
                popUpTo(mother) {
                    saveState = true
                }
                restoreState = true
            }
            launchSingleTop = true
        }
    }
}