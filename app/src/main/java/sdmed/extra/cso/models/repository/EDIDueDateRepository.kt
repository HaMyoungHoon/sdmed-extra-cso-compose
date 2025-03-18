package sdmed.extra.cso.models.repository

import sdmed.extra.cso.interfaces.repository.IEDIDueDateRepository
import sdmed.extra.cso.interfaces.services.IEDIDueDateService
import javax.inject.Inject
import javax.inject.Singleton
import sdmed.extra.cso.utils.FExtensions

@Singleton
class EDIDueDateRepository @Inject constructor(private val service: IEDIDueDateService): IEDIDueDateRepository {
    override suspend fun getList(date: String, isYear: Boolean) = FExtensions.restTryT { service.getList(date, isYear) }
    override suspend fun getListRange(startDate: String, endDate: String) = FExtensions.restTryT { service.getListRange(startDate, endDate) }
}