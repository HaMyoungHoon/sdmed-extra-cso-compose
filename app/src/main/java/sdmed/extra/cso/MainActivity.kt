package sdmed.extra.cso

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import sdmed.extra.cso.bases.FBaseActivity

class MainActivity: FBaseActivity<MainActivityVM>() {
    override val dataContext: MainActivityVM by viewModels()
    @Composable
    override fun content(dataContext: MainActivityVM) {
    }
}