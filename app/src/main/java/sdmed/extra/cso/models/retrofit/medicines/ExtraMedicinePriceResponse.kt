package sdmed.extra.cso.models.retrofit.medicines

import sdmed.extra.cso.bases.FDataModelClass

data class ExtraMedicinePriceResponse(
    var thisPK: String = "",
    var mainIngredientCode: String = "",
    var mainIngredientName: String = "",
    var clientName: String? = null,
    var makerName: String? = null,
    var orgName: String = "",
    var kdCode: String = "",
    var customPrice: Int = 0,
    var maxPrice: Int = 0,
    var standard: String = "",
    var etc1: String = "",
): FDataModelClass<ExtraMedicinePriceResponse.ClickEvent>() {
    val maxPriceString: String get() = "$maxPrice"

    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}