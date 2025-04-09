package sdmed.extra.cso.models.retrofit.edi

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FDataModelClass
import sdmed.extra.cso.fDate.FDateTime

data class ExtraEDIListResponse(
    var thisPK: String = "",
    var year: String = "",
    var month: String = "",
    var orgName: String = "",
    var tempHospitalPK: String = "",
    var tempOrgName: String = "",
    var ediState: EDIState = EDIState.None,
    var ediType: EDIType = EDIType.DEFAULT,
    var regDate: String = "",
    var pharmaList: MutableList<String> = mutableListOf()
): FDataModelClass<ExtraEDIListResponse.ClickEvent>() {
    val isSelected = MutableStateFlow(false)
    val orgViewName get() = if (ediType == EDIType.DEFAULT) orgName else tempOrgName
    val tempOrgString get() = "(${tempOrgName})"
    val isDefault get() = ediType == EDIType.DEFAULT
    val isNew get() = ediType == EDIType.NEW
    val isTransfer get() = ediType == EDIType.TRANSFER
    fun getYearMonth() = "${year}-$month"
    fun getRegDateString() = FDateTime().setThis(regDate).toString("yyyy-MM")
    @Composable
    fun getEdiColor() = ediState.parseEDIColor()
    @Composable
    fun getEdiBackColor() = ediState.parseEDIBackColor()
    enum class ClickEvent(var index: Int) {
        OPEN(0)
    }
}