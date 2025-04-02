package sdmed.extra.cso.models.retrofit.edi

import androidx.compose.runtime.Composable
import sdmed.extra.cso.bases.FDataModelClass
import sdmed.extra.cso.fDate.FDateTime
import sdmed.extra.cso.utils.FExtensions

data class EDIPharmaDueDateModel(
    var thisPK: String = "",
    var pharmaPK: String = "",
    var orgName: String = "",
    var year: String = "",
    var month: String = "",
    var day: String = "",
    var regDate: String = ""
): FDataModelClass<EDIPharmaDueDateModel.ClickEvent>() {
    val yearMonthDay get() = "${year}-${month}-${day}"
    val dayOfTheWeek get() = FDateTime().setThis("${year}-${month}-${day}", FExtensions.getLocalize()).getLocalizeDayOfWeek(false)
    val parseColor @Composable
    get() = FDateTime().setThis("${year}-${month}-${day}}", FExtensions.getLocalize()).dayOfWeek.parseColor()
    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}