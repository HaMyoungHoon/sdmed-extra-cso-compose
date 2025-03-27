package sdmed.extra.cso.views.navigation

import sdmed.extra.cso.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
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
fun permanentDrawerNavigationBar(navDestination: NavDestination?,
                                 navigationContentType: NavigationContentType,
                                 menuClick: (MenuItem) -> Unit,
                                 drawerClick: () -> Unit = {}) {
    val color = FThemeUtil.safeColor()
    ModalDrawerSheet {
        Layout({
            Column(Modifier.layoutId(MenuLayoutType.HEADER),
                Arrangement.spacedBy(4.dp),
                Alignment.CenterHorizontally) {
                Row(Modifier.fillMaxWidth().padding(16.dp),
                    Arrangement.SpaceBetween,
                    Alignment.CenterVertically) {
                    customText(CustomTextData().apply {
                        text = stringResource(R.string.app_name)
                        textColor = color.cardParagraph
                        textSize = FThemeUtil.textUnit(16F)
                    })
                    IconButton(drawerClick) { Icon(vectorMenuOpen(FVectorData(color.cardBackground, color.cardForeground)), stringResource(R.string.menu_desc)) }
                }
            }
            Column(Modifier.layoutId(MenuLayoutType.CONTENT),
                Arrangement.spacedBy(0.dp),
                Alignment.CenterHorizontally) {
                MenuList.getMenuList().forEach { x ->
                    NavigationDrawerItem({
                        customText(CustomTextData().apply {
                            text = x.contentDescription
                            textColor = color.cardForeground
                            textSize = FThemeUtil.textUnit(16F)
                        })
                    }, navDestination.hasRoute(x), { menuClick }, Modifier, {
                        if (navDestination.hasRoute(x)) {
                            Icon(x.selectedIcon, x.contentDescription, tint = Color.Unspecified)
                        } else {
                            Icon(x.unselectedIcon, x.contentDescription, tint = Color.Unspecified)
                        }
                    }, colors = MenuList.navigationDrawerItemColors())
                }
            }},
            Modifier.background(color.cardBackground).padding(16.dp),
            navigationMeasurePolicy(navigationContentType)
        )
    }
}


//@Preview
@Composable
private fun previewScreen() {
    permanentDrawerNavigationBar(null, NavigationContentType.TOP, { })
}