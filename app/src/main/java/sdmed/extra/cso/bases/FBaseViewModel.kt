package sdmed.extra.cso.bases

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.DIContext
import org.kodein.di.diContext
import org.kodein.di.instance
import sdmed.extra.cso.interfaces.command.IAsyncEventListener
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.interfaces.repository.IAzureBlobRepository
import sdmed.extra.cso.interfaces.repository.ICommonRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.command.AsyncRelayCommand
import sdmed.extra.cso.models.retrofit.users.UserStatus
import sdmed.extra.cso.models.services.FUIStateService
import sdmed.extra.cso.utils.FDI

abstract class FBaseViewModel: ViewModel() {
    val context: Context by lazy {
        FMainApplication.ins
    }
    val uiStateService: FUIStateService by lazy {
        FDI.uiStateService()
    }
    protected val azureBlobRepository: IAzureBlobRepository by lazy {
        FDI.azureBlobRepository()
    }
    protected val commonRepository: ICommonRepository by lazy {
        FDI.commonRepository()
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

    fun toast(msg: String?, duration: Int = Toast.LENGTH_SHORT) = uiStateService.toast(msg, duration)
    fun loading(isVisible: Boolean = true, msg: String = "") {
        uiStateService.loading(isVisible, msg)
    }

    var relayCommand: ICommand = AsyncRelayCommand({})
    fun addEventListener(listener: IAsyncEventListener) {
        (relayCommand as AsyncRelayCommand).addEventListener(listener)
    }

    open fun fakeInit() {

    }
}