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
        return MenuItem(Route.EDI.Main,
            stringResource(R.string.menu_edi_desc),
            vectorMenuEDI(FVectorData(color.cardBackground, color.primary)),
            vectorMenuEDI(FVectorData(color.cardBackground, color.gray)))
    }
    @Composable
    fun menuPrice(): MenuItem {
        val color = FThemeUtil.safeColor()
        return MenuItem(Route.PRICE.Main,
            stringResource(R.string.menu_price_desc),
            vectorMenuPrice(FVectorData(color.cardBackground, color.primary)),
            vectorMenuPrice(FVectorData(color.cardBackground, color.gray)))
    }
    @Composable
    fun menuHome(): MenuItem {
        val color = FThemeUtil.safeColor()
        return MenuItem(Route.HOME.Main,
            stringResource(R.string.menu_home_desc),
            vectorMenuHome(FVectorData(color.background, color.primary)),
            vectorMenuHome(FVectorData(color.background, color.gray)))
    }
    @Composable
    fun menuQNA(): MenuItem {
        val color = FThemeUtil.safeColor()
        return MenuItem(Route.QNA.Main,
            stringResource(R.string.menu_qna_desc),
            vectorMenuQnA(FVectorData(color.cardBackground, color.primary)),
            vectorMenuQnA(FVectorData(color.cardBackground, color.gray)))
    }
    @Composable
    fun menuMY(): MenuItem {
        val color = FThemeUtil.safeColor()
        return MenuItem(Route.MY.Main,
            stringResource(R.string.menu_my_desc),
            vectorMenuMy(FVectorData(color.background, color.primary)),
            vectorMenuMy(FVectorData(color.background, color.gray)))
    }

    @Composable
    fun menuLanding() = MenuItem(Route.LANDING.Main,
        stringResource(R.string.landing_desc))
    @Composable
    fun menuLogin() = MenuItem(Route.LOGIN.Main,
        stringResource(R.string.login_title_desc))
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