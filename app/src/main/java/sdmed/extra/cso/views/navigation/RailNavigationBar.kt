package sdmed.extra.cso.views.navigation

import sdmed.extra.cso.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import sdmed.extra.cso.models.menu.MenuItem
import sdmed.extra.cso.models.menu.MenuLayoutType
import sdmed.extra.cso.models.menu.MenuList
import sdmed.extra.cso.models.menu.NavigationContentType
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.vector.FVectorData
import sdmed.extra.cso.views.component.vector.vectorMenuOpen
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun railNavigationBar(navDestination: NavDestination?,
                      navigationContentType: NavigationContentType,
                      menuClick: (MenuItem, Boolean) -> Unit,
                      drawerClick: () -> Unit = {}) {
    val color = FThemeUtil.safeColor()
    NavigationRail(Modifier.fillMaxHeight(), color.cardBackground) {
        Layout({
            Column(Modifier.layoutId(MenuLayoutType.HEADER),
                Arrangement.spacedBy(4.dp),
                Alignment.CenterHorizontally) {
                NavigationRailItem(false, drawerClick, {
                    Icon(vectorMenuOpen(FVectorData(color.cardBackground, color.cardForeground)), stringResource(R.string.menu_my_desc))
                })
            }
            Column(Modifier.layoutId(MenuLayoutType.CONTENT),
                Arrangement.spacedBy(4.dp),
                Alignment.CenterHorizontally) {
                MenuList.getMenuList().forEach { x ->
                    NavigationRailItem(navDestination.hasRoute(x),
                        { menuClick(x, false) },
                        {
                            if (navDestination.hasRoute(x)) {
                                Icon(x.selectedIcon, x.contentDescription, tint = Color.Unspecified)
                            } else {
                                Icon(x.unselectedIcon, x.contentDescription, tint = Color.Unspecified)
                            }},
                        Modifier,
                        true,
                        { customText(CustomTextData().apply {
                            text = x.contentDescription
                            textColor = color.cardForeground
                            textSize = FThemeUtil.textUnit(12F) }) },
                        colors = MenuList.navigationRailItemColors())
                }
            }},
            Modifier.widthIn(max = 80.dp),
            navigationMeasurePolicy(navigationContentType)
        )
    }
}


//@Preview
@Composable
private fun previewRailNavigationBar() {
    railNavigationBar(null, NavigationContentType.TOP,  { a,b -> })
}