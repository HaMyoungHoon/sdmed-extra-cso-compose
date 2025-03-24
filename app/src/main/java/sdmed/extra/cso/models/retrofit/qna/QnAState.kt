package sdmed.extra.cso.models.retrofit.qna

import androidx.compose.runtime.Composable
import sdmed.extra.cso.views.theme.FThemeUtil
import sdmed.extra.cso.views.theme.LightColor

enum class QnAState(var index: Int, var desc: String) {
    None(0, "미지정"),
    OK(1, "완료"),
    Recep(2, "접수"),
    Reply(3, "답변");

    fun isEditable(): Boolean {
        return this == Reply
    }
    @Composable
    fun parseQnAColor() = when (this) {
        None -> FThemeUtil.baseColor().qnaStateNone
        OK -> FThemeUtil.baseColor().qnaStateOk
        Recep -> FThemeUtil.baseColor().qnaStateRecep
        Reply -> FThemeUtil.baseColor().qnaStateReply
    }
    @Composable
    fun parseQnABackColor() = when (this) {
        None -> FThemeUtil.baseColor().qnaBackStateNone
        OK -> FThemeUtil.baseColor().qnaBackStateOk
        Recep -> FThemeUtil.baseColor().qnaBackStateRecep
        Reply -> FThemeUtil.baseColor().qnaBackStateReply
    }
}