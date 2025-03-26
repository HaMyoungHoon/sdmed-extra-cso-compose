package sdmed.extra.cso.bases

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.models.common.ToastMessageModel
import sdmed.extra.cso.models.retrofit.FRetrofitVariable
import sdmed.extra.cso.models.retrofit.users.UserMultiLoginModel
import sdmed.extra.cso.models.retrofit.users.UserRole
import sdmed.extra.cso.models.retrofit.users.UserRole.Companion.getFlag
import sdmed.extra.cso.models.retrofit.users.UserRoles
import sdmed.extra.cso.models.retrofit.users.UserStatus
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FStorage
import sdmed.extra.cso.views.component.LoadingDialog

abstract class FBaseActivity<T: FBaseViewModel>(val needRoles: UserRoles = UserRole.None.toS()): ComponentActivity() {
    protected abstract val dataContext: T
    private var _needTokenRefresh = true
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
        if (!_needTokenRefresh) {
            _needTokenRefresh = getToken()
        }
    }
    override fun onPause() {
        isActivityPause = true
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        loading(false)
    }

    protected fun toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) = toast(resources.getString(resId), duration)
    protected fun toast(msg: String?, duration: Int = Toast.LENGTH_SHORT) = dataContext.uiStateService.toast(msg, duration)
    protected fun loading(isVisible: Boolean = true, msg: String = "") {
        dataContext.uiStateService.loading(isVisible, msg)
    }

    @Composable
    protected fun setToast() {
        val toastMessageModel by dataContext.uiStateService.toast.collectAsState()
        val toast = toastMessageModel as? ToastMessageModel.Visible ?: return
        Toast.makeText(LocalContext.current, toast.msg, toast.duration).show()
        dataContext.uiStateService.unToast()
    }
    @Composable
    protected fun setLoading() {
        val loading by dataContext.uiStateService.isLoading.collectAsState()
        LoadingDialog().screen(loading)
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
    fun checkToken() {
        getToken()
    }
    private fun getToken(): Boolean {
        if (FRetrofitVariable.token.value == null) {
            FRetrofitVariable.token.value = FStorage.getAuthToken(this)
        }
        if (FRetrofitVariable.token.value.isNullOrBlank()) {
            requireLogin.value = true
            return true
        }
        try {
            if (!FAmhohwa.checkInvalidToken(this)) {
                if (FStorage.getRefreshing(this)) {
                    return true
                }
                FStorage.setRefreshing(this, true)
                tokenRefresh()
                return true
            }
            requireLogin.value = false
            return false
        } catch (_: Exception) {
        }
        return true
    }
    private fun tokenRefresh() {
        if (FRetrofitVariable.token.value.isNullOrBlank()) {
            requireLogin.value = true
            return
        }
        val context = this@FBaseActivity
        FCoroutineUtil.coroutineScope({
            val ret = dataContext.tokenRefresh()
            FStorage.setRefreshing(context, false)
            if (ret.result == true) {
                val newToken = ret.data ?: ""
                if (FAmhohwa.rhsTokenIsMost(newToken)) {
                    FStorage.setAuthToken(context, newToken)
                    addLoginData()
                }
                FRetrofitVariable.token.value = FStorage.getAuthToken(context)
                requireLogin.value = false
            } else {
                if (ret.code == -10002) {
                    delLoginData()
                }
                FStorage.removeAuthToken(context)
            }
        })
    }
    protected fun addLoginData() {
        val context = this
        FStorage.addMultiLoginData(context, UserMultiLoginModel().apply {
            thisPK = FAmhohwa.getThisPK(context)
            id = FAmhohwa.getTokenID(context)
            name = FAmhohwa.getTokenName(context)
            token = FStorage.getAuthToken(context) ?: ""
            isLogin = true
        })
    }
    protected fun delLoginData() {
        val context = this
        FStorage.delMultiLoginData(context, UserMultiLoginModel().apply {
            thisPK = FAmhohwa.getThisPK(context)
            id = FAmhohwa.getUserID(context)
            name = FAmhohwa.getUserName(context)
            token = FStorage.getAuthToken(context) ?: ""
            isLogin = true
        })
    }

    protected fun shouldShowRequestPermissionRationale(permissions: Array<String>) = permissions.any { ActivityCompat.shouldShowRequestPermissionRationale(this, it) }
    protected fun hasPermissionsGranted(permissions: Array<String>) = permissions.none {
        ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
    }
}