package sdmed.extra.cso.views.theme

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import sdmed.extra.cso.interfaces.theme.IBaseColor
import sdmed.extra.cso.utils.FStorage

class FThemeUtil {
    companion object {
        const val LIGHT_MODE = "light"
        const val DARK_MODE = "dark"
        const val DEFAULT_MODE = "default"

        @Composable
        fun baseColor(darkTheme: Boolean = isSystemInDarkTheme()): IBaseColor {
            val appColor = FStorage.getAppColor(LocalContext.current)
            return if (appColor == LIGHT_MODE) {
                LightColor
            } else if (appColor == DARK_MODE) {
                DarkColor
            } else if (darkTheme) {
                DarkColor
            } else {
                LightColor
            }
        }

        fun applyTheme(mode: ThemeMode? = null) {
            AppCompatDelegate.setDefaultNightMode(when (mode) {
                ThemeMode.LIGHT_MODE -> AppCompatDelegate.MODE_NIGHT_NO
                ThemeMode.DARK_MODE -> AppCompatDelegate.MODE_NIGHT_YES
                else -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                } else {
                    AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                }
            })
        }

        enum class ThemeMode(val str: String) {
            LIGHT_MODE(FThemeUtil.LIGHT_MODE),
            DARK_MODE(FThemeUtil.DARK_MODE),
            DEFAULT_MODE(FThemeUtil.DEFAULT_MODE)
        }
    }
}