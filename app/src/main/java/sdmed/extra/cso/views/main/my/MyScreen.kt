package sdmed.extra.cso.views.main.my

import androidx.compose.runtime.Composable
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.models.retrofit.hospitals.HospitalModel
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FStorage
import java.util.ArrayList

@Composable
fun myScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
             displayFeatures: List<DisplayFeature> = emptyList(),
             navigationType: NavigationType = NavigationType.BOTTOM) {
    fBaseScreen<MyScreenVM>({ data, dataContext -> setLayoutCommand(data, dataContext) },
        null,
        windowPanelType, navigationType,
        { dataContext -> myScreenDual(dataContext, displayFeatures) },
        { dataContext -> myScreenSingle(dataContext) },
        { dataContext -> myScreenDual(dataContext, displayFeatures) })
}
fun getMyScreenData(dataContext: MyScreenVM) {
    dataContext.loading()
    FCoroutineUtil.coroutineScope({
        val ret = dataContext.getData()
        dataContext.loading(false)
        if (ret.result != true) {
            dataContext.toast(ret.msg)
        }
    })
}
private fun setLayoutCommand(data: Any?, dataContext: MyScreenVM) {
    setThisCommand(data, dataContext)
    setHospitalCommand(data, dataContext)
}

private fun setThisCommand(data: Any?, dataContext: MyScreenVM) {
    val eventName = data as? MyScreenVM.ClickEvent ?: return
    when (eventName) {
        MyScreenVM.ClickEvent.LOGOUT -> logout(dataContext)
        MyScreenVM.ClickEvent.PASSWORD_CHANGE -> passwordChange(dataContext)
        MyScreenVM.ClickEvent.MULTI_LOGIN -> multiLogin(dataContext)
        MyScreenVM.ClickEvent.IMAGE_TRAINING -> training(dataContext)
        MyScreenVM.ClickEvent.TRAINING_CERTIFICATE_ADD -> trainingCertificateAdd(dataContext)
        MyScreenVM.ClickEvent.IMAGE_TAXPAYER -> taxpayer(dataContext)
        MyScreenVM.ClickEvent.IMAGE_BANK_ACCOUNT -> bankAccount(dataContext)
        MyScreenVM.ClickEvent.IMAGE_CSO_REPORT -> csoReport(dataContext)
        MyScreenVM.ClickEvent.IMAGE_MARKETING_CONTRACT -> marketingContract(dataContext)
    }
}
private fun setHospitalCommand(data: Any?, dataContext: MyScreenVM) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? HospitalModel.ClickEvent ?: return
    val dataBuff = data[1] as? HospitalModel ?: return
    when (eventName) {
        HospitalModel.ClickEvent.THIS -> {
            if (dataContext.selectedHos.value == dataBuff) {
                dataContext.selectedHos.value = HospitalModel()
            } else {
                dataContext.selectedHos.value = dataBuff
            }
        }
    }
}

private fun logout(dataContext: MyScreenVM) {
    FAmhohwa.logout(dataContext.context)
    FStorage.logoutMultiLoginData(dataContext.context)
}
private fun passwordChange(dataContext: MyScreenVM) {

}
private fun multiLogin(dataContext: MyScreenVM) {

}
private fun training(dataContext: MyScreenVM) {

}
private fun trainingCertificateAdd(dataContext: MyScreenVM) {

}
private fun taxpayer(dataContext: MyScreenVM) {

}
private fun bankAccount(dataContext: MyScreenVM) {

}
private fun csoReport(dataContext: MyScreenVM) {

}
private fun marketingContract(dataContext: MyScreenVM) {

}