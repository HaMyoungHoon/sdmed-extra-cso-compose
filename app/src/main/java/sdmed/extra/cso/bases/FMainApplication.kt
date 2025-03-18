package sdmed.extra.cso.bases

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.MainActivity
import sdmed.extra.cso.models.services.ForcedTerminationService

@HiltAndroidApp
class FMainApplication: MultiDexApplication(), LifecycleEventObserver {
    companion object {
        var isForeground = MutableStateFlow(false)
        private set
        var isMainActivityRunning = MutableStateFlow(false)
        private set

        private var _ins: FMainApplication? = null
        val ins: FMainApplication get() {
            if (_ins == null) {
                _ins = FMainApplication()
            }
            return _ins!!
        }
    }

    fun isDebug() = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    fun getVersionCode(flags: Int = 0): Long {
        val pInfo = getPackageInfo(flags)
        return pInfo?.longVersionCode ?: 0
    }
    fun getVersionName(flags: Int = 0): Long {
        val versionName = getVersionNameString(flags)
        return if (versionName.isEmpty()) {
            0L
        } else {
            versionName.replace(".","").toLong()
        }
    }
    fun getVersionNameString(flags: Int = 0): String {
        val pInfo = getPackageInfo(flags)
        return pInfo?.versionName ?: ""
    }
    fun getPackageInfo(flags: Int = 0): PackageInfo? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
            } else {
                packageManager.getPackageInfo(packageName, flags)
            }
        } catch (e: Exception) {
            return null
        }
    }
    fun getApplicationID() = applicationInfo.processName.toString()
    fun getSignatureArray(): Array<Signature>? {
        val flag = PackageManager.GET_SIGNING_CERTIFICATES
        val pInfo = getPackageInfo(flag) ?: return null
        return try {
            pInfo.signingInfo?.apkContentsSigners
        } catch (_: Exception) {
            return null
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    override fun onCreate() {
        super.onCreate()
        _ins = this
        registerActivity()
//        FThemeUtil.applyTheme()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        try {
            startService(Intent(this, ForcedTerminationService::class.java))
        } catch (_: Exception) { }
    }
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_START) {
            isForeground.value = true
        } else if (event == Lifecycle.Event.ON_STOP) {
            isForeground.value = false
        }
    }
    private fun registerActivity() {
        registerActivityLifecycleCallbacks(object: ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {
                if (activity is MainActivity) {
                    isMainActivityRunning.value = true
                }
            }

            override fun onActivityStopped(activity: Activity) {
                if (activity is MainActivity) {
                    isMainActivityRunning.value = false
                }
            }

            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {
                if (activity is MainActivity) {
                    isMainActivityRunning.value = false
                }
            }
        })
    }
}