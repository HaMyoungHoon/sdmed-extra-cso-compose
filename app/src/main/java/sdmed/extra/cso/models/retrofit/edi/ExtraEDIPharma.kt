package sdmed.extra.cso.models.retrofit.edi

import com.fasterxml.jackson.annotation.JsonIgnore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import sdmed.extra.cso.bases.FDataModelClass
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.utils.FExtensions

data class ExtraEDIPharma(
    var thisPK: String = "",
    var ediPK: String = "",
    var pharmaPK: String = "",
    var orgName: String = "",
    var year: String = "",
    var month: String = "",
    var day: String = "",
    var isCarriedOver: Boolean = false,
    var ediState: EDIState = EDIState.None,
    var fileList: MutableList<EDIUploadPharmaFileModel> = mutableListOf(),
): FDataModelClass<ExtraEDIPharma.ClickEvent>() {
    @Transient
    @JsonIgnore
    var uploadItems: MutableStateFlow<MutableList<MediaPickerSourceModel>> = MutableStateFlow(mutableListOf())
    val isAddable get() = ediState.isEditable()
    val isOpen = MutableStateFlow(false)
    val isSavable: StateFlow<Boolean> = FExtensions.stateIn(uploadItems.map { it.isNotEmpty() }, false)
    fun getYearMonth() = "${year}-${month}"

    fun toEDIUploadPharmaModel(): EDIUploadPharmaModel {
        val ret = EDIUploadPharmaModel()
        ret.thisPK = this.thisPK
        ret.ediPK = this.ediPK
        ret.pharmaPK = this.pharmaPK
        ret.orgName = this.orgName
        ret.year = this.year
        ret.month = this.month
        ret.day = this.day
        ret.isCarriedOver = this.isCarriedOver
        ret.ediState = this.ediState
        ret.uploadItems = this.uploadItems
        return ret
    }
    enum class ClickEvent(var index: Int) {
        OPEN(0),
        ADD(1),
        SAVE(2),
    }
}