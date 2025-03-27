package sdmed.extra.cso

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.accompanist.adaptive.calculateDisplayFeatures
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.common.NotifyIndex
import sdmed.extra.cso.models.menu.Route
import sdmed.extra.cso.models.retrofit.FRetrofitVariable
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.views.main.landing.landingScreen
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
        checkToken()
        super.onCreate(savedInstanceState)
        setContent {
            FThemeUtil.thisTheme {
                setToast()
                setLoading()
                val windowSize = calculateWindowSizeClass(this)
                val displayFeatures = calculateDisplayFeatures(this)
                val foldingDevicePosture = getFoldingDevicePosture(displayFeatures)
                val windowPanelType = getWindowPaneType(windowSize, foldingDevicePosture)
                val require = requireLogin.collectAsState()
                val token by FRetrofitVariable.token.collectAsState()
                LaunchedEffect(token) {
                    checkToken()
                }
                Box(Modifier
                    .windowInsetsPadding(WindowInsets.statusBars.only(WindowInsetsSides.Top))
                    .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom))
                    .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Horizontal))
                    ) {
                    if (require.value) {
                        landingScreen(windowPanelType, displayFeatures)
                    } else {
                        val startDest = openPage()
                        thisApp(windowPanelType, displayFeatures, startDest)
                    }
                }
                mqttInit()
            }
        }
        setBackPressed()
    }

    private fun openPage(): Route {
        val notifyIndex = NotifyIndex.parseIndex(intent.getIntExtra(FConstants.NOTIFY_INDEX, 0))
        val thisPK = intent.getStringExtra(FConstants.NOTIFY_PK) ?: ""
        val route = when (notifyIndex) {
            NotifyIndex.QNA_UPLOAD,
            NotifyIndex.QNA_FILE_UPLOAD,
            NotifyIndex.QNA_RESPONSE -> return Route.QNA(thisPK)
            NotifyIndex.USER_FILE_UPLOAD -> return Route.MY()
            else -> Route.EDI(thisPK)
        }

        return route
    }

    private fun mqttInit() {
        FCoroutineUtil.coroutineScope({
            dataContext.mqttService.mqttInit()
        })
    }
    private fun setBackPressed() {
        _backPressed = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                moveTaskToBack(true)
            }
        }
        this.onBackPressedDispatcher.addCallback(this, _backPressed!!)
    }
}