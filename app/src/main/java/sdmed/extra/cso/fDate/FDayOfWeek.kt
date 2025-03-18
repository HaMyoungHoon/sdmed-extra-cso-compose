package sdmed.extra.cso.fDate

enum class FDayOfWeek(val dayOfIndex: Int) {
    NULL(-1),
    SUNDAY(0),
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6);
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