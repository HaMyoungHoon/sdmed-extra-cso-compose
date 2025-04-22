package sdmed.extra.cso

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.adaptive.calculateDisplayFeatures
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.bases.FMainApplication
import sdmed.extra.cso.models.common.NotifyIndex
import sdmed.extra.cso.models.menu.MenuList
import sdmed.extra.cso.models.menu.Route
import sdmed.extra.cso.models.retrofit.FRetrofitVariable
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.utils.FStorage
import sdmed.extra.cso.utils.FVersionControl
import sdmed.extra.cso.views.dialog.message.MessageDialogData
import sdmed.extra.cso.views.dialog.message.MessageDialogVM
import sdmed.extra.cso.views.dialog.message.messageDialog
import sdmed.extra.cso.views.navigation.NavigationAction
import sdmed.extra.cso.views.navigation.getFoldingDevicePosture
import sdmed.extra.cso.views.navigation.getWindowPaneType
import sdmed.extra.cso.views.navigation.thisApp
import sdmed.extra.cso.views.theme.FThemeUtil

class MainActivity: FBaseActivity<MainActivityVM>() {
    override val dataContext: MainActivityVM by viewModels()
    private var _backPressed: OnBackPressedCallback? = null
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        initCommand()
        versionCheck(dataContext)
        notificationCheck()
        setContent {
            FThemeUtil.thisTheme {
                setToast()
                setLoading()
                val windowSize = calculateWindowSizeClass(this)
                val displayFeatures = calculateDisplayFeatures(this)
                val foldingDevicePosture = getFoldingDevicePosture(displayFeatures)
                val windowPanelType = getWindowPaneType(windowSize, foldingDevicePosture)
                val updateVisible by dataContext.updateVisible.collectAsState()
                val updateApp by dataContext.updateApp.collectAsState()
                val tokenExpired by dataContext.tokenExpired.collectAsState()
                Box(Modifier
                    .windowInsetsPadding(WindowInsets.statusBars.only(WindowInsetsSides.Top))
                    .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom))
                    .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Horizontal))
                    ) {

                    if (updateVisible) {
                        messageDialog(MessageDialogData().apply {
                            title = stringResource(R.string.new_version_update_desc)
                            leftBtnText = stringResource(R.string.update_desc)
                            rightBtnText = ""
                            isCancel = false
                            relayCommand = dataContext.relayCommand
                        })
                    }
                    if (updateApp) {
                        startActivity(Intent(Intent.ACTION_VIEW).apply {
                            this.data = "market://details?id=${packageName}".toUri()
                        })
                    }
                    val dest = openPage()
                    thisApp(windowPanelType, displayFeatures, dest)

                    if (tokenExpired) {
                        val navHostController = rememberNavController()
                        val navigationActions = remember(navHostController) {
                            NavigationAction(navHostController)
                        }
                        navigationActions.navigateTo(MenuList.menuLanding(), true)
                        dataContext.tokenExpired.value = false
                    }
                }
                mqttInit()
            }
        }
        setBackPressed()
    }

    override fun onResume() {
        super.onResume()
        if (FRetrofitVariable.token.value.isNullOrEmpty()) {
            if (!FAmhohwa.tokenCheck(dataContext)) {
                FAmhohwa.tokenRefresh(dataContext) {
                    if (it.result == true) {
                        dataContext.mqttReInit()
                    } else {
                        FAmhohwa.logout(this, true)
                        dataContext.mqttDisconnect()
                        dataContext.tokenExpired.value = true
                    }
                }
            }
        }
        versionCheck(dataContext)
    }

    private fun openPage(): String {
        val notifyIndex = NotifyIndex.parseIndex(intent.getIntExtra(FConstants.NOTIFY_INDEX, 0))
        val thisPK = intent.getStringExtra(FConstants.NOTIFY_PK) ?: ""
        val route = when (notifyIndex) {
            NotifyIndex.EDI_UPLOAD,
            NotifyIndex.EDI_FILE_UPLOAD,
            NotifyIndex.EDI_FILE_REMOVE,
            NotifyIndex.EDI_RESPONSE -> Route.EDI.Main
            NotifyIndex.QNA_UPLOAD,
            NotifyIndex.QNA_FILE_UPLOAD,
            NotifyIndex.QNA_RESPONSE -> Route.QNA.Main
            NotifyIndex.USER_FILE_UPLOAD -> Route.MY.Main
            else -> Route.LANDING.Main
        }

        return route.data.path
    }

    private fun versionCheck(dataContext: MainActivityVM) {
        dataContext.loading()
        FCoroutineUtil.coroutineScope({
            val ret = dataContext.versionCheck()
            dataContext.loading(false)
            if (ret.result != true) {
                dataContext.toast(ret.msg)
                return@coroutineScope
            }
            if (!ret.data.isNullOrEmpty()) {
                val versionModel = ret.data?.firstOrNull { it.able } ?: return@coroutineScope
                val currentVersion = FMainApplication.ins.getVersionNameString()
                val check = FVersionControl.checkVersion(versionModel, currentVersion)
                if (check == FVersionControl.VersionResultType.NEED_UPDATE) {
                    dataContext.updateVisible.value = true
                    return@coroutineScope
                }
            }
        })
    }

    private fun mqttInit() {
        dataContext.mqttInit()
    }
    private fun setBackPressed() {
        _backPressed = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                moveTaskToBack(true)
            }
        }
        this.onBackPressedDispatcher.addCallback(this, _backPressed!!)
    }

    override fun setLayoutCommand(data: Any?) {
        setUpDateCommand(data)
    }
    private fun setUpDateCommand(data: Any?) {
        val eventName = data as? MessageDialogVM.ClickEvent ?: return
        when (eventName) {
            MessageDialogVM.ClickEvent.CLOSE -> dialogClose()
            MessageDialogVM.ClickEvent.LEFT -> dialogLeft()
            MessageDialogVM.ClickEvent.RIGHT -> dialogRight()
        }
    }
    private fun dialogClose() {
        dataContext.updateVisible.value = false
    }
    private fun dialogLeft() {
        dataContext.updateApp.value = true
    }
    private fun dialogRight() {
        dataContext.updateApp.value = true
    }
    private fun notificationCheck() {
        requestNotificationGranted()
    }
    private fun requestNotificationGranted() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return
        }
        if (!dataContext.permissionService.hasPermissionsGranted(this, FConstants.NOTIFICATION_PERMISSION)) {
            return
        }
        dataContext.permissionService.requestPermissions(FConstants.NOTIFICATION_PERMISSION, { x ->
        })
    }
}