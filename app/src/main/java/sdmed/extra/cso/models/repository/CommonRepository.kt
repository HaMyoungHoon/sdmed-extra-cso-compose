package sdmed.extra.cso.models.repository

import okhttp3.ResponseBody
import retrofit2.Response
import sdmed.extra.cso.interfaces.repository.ICommonRepository
import sdmed.extra.cso.interfaces.services.ICommonService
import sdmed.extra.cso.models.RestResult
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.common.BlobStorageInfoModel
import sdmed.extra.cso.models.retrofit.common.VersionCheckModel
import sdmed.extra.cso.models.retrofit.common.VersionCheckType
import sdmed.extra.cso.models.retrofit.users.UserStatus
import sdmed.extra.cso.utils.FExtensions
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommonRepository @Inject constructor(private val service: ICommonService): ICommonRepository {
    override suspend fun signIn(id: String, pw: String) = FExtensions.restTryT { service.signIn(id, pw) }
    override suspend fun multiSign(token: String) = FExtensions.restTryT { service.multiSign(token) }
    override suspend fun tokenRefresh() = FExtensions.restTryT { service.tokenRefresh() }

    override suspend fun getFindIDAuthNumber(name: String, phoneNumber: String) = FExtensions.restTry { service.getFindIDAuthNumber(name, phoneNumber) }
    override suspend fun getFindPWAuthNumber(id: String, phoneNumber: String) = FExtensions.restTry { service.getFindPWAuthNumber(id, phoneNumber) }
    override suspend fun getCheckAuthNumber(authNumber: String, phoneNumber: String) = FExtensions.restTryT { service.getCheckAuthNumber(authNumber, phoneNumber) }

    override suspend fun versionCheck(versionCheckType: VersionCheckType) = FExtensions.restTryT { service.versionCheck(versionCheckType) }
    override suspend fun serverTime() = FExtensions.restTryT { service.serverTime() }
    override suspend fun setLanguage(lang: String) = FExtensions.restTry { service.setLanguage(lang) }

    override suspend fun getMyRole() = FExtensions.restTryT { service.getMyRole() }
    override suspend fun getMyState() = FExtensions.restTryT { service.getMyState() }
    override suspend fun getGenerateSas(blobName: String) = FExtensions.restTryT { service.getGenerateSas(blobName) }
    override suspend fun postGenerateSasList(blobName: List<String>) = FExtensions.restTryT { service.postGenerateSasList(blobName) }
    override suspend fun downloadFile(url: String) =
        try {
            service.downloadFile(url)
        } catch (e: Exception) {
            null
        }
}