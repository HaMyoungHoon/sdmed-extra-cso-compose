package sdmed.extra.cso.views.main.my

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.models.common.MediaViewParcelModel
import sdmed.extra.cso.models.menu.MenuItem
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.models.retrofit.hospitals.HospitalModel
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FStorage
import sdmed.extra.cso.utils.FStorage.putParcelableList
import sdmed.extra.cso.views.media.listView.MediaListViewActivity
import java.util.ArrayList

@Composable
fun myScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
             displayFeatures: List<DisplayFeature> = emptyList(),
             navigationType: NavigationType = NavigationType.BOTTOM,
             navigate: (MenuItem, Boolean) -> Unit) {
    var navigateCalled by remember { mutableStateOf(false) }
    val dataContext = fBaseScreen<MyScreenVM>({ data, dataContext -> setLayoutCommand(data, dataContext) },
        null,
        windowPanelType, navigationType,
        { dataContext -> myScreenDual(dataContext, displayFeatures) },
        { dataContext -> myScreenSingle(dataContext) },
        { dataContext -> myScreenDual(dataContext, displayFeatures) })
    LaunchedEffect(navigateCalled) {
        if (!navigateCalled) {
            navigateCalled = true
            checkExternalStorage(dataContext)
            getMyScreenData(dataContext)
        }
    }
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
    val blobUrl = dataContext.thisData.value.trainingUrl
    if (blobUrl != null) {
        val ret = arrayListOf<MediaViewParcelModel>()
        dataContext.thisData.value.trainingList.forEach { x ->
            ret.add(MediaViewParcelModel().parse(x))
        }
        dataContext.context.startActivity(Intent(dataContext.context, MediaListViewActivity::class.java).apply {
            putParcelableList(FConstants.MEDIA_LIST, ret)
        })
    }
}
private fun trainingCertificateAdd(dataContext: MyScreenVM) {
    checkCamera(dataContext) {
        checkReadStorage(dataContext) {

        }
    }
}
private fun taxpayer(dataContext: MyScreenVM) {
//    val context = dataContext.context
//    val blobUrl = dataContext.thisData.value.taxPayerUrl
//    if (blobUrl != null) {
//        context.startActivity((Intent(context, MediaViewActivity::class.java).apply {
//            putParcelable(FConstants.MEDIA_ITEM, MediaViewParcelModel().apply {
//                this.blobUrl = blobUrl
//                this.mimeType = dataContext.thisData.value.taxPayerMimeType ?: ""
//            })
//        }))
//        return
//    }
//    checkCamera(dataContext) {
//        checkReadStorage(dataContext) {
//            _imagePickerResult?.launch(Intent(contextBuff, MediaPickerActivity::class.java).apply {
//                putExtra(FConstants.MEDIA_TARGET_PK, UserFileType.Taxpayer.index.toString())
//                putExtra(FConstants.MEDIA_MAX_COUNT, 1)
//            })
//        }
//    }
}
private fun bankAccount(dataContext: MyScreenVM) {

}
private fun csoReport(dataContext: MyScreenVM) {

}
private fun marketingContract(dataContext: MyScreenVM) {

}

private fun checkCamera(dataContext: MyScreenVM, callback: (Boolean) -> Unit) {
    dataContext.permissionService.requestCameraPermissions(callback)
}
private fun checkExternalStorage(dataContext: MyScreenVM) {
    dataContext.permissionService.externalStorage()
}
private fun checkReadStorage(dataContext: MyScreenVM, callback: (Boolean) -> Unit) {
    dataContext.permissionService.requestReadExternalPermissions(callback)
}