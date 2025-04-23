package sdmed.extra.cso.views.main.edi

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.kodein.di.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IEDIListRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.common.EDISASKeyQueueModel
import sdmed.extra.cso.models.common.MediaFileType
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.common.MediaViewParcelModel
import sdmed.extra.cso.models.eventbus.EventList
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaFileModel
import sdmed.extra.cso.models.retrofit.edi.ExtraEDIDetailResponse
import sdmed.extra.cso.models.retrofit.edi.ExtraEDIPharma
import sdmed.extra.cso.models.services.FBackgroundEDIFileUpload
import sdmed.extra.cso.utils.FDI
import sdmed.extra.cso.utils.FEventBus

class EDIScreenDetailVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    private val ediListRepository: IEDIListRepository by FDI.di(applicationContext).instance(IEDIListRepository::class)
    private val backgroundService: FBackgroundEDIFileUpload by FDI.di(applicationContext).instance(FBackgroundEDIFileUpload::class)
    private val eventChannel = FEventBus.createEventChannel<EventList.EDIUploadEvent>()

    val thisPK = MutableStateFlow("")
    val item = MutableStateFlow(ExtraEDIDetailResponse())
    val closeAble = MutableStateFlow(true)
    val addPharmaFilePK = MutableStateFlow<String?>(null)
    val hospitalTempDetail = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            for (event in eventChannel) {
                loading(false)
                getData()
                closeAble.value = true
            }
        }
    }
    fun reSet() {
        thisPK.value = ""
        item.value = ExtraEDIDetailResponse()
    }
    suspend fun getData(): RestResultT<ExtraEDIDetailResponse> {
        if (thisPK.value.isEmpty()) {
            return RestResultT<ExtraEDIDetailResponse>().emptyResult()
        }
        val ret = ediListRepository.getData(thisPK.value)
        if (ret.result == true) {
            item.value = ret.data ?: ExtraEDIDetailResponse()
        }
        return ret
    }
    fun getMediaViewPharmaFiles(data: EDIUploadPharmaFileModel): ArrayList<MediaViewParcelModel> {
        val buff = item.value.pharmaList.toMutableList()
        val ret = arrayListOf<MediaViewParcelModel>()
        val findBuff = buff.find { x -> x.fileList.find { y -> y.thisPK == data.thisPK } != null }
        findBuff?.fileList?.forEach { x ->
            ret.add(MediaViewParcelModel().parse(x))
        }
        return ret
    }
    fun addImage(pharmaBuffPK: String, uri: Uri?, name: String, fileType: MediaFileType, mimeType: String) {
        uri ?: return
        val buff = item.value.pharmaList.toMutableList()
        try {
            val findBuff = buff.find { x -> x.thisPK == pharmaBuffPK } ?: return
            val imageBuff = findBuff.uploadItems.value.toMutableList()
            imageBuff.add(MediaPickerSourceModel().apply {
                mediaUri = uri
                mediaName = name
                mediaFileType = fileType
                mediaMimeType = mimeType
            })
            findBuff.uploadItems.value = imageBuff
            item.value.pharmaList = buff
        } catch (_: Exception) {
        }
    }
    fun delImage(imagePK: String) {
        val buff = item.value.pharmaList.toMutableList()
        val findBuff = buff.find { x -> x.uploadItems.value.find { y -> y.thisPK == imagePK } != null } ?: return
        findBuff.uploadItems.value = findBuff.uploadItems.value.filter { it.thisPK != imagePK }.toMutableList()
        item.value.pharmaList = buff
    }
    fun reSetImage(pharmaBuffPK: String, mediaList: ArrayList<MediaPickerSourceModel>?) {
        val buff = item.value.pharmaList.toMutableList()
        val findBuff = buff.find { x -> x.thisPK == pharmaBuffPK } ?: return
        findBuff.uploadItems.value = mediaList?.toMutableList() ?: mutableListOf()
        item.value.pharmaList = buff
    }

    fun startBackgroundService(data: ExtraEDIPharma) {
        closeAble.value = false
        backgroundService.sasKeyEnqueue(EDISASKeyQueueModel().apply {
            pharmaPK = data.thisPK
            ediUploadModel = EDIUploadModel().apply {
                ediType = item.value.ediType
                year = item.value.year
                month = item.value.month
                tempHospitalPK = item.value.tempHospitalPK
                tempOrgName = item.value.tempOrgName
                pharmaList = item.value.pharmaList.filter { x -> x.uploadItems.value.isNotEmpty() }.map { it.toEDIUploadPharmaModel() }.toMutableList()
            }
        })
    }

    enum class ClickEvent(var index: Int) {
        CLOSE(0),
        HOSPITAL_DETAIL(1)
    }
}