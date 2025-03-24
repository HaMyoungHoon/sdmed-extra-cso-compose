package sdmed.extra.cso.views.landing

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import sdmed.extra.cso.bases.FBaseActivity

class LandingActivity: FBaseActivity<LandingActivityVM>() {
    override val dataContext: LandingActivityVM by viewModels()
    @Composable
    override fun content(dataContext: LandingActivityVM) {
    }
}