package sdmed.extra.cso.interfaces.repository

import sdmed.extra.cso.models.RestPage
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.medicines.ExtraMedicinePriceResponse
import sdmed.extra.cso.models.retrofit.medicines.MedicineModel

interface IMedicinePriceListRepository {
    suspend fun getList(page: Int = 0, size: Int = 10): RestResultT<RestPage<MutableList<ExtraMedicinePriceResponse>>>
    suspend fun getLike(searchString: String, page: Int = 0, size: Int = 10): RestResultT<RestPage<MutableList<ExtraMedicinePriceResponse>>>
}