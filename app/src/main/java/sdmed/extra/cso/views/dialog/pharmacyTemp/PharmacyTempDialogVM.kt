package sdmed.extra.cso.views.dialog.pharmacyTemp

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.retrofit.hospitals.PharmacyTempModel

class PharmacyTempDialogVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    val item = MutableStateFlow(PharmacyTempModel())
    enum class ClickEvent(var index: Int) {
        THIS(0),
        PHONE_NUMBER(1),
    }
}