package sdmed.extra.cso.fDate

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import sdmed.extra.cso.views.theme.FThemeUtil

enum class FDayOfWeek(val dayOfIndex: Int) {
    NULL(-1),
    SUNDAY(0),
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6);
    @Composable
    fun parseColor(): Color {
        val color = FThemeUtil.safeColorC()
        return when (this) {
            NULL -> color.foreground
            SUNDAY -> color.sunday
            MONDAY -> color.monday
            TUESDAY -> color.tuesday
            WEDNESDAY -> color.wednesday
            THURSDAY -> color.thursday
            FRIDAY -> color.friday
            SATURDAY -> color.saturday
        }
    }
    companion object {
        fun fromInt(value: Int) = FDayOfWeek.values().firstOrNull { it.dayOfIndex == value } ?: SUNDAY
        fun fromString(value: String?): FDayOfWeek {
            if (value.isNullOrEmpty()) {
                return SUNDAY
            }
            return try {
                FDayOfWeek.values().find { it.dayOfIndex == (value.toInt()) } ?: SUNDAY
            } catch (e: Exception) {
                return SUNDAY
            }
        }
    }
}