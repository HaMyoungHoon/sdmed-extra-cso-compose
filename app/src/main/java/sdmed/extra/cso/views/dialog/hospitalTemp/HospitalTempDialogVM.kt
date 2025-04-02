package sdmed.extra.cso.views.dialog.hospitalTemp

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel

class HospitalTempDialogVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    val item = MutableStateFlow(HospitalTempModel())

    enum class ClickEvent(var index: Int) {
        THIS(0),
        WEB_SITE(1),
        PHONE_NUMBER(2),
    }
}