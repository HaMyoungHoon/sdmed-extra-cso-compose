package sdmed.extra.cso.views.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Shapes
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import sdmed.extra.cso.bases.FMainApplication
import sdmed.extra.cso.interfaces.theme.IBaseColor
import sdmed.extra.cso.utils.FStorage

class FThemeUtil {
    companion object {
        const val LIGHT_MODE = "light"
        const val DARK_MODE = "dark"
        const val DEFAULT_MODE = "default"

        @Composable
        fun baseColor(darkTheme: Boolean = isSystemInDarkTheme()): IBaseColor {
            val appColor = FMainApplication.appColor.value
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

        @Composable
        fun safeColor(isDark: Boolean = false, darkTheme: Boolean = isSystemInDarkTheme()) =
            if (LocalInspectionMode.current)
                if (isDark) DarkColor else LightColor
            else baseColor(darkTheme)

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
        @Composable
        fun thisTheme(darkTheme: Boolean = isSystemInDarkTheme(),
                      content: @Composable() () -> Unit) {
            val colorScheme = safeColor(darkTheme).materialTheme()
            val view = LocalView.current
            if (!view.isInEditMode) {
                val statusBarColor = safeColor().cardBackground.toArgb()
                SideEffect {
                    val window = (view.context as Activity).window
                    window.statusBarColor = statusBarColor
                    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
                }
            }
            MaterialTheme(colorScheme, Shapes.thisShapes, Typography.thisTypography, content)
        }

        fun textUnit(size: Float, unitType: TextUnitType = TextUnitType.Sp) = TextUnit(size, unitType)

        enum class ThemeMode(val str: String) {
            LIGHT_MODE(FThemeUtil.LIGHT_MODE),
            DARK_MODE(FThemeUtil.DARK_MODE),
            DEFAULT_MODE(FThemeUtil.DEFAULT_MODE)
        }

        object Shapes {
            val thisShapes = Shapes(RoundedCornerShape(4.dp),
                RoundedCornerShape(8.dp),
                RoundedCornerShape(16.dp),
                RoundedCornerShape(24.dp),
                RoundedCornerShape(32.dp))
        }
        object Typography {
            val thisTypography = Typography(
                headlineLarge = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 32.sp,
                    lineHeight = 40.sp,
                    letterSpacing = 0.sp
                ),
                headlineMedium = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 28.sp,
                    lineHeight = 36.sp,
                    letterSpacing = 0.sp
                ),
                headlineSmall = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                    letterSpacing = 0.sp
                ),
                titleLarge = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                    letterSpacing = 0.sp
                ),
                titleMedium = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.15.sp
                ),
                titleSmall = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.1.sp
                ),
                bodyLarge = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.15.sp
                ),
                bodyMedium = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.25.sp
                ),
                bodySmall = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.4.sp
                ),
                labelLarge = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.1.sp
                ),
                labelMedium = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.5.sp
                ),
                labelSmall = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}