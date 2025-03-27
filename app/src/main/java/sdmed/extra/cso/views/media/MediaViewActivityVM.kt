package sdmed.extra.cso.views.media

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.common.MediaViewModel
import sdmed.extra.cso.models.common.MediaViewParcelModel

class MediaViewActivityVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    val title = MutableStateFlow<String>("")
    val item = MutableStateFlow(MediaViewModel())

    fun setItemData(data: MediaViewParcelModel?) {
        data ?: return
        item.value = MediaViewModel().parse(data)
    }

    enum class ClickEvent(var index: Int) {
        CLOSE(0)
    }
}