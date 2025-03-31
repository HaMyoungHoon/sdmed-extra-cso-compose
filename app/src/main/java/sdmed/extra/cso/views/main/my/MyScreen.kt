package sdmed.extra.cso.views.main.my

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.common.MediaViewParcelModel
import sdmed.extra.cso.models.menu.MenuItem
import sdmed.extra.cso.models.menu.MenuList
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.models.retrofit.hospitals.HospitalModel
import sdmed.extra.cso.models.retrofit.users.UserFileType
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FLog
import sdmed.extra.cso.utils.FStorage
import sdmed.extra.cso.utils.FStorage.getParcelableList
import sdmed.extra.cso.utils.FStorage.putParcelable
import sdmed.extra.cso.utils.FStorage.putParcelableList
import sdmed.extra.cso.views.component.loginDialog.loginDialog
import sdmed.extra.cso.views.component.multiLogin.multiLoginDialog
import sdmed.extra.cso.views.media.listView.MediaListViewActivity
import sdmed.extra.cso.views.media.picker.MediaPickerActivity
import sdmed.extra.cso.views.media.singleView.MediaViewActivity
import java.util.ArrayList

@Composable
fun myScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
             displayFeatures: List<DisplayFeature> = emptyList(),
             navigationType: NavigationType = NavigationType.BOTTOM,
             navigate: (MenuItem, Boolean) -> Unit) {
    var navigateCalled by remember { mutableStateOf(false) }
    val dataContext = fBaseScreen<MyScreenVM>({ data, dataContext -> setLayoutCommand(data, dataContext, navigate) },
        null,
        windowPanelType, navigationType,
        { dataContext -> myScreenDual(dataContext, displayFeatures) },
        { dataContext -> myScreenSingle(dataContext) },
        { dataContext -> myScreenDual(dataContext, displayFeatures) })
    multiLogin(dataContext, windowPanelType, displayFeatures, navigationType)
    loginDialog(dataContext, windowPanelType, displayFeatures, navigationType)
    passwordChangeDialog(dataContext)
    userFileSelect(dataContext)
    trainingCertificateAdd(dataContext, windowPanelType, displayFeatures, navigationType)
    LaunchedEffect(navigateCalled) {
        if (!navigateCalled) {
            navigateCalled = true
            checkExternalStorage(dataContext)
            getMyScreenData(dataContext)
        }
    }
}
@Composable
fun multiLogin(dataContext: MyScreenVM,
               windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
               displayFeatures: List<DisplayFeature> = emptyList(),
               navigationType: NavigationType = NavigationType.BOTTOM) {
    val multiLogin by dataContext.multiLogin.collectAsState()
    if (multiLogin) {
        multiLoginDialog(windowPanelType, displayFeatures, navigationType, true, {
            dataContext.addLogin.value = true
        }) {
            dataContext.multiLogin.value = false
        }
    }
}
@Composable
fun loginDialog(dataContext: MyScreenVM,
                windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                displayFeatures: List<DisplayFeature> = emptyList(),
                navigationType: NavigationType = NavigationType.BOTTOM) {
    val addLogin by dataContext.addLogin.collectAsState()
    if (addLogin) {
        loginDialog(windowPanelType, displayFeatures, navigationType) {
            dataContext.addLogin.value = false
        }
    }
}
@Composable
fun passwordChangeDialog(dataContext: MyScreenVM) {
    val passwordChange by dataContext.passwordChange.collectAsState()
    if (passwordChange) {


    }
}
@Composable
private fun userFileSelect(dataContext: MyScreenVM) {
    val context = LocalContext.current
    val userFileSelect by dataContext.userFileSelect.collectAsState()
    val activityResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode != Activity.RESULT_OK) {
            return@rememberLauncherForActivityResult
        }
        val mediaTypeIndex = it.data?.getStringExtra(FConstants.MEDIA_TARGET_PK)?.toIntOrNull() ?: return@rememberLauncherForActivityResult
        val mediaList = it.data?.getParcelableList<MediaPickerSourceModel>(FConstants.MEDIA_LIST) ?: return@rememberLauncherForActivityResult
        if (mediaTypeIndex == -1) return@rememberLauncherForActivityResult
        if (mediaList.isEmpty()) return@rememberLauncherForActivityResult
        dataContext.loading()
        dataContext.userFileUpload(mediaTypeIndex, mediaList)
    }
    LaunchedEffect(userFileSelect) {
        if (userFileSelect == UserFileType.Taxpayer.index) {
            taxImageSelect(dataContext, context, activityResult)
        } else if (userFileSelect == UserFileType.BankAccount.index) {
            bankAccountSelect(dataContext, context, activityResult)
        } else if (userFileSelect == UserFileType.CsoReport.index) {
            csoReportSelect(dataContext, context, activityResult)
        } else if (userFileSelect == UserFileType.MarketingContract.index) {
            marketingContractSelect(dataContext, context, activityResult)
        }
    }
}
@Composable
private fun trainingCertificateAdd(dataContext: MyScreenVM,
                                   windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                                   displayFeatures: List<DisplayFeature> = emptyList(),
                                   navigationType: NavigationType = NavigationType.BOTTOM) {
    val trainingCertificateAdd by dataContext.trainingCertificateAdd.collectAsState()
    if (trainingCertificateAdd) {
        myScreenTrainingCertificate(dataContext.thisData.value.trainingList, windowPanelType, displayFeatures, navigationType, {
            dataContext.trainingCertificateAdd.value = false
        })
    }
}
private fun taxImageSelect(dataContext: MyScreenVM, context: Context, activityResult: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    if (dataContext.userFileSelect.value != UserFileType.Taxpayer.index) {
        return
    }
    activityResult.launch(Intent(context, MediaPickerActivity::class.java).apply {
        putExtra(FConstants.MEDIA_TARGET_PK, UserFileType.Taxpayer.index.toString())
        putExtra(FConstants.MEDIA_MAX_COUNT, 1)
    })
    dataContext.userFileSelect.value = -1
}
private fun bankAccountSelect(dataContext: MyScreenVM, context: Context, activityResult: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    if (dataContext.userFileSelect.value != UserFileType.BankAccount.index) {
        return
    }
    activityResult.launch(Intent(context, MediaPickerActivity::class.java).apply {
        putExtra(FConstants.MEDIA_TARGET_PK, UserFileType.BankAccount.index.toString())
        putExtra(FConstants.MEDIA_MAX_COUNT, 1)
    })
    dataContext.userFileSelect.value = -1
}
private fun csoReportSelect(dataContext: MyScreenVM, context: Context, activityResult: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    if (dataContext.userFileSelect.value != UserFileType.CsoReport.index) {
        return
    }
    activityResult.launch(Intent(context, MediaPickerActivity::class.java).apply {
        putExtra(FConstants.MEDIA_TARGET_PK, UserFileType.CsoReport.index.toString())
        putExtra(FConstants.MEDIA_MAX_COUNT, 1)
    })
    dataContext.userFileSelect.value = -1
}
private fun marketingContractSelect(dataContext: MyScreenVM, context: Context, activityResult: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    if (dataContext.userFileSelect.value != UserFileType.MarketingContract.index) {
        return
    }
    activityResult.launch(Intent(context, MediaPickerActivity::class.java).apply {
        putExtra(FConstants.MEDIA_TARGET_PK, UserFileType.MarketingContract.index.toString())
        putExtra(FConstants.MEDIA_MAX_COUNT, 1)
    })
    dataContext.userFileSelect.value = -1
}
private fun getMyScreenData(dataContext: MyScreenVM) {
    dataContext.loading()
    FCoroutineUtil.coroutineScope({
        val ret = dataContext.getData()
        dataContext.loading(false)
        if (ret.result != true) {
            dataContext.toast(ret.msg)
        }
    })
}
private fun setLayoutCommand(data: Any?, dataContext: MyScreenVM, navigate: (MenuItem, Boolean) -> Unit = {menu, clear -> }) {
    setThisCommand(data, dataContext, navigate)
    setHospitalCommand(data, dataContext)
}

private fun setThisCommand(data: Any?, dataContext: MyScreenVM, navigate: (MenuItem, Boolean) -> Unit = {menu, clear -> }) {
    val eventName = data as? MyScreenVM.ClickEvent ?: return
    when (eventName) {
        MyScreenVM.ClickEvent.LOGOUT -> logout(dataContext, navigate)
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

private fun logout(dataContext: MyScreenVM, navigate: (MenuItem, Boolean) -> Unit) {
    FAmhohwa.logout(dataContext.context)
    FStorage.logoutMultiLoginData(dataContext.context)
    navigate(MenuList.menuLanding(), true)
}
private fun passwordChange(dataContext: MyScreenVM) {
    dataContext.passwordChange.value = true
}
private fun multiLogin(dataContext: MyScreenVM) {
    dataContext.multiLogin.value = true
}
private fun training(dataContext: MyScreenVM) {
    val context = dataContext.context
    val blobUrl = dataContext.thisData.value.trainingUrl
    if (blobUrl != null) {
        val ret = arrayListOf<MediaViewParcelModel>()
        dataContext.thisData.value.trainingList.forEach { x ->
            ret.add(MediaViewParcelModel().parse(x))
        }
        context.startActivity(Intent(context, MediaListViewActivity::class.java).apply {
            putParcelableList(FConstants.MEDIA_LIST, ret)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }
}
private fun trainingCertificateAdd(dataContext: MyScreenVM) {
    checkReadStorage(dataContext) {
        dataContext.trainingCertificateAdd.value = true
    }
}
private fun taxpayer(dataContext: MyScreenVM) {
    val context = dataContext.context
    val blobUrl = dataContext.thisData.value.taxPayerUrl
    if (blobUrl != null) {
        context.startActivity((Intent(context, MediaViewActivity::class.java).apply {
            putParcelable(FConstants.MEDIA_ITEM, MediaViewParcelModel().apply {
                this.blobUrl = blobUrl
                this.mimeType = dataContext.thisData.value.taxPayerMimeType ?: ""
                this.originalFilename = dataContext.thisData.value.trainingFilename ?: ""
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }))
        return
    }
    checkReadStorage(dataContext) {
        dataContext.userFileSelect.value = UserFileType.Taxpayer.index
    }
}
private fun bankAccount(dataContext: MyScreenVM) {
    val context = dataContext.context
    val blobUrl = dataContext.thisData.value.bankAccountUrl
    if (blobUrl != null) {
        context.startActivity((Intent(context, MediaViewActivity::class.java).apply {
            putParcelable(FConstants.MEDIA_ITEM, MediaViewParcelModel().apply {
                this.blobUrl = blobUrl
                this.mimeType = dataContext.thisData.value.bankAccountMimeType ?: ""
                this.originalFilename = dataContext.thisData.value.bankAccountFilename ?: ""
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }))
        return
    }
    checkReadStorage(dataContext) {
        dataContext.userFileSelect.value = UserFileType.BankAccount.index
    }
}
private fun csoReport(dataContext: MyScreenVM) {
    val context = dataContext.context
    val blobUrl = dataContext.thisData.value.csoReportUrl
    if (blobUrl != null) {
        context.startActivity((Intent(context, MediaViewActivity::class.java).apply {
            putParcelable(FConstants.MEDIA_ITEM, MediaViewParcelModel().apply {
                this.blobUrl = blobUrl
                this.mimeType = dataContext.thisData.value.csoReportMimeType ?: ""
                this.originalFilename = dataContext.thisData.value.csoReportFilename ?: ""
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }))
        return
    }
    checkReadStorage(dataContext) {
        dataContext.userFileSelect.value = UserFileType.CsoReport.index
    }
}
private fun marketingContract(dataContext: MyScreenVM) {
    val context = dataContext.context
    val blobUrl = dataContext.thisData.value.marketingContractUrl
    if (blobUrl != null) {
        context.startActivity((Intent(context, MediaViewActivity::class.java).apply {
            putParcelable(FConstants.MEDIA_ITEM, MediaViewParcelModel().apply {
                this.blobUrl = blobUrl
                this.mimeType = dataContext.thisData.value.marketingContractMimeType ?: ""
                this.originalFilename = dataContext.thisData.value.marketingContractFilename ?: ""
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }))
        return
    }
    checkReadStorage(dataContext) {
        dataContext.userFileSelect.value = UserFileType.MarketingContract.index
    }
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