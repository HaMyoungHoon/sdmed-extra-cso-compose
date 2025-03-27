package sdmed.extra.cso.bases

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.lifecycle.ViewModel
import org.kodein.di.direct
import org.kodein.di.instance
import sdmed.extra.cso.interfaces.command.IAsyncEventListener
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.interfaces.repository.IAzureBlobRepository
import sdmed.extra.cso.interfaces.repository.ICommonRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.command.AsyncRelayCommand
import sdmed.extra.cso.models.retrofit.users.UserStatus
import sdmed.extra.cso.models.services.FUIStateService
import sdmed.extra.cso.utils.FComposableDI
import sdmed.extra.cso.utils.FDI

abstract class FBaseViewModel(applicationContext: Context? = null): ViewModel() {
    val context: Context by lazy {
        FMainApplication.ins
    }
    private val _uiStateService: FUIStateService = FDI.uiStateService(applicationContext)
    protected val azureBlobRepository: IAzureBlobRepository = FDI.azureBlobRepository(applicationContext)
    protected val commonRepository: ICommonRepository = FDI.commonRepository(applicationContext)

    @Composable
    fun uiStateService(): FUIStateService {
        return if (LocalInspectionMode.current) {
            FComposableDI.uiStateService()
        } else {
            _uiStateService
        }
    }
    suspend fun getMyState(): RestResultT<UserStatus> {
        return commonRepository.getMyState()
    }
    suspend fun getMyRole(): RestResultT<Int> {
        return commonRepository.getMyRole()
    }
    suspend fun tokenRefresh(): RestResultT<String> {
        return commonRepository.tokenRefresh()
    }

    fun toast(msg: String?, duration: Int = Toast.LENGTH_SHORT) = _uiStateService.toast(msg, duration)
    @Composable
    fun toastComposable(msg: String?, duration: Int = Toast.LENGTH_SHORT) = uiStateService().toast(msg, duration)
    fun unToast() = _uiStateService.unToast()
    @Composable
    fun unToastComposable() = uiStateService().unToast()
    fun loading(isVisible: Boolean = true, msg: String = "") = _uiStateService.loading(isVisible, msg)
    @Composable
    fun loadingComposable(isVisible: Boolean = true, msg: String = "") = uiStateService().loading(isVisible, msg)

    var relayCommand: ICommand = AsyncRelayCommand({})
    fun addEventListener(listener: IAsyncEventListener) {
        (relayCommand as AsyncRelayCommand).addEventListener(listener)
    }

    open fun fakeInit() {

    }
}