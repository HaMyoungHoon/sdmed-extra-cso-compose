package sdmed.extra.cso.views.navigation

import androidx.navigation.NavHostController
import sdmed.extra.cso.models.menu.MenuItem

class NavigationAction(private val navController: NavHostController) {
    fun navigateTo(menu: MenuItem, clearAll: Boolean) {
        val mother = menu.route.data.mother
        navController.navigate(menu.route.data.path) {
            if (clearAll) {
                popUpTo(menu.route.data.path) {
                    saveState = true
                }
            } else if (!mother.isNullOrBlank()) {
                popUpTo(mother) {
                    saveState = true
                }
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}