package sdmed.extra.cso.views.component.shape

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ShapeRoundedBoxData(
    var backgroundColor: Color = Color.Transparent,
    var borderColor: Color = Color.Transparent,
    var modifier: Modifier = Modifier,
    var roundedSize: Dp = 5.dp,
    var borderSize: Dp = 0.dp) {
}