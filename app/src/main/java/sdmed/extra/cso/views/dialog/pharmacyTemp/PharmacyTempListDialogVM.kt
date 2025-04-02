package sdmed.extra.cso.views.dialog.pharmacyTemp

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.retrofit.hospitals.PharmacyTempModel

class PharmacyTempListDialogVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    val items = MutableStateFlow(mutableListOf(PharmacyTempModel()))
}