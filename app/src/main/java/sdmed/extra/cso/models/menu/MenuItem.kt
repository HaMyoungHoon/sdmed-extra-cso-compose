package sdmed.extra.cso.models.menu

import androidx.compose.ui.graphics.vector.ImageVector
import sdmed.extra.cso.views.component.vector.vectorEmpty

data class MenuItem(
    val route: Route,
    val contentDescription: String = "",
    val selectedIcon: ImageVector = vectorEmpty(),
    val unselectedIcon: ImageVector = vectorEmpty(),
) {
}