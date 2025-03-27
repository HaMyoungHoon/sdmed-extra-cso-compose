package sdmed.extra.cso.views.component.shape

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import sdmed.extra.cso.views.component.vector.FVectorData
import sdmed.extra.cso.views.component.vector.PreviewColorProvider
import sdmed.extra.cso.views.component.vector.vectorCheck
import sdmed.extra.cso.views.component.vector.vectorCircle
import sdmed.extra.cso.views.component.vector.vectorCross
import sdmed.extra.cso.views.component.vector.vectorPlus

@Composable
fun circleCheck(data: FVectorData = FVectorData(), size: Dp = 24.dp) {
    val circle = vectorCircle(data)
    val check = vectorCheck(data)
    val contentDescription: String? = null
    val tint = Color.Unspecified
    Box(contentAlignment = Alignment.Center) {
        Icon(circle, contentDescription, Modifier.size(size), tint)
        Icon(check, contentDescription, Modifier.size(size), tint)
    }
}
@Composable
fun circleCheck(tintColor: Color, fillColor: Color, size: Dp = 24.dp) {
    circleCheck(FVectorData().apply {
        this.tintColor = tintColor
        this.fillColor = fillColor
    }, size)
}
@Composable
fun circlePlus(data: FVectorData, size: Dp = 24.dp) {
    val circle = vectorCheck(data)
    val plus = vectorPlus(data)
    val contentDescription: String? = null
    val tint = Color.Unspecified
    Box(contentAlignment = Alignment.Center) {
        Icon(circle, contentDescription, Modifier.size(size), tint)
        Icon(plus, contentDescription, Modifier.size(size), tint)
    }
}
@Composable
fun circlePlus(tintColor: Color, fillColor: Color, size: Dp = 24.dp) {
    circlePlus(FVectorData().apply {
        this.tintColor = tintColor
        this.fillColor = fillColor
    }, size)
}
@Composable
fun circleCross(data: FVectorData, size: Dp = 24.dp) {
    val circle = vectorCircle(data)
    val cross = vectorCross(data)
    val contentDescription: String? = null
    val tint = Color.Unspecified
    Box(contentAlignment = Alignment.Center) {
        Icon(circle, contentDescription, Modifier.size(size), tint)
        Icon(cross, contentDescription, Modifier.size(size), tint)
    }
}
@Composable
fun circleCross(tintColor: Color, fillColor: Color, size: Dp = 24.dp) {
    circleCross(FVectorData().apply {
        this.tintColor = tintColor
        this.fillColor = fillColor
    }, size)
}

@Preview
@Composable
fun previewCheck(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface { circleCheck(data.first, data.second, 24.dp) }
}
@Preview
@Composable
fun previewPlus(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface { circlePlus(data.first, data.second, 24.dp) }
}
@Preview
@Composable
fun previewCross(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface { circleCross(data.first, data.second, 24.dp) }
}