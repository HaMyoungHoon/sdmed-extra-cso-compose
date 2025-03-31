package sdmed.extra.cso.bases

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.window.layout.DisplayFeature
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.interfaces.command.IAsyncEventListener
import sdmed.extra.cso.models.command.AsyncRelayCommand
import sdmed.extra.cso.models.common.ToastMessageModel
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.models.retrofit.FRetrofitVariable
import sdmed.extra.cso.models.retrofit.users.UserRole
import sdmed.extra.cso.models.retrofit.users.UserRole.Companion.getFlag
import sdmed.extra.cso.models.retrofit.users.UserRoles
import sdmed.extra.cso.models.retrofit.users.UserStatus
import sdmed.extra.cso.views.component.loadingDialog
import sdmed.extra.cso.views.navigation.getFoldingDevicePosture
import sdmed.extra.cso.views.navigation.getWindowPaneType
import sdmed.extra.cso.views.navigation.isCompact
import sdmed.extra.cso.views.navigation.toNavType

abstract class FBaseActivity<T: FBaseViewModel>(val needRoles: UserRoles = UserRole.None.toS()): ComponentActivity() {
    protected abstract val dataContext: T
    var isActivityPause = false
        private set
    val requireLogin = MutableStateFlow(true)
    protected var myState: UserStatus = UserStatus.None
        private set
    protected var haveRole: Boolean = false
        private set

    override fun onResume() {
        isActivityPause = false
        super.onResume()
    }
    override fun onPause() {
        isActivityPause = true
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    protected fun initCommand() {
        dataContext.relayCommand = AsyncRelayCommand()
        dataContext.addEventListener(object: IAsyncEventListener {
            override suspend fun onEvent(data: Any?) {
                setLayoutCommand(data)
            }
        })
    }
    @Composable
    protected fun calcScreen(windowSize: WindowSizeClass, displayFeatures: List<DisplayFeature> = emptyList()) {
        val foldingDevicePosture = getFoldingDevicePosture(displayFeatures)
        val windowPanelType = getWindowPaneType(windowSize, foldingDevicePosture)
        val adaptiveInfo = currentWindowAdaptiveInfo()
        val windowSize = with(LocalDensity.current) { currentWindowSize().toSize().toDpSize() }
        val navLayoutType = when {
            adaptiveInfo.windowPosture.isTabletop -> NavigationSuiteType.NavigationBar
            adaptiveInfo.windowSizeClass.isCompact() -> NavigationSuiteType.NavigationBar
            adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED &&
                    windowSize.width >= 1200.dp -> NavigationSuiteType.NavigationDrawer
            else -> NavigationSuiteType.NavigationRail
        }
        when (windowPanelType) {
            WindowPanelType.SINGLE_PANE -> {
                if (navLayoutType.toNavType() == NavigationType.BOTTOM) {
                    phone(dataContext)
                } else {
                    tablet(dataContext)
                }
            }
            WindowPanelType.DUAL_PANE -> twoPane(dataContext, displayFeatures)
        }
    }
    @Composable
    open fun phone(dataContext: T) {
    }
    @Composable
    open fun tablet(dataContext: T) {
    }
    @Composable
    open fun twoPane(dataContext: T, displayFeatures: List<DisplayFeature>) {
    }
    open fun setLayoutCommand(data: Any?) {
    }

    protected fun toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) = toast(resources.getString(resId), duration)
    protected fun toast(msg: String?, duration: Int = Toast.LENGTH_SHORT) = dataContext.toast(msg, duration)
    protected fun loading(isVisible: Boolean = true, msg: String = "") {
        dataContext.loading(isVisible, msg)
    }

    @Composable
    protected fun setToast() {
        val toastMessageModel by dataContext.uiStateService.toast.collectAsState()
        val toast = toastMessageModel as? ToastMessageModel.Visible ?: return
        Toast.makeText(LocalContext.current, toast.msg, toast.duration).show()
        dataContext.unToastComposable()
    }
    @Composable
    protected fun setLoading() {
        val loading by dataContext.uiStateService.isLoading.collectAsState()
        loadingDialog(loading)
    }

    private suspend fun stateCheck() {
        if (FRetrofitVariable.token.value == null) {
            return
        }
        val ret = dataContext.getMyState()
        if (ret.result == true) {
            myState = ret.data ?: UserStatus.None
        } else {
            toast(ret.msg)
        }
    }
    private suspend fun roleCheck() {
        if (FRetrofitVariable.token.value == null) {
            return
        }
        if (needRoles.getFlag() == 0) {
            haveRole = true
            return
        }
        val ret = dataContext.getMyRole()
        if (ret.result == true) {
            haveRole = ((ret.data ?: 0) and needRoles.getFlag()) != 0
        } else {
            haveRole = false
            toast(ret.msg)
        }
    }
}