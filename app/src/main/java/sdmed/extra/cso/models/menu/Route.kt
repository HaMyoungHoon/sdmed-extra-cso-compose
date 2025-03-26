package sdmed.extra.cso.models.menu

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable data class EDI(val thisPK: String = ""): Route
    @Serializable data class PRICE(val thisPK: String = ""): Route
    @Serializable data class HOME(val thisPK: String = ""): Route
    @Serializable data class QNA(val thisPK: String = ""): Route
    @Serializable data class MY(val thisPK: String = ""): Route
}