package sdmed.extra.cso.views.component.shape

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun shapeRoundedBox(data: ShapeRoundedBoxData,
                    content: @Composable BoxScope.() -> Unit) {
    shapeRoundedBox(data.backgroundColor, data.borderColor, data.modifier, data.roundedSize, data.borderSize, content)
}
@Composable
fun shapeRoundedBox(backgroundColor: Color,
                    borderColor: Color = Color.Transparent,
                    modifier: Modifier = Modifier,
                    roundedSize: Dp = 5.dp,
                    borderSize: Dp = 0.dp,
                    content: @Composable BoxScope.() -> Unit) {
    val shape = RoundedCornerShape(roundedSize)
    val border = BorderStroke(borderSize, borderColor)
    val modifier = modifier.background(backgroundColor, shape).border(border, shape)
    Box(modifier) {
        content()
    }
}