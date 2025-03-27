package sdmed.extra.cso.views.dialog.message

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel

class MessageDialogVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    val data = MutableStateFlow(MessageDialogData())

    enum class ClickEvent(var index: Int) {
        CLOSE(0),
        LEFT(1),
        RIGHT(2)
    }
}