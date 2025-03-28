package sdmed.extra.cso.views.component.vector

import androidx.compose.ui.graphics.vector.ImageVector

fun vectorEmpty(data: FVectorData = FVectorData()): ImageVector {
    return fVectorBase(data).build()
}