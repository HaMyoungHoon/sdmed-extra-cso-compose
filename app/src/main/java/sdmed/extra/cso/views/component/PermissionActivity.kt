package sdmed.extra.cso.views.component

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import sdmed.extra.cso.bases.FConstants

class PermissionActivity: ComponentActivity() {
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { x ->
        permissionCallback?.invoke(x)
        finish()
    }
    private val permissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { x ->
        permissionCallback?.invoke(x.values.all { y -> y })
    }
    companion object {
        private var permissionCallback: ((Boolean) -> Unit)? = null
        fun setPermissionCallback(callback: (Boolean) -> Unit) {
            permissionCallback = callback
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permission = intent.getStringExtra(FConstants.PERMISSION)
        if (permission != null) {
            singleCheck(permission)
            return
        }
        val permissions = intent.getStringArrayExtra(FConstants.PERMISSIONS)
        if (permissions != null) {
            multiCheck(permissions)
        }
    }
    private fun singleCheck(permission: String) {
        if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            permissionCallback?.invoke(true)
            finish()
        } else {
            permissionLauncher.launch(permission)
        }
        return
    }
    private fun multiCheck(permissions: Array<String>) {
        val permissionsToRequest = permissions.filter {
            ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (permissionsToRequest.isEmpty()) {
            permissionCallback?.invoke(permissions.associateWith { true }.values.all { it })
            finish()
        } else {
            permissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }
}