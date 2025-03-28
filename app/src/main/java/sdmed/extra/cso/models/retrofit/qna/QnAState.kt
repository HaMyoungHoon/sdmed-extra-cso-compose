package sdmed.extra.cso.models.retrofit.qna

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import sdmed.extra.cso.views.theme.FThemeUtil

enum class QnAState(var index: Int, var desc: String) {
    None(0, "미지정"),
    OK(1, "완료"),
    Recep(2, "접수"),
    Reply(3, "답변");

    fun isEditable(): Boolean {
        return this == Reply
    }
    @Composable
    fun parseQnAColor(): Color {
        val color = FThemeUtil.safeColorC()
        return when (this) {
            None -> color.qnaStateNone
            OK -> color.qnaStateOk
            Recep -> color.qnaStateRecep
            Reply -> color.qnaStateReply
        }
    }
    @Composable
    fun parseQnABackColor(): Color {
        val color = FThemeUtil.safeColorC()
        return when (this) {
            None -> color.qnaBackStateNone
            OK -> color.qnaBackStateOk
            Recep -> color.qnaBackStateRecep
            Reply -> color.qnaBackStateReply
        }
    }
}