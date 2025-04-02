package sdmed.extra.cso.models.services

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import sdmed.extra.cso.bases.FBaseService
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.views.component.PermissionActivity

class FPermissionService(applicationContext: Context): FBaseService(applicationContext) {
    fun externalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val uri = "package:${context.packageName}".toUri()
                context.startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }
        }
    }
    fun requestCameraPermissions(callback: (Boolean) -> Unit) {
        if (hasCameraGranted(context)) {
            callback(true)
            return
        }
        requestPermissions(FConstants.CAMERA_PERMISSION, callback)
    }
    fun requestReadExternalPermissions(callback: (Boolean) -> Unit) {
        if (hasReadExternalGranted(context)) {
            callback(true)
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(FConstants.READ_EXTERNAL_PERMISSION_32, callback)
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(FConstants.READ_EXTERNAL_PERMISSION_33, callback)
        } else {
            requestPermissions(FConstants.READ_EXTERNAL_PERMISSION_34, callback)
        }
    }
    fun requestLocation(callback: (Boolean) -> Unit) {
        if (hasLocationGranted(context)) {
            callback(true)
            return
        }

        requestPermissions(FConstants.LOCATION_PERMISSION, callback)
    }
    fun requestPermissions(permit: String, callback: (Boolean) -> Unit) {
        val intent = Intent(context, PermissionActivity::class.java).apply {
            putExtra(FConstants.PERMISSION, permit)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        PermissionActivity.setPermissionCallback(callback)
        context.startActivity(intent)
    }
    fun requestPermissions(permit: Array<String>, callback: (Boolean) -> Unit) {
        val intent = Intent(context, PermissionActivity::class.java).apply {
            putExtra(FConstants.PERMISSIONS, permit)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        PermissionActivity.setPermissionCallback(callback)
        context.startActivity(intent)
    }

    fun hasCameraGranted(context: Context): Boolean {
        return hasPermissionsGranted(context, FConstants.CAMERA_PERMISSION)
    }
    fun hasReadExternalGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            hasPermissionsGranted(context, FConstants.READ_EXTERNAL_PERMISSION_32)
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            hasPermissionsGranted(context, FConstants.READ_EXTERNAL_PERMISSION_33)
        } else {
            hasPermissionsGranted(context, FConstants.READ_EXTERNAL_PERMISSION_34)
        }
    }
    fun hasLocationGranted(context: Context): Boolean {
        return hasPermissionsGranted(context, FConstants.LOCATION_PERMISSION)
    }
    fun hasPermissionsGranted(context: Context, permission: String) = ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
    fun hasPermissionsGranted(context: Context, permissions: Array<String>) = permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}