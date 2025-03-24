package sdmed.extra.cso.bases

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.kodein.di.DIAware
import org.kodein.di.DIContext
import org.kodein.di.diContext
import sdmed.extra.cso.interfaces.command.IAsyncEventListener
import sdmed.extra.cso.models.common.ToastMessageModel
import sdmed.extra.cso.models.eventbus.TokenCheckEvent
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
import sdmed.extra.cso.views.landing.LandingActivity

abstract class FBaseActivity<T: FBaseViewModel>(val needRoles: UserRoles = UserRole.None.toS()): ComponentActivity(), DIAware {
    final override val diContext: DIContext<*> get() = diContext(this)
    override val di by lazy { (application as FMainApplication).di }
    protected abstract val dataContext: T
    private var _needTokenRefresh = true
    val initAble get() = !_needTokenRefresh
    var isActivityPause = false
        private set
    protected var myState: UserStatus = UserStatus.None
        private set
    protected var haveRole: Boolean = false
        private set

    @Composable
    protected abstract fun content(dataContext: T)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setToast()
            setLoading()
            content(dataContext)
        }
        _needTokenRefresh = getToken()
        if (initAble) {
            FCoroutineUtil.coroutineScope({
                afterOnCreate()
            })
        }
    }
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
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    private suspend fun afterOnCreate() {
        loading()
        if (this !is LandingActivity) {// && this !is LoginActivity) {
            stateCheck()
            if (myState != UserStatus.Live) {
                loading(false)
                return
            }
            roleCheck()
            if (!haveRole) {
                loading(false)
                return
            }
        }
        loading(false)

        viewInit()
    }
    open fun viewInit() {
        setEventListener()
    }
    open fun setEventListener() {
        dataContext.addEventListener(object: IAsyncEventListener {
            override suspend fun onEvent(data: Any?) {
                setLayoutCommand(data)
            }
        })
    }
    open fun setLayoutCommand(data: Any?) {
    }

    protected fun toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) = toast(resources.getString(resId), duration)
    protected fun toast(msg: String?, duration: Int = Toast.LENGTH_SHORT) = dataContext.uiStateService.toast(msg, duration)
    protected fun loading(isVisible: Boolean = true, msg: String = "", indicatorColor: Color? = null, textColor: Color? = null) {
        dataContext.uiStateService.loading(isVisible, msg, indicatorColor, textColor)
    }

    @Composable
    private fun setToast() {
        val toastMessageModel = dataContext.uiStateService.toast.collectAsState()
        val toast = toastMessageModel.value as? ToastMessageModel.Visible ?: return
        Toast.makeText(LocalContext.current, toast.msg, toast.duration).show()
    }
    @Composable
    private fun setLoading() {
        val loading = dataContext.uiStateService.isLoading.collectAsState()
        LoadingDialog.screen(loading)
    }

    private suspend fun stateCheck() {
        val ret = dataContext.getMyState()
        if (ret.result == true) {
            myState = ret.data ?: UserStatus.None
        } else {
            toast(ret.msg)
        }
    }
    private suspend fun roleCheck() {
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
    private fun getToken(): Boolean {
        if (FRetrofitVariable.token == null) {
            FRetrofitVariable.token = FStorage.getAuthToken(this)
        }
        if (FRetrofitVariable.token.isNullOrBlank()) {
            goToLogin()
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
            return false
        } catch (_: Exception) {
            goToLogin()
        }
        return true
    }
    private fun goToLogin(expired: Boolean = false) {
        if (this is LandingActivity) { // || this is LoginActivity) {
            FCoroutineUtil.coroutineScope({
                afterOnCreate()
            })
        } else {
            FAmhohwa.logout(this, expired = expired)
        }
    }
    private fun reCreate() {
        if (this is LandingActivity) {
            return
        } else {
            recreate()
        }
    }
    private fun tokenRefresh() {
        if (FRetrofitVariable.token.isNullOrBlank()) {
            goToLogin(true)
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
                FRetrofitVariable.token = FStorage.getAuthToken(context)
            } else {
                if (ret.code == -10002) {
                    delLoginData()
                }
                FStorage.removeAuthToken(context)
                goToLogin(true)
            }
            FCoroutineUtil.coroutineScope({
                afterOnCreate()
            })
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun tokenCheckEvent(event: TokenCheckEvent) {
    }
}