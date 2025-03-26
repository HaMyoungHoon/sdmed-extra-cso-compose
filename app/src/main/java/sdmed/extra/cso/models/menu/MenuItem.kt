package sdmed.extra.cso.models.menu

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val route: Route,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val contentDescription: String,
) {
}