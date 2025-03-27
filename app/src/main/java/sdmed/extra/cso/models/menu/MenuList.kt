package sdmed.extra.cso.models.menu

import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailItemColors
import sdmed.extra.cso.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import sdmed.extra.cso.views.component.vector.FVectorData
import sdmed.extra.cso.views.component.vector.vectorMenuEDI
import sdmed.extra.cso.views.component.vector.vectorMenuHome
import sdmed.extra.cso.views.component.vector.vectorMenuMy
import sdmed.extra.cso.views.component.vector.vectorMenuPrice
import sdmed.extra.cso.views.component.vector.vectorMenuQnA
import sdmed.extra.cso.views.theme.FThemeUtil

object MenuList {
    @Composable
    fun getMenuList(): List<MenuItem> {
        return listOf(menuEDI(), menuPrice(), menuHome(), menuQNA(), menuMY())
    }
    @Composable
    fun menuEDI(): MenuItem {
        val color = FThemeUtil.safeColor()
        return MenuItem(Route.EDI(),
            vectorMenuEDI(FVectorData(color.cardBackground, color.primary)),
            vectorMenuEDI(FVectorData(color.cardBackground, color.gray)),
            stringResource(R.string.menu_edi_desc))
    }
    @Composable
    fun menuPrice(): MenuItem {
        val color = FThemeUtil.safeColor()
        return MenuItem(Route.PRICE(),
            vectorMenuPrice(FVectorData(color.cardBackground, color.primary)),
            vectorMenuPrice(FVectorData(color.cardBackground, color.gray)),
            stringResource(R.string.menu_price_desc))
    }
    @Composable
    fun menuHome(): MenuItem {
        val color = FThemeUtil.safeColor()
        return MenuItem(Route.HOME(),
            vectorMenuHome(FVectorData(color.background, color.primary)),
            vectorMenuHome(FVectorData(color.background, color.gray)),
            stringResource(R.string.menu_home_desc))
    }
    @Composable
    fun menuQNA(): MenuItem {
        val color = FThemeUtil.safeColor()
        return MenuItem(Route.QNA(),
            vectorMenuQnA(FVectorData(color.cardBackground, color.primary)),
            vectorMenuQnA(FVectorData(color.cardBackground, color.gray)),
            stringResource(R.string.menu_qna_desc))
    }
    @Composable
    fun menuMY(): MenuItem {
        val color = FThemeUtil.safeColor()
        return MenuItem(Route.MY(),
            vectorMenuMy(FVectorData(color.background, color.primary)),
            vectorMenuMy(FVectorData(color.background, color.gray)),
            stringResource(R.string.menu_my_desc))
    }
    fun navigationBarItemColors(): NavigationBarItemColors {
        return NavigationBarItemColors(Color.Transparent,
            Color.Transparent,
            Color.Transparent,
            Color.Transparent,
            Color.Transparent,
            Color.Transparent,
            Color.Transparent)
    }
    fun navigationRailItemColors(): NavigationRailItemColors {
        return NavigationRailItemColors(Color.Transparent,
            Color.Transparent,
            Color.Transparent,
            Color.Transparent,
            Color.Transparent,
            Color.Transparent,
            Color.Transparent)
    }
    @Composable
    fun navigationDrawerItemColors(): NavigationDrawerItemColors {
        return NavigationDrawerItemDefaults.colors(Color.Transparent,
            Color.Transparent,
            Color.Transparent,
            Color.Transparent,
            Color.Transparent,
            Color.Transparent,
            Color.Transparent,
            Color.Transparent)
    }
}