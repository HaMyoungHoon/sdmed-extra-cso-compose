package sdmed.extra.cso.models.repository

import sdmed.extra.cso.interfaces.repository.IHospitalTempRepository
import sdmed.extra.cso.interfaces.services.IHospitalTempService
import sdmed.extra.cso.utils.FExtensions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HospitalTempRepository @Inject constructor(private val service: IHospitalTempService): IHospitalTempRepository {
    override suspend fun getData(thisPK: String) = FExtensions.restTryT { service.getData(thisPK) }
    override suspend fun getListSearch(searchString: String) = FExtensions.restTryT { service.getListSearch(searchString) }
    override suspend fun getListNearby(latitude: Double, longitude: Double, distance: Int) = FExtensions.restTryT { service.getListNearby(latitude, longitude, distance) }
    override suspend fun getPharmacyListNearby(latitude: Double, longitude: Double, distance: Int) = FExtensions.restTryT { service.getPharmacyListNearby(latitude, longitude, distance) }
}