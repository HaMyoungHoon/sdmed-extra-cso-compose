package sdmed.extra.cso.interfaces.services

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaFileModel
import sdmed.extra.cso.models.retrofit.edi.ExtraEDIDetailResponse
import sdmed.extra.cso.models.retrofit.edi.ExtraEDIListResponse

interface IEDIListService {
    @GET("${FConstants.REST_API_EDI_LIST}/list")
    suspend fun getList(@Query("startDate") startDate: String, @Query("endDate") endDate: String): RestResultT<List<ExtraEDIListResponse>>
    @GET("${FConstants.REST_API_EDI_LIST}/data/{thisPK}")
    suspend fun getData(@Path("thisPK") thisPK: String): RestResultT<ExtraEDIDetailResponse>
    @POST("${FConstants.REST_API_EDI_LIST}/file/{thisPK}/pharma/{ediPharmaPK}")
    suspend fun postPharmaFile(@Path("thisPK") thisPK: String, @Path("ediPharmaPK") ediPharmaPK: String, @Body ediUploadPharmaFileModel: List<EDIUploadPharmaFileModel>): RestResultT<List<EDIUploadPharmaFileModel>>
    @DELETE("${FConstants.REST_API_EDI_LIST}/data/pharma/file/{thisPK}")
    suspend fun deleteEDIPharmaFile(@Path("thisPK") thisPK: String): RestResultT<EDIUploadPharmaFileModel>
}