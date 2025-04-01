package sdmed.extra.cso.models.retrofit.edi

import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FDataModelClass
import sdmed.extra.cso.fDate.FDateTime

data class EDIUploadResponseModel(
    var thisPK: String = "",
    var ediPK: String = "",
    var pharmaPK: String = "",
    var pharmaName: String = "",
    var userPK: String = "",
    var userName: String = "",
    var etc: String = "",
    var ediState: EDIState = EDIState.None,
    var regDate: String = "",
): FDataModelClass<EDIUploadResponseModel.ClickEvent>() {
    var isOpen = MutableStateFlow(false)

    fun getResponseDate() = FDateTime().setThis(regDate).toString("yyyy-MM")
//    fun getEdiColor() = ediState.parseEDIColor()
//    fun getEdiBackColor() = ediState.parseEDIBackColor()

    enum class ClickEvent(var index: Int) {
        OPEN(0)
    }
}