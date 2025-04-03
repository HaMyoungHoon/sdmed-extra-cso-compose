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
import sdmed.extra.cso.models.retrofit.edi.EDIApplyDateModel
import sdmed.extra.cso.models.retrofit.edi.EDIHosBuffModel
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaBuffModel
import sdmed.extra.cso.models.retrofit.edi.EDIType
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaMedicineModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaModel
import sdmed.extra.cso.models.services.FBackgroundEDIRequestUpload
import sdmed.extra.cso.utils.FDI
import sdmed.extra.cso.utils.FEventBus
import sdmed.extra.cso.utils.FExtensions

class HomeEDIRequestScreenVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    private val ediRequestRepository: IEDIRequestRepository by FDI.di(applicationContext).instance(IEDIRequestRepository::class)
    private val backgroundService: FBackgroundEDIRequestUpload by FDI.di(applicationContext).instance(FBackgroundEDIRequestUpload::class)
    private val eventChannel = FEventBus.createEventChannel<EventList.EDIUploadEvent>()
    val applyDateModel = MutableStateFlow(mutableListOf<EDIApplyDateModel>())
    val selectApplyDate = MutableStateFlow<EDIApplyDateModel?>(null)
    val hospitalOpen = MutableStateFlow(true)
    val hospitalModel = MutableStateFlow(mutableListOf<EDIHosBuffModel>())
    val selectHospital = MutableStateFlow<EDIHosBuffModel?>(null)
    val searchString = MutableStateFlow<String>("")
    val pharmaModel = MutableStateFlow(mutableListOf<EDIPharmaBuffModel>())
    val pharmaViewModel = MutableStateFlow(mutableListOf<EDIPharmaBuffModel>())
    val isSavable = MutableStateFlow(false)

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
    suspend fun getApplyData(): RestResultT<List<EDIApplyDateModel>> {
        val ret = ediRequestRepository.getApplyDateList()
        if (ret.result == true) {
            applyDateModel.value = ret.data?.toMutableList() ?: mutableListOf()
            if (applyDateModel.value.size == 1) {
                applyDateSelect(applyDateModel.value[0])
            } else {
                applyDateSelect(null)
            }
        } else {
            applyDateSelect(null)
        }
        return ret
    }
    suspend fun getHospitalData(): RestResultT<List<EDIHosBuffModel>> {
        val yearMonthDay = selectApplyDate.value?.yearMonthDay ?: return RestResultT<List<EDIHosBuffModel>>().emptyResult()
        val ret = ediRequestRepository.getHospitalList(yearMonthDay)
        if (ret.result == true) {
            hospitalModel.value = ret.data?.toMutableList() ?: mutableListOf()
            if (this.hospitalModel.value.isNotEmpty()) {
                hospitalSelect(this.hospitalModel.value[0])
            } else {
                hospitalSelect(null)
            }
        } else {
            hospitalSelect(null)
        }
        return ret
    }
    fun getPharmaData() {
        pharmaModel.value = selectHospital.value?.pharmaList ?: mutableListOf()
        pharmaFileClear()
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
        val hospitalData = selectHospital.value ?: return
        val ediUploadModel = EDIUploadModel().apply {
            year = applyDate.year
            month = applyDate.month
            ediType = EDIType.DEFAULT
            regDate = FExtensions.getTodayString()
            hospitalPK = hospitalData.thisPK
            orgName = hospitalData.orgName
        }
        pharmaModel.value.forEach { x ->
            if (x.uploadItems.value.isNotEmpty()) {
                ediUploadModel.pharmaList.add(EDIUploadPharmaModel().apply {
                    this.pharmaPK = x.thisPK
                    this.uploadItems.value = x.uploadItems.value
                    x.medicineList.forEach { y ->
                        this@apply.medicineList.add(EDIUploadPharmaMedicineModel().apply {
                            this.pharmaPK = x.thisPK
                            this.medicinePK = y.thisPK
                        })
                    }
                })
            }
        }
        val data = EDISASKeyQueueModel().apply {
            this.ediUploadModel = ediUploadModel
        }
        backgroundService.sasKeyEnqueue(data)
//        pharmaFileClear()
    }
    suspend fun applyDateSelect(data: EDIApplyDateModel?) {
        selectApplyDate.value?.isSelect?.value = false
        hospitalModel.value = mutableListOf()
        if (data == null) {
            selectApplyDate.value = null
            hospitalSelect(null)
            return
        }
        if (selectApplyDate.value?.thisPK == data.thisPK) {
            selectApplyDate.value = null
            hospitalSelect(null)
            return
        }
        selectApplyDate.value = data
        selectApplyDate.value?.isSelect?.value = true
        getHospitalData()
        savableCheck()
    }
    fun hospitalSelect(data: EDIHosBuffModel?) {
        selectHospital.value?.isSelect?.value = false
        if (data == null) {
            selectHospital.value = null
            pharmaClear()
            return
        }
        if (selectHospital.value?.thisPK == data.thisPK) {
            selectHospital.value = null
            pharmaClear()
            return
        }
        selectHospital.value = data
        selectHospital.value?.isSelect?.value = true
        getPharmaData()
        savableCheck()
    }
    fun pharmaClear() {
        pharmaModel.value = mutableListOf()
        pharmaFileClear()
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
        if (selectHospital.value == null) {
            isSavable.value = false
            return
        }
        if (pharmaModel.value.none { x -> x.uploadItems.value.isNotEmpty() }) {
            isSavable.value = false
            return
        }
        isSavable.value = true
    }
    fun addImage(pharmaBuffPK: String, url: String?, name: String, fileType: MediaFileType, mimeType: String) {
        url ?: return
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
        HOSPITAL_OPEN(1)
    }
}