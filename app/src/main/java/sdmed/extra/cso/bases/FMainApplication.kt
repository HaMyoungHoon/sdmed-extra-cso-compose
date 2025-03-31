package sdmed.extra.cso.bases

import android.app.Application
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.bindSingleton
import sdmed.extra.cso.interfaces.repository.*
import sdmed.extra.cso.interfaces.services.*
import sdmed.extra.cso.models.repository.*
import sdmed.extra.cso.models.services.*
import sdmed.extra.cso.utils.FStorage
import sdmed.extra.cso.views.theme.FThemeUtil

class FMainApplication: Application(), DIAware {
    companion object {
        private var _ins: FMainApplication? = null
        val ins: FMainApplication get() {
            if (_ins == null) {
                _ins = FMainApplication()
            }
            return _ins!!
        }
        val appColor: MutableStateFlow<String?> by lazy {
            MutableStateFlow(FStorage.getAppColor(ins))
        }
    }

    override val di = DI.direct {
        import(androidXModule(this@FMainApplication))
        bindSingleton<FPermissionService>(FPermissionService::class) { FPermissionService(applicationContext) }
        bindSingleton<FUIStateService>(FUIStateService::class) { FUIStateService() }
        bindSingleton<FNotificationService>(FNotificationService::class) { FNotificationService(applicationContext) }
        bindSingleton<FMqttService>(FMqttService::class) { FMqttService(applicationContext) }
        bindSingleton<FBackgroundEDIRequestUpload>(FBackgroundEDIRequestUpload::class) { FBackgroundEDIRequestUpload(applicationContext) }
        bindSingleton<FBackgroundEDIRequestNewUploadService>(FBackgroundEDIRequestNewUploadService::class) { FBackgroundEDIRequestNewUploadService(applicationContext) }
        bindSingleton<FBackgroundEDIFileUpload>(FBackgroundEDIFileUpload::class) { FBackgroundEDIFileUpload(applicationContext) }
        bindSingleton<FBackgroundQnAUpload>(FBackgroundQnAUpload::class) { FBackgroundQnAUpload(applicationContext) }
        bindSingleton<FBackgroundUserFileUpload>(FBackgroundUserFileUpload::class) { FBackgroundUserFileUpload(applicationContext) }

        bindSingleton<IAzureBlobRepository>(IAzureBlobRepository::class) { AzureBlobRepository(RetrofitService.create(IAzureBlobService::class.java)) }
        bindSingleton<ICommonRepository>(ICommonRepository::class) { CommonRepository(RetrofitService.create(ICommonService::class.java)) }
        bindSingleton<IMqttRepository>(IMqttRepository::class) { MqttRepository(RetrofitService.create(IMqttService::class.java)) }
        bindSingleton<IEDIListRepository>(IEDIListRepository::class) { EDIListRepository(RetrofitService.create(IEDIListService::class.java)) }
        bindSingleton<IEDIDueDateRepository>(IEDIDueDateRepository::class) { EDIDueDateRepository(RetrofitService.create(IEDIDueDateService::class.java)) }
        bindSingleton<IEDIRequestRepository>(IEDIRequestRepository::class) { EDIRequestRepository(RetrofitService.create(IEDIRequestService::class.java)) }
        bindSingleton<IMedicinePriceListRepository>(IMedicinePriceListRepository::class) { MedicinePriceListRepository(RetrofitService.create(IMedicinePriceListService::class.java)) }
        bindSingleton<IQnAListRepository>(IQnAListRepository::class) { QnAListRepository(RetrofitService.create(IQnAListService::class.java)) }
        bindSingleton<IMyInfoRepository>(IMyInfoRepository::class) { MyInfoRepository(RetrofitService.create(IMyInfoService::class.java)) }
        bindSingleton<IHospitalTempRepository>(IHospitalTempRepository::class) { HospitalTempRepository(RetrofitService.create(IHospitalTempService::class.java)) }
    }.di

    fun isDebug(): Boolean {
        return try {
            0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        } catch (_: Exception) {
            true
        }
    }
    fun getVersionCode(flags: Int = 0): Long {
        val pInfo = getPackageInfo(flags)
        return pInfo?.longVersionCode ?: 0
    }
    fun getVersionName(flags: Int = 0): Long {
        val versionName = getVersionNameString(flags)
        return if (versionName.isEmpty()) {
            0L
        } else {
            versionName.replace(".","").toLong()
        }
    }
    fun getVersionNameString(flags: Int = 0): String {
        val pInfo = getPackageInfo(flags)
        return pInfo?.versionName ?: ""
    }
    fun getPackageInfo(flags: Int = 0): PackageInfo? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
            } else {
                packageManager.getPackageInfo(packageName, flags)
            }
        } catch (e: Exception) {
            return null
        }
    }
    fun getApplicationID() = applicationInfo.processName.toString()
    fun getSignatureArray(): Array<Signature>? {
        val flag = PackageManager.GET_SIGNING_CERTIFICATES
        val pInfo = getPackageInfo(flag) ?: return null
        return try {
            pInfo.signingInfo?.apkContentsSigners
        } catch (_: Exception) {
            return null
        }
    }

    override fun onCreate() {
        super.onCreate()
        _ins = this
        FThemeUtil.applyTheme()
        try {
            startService(Intent(this, ForcedTerminationService::class.java))
        } catch (_: Exception) { }
    }
}