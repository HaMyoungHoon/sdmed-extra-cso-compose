package sdmed.extra.cso.models.repository

import sdmed.extra.cso.interfaces.repository.IMedicinePriceListRepository
import sdmed.extra.cso.interfaces.services.IMedicinePriceListService
import sdmed.extra.cso.utils.FExtensions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicinePriceListRepository @Inject constructor(private val service: IMedicinePriceListService): IMedicinePriceListRepository{
    override suspend fun getList(page: Int, size: Int) = FExtensions.restTryT { service.getList(page, size) }
    override suspend fun getLike(searchString: String, page: Int, size: Int) = FExtensions.restTryT { service.getLike(searchString, page, size) }
}