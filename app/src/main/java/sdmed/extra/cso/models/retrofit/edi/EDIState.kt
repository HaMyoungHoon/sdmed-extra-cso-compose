package sdmed.extra.cso.models.retrofit.edi

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import sdmed.extra.cso.views.theme.FThemeUtil

enum class EDIState(var index: Int, var desc: String) {
    None(0, "미지정"),
    OK(1, "완료"),
    Reject(2, "반려"),
    Pending(3, "보류"),
    Partial(4, "부분");

    fun isEditable(): Boolean {
        return !(this == OK || this == Reject)
    }
    @Composable
    fun parseEDIColor(): Color {
        val color = FThemeUtil.safeColor()
        return when (this) {
            None -> color.ediStateNone
            OK -> color.ediStateOk
            Reject -> color.ediStateReject
            Pending -> color.ediStatePending
            Partial -> color.ediStatePartial
        }
    }
    @Composable
    fun parseEDIBackColor(): Color {
        val color = FThemeUtil.safeColor()
        return when (this) {
            None -> color.ediBackStateNone
            OK -> color.ediBackStateOk
            Reject -> color.ediBackStateReject
            Pending -> color.ediBackStatePending
            Partial -> color.ediBackStatePartial
        }
    }
    companion object {
        fun parseEDIState(index: Int?) = when (index) {
            1 -> OK
            2 -> Reject
            3 -> Pending
            4 -> Partial
            else -> None
        }
        fun isEditable(ediState: EDIState): Boolean {
            return !(ediState == OK || ediState == Reject)
        }
    }
}