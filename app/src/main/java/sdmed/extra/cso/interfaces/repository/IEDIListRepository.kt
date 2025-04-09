package sdmed.extra.cso.interfaces.repository

import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaFileModel
import sdmed.extra.cso.models.retrofit.edi.ExtraEDIDetailResponse
import sdmed.extra.cso.models.retrofit.edi.ExtraEDIListResponse

interface IEDIListRepository {
    suspend fun getList(startDate: String, endDate: String): RestResultT<List<ExtraEDIListResponse>>
    suspend fun getData(thisPK: String): RestResultT<ExtraEDIDetailResponse>
    suspend fun postPharmaFile(thisPK: String, ediPharmaPK: String, ediUploadPharmaFileModel: List<EDIUploadPharmaFileModel>): RestResultT<List<EDIUploadPharmaFileModel>>
    suspend fun deleteEDIPharmaFile(thisPK: String): RestResultT<EDIUploadPharmaFileModel>
}