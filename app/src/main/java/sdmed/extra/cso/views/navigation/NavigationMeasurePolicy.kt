package sdmed.extra.cso.views.navigation

import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.offset
import sdmed.extra.cso.models.menu.MenuLayoutType
import sdmed.extra.cso.models.menu.NavigationContentType

fun navigationMeasurePolicy(navigationContentType: NavigationContentType): MeasurePolicy {
    return MeasurePolicy { measurables, constraints ->
        lateinit var headerMeasurable: Measurable
        lateinit var contentMeasurable: Measurable
        measurables.forEach { x ->
            when (x.layoutId) {
                MenuLayoutType.HEADER -> headerMeasurable = x
                MenuLayoutType.CONTENT -> contentMeasurable = x
                else -> error("unknown layout")
            }
        }
        val headerPlaceable = headerMeasurable.measure(constraints)
        val contentPlaceable = contentMeasurable.measure(constraints.offset(vertical = -headerPlaceable.height))
        layout(constraints.maxWidth, constraints.maxHeight) {
            headerPlaceable.placeRelative(0, 0)
            val nonContentVerticalSpace = constraints.maxHeight - contentPlaceable.height
            val contentPlaceableY = when (navigationContentType) {
                NavigationContentType.TOP -> 0
                NavigationContentType.CENTER -> nonContentVerticalSpace / 2
            }.coerceAtLeast(headerPlaceable.height)
            contentPlaceable.placeRelative(0, contentPlaceableY)
        }
    }
}