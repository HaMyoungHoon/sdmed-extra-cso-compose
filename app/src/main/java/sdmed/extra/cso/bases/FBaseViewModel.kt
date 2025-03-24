package sdmed.extra.cso.bases

import androidx.lifecycle.ViewModel
import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.StateFlow
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import sdmed.extra.cso.interfaces.command.IAsyncEventListener
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.interfaces.repository.IAzureBlobRepository
import sdmed.extra.cso.interfaces.repository.ICommonRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.command.AsyncRelayCommand
import sdmed.extra.cso.models.common.LoadingMessageModel
import sdmed.extra.cso.models.common.ToastMessageModel
import sdmed.extra.cso.models.retrofit.users.UserStatus
import sdmed.extra.cso.models.services.FUIStateService

abstract class FBaseViewModel(application: MultiDexApplication): ViewModel(), DIAware {
    final override val di: DI by lazy { (application as FMainApplication).di }
    val uiStateService: FUIStateService = FUIStateService()
    protected val azureBlobRepository: IAzureBlobRepository by di.instance(IAzureBlobRepository::class)
    protected val commonRepository: ICommonRepository by di.instance(ICommonRepository::class)
    val loadingState: StateFlow<LoadingMessageModel> by lazy {
        uiStateService.isLoading
    }
    val toastState: StateFlow<ToastMessageModel> by lazy {
        uiStateService.toast
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

    val relayCommand: ICommand = AsyncRelayCommand({})
    fun addEventListener(listener: IAsyncEventListener) {
        (relayCommand as AsyncRelayCommand).addEventListener(listener)
    }
}