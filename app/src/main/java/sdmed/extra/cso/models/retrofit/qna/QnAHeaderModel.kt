package sdmed.extra.cso.models.retrofit.qna

import sdmed.extra.cso.bases.FDataModelClass
import sdmed.extra.cso.fDate.FDateTime
import java.sql.Timestamp
import java.util.Date

data class QnAHeaderModel(
    var thisPK: String = "",
    var userPK: String = "",
    var name: String = "",
    var title: String = "",
    var regDate: Timestamp = Timestamp(Date().time),
    var qnaState: QnAState = QnAState.None,
): FDataModelClass<QnAHeaderModel.ClickEvent>() {
    val regDateString get() = FDateTime().setThis(regDate.time).toString("yyyy-MM-dd hh:mm:ss")

    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}