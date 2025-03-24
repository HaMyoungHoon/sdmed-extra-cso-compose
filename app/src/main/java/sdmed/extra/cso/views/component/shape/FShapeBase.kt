package sdmed.extra.cso.views.component.shape

import androidx.compose.ui.graphics.Color

open class FShapeBase {
    open var name = ""
    open fun getContentDescription(): String? = null
    open fun getTint(): Color = Color.Unspecified
}