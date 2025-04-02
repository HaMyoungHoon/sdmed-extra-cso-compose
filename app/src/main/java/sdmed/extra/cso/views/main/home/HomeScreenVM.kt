package sdmed.extra.cso.views.main.home

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel

class HomeScreenVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {

    val tabIndex = MutableStateFlow(0)
}