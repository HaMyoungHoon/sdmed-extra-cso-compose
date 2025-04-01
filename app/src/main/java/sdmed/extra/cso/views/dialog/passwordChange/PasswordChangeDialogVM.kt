package sdmed.extra.cso.views.dialog.passwordChange

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IMyInfoRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.users.UserDataModel
import sdmed.extra.cso.utils.FDI

class PasswordChangeDialogVM(applicationContext: Context? = null): FBaseViewModel(applicationContext) {
    val myInfoRepository: IMyInfoRepository by FDI.di(applicationContext).instance(IMyInfoRepository::class)
    val currentPW = MutableStateFlow<String>("")
    val afterPW = MutableStateFlow<String>("")
    val confirmPW = MutableStateFlow<String>("")
    val changeAble = MutableStateFlow(false)
    val afterPWRuleVisible = MutableStateFlow(false)
    val confirmPWRuleVisible = MutableStateFlow(false)
    val pwUnMatchVisible = MutableStateFlow(false)

    suspend fun putPasswordChange(): RestResultT<UserDataModel> {
        val ret = myInfoRepository.putPasswordChange(currentPW.value, afterPW.value, confirmPW.value)
        return ret
    }

    enum class ClickEvent(var index: Int) {
        CHANGE(0)
    }
}