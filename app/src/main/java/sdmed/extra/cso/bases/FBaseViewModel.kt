package sdmed.extra.cso.bases

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.lifecycle.ViewModel
import org.kodein.di.direct
import org.kodein.di.instance
import sdmed.extra.cso.interfaces.command.IAsyncEventListener
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.interfaces.repository.IAzureBlobRepository
import sdmed.extra.cso.interfaces.repository.ICommonRepository
import sdmed.extra.cso.interfaces.services.IMqttService
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.command.AsyncRelayCommand
import sdmed.extra.cso.models.retrofit.users.UserStatus
import sdmed.extra.cso.models.services.FMqttService
import sdmed.extra.cso.models.services.FPermissionService
import sdmed.extra.cso.models.services.FUIStateService
import sdmed.extra.cso.utils.FDI

abstract class FBaseViewModel(applicationContext: Context? = null): ViewModel() {
    val context: Context by lazy {
        FMainApplication.ins
    }
    val permissionService: FPermissionService by FDI.di(applicationContext).instance(FPermissionService::class)
    val uiStateService: FUIStateService by FDI.di(applicationContext).instance(FUIStateService::class)
    protected val mqttService: FMqttService by FDI.di(applicationContext).instance(FMqttService::class)
    protected val azureBlobRepository: IAzureBlobRepository by FDI.di(applicationContext).instance(IAzureBlobRepository::class)
    protected val commonRepository: ICommonRepository by FDI.di(applicationContext).instance(ICommonRepository::class)

    suspend fun getMyState(): RestResultT<UserStatus> {
        return commonRepository.getMyState()
    }
    suspend fun getMyRole(): RestResultT<Int> {
        return commonRepository.getMyRole()
    }
    suspend fun tokenRefresh(): RestResultT<String> {
        return commonRepository.tokenRefresh()
    }

    fun toast(@StringRes id: Int, duration: Int = Toast.LENGTH_SHORT) = uiStateService.toast(context.getString(id), duration)
    fun toast(msg: String?, duration: Int = Toast.LENGTH_SHORT) = uiStateService.toast(msg, duration)
    @Composable
    fun toastComposable(msg: String?, duration: Int = Toast.LENGTH_SHORT) = uiStateService.toast(msg, duration)
    fun unToast() = uiStateService.unToast()
    @Composable
    fun unToastComposable() = uiStateService.unToast()
    fun loading(isVisible: Boolean = true, msg: String = "") = uiStateService.loading(isVisible, msg)
    @Composable
    fun loadingComposable(isVisible: Boolean = true, msg: String = "") = uiStateService.loading(isVisible, msg)

    var relayCommand: ICommand = AsyncRelayCommand({})
    fun addEventListener(listener: IAsyncEventListener) {
        (relayCommand as AsyncRelayCommand).addEventListener(listener)
    }

    open fun fakeInit() {

    }
}