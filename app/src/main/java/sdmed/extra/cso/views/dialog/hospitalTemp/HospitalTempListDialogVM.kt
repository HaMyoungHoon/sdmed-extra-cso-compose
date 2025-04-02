package sdmed.extra.cso.views.dialog.hospitalTemp

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel

class HospitalTempListDialogVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    val items = MutableStateFlow(mutableListOf(HospitalTempModel()))
}