package sdmed.extra.cso.views.login

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.views.theme.FThemeUtil

class LoginActivity: FBaseActivity<LoginActivityVM>() {
    override val dataContext: LoginActivityVM by viewModels()
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            FThemeUtil.thisTheme {
                setToast()
                setLoading()
            }
        }
    }

    override fun setLayoutCommand(data: Any?) {

    }
}