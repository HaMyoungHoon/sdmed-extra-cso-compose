package sdmed.extra.cso.models.services

import android.widget.Toast
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.models.common.LoadingMessageModel
import sdmed.extra.cso.models.common.ToastMessageModel

class FUIStateService {
    val isNavigationVisible = MutableStateFlow(false)
    val isLoading = MutableStateFlow<LoadingMessageModel>(LoadingMessageModel.Hidden)
    val toast = MutableStateFlow<ToastMessageModel>(ToastMessageModel.Hidden)

    fun loading(isVisible: Boolean = true, msg: String = "") {
        if (isVisible) {
            isLoading.value = LoadingMessageModel.Visible(msg)
        } else {
            isLoading.value = LoadingMessageModel.Hidden
        }
    }
    fun toast(msg: String?, duration: Int = Toast.LENGTH_SHORT) {
        if (msg.isNullOrEmpty()) {
            return
        }
        toast.value = ToastMessageModel.Visible(msg, duration)
    }
    fun unToast() {
        toast.value = ToastMessageModel.Hidden
    }
}