package sdmed.extra.cso.views.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import sdmed.extra.cso.models.menu.MenuItem
import sdmed.extra.cso.models.menu.MenuList
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun bottomNavigationBar(navDestination: NavDestination?, navigateTo: (MenuItem, Boolean) -> Unit) {
    val color = FThemeUtil.safeColorC()
    NavigationBar(Modifier.fillMaxWidth().height(50.dp), color.cardBackground) {
        MenuList.getMenuList().forEach { x ->
            NavigationBarItem(navDestination.hasRoute(x), { navigateTo(x, false) }, {
                if (navDestination.hasRoute(x)) {
                    Icon(x.selectedIcon, x.contentDescription, tint = Color.Unspecified)
                } else {
                    Icon(x.unselectedIcon, x.contentDescription, tint = Color.Unspecified)
                }
            }, colors = MenuList.navigationBarItemColors())
        }
    }
}

//@Preview
@Composable
private fun previewBottomNavigationBar() {
    bottomNavigationBar(null) { a, b -> }
}