package sdmed.extra.cso.models.repository

import sdmed.extra.cso.interfaces.repository.IEDIRequestRepository
import sdmed.extra.cso.interfaces.services.IEDIRequestService
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel
import sdmed.extra.cso.utils.FExtensions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EDIRequestRepository @Inject constructor(private val service: IEDIRequestService): IEDIRequestRepository {
    override suspend fun getApplyDateList() = FExtensions.restTryT { service.getApplyDateList() }
    override suspend fun getHospitalList(applyDate: String) = FExtensions.restTryT { service.getHospitalList(applyDate) }
    override suspend fun getPharmaList() = FExtensions.restTryT { service.getPharmaList() }
    override suspend fun getPharmaList(hosPK: String, applyDate: String) = FExtensions.restTryT { service.getPharmaList(hosPK, applyDate) }
    override suspend fun getMedicineList(hosPK: String, pharmaPK: List<String>) = FExtensions.restTryT { service.getMedicineList(hosPK, pharmaPK) }
    override suspend fun postData(ediUploadModel: EDIUploadModel) = FExtensions.restTryT { service.postData(ediUploadModel) }
    override suspend fun postNewData(ediUploadModel: EDIUploadModel) = FExtensions.restTryT { service.postNewData(ediUploadModel) }
}