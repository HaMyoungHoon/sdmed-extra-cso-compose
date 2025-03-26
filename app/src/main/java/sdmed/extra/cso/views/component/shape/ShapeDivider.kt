package sdmed.extra.cso.views.component.shape

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import sdmed.extra.cso.views.component.vector.PreviewColorProvider

@Composable
fun dividerHorizontal(color: Color) {
    Box(Modifier.fillMaxWidth().height(1.dp).background(color))
}
@Composable
fun dividerVertical(color: Color) {
    Box(Modifier.fillMaxHeight().width(1.dp).background(color))
}

@Preview(showBackground = true)
@Composable
fun previewHorizontal(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    dividerHorizontal(data.second)
}
@Preview(showBackground = true)
@Composable
fun previewVertical(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    dividerVertical(data.second)
}