package sdmed.extra.cso.models.retrofit.users

import sdmed.extra.cso.fDate.FDateTime
import java.sql.Timestamp
import java.util.Date

data class ExtraMyInfoResponse(
    var thisPK: String = "",
    var id: String = "",
    var name: String = "",
    var companyName: String = "",
    var companyNumber: String = "",
    var bankAccount: String = "",
    var csoReportNumber: String = "",
    var contractDate: String? = null,
    var regDate: Timestamp = Timestamp(Date().time),
    var lastLoginDate: Timestamp = Timestamp(Date().time),
    var hosList: MutableList<ExtraMyInfoHospital> = mutableListOf(),
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