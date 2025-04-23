package sdmed.extra.cso.models.retrofit.edi

import com.fasterxml.jackson.annotation.JsonIgnore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import sdmed.extra.cso.bases.FDataModelClass
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.utils.FExtensions

data class EDIUploadPharmaModel(
    var thisPK: String = "",
    var ediPK: String = "",
    var pharmaPK: String = "",
    var orgName: String = "",
    var year: String = "",
    var month: String = "",
    var day: String = "",
    var isCarriedOver: Boolean = false,
    var ediState: EDIState = EDIState.None,
    var medicineList: MutableList<EDIUploadPharmaMedicineModel> = mutableListOf(),
    var fileList: MutableList<EDIUploadPharmaFileModel> = mutableListOf(),
): FDataModelClass<EDIUploadPharmaModel.ClickEvent>() {
    @Transient
    @JsonIgnore
    var uploadItems: MutableStateFlow<MutableList<MediaPickerSourceModel>> = MutableStateFlow(mutableListOf())
    val isOpen = MutableStateFlow(false)
    val isSavable: StateFlow<Boolean> = FExtensions.stateIn(uploadItems.map { it.isNotEmpty() }, false)

    enum class ClickEvent(var index: Int) {
        OPEN(0),
        ADD(1),
        SAVE(2),
    }
}