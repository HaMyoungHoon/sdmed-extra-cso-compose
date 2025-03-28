package sdmed.extra.cso.views.theme

import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.core.view.WindowCompat
import sdmed.extra.cso.bases.FMainApplication
import sdmed.extra.cso.interfaces.theme.IBaseColor
import sdmed.extra.cso.utils.FDI

object FThemeUtil {
    const val LIGHT_MODE = "light"
    const val DARK_MODE = "dark"
    const val DEFAULT_MODE = "default"

    @Composable
    fun baseColorC(darkTheme: Boolean = isSystemInDarkTheme()): IBaseColor {
        val appColor = FMainApplication.appColor.value
        return if (appColor == LIGHT_MODE) {
            FLightColor
        } else if (appColor == DARK_MODE) {
            FDarkColor
        } else if (darkTheme) {
            FDarkColor
        } else {
            FLightColor
        }
    }
    fun baseColor(): IBaseColor {
        val appColor = FMainApplication.appColor.value
        val uiModeManager = FDI.context().getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val darkTheme = (uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES)
        return if (appColor == LIGHT_MODE) {
            FLightColor
        } else if (appColor == DARK_MODE) {
            FDarkColor
        } else if (darkTheme) {
            FDarkColor
        } else {
            FLightColor
        }
    }

    @Composable
    fun safeColorC(isDark: Boolean = false, darkTheme: Boolean = isSystemInDarkTheme()) =
        if (LocalInspectionMode.current)
            if (isDark) FDarkColor else FLightColor
        else baseColorC(darkTheme)
    fun safeColor(isDark: Boolean = false) = baseColor()

    fun applyTheme(mode: FThemeMode? = null) {
        AppCompatDelegate.setDefaultNightMode(when (mode) {
            FThemeMode.LIGHT_MODE -> AppCompatDelegate.MODE_NIGHT_NO
            FThemeMode.DARK_MODE -> AppCompatDelegate.MODE_NIGHT_YES
            else -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            } else {
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            }
        })
    }
    @Composable
    fun thisTheme(darkTheme: Boolean = isSystemInDarkTheme(),
                  content: @Composable() () -> Unit) {
        val colorScheme = safeColorC(darkTheme).materialTheme()
        val view = LocalView.current
        if (!view.isInEditMode) {
            val statusBarColor = safeColorC().cardBackground.toArgb()
            SideEffect {
                val window = (view.context as Activity).window
                window.statusBarColor = statusBarColor
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
            }
        }
        MaterialTheme(colorScheme, FMaterialShape.thisShapes, FMaterialTypography.thisTypography, content)
    }

    fun textUnit(size: Float, unitType: TextUnitType = TextUnitType.Sp) = TextUnit(size, unitType)
}