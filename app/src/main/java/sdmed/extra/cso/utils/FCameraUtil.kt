package sdmed.extra.cso.utils

import sdmed.extra.cso.R
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import com.gun0912.tedpermission.coroutine.TedPermission
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.bases.FMainApplication
import sdmed.extra.cso.models.services.FUIStateService
import java.io.File
import java.io.IOException

class FCameraUtil(var context: Context, private var cameraResult: ActivityResultLauncher<Intent>) {
    var path: String = ""
    var targetPK: String = ""

    fun showCamera(targetPK: String = "") : String {
        if (Intent(MediaStore.ACTION_IMAGE_CAPTURE).resolveActivity(context.packageManager) != null) {
            FCoroutineUtil.coroutineScope({
                val permissionResult = TedPermission.create()
                    .setRationaleTitle(context.getString(R.string.permit_title))
                    .setRationaleMessage(context.getString(R.string.permit_camera))
                    .setDeniedTitle(context.getString(R.string.cancel_desc))
                    .setDeniedMessage(context.getString(R.string.permit_denied))
                    .setGotoSettingButtonText(context.getString(R.string.permit_setting))
                    .setPermissions(*FConstants.CAMERA_PERMISSION)
                    .check()

                if (!permissionResult.isGranted) {
                    FUIStateService().toast(context.getString(R.string.permit_require))
                } else {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (takePictureIntent.resolveActivity(context.packageManager) != null) {
                        var photoFile: File? = null
                        try {
                            photoFile = FImageUtils.createImageFile(context)
                        } catch (_: IOException) {
                        }
                        if (photoFile != null) {
                            val photoURI: Uri = FileProvider.getUriForFile(context, FMainApplication.ins.getApplicationID() + ".provider", photoFile)
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            path = photoFile.absolutePath
                            this@FCameraUtil.targetPK = targetPK
                        }
                    }
                    cameraResult.launch(takePictureIntent)
                }
            })
        }
        return path
    }
}