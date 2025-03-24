package sdmed.extra.cso.models.retrofit.edi

import androidx.compose.runtime.Composable
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
    fun parseEDIColor() = when (this) {
        None -> FThemeUtil.baseColor().ediStateNone
        OK -> FThemeUtil.baseColor().ediStateOk
        Reject -> FThemeUtil.baseColor().ediStateReject
        Pending -> FThemeUtil.baseColor().ediStatePending
        Partial -> FThemeUtil.baseColor().ediStatePartial
    }
    @Composable
    fun parseEDIBackColor() = when (this) {
        None -> FThemeUtil.baseColor().ediBackStateNone
        OK -> FThemeUtil.baseColor().ediBackStateOk
        Reject -> FThemeUtil.baseColor().ediBackStateReject
        Pending -> FThemeUtil.baseColor().ediBackStatePending
        Partial -> FThemeUtil.baseColor().ediBackStatePartial
    }
    companion object {
        fun parseEDIState(index: Int?) = when (index) {
            1 -> OK
            2 -> Reject
            3 -> Pending
            4 -> Partial
            else -> None
        }
        @Composable
        fun parseEDIColor(ediState: EDIState?) = when (ediState) {
            None -> FThemeUtil.baseColor().ediStateNone
            OK -> FThemeUtil.baseColor().ediStateOk
            Reject -> FThemeUtil.baseColor().ediStateReject
            Pending -> FThemeUtil.baseColor().ediStatePending
            Partial -> FThemeUtil.baseColor().ediStatePartial
            null -> FThemeUtil.baseColor().ediStateNone
        }
        fun isEditable(ediState: EDIState): Boolean {
            return !(ediState == OK || ediState == Reject)
        }
    }
}