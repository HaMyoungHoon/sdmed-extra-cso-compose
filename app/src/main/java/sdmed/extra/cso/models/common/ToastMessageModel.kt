package sdmed.extra.cso.models.common

import android.widget.Toast

sealed class ToastMessageModel {
    data class Visible(val msg: String? = null, val duration: Int = Toast.LENGTH_SHORT): ToastMessageModel()
    object Hidden: ToastMessageModel()
}