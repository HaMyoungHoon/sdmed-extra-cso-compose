package sdmed.extra.cso.models.retrofit.users

import sdmed.extra.cso.bases.FDataModelClass

data class ExtraMyInfoHospital(
    var thisPK: String = "",
    var orgName: String = "",
    var address: String = "",
    var pharmaList: MutableList<ExtraMyInfoPharma> = mutableListOf()
): FDataModelClass<ExtraMyInfoHospital.ClickEvent>() {

    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}