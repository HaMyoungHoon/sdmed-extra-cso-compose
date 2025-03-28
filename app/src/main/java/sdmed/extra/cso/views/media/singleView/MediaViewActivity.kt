package sdmed.extra.cso.views.media.singleView

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.common.MediaViewModel
import sdmed.extra.cso.models.common.MediaViewParcelModel
import sdmed.extra.cso.utils.FStorage.getParcelable
import sdmed.extra.cso.views.theme.FThemeUtil
import kotlin.getValue

class MediaViewActivity: FBaseActivity<MediaViewActivityVM>() {
    override val dataContext: MediaViewActivityVM by viewModels()
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        initCommand()
        setContent {
            FThemeUtil.thisTheme {
                setToast()
                setLoading()
                val buffItem = intent.getParcelable<MediaViewParcelModel>(FConstants.MEDIA_ITEM)
                dataContext.setItemData(buffItem)
                mediaViewScreen(dataContext)
            }
        }
    }

    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
        setAdapterCommand(data)
    }

    private fun setThisCommand(data: Any?) {
        val eventName = data as? MediaViewActivityVM.ClickEvent ?: return
        when (eventName) {
            MediaViewActivityVM.ClickEvent.CLOSE -> close()
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
    private fun close() {
        finish()
    }
}