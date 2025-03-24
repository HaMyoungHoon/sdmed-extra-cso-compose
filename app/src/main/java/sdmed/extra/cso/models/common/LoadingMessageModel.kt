package sdmed.extra.cso.models.common

import androidx.compose.ui.graphics.Color

sealed class LoadingMessageModel {
    data class Visible(val msg: String = "", val indicatorColor: Color? = null, val textColor: Color? = null): LoadingMessageModel()
    object Hidden: LoadingMessageModel()
}