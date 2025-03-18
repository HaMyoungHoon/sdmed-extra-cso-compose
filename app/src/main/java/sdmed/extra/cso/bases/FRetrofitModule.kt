package sdmed.extra.cso.bases

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import sdmed.extra.cso.interfaces.repository.IAzureBlobRepository
import sdmed.extra.cso.interfaces.services.IAzureBlobService
import sdmed.extra.cso.interfaces.services.ICommonService
import sdmed.extra.cso.interfaces.services.IEDIDueDateService
import sdmed.extra.cso.interfaces.services.IEDIListService
import sdmed.extra.cso.interfaces.services.IEDIRequestService
import sdmed.extra.cso.interfaces.services.IHospitalTempService
import sdmed.extra.cso.interfaces.services.IMedicinePriceListService
import sdmed.extra.cso.interfaces.services.IMqttService
import sdmed.extra.cso.interfaces.services.IMyInfoService
import sdmed.extra.cso.interfaces.services.IQnAListService
import sdmed.extra.cso.models.services.RetrofitService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FRetrofitModule {
    @Provides @Singleton fun azureBlobService() = RetrofitService.create(IAzureBlobService::class.java)
    @Provides @Singleton fun commonService() = RetrofitService.create(ICommonService::class.java)
    @Provides @Singleton fun mqttService() = RetrofitService.create(IMqttService::class.java)
    @Provides @Singleton fun ediListService() = RetrofitService.create(IEDIListService::class.java)
    @Provides @Singleton fun ediDueDateService() = RetrofitService.create(IEDIDueDateService::class.java)
    @Provides @Singleton fun ediRequestService() = RetrofitService.create(IEDIRequestService::class.java)
    @Provides @Singleton fun medicinePriceListService() = RetrofitService.create(IMedicinePriceListService::class.java)
    @Provides @Singleton fun qnaListService() = RetrofitService.create(IQnAListService::class.java)
    @Provides @Singleton fun myInfoService() = RetrofitService.create(IMyInfoService::class.java)
    @Provides @Singleton fun hospitalTempService() = RetrofitService.create(IHospitalTempService::class.java)
}