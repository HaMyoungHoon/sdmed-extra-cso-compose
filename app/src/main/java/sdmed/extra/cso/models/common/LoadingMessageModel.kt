package sdmed.extra.cso.models.common

sealed class LoadingMessageModel {
    data class Visible(val msg: String = ""): LoadingMessageModel()
    object Hidden: LoadingMessageModel()
}