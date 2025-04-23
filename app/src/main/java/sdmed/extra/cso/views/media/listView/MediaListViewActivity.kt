package sdmed.extra.cso.views.media.listView

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.common.MediaViewModel
import sdmed.extra.cso.models.common.MediaViewParcelModel
import sdmed.extra.cso.utils.FStorage.getParcelableList
import sdmed.extra.cso.views.theme.FThemeUtil

class MediaListViewActivity: FBaseActivity<MediaListViewActivityVM>() {
    override val dataContext: MediaListViewActivityVM by viewModels()
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        initCommand()
        setContent {
            FThemeUtil.thisTheme {
                setToast()
                setLoading()
                val buffList = intent.getParcelableList<MediaViewParcelModel>(FConstants.MEDIA_LIST)
                dataContext.setItemData(buffList)
                mediaListViewScreen(dataContext)
            }
        }
    }

    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
        setAdapterCommand(data)
    }

    private fun setThisCommand(data: Any?) {
        val eventName = data as? MediaListViewActivityVM.ClickEvent ?: return
        when (eventName) {
            MediaListViewActivityVM.ClickEvent.PREV -> prev()
            MediaListViewActivityVM.ClickEvent.NEXT -> next()
        }
    }
    private fun setAdapterCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? MediaViewModel.ClickEvent ?: return
        val dataBuff = data[1] as? MediaViewModel ?: return
        when (eventName) {
            MediaViewModel.ClickEvent.THIS -> { }
        }
    }
    private fun prev() {
        if (dataContext.selectedIndex.value <= 0) {
            return
        }
        dataContext.selectedIndex.value--
    }
    private fun next() {
        if (dataContext.selectedIndex.value >= dataContext.items.value.count() - 1) {
            return
        }
        dataContext.selectedIndex.value++
    }
}