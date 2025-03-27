package sdmed.extra.cso.views.media

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.calculateDisplayFeatures
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.views.theme.FThemeUtil

class MediaPickerActivity: FBaseActivity<MediaPickerActivityVM>() {
    override val dataContext: MediaPickerActivityVM by viewModels()
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            FThemeUtil.thisTheme {
                setToast()
                setLoading()
                val windowSize = calculateWindowSizeClass(this)
                val displayFeatures = calculateDisplayFeatures(this)
                calcScreen(windowSize, displayFeatures)
            }
        }
    }

    @Composable
    override fun phone(dataContext: MediaPickerActivityVM) {
    }
    @Composable
    override fun tablet(dataContext: MediaPickerActivityVM) {
        super.tablet(dataContext)
    }
    @Composable
    override fun twoPane(dataContext: MediaPickerActivityVM, displayFeatures: List<DisplayFeature>) {
        super.twoPane(dataContext, displayFeatures)
    }

    override fun setLayoutCommand(data: Any?) {

    }
}