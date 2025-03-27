package sdmed.extra.cso.views.media

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.bases.FConstants
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
                val buffItem =  intent.getParcelable<MediaViewParcelModel>(FConstants.MEDIA_ITEM)
                dataContext.setItemData(buffItem)
            }
        }
    }

    override fun setLayoutCommand(data: Any?) {

    }
}