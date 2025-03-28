package sdmed.extra.cso.views.main.landing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.models.menu.MenuItem
import sdmed.extra.cso.models.menu.MenuList
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.models.retrofit.FRetrofitVariable
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FStorage

@Composable
fun loginScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                displayFeatures: List<DisplayFeature> = emptyList(),
                navigationType: NavigationType = NavigationType.BOTTOM,
                navigate: (MenuItem, Boolean) -> Unit) {
    val dataContext = fBaseScreen<LoginScreenVM>({ data, dataContext -> setLayoutCommand(data, dataContext) },
        { dataContext -> loginScreenDetail(dataContext, navigate) },
        windowPanelType, navigationType)
    dataContext.gatheringMultiSign()
}
private fun setLayoutCommand(data: Any?, dataContext: LoginScreenVM) {
    val eventName = data as? LoginScreenVM.ClickEvent ?: return
    when (eventName) {
        LoginScreenVM.ClickEvent.FORGOT_ID -> { }
        LoginScreenVM.ClickEvent.FORGOT_PW -> { }
        LoginScreenVM.ClickEvent.SIGN_IN -> login(dataContext)
        LoginScreenVM.ClickEvent.MULTI_LOGIN -> multiLogin(dataContext)
    }
}


private fun login(dataContext: LoginScreenVM) {
    val context = dataContext.context
    FCoroutineUtil.coroutineScope({
        dataContext.loading()
        val ret = dataContext.signIn()
        dataContext.loading(false)
        if (ret.result == true) {
            FRetrofitVariable.token.value = ret.data
            FStorage.setAuthToken(context, ret.data)
            FAmhohwa.addMultiLoginData(context)
            dataContext.loginEnd.value = true
            return@coroutineScope
        }
        dataContext.toast(ret.msg)
    })
}
private fun multiLogin(dataContext: LoginScreenVM) {

}