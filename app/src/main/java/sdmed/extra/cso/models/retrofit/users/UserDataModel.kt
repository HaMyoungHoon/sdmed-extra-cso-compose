package sdmed.extra.cso.models.retrofit.users

import sdmed.extra.cso.fDate.FDateTime
import sdmed.extra.cso.fDate.FDateTime2
import sdmed.extra.cso.models.retrofit.hospitals.HospitalModel
import sdmed.extra.cso.utils.FExtensions
import java.sql.Timestamp
import java.util.Date

data class UserDataModel(
    var thisPK: String = "",
    var id: String = "",
    var pw: String = "",
    var name: String = "",
    var mail: String = "",
    var phoneNumber: String = "",
    var role: Int = 0,
    var dept: Int = 0,
    var status: UserStatus = UserStatus.None,
    var companyName: String = "",
    var companyNumber: String = "",
    var companyOwner: String = "",
    var companyAddress: String = "",
    var bankAccount: String = "",
    var csoReportNumber: String = "",
    var contractDate: String? = null,
    var regDate: Timestamp = Timestamp(Date().time),
    var lastLoginDate: Timestamp = Timestamp(Date().time),
    var motherPK: String = "",
    var children: MutableList<UserDataModel> = mutableListOf(),
    var hosList: MutableList<HospitalModel> = mutableListOf(),
    var fileList: MutableList<UserFileModel> = mutableListOf(),
    var trainingList: MutableList<UserTrainingModel> = mutableListOf()
) {
    val lastLoginDateString: String = FDateTime().setThis(lastLoginDate.time).toString("yyyy-MM-dd")
    val taxPayerUrl: String? get() = fileList.find { it.userFileType == UserFileType.Taxpayer }?.blobUrl
    val taxPayerMimeType: String? get() = fileList.find { it.userFileType == UserFileType.Taxpayer }?.mimeType
    val taxPayerFilename: String? get() = fileList.find { it.userFileType == UserFileType.Taxpayer }?.originalFilename
    val bankAccountUrl: String? get() = fileList.find { it.userFileType == UserFileType.BankAccount }?.blobUrl
    val bankAccountMimeType: String? get() = fileList.find { it.userFileType == UserFileType.BankAccount }?.mimeType
    val bankAccountFilename: String? get() = fileList.find { it.userFileType == UserFileType.BankAccount }?.originalFilename
    val csoReportUrl: String? get() = fileList.find { it.userFileType == UserFileType.CsoReport }?.blobUrl
    val csoReportMimeType: String? get() = fileList.find { it.userFileType == UserFileType.CsoReport }?.mimeType
    val csoReportFilename: String? get() = fileList.find { it.userFileType == UserFileType.CsoReport }?.originalFilename
    val marketingContractUrl: String? get() = fileList.find { it.userFileType == UserFileType.MarketingContract }?.blobUrl
    val marketingContractMimeType: String? get() = fileList.find { it.userFileType == UserFileType.MarketingContract }?.mimeType
    val marketingContractFilename: String? get() = fileList.find { it.userFileType == UserFileType.MarketingContract }?.originalFilename

    val trainingUrl: String? get() = trainingList.firstOrNull()?.blobUrl
    val trainingMimeType: String? get() = trainingList.firstOrNull()?.mimeType
    val trainingFilename: String? get() = trainingList.firstOrNull()?.originalFilename
    val trainingDate: String get() = trainingList.firstOrNull()?.trainingDate?.let { FDateTime().setThis(it.time).toString("yyyy-MM-dd") } ?: ""
    val contractDateString: String get() = contractDate?.let { FDateTime().setThis(it).toString("yyyy-MM-dd") } ?: ""
}