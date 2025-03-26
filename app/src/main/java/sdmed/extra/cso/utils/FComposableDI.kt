package sdmed.extra.cso.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.direct
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

object FComposableDI {
    @Composable
    fun di(context: Context? = null): DI {
        if (LocalInspectionMode.current) {
            return FMainApplication().di
        }
        val context = context ?: LocalContext.current
        return (context.applicationContext as DIAware).di
    }

    @Composable
    fun uiStateService(context: Context? = null): FUIStateService {
        val di = di(context)
        val ret: FUIStateService by di.instance(FUIStateService::class)
        return ret
    }
    @Composable
    fun notificationService(context: Context? = null): FNotificationService {
        val di = di(context)
        val ret: FNotificationService by di.instance(FNotificationService::class)
        return ret
    }
    @Composable
    fun mqttService(context: Context? = null): FMqttService {
        val di = di(context)
        val ret: FMqttService by di.instance(FMqttService::class)
        return ret
    }
    @Composable
    fun backgroundEDIRequestUpload(context: Context? = null): FBackgroundEDIRequestUpload {
        val di = di(context)
        val ret: FBackgroundEDIRequestUpload by di.instance(FBackgroundEDIRequestUpload::class)
        return ret
    }
    @Composable
    fun backgroundEDIRequestNewUploadService(context: Context? = null): FBackgroundEDIRequestNewUploadService {
        val di = di(context)
        val ret: FBackgroundEDIRequestNewUploadService by di.instance(FBackgroundEDIRequestNewUploadService::class)
        return ret
    }
    @Composable
    fun backgroundEDIFileUpload(context: Context? = null): FBackgroundEDIFileUpload {
        val di = di(context)
        val ret: FBackgroundEDIFileUpload by di.instance(FBackgroundEDIFileUpload::class)
        return ret
    }
    @Composable
    fun backgroundQnAUpload(context: Context? = null): FBackgroundQnAUpload {
        val di = di(context)
        val ret: FBackgroundQnAUpload by di.instance(FBackgroundQnAUpload::class)
        return ret
    }
    @Composable
    fun backgroundUserFileUploadService(context: Context? = null): FBackgroundUserFileUploadService {
        val di = di(context)
        val ret: FBackgroundUserFileUploadService by di.instance(FBackgroundUserFileUploadService::class)
        return ret
    }

    @Composable
    fun azureBlobRepository(context: Context? = null): IAzureBlobRepository {
        val di = di(context)
        val ret: IAzureBlobRepository by di.instance(IAzureBlobRepository::class)
        return ret
    }
    @Composable
    fun commonRepository(context: Context? = null): ICommonRepository {
        val di = di(context)
        val ret: ICommonRepository by di.instance(ICommonRepository::class)
        return ret
    }
    @Composable
    fun mqttRepository(context: Context? = null): IMqttRepository {
        val di = di(context)
        val ret: IMqttRepository by di.instance(IMqttRepository::class)
        return ret
    }
    @Composable
    fun ediListRepository(context: Context? = null): IEDIListRepository {
        val di = di(context)
        val ret: IEDIListRepository by di.instance(IEDIListRepository::class)
        return ret
    }
    @Composable
    fun ediDueDateRepository(context: Context? = null): IEDIDueDateRepository {
        val di = di(context)
        val ret: IEDIDueDateRepository by di.instance(IEDIDueDateRepository::class)
        return ret
    }
    @Composable
    fun ediRequestRepository(context: Context? = null): IEDIRequestRepository {
        val di = di(context)
        val ret: IEDIRequestRepository by di.instance(IEDIRequestRepository::class)
        return ret
    }
    @Composable
    fun medicinePriceListRepository(context: Context? = null): IMedicinePriceListRepository {
        val di = di(context)
        val ret: IMedicinePriceListRepository by di.instance(IMedicinePriceListRepository::class)
        return ret
    }
    @Composable
    fun qnaListRepository(context: Context? = null): IQnAListRepository {
        val di = di(context)
        val ret: IQnAListRepository by di.instance(IQnAListRepository::class)
        return ret
    }
    @Composable
    fun myInfoRepository(context: Context? = null): IMyInfoRepository {
        val di = di(context)
        val ret: IMyInfoRepository by di.instance(IMyInfoRepository::class)
        return ret
    }
    @Composable
    fun hospitalTempRepository(context: Context? = null): IHospitalTempRepository {
        val di = di(context)
        val ret: IHospitalTempRepository by di.instance(IHospitalTempRepository::class)
        return ret
    }
}