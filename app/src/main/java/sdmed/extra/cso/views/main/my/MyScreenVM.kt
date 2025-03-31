package sdmed.extra.cso.views.main.my

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.kodein.di.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IMyInfoRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.common.UserFileSASKeyQueueModel
import sdmed.extra.cso.models.eventbus.EventList
import sdmed.extra.cso.models.retrofit.hospitals.HospitalModel
import sdmed.extra.cso.models.retrofit.pharmas.PharmaModel
import sdmed.extra.cso.models.retrofit.users.UserDataModel
import sdmed.extra.cso.models.retrofit.users.UserFileModel
import sdmed.extra.cso.models.retrofit.users.UserFileType
import sdmed.extra.cso.models.retrofit.users.UserTrainingModel
import sdmed.extra.cso.models.services.FBackgroundUserFileUpload
import sdmed.extra.cso.utils.FContentsType
import sdmed.extra.cso.utils.FDI
import sdmed.extra.cso.utils.FEventBus
import java.util.Date

class MyScreenVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    private val backgroundService: FBackgroundUserFileUpload by FDI.di(applicationContext).instance(FBackgroundUserFileUpload::class)
    private val myInfoRepository: IMyInfoRepository by FDI.di(applicationContext).instance(IMyInfoRepository::class)
    private val eventChannel = FEventBus.createEventChannel<EventList.UserFileUploadEvent>()
    private val multiLoginChannel = FEventBus.createEventChannel<EventList.MultiLoginEvent>()

    val thisData = MutableStateFlow(UserDataModel())
    val hosList = MutableStateFlow(mutableListOf<HospitalModel>())
    val selectedHos = MutableStateFlow<HospitalModel>(HospitalModel())
    val pharmaList = MutableStateFlow(mutableListOf<PharmaModel>())

    val passwordChange = MutableStateFlow(false)
    val multiLogin = MutableStateFlow(false)
    val addLogin = MutableStateFlow(false)
    val trainingCertificateAdd = MutableStateFlow(false)
    val userFileSelect = MutableStateFlow(-1)
    init {
        viewModelScope.launch {
            for (event in eventChannel) {
                loading(false)
                if (event.thisPK.isNotEmpty()) {
                    getData()
                }
            }
        }
        viewModelScope.launch {
            for (event in multiLoginChannel) {
                loading(false)
                getData()
            }
        }
    }

    suspend fun getData(): RestResultT<UserDataModel> {
        val ret = myInfoRepository.getData()
        if (ret.result == true) {
            thisData.value = ret.data ?: UserDataModel()
        }
        return ret
    }
    fun userFileUpload(mediaTypeIndex: Int, mediaList: ArrayList<MediaPickerSourceModel>) {
        if (mediaTypeIndex == -1) return
        if (mediaList.isEmpty()) return

        backgroundService.sasKeyEnqueue(UserFileSASKeyQueueModel().apply {
            medias.add(mediaList.first())
            this.mediaTypeIndex = mediaTypeIndex
        })
    }

    override fun fakeInit() {
        thisData.value = UserDataModel().apply {
            id = "ididid"
            name = "이름이름"
            companyName = "뿅뿅 주식회사"
            companyNumber = "12-345-6789"
            bankAccount = "짱짱 은행 1231212 d sdf 3132"
            csoReportNumber = "123-123-123"
            contractDate = "2025-01-02"
            trainingList.add(UserTrainingModel().apply {
                mimeType = FContentsType.type_xlsx
                originalFilename = "교육이수"
                trainingDate = Date()
            })
            fileList.add(UserFileModel().apply {
                userFileType = UserFileType.Taxpayer
                mimeType = FContentsType.type_doc
                originalFilename = "사업자등록증"
            })
            fileList.add(UserFileModel().apply {
                userFileType = UserFileType.BankAccount
                mimeType = FContentsType.type_zip
                originalFilename = "통장계좌"
            })
            fileList.add(UserFileModel().apply {
                userFileType = UserFileType.CsoReport
                mimeType = FContentsType.type_pdf
                originalFilename = "CSO계약서"
            })
            fileList.add(UserFileModel().apply {
                userFileType = UserFileType.MarketingContract
                originalFilename = "마케팅계약서"
            })
        }
        hosList.value.add(HospitalModel().apply {
            thisPK = "123123"
            orgName = "가짜 병원1"
            address = "가짜시 가짜구 가짜로 1-2"
        })
        hosList.value.add(HospitalModel().apply {
            thisPK = "456456"
            orgName = "뻥 병원1"
            address = "뻥시 뻥구 뻥로 1-2 asdfkj asdlkfj asdflkj aslkdfjalksd fjsdfl  asdfklj aslkdfjas lkdjf"
        })
        pharmaList.value.add(PharmaModel().apply {
            thisPK = "123123"
            orgName = "가짜 제약사1"
            address = "가짜도 가짜군 가짜면 가짜리"
        })
        pharmaList.value.add(PharmaModel().apply {
            thisPK = "456456"
            orgName = "가짜 제약사2"
            address = "가짜도 가짜시 가짜동"
        })
        selectedHos.value = hosList.value.first()
    }

    enum class ClickEvent(var index: Int) {
        LOGOUT(0),
        PASSWORD_CHANGE(1),
        MULTI_LOGIN(2),
        IMAGE_TRAINING(3),
        TRAINING_CERTIFICATE_ADD(4),
        IMAGE_TAXPAYER(5),
        IMAGE_BANK_ACCOUNT(6),
        IMAGE_CSO_REPORT(7),
        IMAGE_MARKETING_CONTRACT(8),
    }
}