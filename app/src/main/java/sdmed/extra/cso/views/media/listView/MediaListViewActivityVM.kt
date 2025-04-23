package sdmed.extra.cso.views.media.listView

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.common.MediaViewModel
import sdmed.extra.cso.models.common.MediaViewParcelModel
import sdmed.extra.cso.utils.FExtensions

class MediaListViewActivityVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    val items = MutableStateFlow(mutableListOf<MediaViewModel>())
    val selectedIndex = MutableStateFlow(0)

    fun setItemData(data: ArrayList<MediaViewParcelModel>?) {
        data ?: return
        val ret = mutableListOf<MediaViewModel>()
        data.toMutableList().forEach {
            ret.add(MediaViewModel().parse(it))
        }
        items.value = ret
    }

    enum class ClickEvent(var index: Int) {
        PREV(0),
        NEXT(1)
    }
}