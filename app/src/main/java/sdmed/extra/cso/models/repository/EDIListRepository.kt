package sdmed.extra.cso.models.repository

import sdmed.extra.cso.interfaces.repository.IEDIListRepository
import sdmed.extra.cso.interfaces.services.IEDIListService
import sdmed.extra.cso.models.retrofit.edi.EDIUploadFileModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaFileModel
import sdmed.extra.cso.utils.FExtensions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EDIListRepository @Inject constructor(private val service: IEDIListService): IEDIListRepository {
    override suspend fun getList(startDate: String, endDate: String) = FExtensions.restTryT { service.getList(startDate, endDate) }
    override suspend fun getData(thisPK: String) = FExtensions.restTryT { service.getData(thisPK) }
    override suspend fun postFile(thisPK: String, ediUploadFileModel: List<EDIUploadFileModel>) = FExtensions.restTryT { service.postFile(thisPK, ediUploadFileModel) }
    override suspend fun postPharmaFile(thisPK: String, ediPharmaPK: String, ediUploadPharmaFileModel: List<EDIUploadPharmaFileModel>) =
        FExtensions.restTryT { service.postPharmaFile(thisPK, ediPharmaPK, ediUploadPharmaFileModel) }
    override suspend fun deleteEDIFile(thisPK: String) = FExtensions.restTryT { service.deleteEDIFile(thisPK) }
}