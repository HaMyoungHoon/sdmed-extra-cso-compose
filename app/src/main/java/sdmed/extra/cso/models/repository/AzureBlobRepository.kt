package sdmed.extra.cso.models.repository

import okhttp3.MediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import sdmed.extra.cso.interfaces.repository.IAzureBlobRepository
import sdmed.extra.cso.interfaces.services.IAzureBlobService
import sdmed.extra.cso.utils.FContentsType
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AzureBlobRepository @Inject constructor(private val service: IAzureBlobService): IAzureBlobRepository {
    override suspend fun upload(sasUrl: String, file: File, mimeType: String): Response<ResponseBody> {
        return upload(sasUrl, file, FContentsType.getMediaType(mimeType))
    }
    override suspend fun upload(sasUrl: String, file: File, mimeType: MediaType): Response<ResponseBody> {
        val reqFile = file.asRequestBody(mimeType)
        val response = service.upload(sasUrl, reqFile)
        return response
    }
}