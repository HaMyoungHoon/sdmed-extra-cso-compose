package sdmed.extra.cso.views.main.my

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.kodein.di.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.common.UserTrainingFileSASKeyQueueModel
import sdmed.extra.cso.models.eventbus.EventList
import sdmed.extra.cso.models.retrofit.users.UserTrainingModel
import sdmed.extra.cso.models.services.FBackgroundUserFileUpload
import sdmed.extra.cso.utils.FDI
import sdmed.extra.cso.utils.FEventBus
import sdmed.extra.cso.utils.FExtensions

class MyScreenTrainingCertificateVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    private val backgroundService: FBackgroundUserFileUpload by FDI.di(applicationContext).instance(FBackgroundUserFileUpload::class)
    private val eventChannel = FEventBus.createEventChannel<EventList.UserFileUploadEvent>()
    val trainingList = MutableStateFlow(mutableListOf<UserTrainingModel>())
    val uploadBuff = MutableStateFlow<MediaPickerSourceModel?>(null)
    val trainingDate = MutableStateFlow(FExtensions.getTodayString())
    val isSavable = MutableStateFlow(false)
    val trainingDateSelect = MutableStateFlow(false)
    val imageSelect = MutableStateFlow(false)
    init {
        viewModelScope.launch {
            for (event in eventChannel) {
                loading(false)
            }
        }
    }

    fun setUploadBuff(data: ArrayList<MediaPickerSourceModel>) {
        if (data.isEmpty()) {
            return
        }
        uploadBuff.value = data[0]
        checkSavable()
    }
    fun startBackground() {
        if (!isSavable.value) {
            return
        }
        loading()
        val uploadBuff = uploadBuff.value ?: return
        backgroundService.sasKeyEnqueue(UserTrainingFileSASKeyQueueModel().apply {
            media = uploadBuff
            this.trainingDate = this@MyScreenTrainingCertificateVM.trainingDate.value
        })
    }
    fun checkSavable() {
        isSavable.value = uploadBuff.value != null
    }

    enum class ClickEvent(var index: Int) {
        CLOSE(0),
        TRAINING_DATE(1),
        ADD(2),
        SAVE(3),
    }
}