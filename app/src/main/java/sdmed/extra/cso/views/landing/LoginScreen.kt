package sdmed.extra.cso.views.landing

import androidx.compose.runtime.Composable
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.bases.FBaseScreen
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.models.retrofit.FRetrofitVariable
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FStorage

class LoginScreen: FBaseScreen<LoginScreenVM>() {
    override val dataContext by lazy {
        LoginScreenVM()
    }
    @Composable
    override fun view(
        windowPanelType: WindowPanelType,
        displayFeatures: List<DisplayFeature>
    ) {
        LoginScreenDetail().screen(dataContext)
    }

    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
    }
    private fun setThisCommand(data: Any?) {
        val eventName = data as? LoginScreenVM.ClickEvent ?: return
        when (eventName) {
            LoginScreenVM.ClickEvent.FORGOT_ID -> { }
            LoginScreenVM.ClickEvent.FORGOT_PW -> { }
            LoginScreenVM.ClickEvent.SIGN_IN -> login()
            LoginScreenVM.ClickEvent.MULTI_LOGIN -> multiLogin()
        }
    }


    private fun login() {
        val context = dataContext.context
        FCoroutineUtil.coroutineScope({
            dataContext.loading()
            val ret = dataContext.signIn()
            dataContext.loading(false)
            if (ret.result == true) {
                FRetrofitVariable.token.value = ret.data
                FStorage.setAuthToken(context, ret.data)
                FAmhohwa.addMultiLoginData(context)
                return@coroutineScope
            }
            dataContext.toast(ret.msg)
        })
    }
    private fun multiLogin() {

    }
}