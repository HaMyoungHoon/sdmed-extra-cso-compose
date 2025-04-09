package sdmed.extra.cso.models.retrofit.users

import sdmed.extra.cso.bases.FDataModelClass

data class ExtraMyInfoPharma(
    var thisPK: String = "",
    var orgName: String = "",
    var address: String = ""
): FDataModelClass<ExtraMyInfoPharma.ClickEvent>() {

    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}