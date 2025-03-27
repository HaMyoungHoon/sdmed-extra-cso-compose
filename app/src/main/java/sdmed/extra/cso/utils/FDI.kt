package sdmed.extra.cso.utils

import android.content.Context
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import sdmed.extra.cso.bases.FMainApplication
import sdmed.extra.cso.interfaces.repository.IAzureBlobRepository
import sdmed.extra.cso.interfaces.repository.ICommonRepository
import sdmed.extra.cso.interfaces.repository.IEDIDueDateRepository
import sdmed.extra.cso.interfaces.repository.IEDIListRepository
import sdmed.extra.cso.interfaces.repository.IEDIRequestRepository
import sdmed.extra.cso.interfaces.repository.IHospitalTempRepository
import sdmed.extra.cso.interfaces.repository.IMedicinePriceListRepository
import sdmed.extra.cso.interfaces.repository.IMqttRepository
import sdmed.extra.cso.interfaces.repository.IMyInfoRepository
import sdmed.extra.cso.interfaces.repository.IQnAListRepository
import sdmed.extra.cso.models.services.FBackgroundEDIFileUpload
import sdmed.extra.cso.models.services.FBackgroundEDIRequestNewUploadService
import sdmed.extra.cso.models.services.FBackgroundEDIRequestUpload
import sdmed.extra.cso.models.services.FBackgroundQnAUpload
import sdmed.extra.cso.models.services.FBackgroundUserFileUploadService
import sdmed.extra.cso.models.services.FMqttService
import sdmed.extra.cso.models.services.FNotificationService
import sdmed.extra.cso.models.services.FUIStateService
import kotlin.getValue

object FDI {
    fun context(context: Context? = null): Context {
        return try {
            context ?: FMainApplication.ins
        } catch (_: Exception) {
            FMainApplication()
        }
    }
    fun di(context: Context? = null): DI {
        val context = context?: FMainApplication.ins
        return try {
            (context.applicationContext as DIAware).di
        } catch (_: Exception) {
            FMainApplication().di
        }
    }

    fun uiStateService(context: Context? = null): FUIStateService {
        val ret: FUIStateService by di(context).instance(FUIStateService::class)
        return ret
    }
    fun notificationService(context: Context? = null): FNotificationService {
        val ret: FNotificationService by di(context).instance(FNotificationService::class)
        return ret
    }
    fun mqttService(context: Context? = null): FMqttService {
        val ret: FMqttService by di(context).instance(FMqttService::class)
        return ret
    }
    fun backgroundEDIRequestUpload(context: Context? = null): FBackgroundEDIRequestUpload {
        val ret: FBackgroundEDIRequestUpload by di(context).instance(FBackgroundEDIRequestUpload::class)
        return ret
    }
    fun backgroundEDIRequestNewUploadService(context: Context? = null): FBackgroundEDIRequestNewUploadService {
        val ret: FBackgroundEDIRequestNewUploadService by di(context).instance(FBackgroundEDIRequestNewUploadService::class)
        return ret
    }
    fun backgroundEDIFileUpload(context: Context? = null): FBackgroundEDIFileUpload {
        val ret: FBackgroundEDIFileUpload by di(context).instance(FBackgroundEDIFileUpload::class)
        return ret
    }
    fun backgroundQnAUpload(context: Context? = null): FBackgroundQnAUpload {
        val ret: FBackgroundQnAUpload by di(context).instance(FBackgroundQnAUpload::class)
        return ret
    }
    fun backgroundUserFileUploadService(context: Context? = null): FBackgroundUserFileUploadService {
        val ret: FBackgroundUserFileUploadService by di(context).instance(FBackgroundUserFileUploadService::class)
        return ret
    }

    fun azureBlobRepository(context: Context? = null): IAzureBlobRepository {
        val ret: IAzureBlobRepository by di(context).instance(IAzureBlobRepository::class)
        return ret
    }
    fun commonRepository(context: Context? = null): ICommonRepository {
        val ret: ICommonRepository by di(context).instance(ICommonRepository::class)
        return ret
    }
    fun mqttRepository(context: Context? = null): IMqttRepository {
        val ret: IMqttRepository by di(context).instance(IMqttRepository::class)
        return ret
    }
    fun ediListRepository(context: Context? = null): IEDIListRepository {
        val ret: IEDIListRepository by di(context).instance(IEDIListRepository::class)
        return ret
    }
    fun ediDueDateRepository(context: Context? = null): IEDIDueDateRepository {
        val ret: IEDIDueDateRepository by di(context).instance(IEDIDueDateRepository::class)
        return ret
    }
    fun ediRequestRepository(context: Context? = null): IEDIRequestRepository {
        val ret: IEDIRequestRepository by di(context).instance(IEDIRequestRepository::class)
        return ret
    }
    fun medicinePriceListRepository(context: Context? = null): IMedicinePriceListRepository {
        val ret: IMedicinePriceListRepository by di(context).instance(IMedicinePriceListRepository::class)
        return ret
    }
    fun qnaListRepository(context: Context? = null): IQnAListRepository {
        val ret: IQnAListRepository by di(context).instance(IQnAListRepository::class)
        return ret
    }
    fun myInfoRepository(context: Context? = null): IMyInfoRepository {
        val ret: IMyInfoRepository by di(context).instance(IMyInfoRepository::class)
        return ret
    }
    fun hospitalTempRepository(context: Context? = null): IHospitalTempRepository {
        val ret: IHospitalTempRepository by di(context).instance(IHospitalTempRepository::class)
        return ret
    }
}