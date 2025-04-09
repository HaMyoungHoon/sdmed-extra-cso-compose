package sdmed.extra.cso.views.main.home

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.kodein.di.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IEDIRequestRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.common.EDISASKeyQueueModel
import sdmed.extra.cso.models.common.MediaFileType
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.eventbus.EventList
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaBuffModel
import sdmed.extra.cso.models.retrofit.edi.EDIType
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaModel
import sdmed.extra.cso.models.retrofit.edi.ExtraEDIApplyDateResponse
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel
import sdmed.extra.cso.models.services.FBackgroundEDIRequestNewUploadService
import sdmed.extra.cso.utils.FDI
import sdmed.extra.cso.utils.FEventBus
import sdmed.extra.cso.utils.FExtensions

class HomeEDIRequestNewScreenVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    private val ediRequestRepository: IEDIRequestRepository by FDI.di(applicationContext).instance(IEDIRequestRepository::class)
    private val backgroundService: FBackgroundEDIRequestNewUploadService by FDI.di(applicationContext).instance(FBackgroundEDIRequestNewUploadService::class)
    private val eventChannel = FEventBus.createEventChannel<EventList.EDIUploadEvent>()
    val ediTypeModel = MutableStateFlow(mutableListOf<EDIType>())
    val selectEDITypePosition = MutableStateFlow(0)
    val applyDateModel = MutableStateFlow(mutableListOf<ExtraEDIApplyDateResponse>())
    val selectApplyDate = MutableStateFlow<ExtraEDIApplyDateResponse?>(null)
    val tempHospitalPK = MutableStateFlow<String>("")
    val tempOrgName = MutableStateFlow<String>("")
    val selectHospitalBuff = MutableStateFlow(HospitalTempModel())

    val searchString = MutableStateFlow<String>("")
    val pharmaModel = MutableStateFlow(mutableListOf<EDIPharmaBuffModel>())
    val pharmaViewModel = MutableStateFlow(mutableListOf<EDIPharmaBuffModel>())
    val isSavable = MutableStateFlow(false)

    val hospitalFind = MutableStateFlow(false)
    val addPharmaFilePK = MutableStateFlow<String?>(null)
    val navigateEDIList = MutableStateFlow(false)
    init {
        viewModelScope.launch {
            for (event in eventChannel) {
                if (event.thisPK.isNotEmpty()) {
                    navigateEDIList.value = true
                }
                loading(false)
            }
        }
    }

    suspend fun getData(): RestResultT<List<ExtraEDIApplyDateResponse>> {
        val ret = ediRequestRepository.getApplyDateList()
        if (ret.result == true) {
            applyDateModel.value = ret.data?.toMutableList() ?: mutableListOf()
        }
        return ret
    }
    suspend fun getPharmaList(): RestResultT<List<EDIPharmaBuffModel>> {
        val ret = ediRequestRepository.getPharmaList()
        if (ret.result == true) {
            pharmaModel.value = ret.data?.toMutableList() ?: mutableListOf()
            pharmaViewModel.value = pharmaModel.value
        }
        return ret
    }
    fun pharmaFileClear() {
        this.pharmaModel.value.forEach { x -> x.uploadItems.value = mutableListOf() }
        if (this.searchString.value == "") {
            filterItem()
        }
        this.searchString.value = ""
    }
    fun startBackgroundService() {
        val applyDate = selectApplyDate.value ?: return
        val ediUploadModel = EDIUploadModel().apply {
            year = applyDate.year
            month = applyDate.month
            ediType = ediTypeModel.value[selectEDITypePosition.value]
            if (selectHospitalBuff.value.orgName == this@HomeEDIRequestNewScreenVM.tempOrgName.value) {
                this.tempHospitalPK = this@HomeEDIRequestNewScreenVM.tempHospitalPK.value
            }
            this.tempOrgName = this@HomeEDIRequestNewScreenVM.tempOrgName.value
            regDate = FExtensions.getTodayString()
        }
        pharmaModel.value.forEach { x ->
            if (x.uploadItems.value.isNotEmpty()) {
                ediUploadModel.pharmaList.add(EDIUploadPharmaModel().apply {
                    this.pharmaPK = x.thisPK
                    this.uploadItems.value = x.uploadItems.value
                })
            }
        }
        val data = EDISASKeyQueueModel().apply {
            this.ediUploadModel = ediUploadModel
        }
        backgroundService.sasKeyEnqueue(data)
//        pharmaFileClear()
    }
    fun applyDateSelect(data: ExtraEDIApplyDateResponse) {
        if (selectApplyDate.value == data) {
            selectApplyDate.value?.isSelect?.value = false
            selectApplyDate.value = null
            savableCheck()
            return
        }
        selectApplyDate.value?.isSelect?.value = false
        selectApplyDate.value = data
        selectApplyDate.value?.isSelect?.value = true
        savableCheck()
    }
    fun setHospitalTemp(hospitalTempModel: HospitalTempModel) {
        tempHospitalPK.value = hospitalTempModel.thisPK
        tempOrgName.value = hospitalTempModel.orgName
        selectHospitalBuff.value = hospitalTempModel
        savableCheck()
    }
    fun filterItem() {
        val searchBuff = searchString.value
        if (searchBuff.isEmpty()) {
            pharmaViewModel.value = pharmaModel.value.toMutableList()
            return
        }

        pharmaViewModel.value = pharmaModel.value.filter { x -> x.orgName.contains(searchBuff, true) }.toMutableList()
    }
    fun savableCheck() {
        if (selectApplyDate.value == null) {
            isSavable.value = false
            return
        }
        if (tempOrgName.value.isBlank()) {
            isSavable.value = false
            return
        }
        if (pharmaModel.value.none { x -> x.uploadItems.value.isNotEmpty() }) {
            isSavable.value = false
            return
        }
        isSavable.value = true
    }

    fun addImage(pharmaBuffPK: String, url: String, name: String, fileType: MediaFileType, mimeType: String) {
        url
        val buff = this.pharmaModel.value.toMutableList()

        try {
            val findBuff = buff.find { x -> x.thisPK == pharmaBuffPK } ?: return
            val imageBuff = findBuff.uploadItems.value.toMutableList()
            imageBuff.add(MediaPickerSourceModel().apply {
                mediaUrl = url
                mediaName = name
                mediaFileType = fileType
                mediaMimeType = mimeType
            })
            findBuff.uploadItems.value = imageBuff
            findBuff.isOpen.value = true
            this.pharmaModel.value = buff
        } catch (_: Exception) {
        }
        savableCheck()
    }
    fun delImage(imagePK: String) {
        val buff = this.pharmaModel.value.toMutableList()
        val findBuff = buff.find { x -> x.uploadItems.value.find { y -> y.thisPK == imagePK } != null } ?: return
        findBuff.uploadItems.value = findBuff.uploadItems.value.filter { it.thisPK != imagePK }.toMutableList()
        this.pharmaModel.value = buff
    }
    fun reSetImage(pharmaBuffPK: String, mediaList: ArrayList<MediaPickerSourceModel>?) {
        val buff = this.pharmaModel.value.toMutableList()
        val findPharma = buff.find { x -> x.thisPK == pharmaBuffPK }
        findPharma?.uploadItems?.value = mediaList?.toMutableList() ?: mutableListOf()
        findPharma?.isOpen?.value = true
        this.pharmaModel.value = buff
        savableCheck()
    }

    enum class ClickEvent(var index: Int) {
        SAVE(0),
        HOSPITAL_FIND(1),
    }
}