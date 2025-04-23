package sdmed.extra.cso.utils

import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.net.toUri
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.roundToInt
import kotlin.math.sqrt
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import androidx.exifinterface.media.ExifInterface
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FConstants
import java.io.ByteArrayOutputStream

object FImageUtils {
    fun getBitmapFromView(view: View): Bitmap {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val bitmap = createBitmap(view.measuredWidth, view.measuredHeight)
        val canvas = Canvas(bitmap)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.draw(canvas)
        return bitmap
    }
    fun urlToBitmap(url: String): BitmapDescriptor {
        return try {
            val connection = URL(url).openConnection()
            val stream = connection.getInputStream()
            val bitmap = BitmapFactory.decodeStream(stream)
            BitmapDescriptorFactory.fromBitmap(bitmap)
        } catch (_: Exception) {
            BitmapDescriptorFactory.fromResource(R.drawable.buff_image)
        }
    }
    fun bitmapHexagonMask(bitmap: Bitmap, radius: Float = 5F, cornerLength: Float = 10F) {
        val canvas = Canvas(bitmap)
        val halfRadius = radius / 2F
        val triangleHeight = (sqrt(3.0) * halfRadius).toFloat()
        val centerX = bitmap.width / 2F
        val centerY = bitmap.height / 2F
        val corner = cornerLength / 1
        val halfCorner = corner / 2
        val paint = Paint().apply {
            color = Color.WHITE
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 1F
            style = Paint.Style.STROKE
        }
        val hexagonPath = Path().apply {
            reset()
            // ↓
            moveTo(centerX + corner, centerY + radius - halfCorner)
            quadTo(centerX, centerY + radius, centerX - corner, centerY + radius - halfCorner)
            // ↙
            lineTo(centerX - triangleHeight + corner, centerY + halfRadius + halfCorner)
            quadTo(centerX - triangleHeight, centerY + halfRadius, centerX - triangleHeight, centerY + halfRadius - halfCorner)
            // ↖
            lineTo(centerX - triangleHeight, centerY - halfRadius + halfCorner)
            quadTo(centerX - triangleHeight, centerY - halfRadius, centerX - triangleHeight + corner, centerY - halfRadius - halfCorner)
            // ↑
            lineTo(centerX - corner, centerY - radius + halfCorner)
            quadTo(centerX, centerY - radius, centerX + corner, centerY - radius + halfCorner)
            // ↗
            lineTo(centerX + triangleHeight - corner, centerY - halfRadius - halfCorner)
            quadTo(centerX + triangleHeight, centerY - halfRadius, centerX + triangleHeight, centerY - halfRadius + halfCorner)
            // ↘
            lineTo(centerX + triangleHeight, centerY + halfRadius - halfCorner)
            quadTo(centerX + triangleHeight, centerY + halfRadius, centerX + triangleHeight - corner, centerY + halfRadius + halfCorner)
            close()
        }
        val hexagonBorderPath = Path().apply {
            reset()
            // ↓
            moveTo(centerX + corner, centerY + radius - halfCorner)
            quadTo(centerX, centerY + radius, centerX - corner, centerY + radius - halfCorner)
            // ↙
            lineTo(centerX - triangleHeight + corner, centerY + halfRadius + halfCorner)
            quadTo(centerX - triangleHeight, centerY + halfRadius, centerX - triangleHeight, centerY + halfRadius - halfCorner)
            // ↖
            lineTo(centerX - triangleHeight, centerY - halfRadius + halfCorner)
            quadTo(centerX - triangleHeight, centerY - halfRadius, centerX - triangleHeight + corner, centerY - halfRadius - halfCorner)
            // ↑
            lineTo(centerX - corner, centerY - radius + halfCorner)
            quadTo(centerX, centerY - radius, centerX + corner, centerY - radius + halfCorner)
            // ↗
            lineTo(centerX + triangleHeight - corner, centerY - halfRadius - halfCorner)
            quadTo(centerX + triangleHeight, centerY - halfRadius, centerX + triangleHeight, centerY - halfRadius + halfCorner)
            // ↘
            lineTo(centerX + triangleHeight, centerY + halfRadius - halfCorner)
            quadTo(centerX + triangleHeight, centerY + halfRadius, centerX + triangleHeight - corner, centerY + halfRadius + halfCorner)
            close()
        }
        canvas.drawPath(hexagonBorderPath, paint)
        canvas.clipPath(hexagonPath)
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
    }
    fun fileDelete(context: Context, file: File) {
        try {
            file.delete()
        } catch (_: Exception) {
        }
    }
    fun fileDelete(context: Context, uri: Uri?) {
        uri ?: return
        if (ableDeleteFile(context, uri)) {
            try {
                context.contentResolver.delete(uri, null, null)
            } catch (_: Exception) {
            }
        }
    }
    fun fileDelete(context: Context, url: String) {
        try {
            File(url).delete()
        } catch (_: Exception) {
        }
    }
    fun ableDeleteFile(context: Context, uri: Uri) = try {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        context.contentResolver.query(uri, projection, null, null, null)?.use {
            if (it.moveToFirst()) {
                val filePath = it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                val file = File(filePath)
                if (file.exists()) {
                    val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                    val isWritable = fileDescriptor.fileDescriptor.valid()
                    fileDescriptor.close()
                    return isWritable
                } else {
                    return false
                }
            } else {
                return true
            }
        }
        false
    } catch (_: Exception) {
        false
    }
    fun isImage(ext: String): Boolean {
        if (ext == "jpeg") return true
        if (ext == "jpg") return true
        if (ext == "png") return true
        if (ext == "bmp") return true
        if (ext == "webp") return true
        if (ext == "heic") return true
        if (ext == "gif") return true

        return false;
    }
    fun isGif(ext: String): Boolean {
        return ext == "gif"
    }
    fun isPdf(ext: String): Boolean {
        return ext == "pdf"
    }
    fun isExcel(ext: String): Boolean {
        if (ext == "xls") return true
        if (ext == "xlsx") return true

        return false
    }
    fun getDefaultImage(mimeType: String?): Int {
        val ext = FContentsType.getExtMimeType(mimeType)
        if (ext == "pdf") {
            return R.drawable.image_pdf
        } else if (ext == "xlsx" || ext == "xls") {
            return R.drawable.image_excel
        } else if (ext == "docx" || ext == "doc") {
            return R.drawable.image_word
        } else if (ext == "zip") {
            return R.drawable.image_zip
        }
        return R.drawable.image_no_image_1920
    }
    fun isVideoFile(context: Context, uri: Uri): Boolean {
        val videoFileHeaders = listOf(
            byteArrayOf(0x00, 0x00, 0x00, 0x18, 0x66, 0x74, 0x79, 0x70, 0x33, 0x67, 0x70, 0x35), // mp4
            byteArrayOf(0x52, 0x49, 0x46, 0x46, 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x41, 0x56, 0x49, 0x20, 0x4C, 0x49, 0x53, 0x54), // avi
            byteArrayOf(0x00, 0x00, 0x00, 0x18, 0x66, 0x74, 0x79, 0x70, 0x71, 0x74, 0x20, 0x20, 0x6D, 0x6F, 0x6F, 0x76), // mov
//            byteArrayOf(0x52, 0x49, 0x46, 0x46, 0x2A, 0x00, 0x00, 0x00, 0x57, 0x45, 0x42, 0x50) // webp
        )

        if (isLocalFile(context, uri)) {
            FileInputStream(uri.toFile()).use { x ->
                val headerBytes = ByteArray(16)
                x.read(headerBytes)
                for (videoHeader in videoFileHeaders) {
                    if (headerBytes.contentEquals(videoHeader)) {
                        return true
                    }
                }
            }
        } else {
            context.contentResolver.openInputStream(uri).use { x ->
                val headerBytes = ByteArray(16)
                x?.read(headerBytes)
                for (videoHeader in videoFileHeaders) {
                    if (headerBytes.contentEquals(videoHeader)) {
                        return true
                    }
                }
            }
        }
        return false
    }
    fun isLocalFile(context: Context, fileUri: Uri): Boolean {
        val scheme = fileUri.scheme
        return scheme == "file" || scheme == "context" && ContentResolver.SCHEME_FILE == context.contentResolver.getType(fileUri)
    }
    fun getFileMediaType(file: File): okhttp3.MediaType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)?.toMediaType() ?: "application/json".toMediaType()

    fun uriCopyToTempFolder(context: Context, file: File, fileName: String): Uri {
        val fileDescriptor = context.contentResolver.openFileDescriptor(file.toUri(), "r") ?: return file.toUri()
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        var rootDir = File(pictureDir, FConstants.APP_NAME)
        if (!rootDir.exists()) {
            if (!rootDir.mkdirs()) {
                rootDir = ContextWrapper(context).getDir("Documents", Context.MODE_PRIVATE)
            }
        }

        val copiedFile = File(rootDir, fileName)
        if (!copiedFile.exists()) {
            val outputStream = FileOutputStream(copiedFile)
            inputStream.copyTo(outputStream)
            outputStream.close()
        }
        inputStream.close()
        fileDescriptor.close()
        return copiedFile.toUri()
    }
    fun urlToFile(context: Context, fileUrl: String, fileName: String): File {
        val fileRet = File(fileUrl)
        val fileExt = fileRet.extension
        if (!isImage(fileExt)) {
            return fileRet
        }
        if (isGif(fileExt)) {
            return urlToGifFile(context, fileUrl, fileName)
        }
        val fileStream = fileRet.inputStream()
        val inputStream = ByteArrayInputStream(fileStream.readBytes())
        val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        var rootDir = File(documentsDir, FConstants.SHARED_FILE_NAME)
        if (!rootDir.exists()) {
            if (!rootDir.mkdirs()) {
                rootDir = ContextWrapper(context).getDir("Documents", Context.MODE_PRIVATE)
            }
        }

        var extension = fileExt
        if (extension.lowercase() != "webp") {
            extension = "webp"
        }

        val file = File(rootDir, "${fileName}.$extension")
        if (!file.exists()) {
            inputStream.mark(inputStream.available())
            if (fileExt != "webp") {
                if (!imageResize(inputStream, file)) {
                    val outputStream = FileOutputStream(file)
                    inputStream.copyTo(outputStream)
                    outputStream.close()
                }
            } else {
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)
                outputStream.close()
            }
        }
        try {
            inputStream.close()
            fileStream.close()
        } catch (_: Exception) {
        }
        return file
    }
    fun uriToFile(context: Context, fileUri: Uri, fileName: String): File {
        if (isLocalFile(context, fileUri)) return fileUri.toFile()

        val fileDescriptor = try {
            context.contentResolver.openFileDescriptor(fileUri, "r") ?: return fileUri.toFile()
        } catch (_: Exception) {
            return File(fileUri.toString())
        }
        val fileExt = FContentsType.getExtMimeType(context.contentResolver.getType(fileUri))
        if (!isImage(fileExt)) {
            return fileUri.toFile()
        }
        if (isGif(fileExt)) {
            return uriToGifFile(context, fileUri, fileName)
        }
        val fileStream = FileInputStream(fileDescriptor.fileDescriptor)
        val inputStream = ByteArrayInputStream(fileStream.readBytes())
        val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        var rootDir = File(documentsDir, FConstants.SHARED_FILE_NAME)
        if (!rootDir.exists()) {
            if (!rootDir.mkdirs()) {
                rootDir = ContextWrapper(context).getDir("Documents", Context.MODE_PRIVATE)
            }
        }

        var extension = fileExt
        if (extension.lowercase() != "webp") {
            extension = "webp"
        }

        val file = File(rootDir, "${fileName}.$extension")
        if (!file.exists()) {
            inputStream.mark(inputStream.available())
            if (fileExt != "webp") {
                if (!imageResize(inputStream, file)) {
                    val outputStream = FileOutputStream(file)
                    inputStream.copyTo(outputStream)
                    outputStream.close()
                }
            } else {
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)
                outputStream.close()
            }
        }
        try {
            inputStream.close()
            fileDescriptor.close()
            fileStream.close()
        } catch (_: Exception) {
        }
        return file
    }
    fun urlToGifFile(context: Context, fileUrl: String, fileName: String): File {
        val fileRet = File(fileUrl)
        val fileExt = fileRet.extension
        if (!isImage(fileExt)) {
            return fileRet
        }
        val fileStream = fileRet.inputStream()
        val inputStream = ByteArrayInputStream(fileStream.readBytes())
        val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        var rootDir = File(documentsDir, FConstants.SHARED_FILE_NAME)
        if (!rootDir.exists()) {
            if (!rootDir.mkdirs()) {
                rootDir = ContextWrapper(context).getDir("Documents", Context.MODE_PRIVATE)
            }
        }

        val extension = fileExt

        val file = File(rootDir, "${fileName}.$extension")
        if (!file.exists()) {
            inputStream.mark(inputStream.available())
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            outputStream.close()
        }
        try {
            inputStream.close()
            fileStream.close()
        } catch (_: Exception) {
        }
        return file
    }
    fun uriToGifFile(context: Context, fileUri: Uri, fileName: String): File {
        if (isLocalFile(context, fileUri)) return fileUri.toFile()

        val fileDescriptor = try {
            context.contentResolver.openFileDescriptor(fileUri, "r") ?: return fileUri.toFile()
        } catch (_: Exception) {
            return File(fileUri.toString())
        }
        val fileExt = FContentsType.getExtMimeType(context.contentResolver.getType(fileUri))
        if (!isImage(fileExt)) {
            return fileUri.toFile()
        }
        val fileStream = FileInputStream(fileDescriptor.fileDescriptor)
        val inputStream = ByteArrayInputStream(fileStream.readBytes())
        val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        var rootDir = File(documentsDir, FConstants.SHARED_FILE_NAME)
        if (!rootDir.exists()) {
            if (!rootDir.mkdirs()) {
                rootDir = ContextWrapper(context).getDir("Documents", Context.MODE_PRIVATE)
            }
        }

        val extension = fileExt

        val file = File(rootDir, "${fileName}.$extension")
        if (!file.exists()) {
            inputStream.mark(inputStream.available())
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            outputStream.close()
        }
        try {
            inputStream.close()
            fileDescriptor.close()
            fileStream.close()
        } catch (_: Exception) {
        }
        return file
    }
    fun createImageFile(context: Context): File {
        val timeStamp: String = FExtensions.getToday().toString("yyyyMMdd_HHmmss")
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        return File.createTempFile(
            "jpg_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
        }
    }
    fun imageResize(inputStream: ByteArrayInputStream, outFile: File): Boolean {
        try {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
                BitmapFactory.decodeStream(inputStream, null, this)
                inSampleSize = calcResize(this).roundToInt()
                inJustDecodeBounds = false
            }
//            if (needConverter) {
//                options.inPreferredConfig = Bitmap.Config.ARGB_8888
//            }
            inputStream.reset()
            val resize = calcResize(options)
//            if (resize <= 1F) {
//                return false
//            }
            val orientation = getOrientation(inputStream)
            val originBitmap = BitmapFactory.decodeStream(inputStream, null, options) ?: return false
            val resizedBitmap = rotateScaledBitmap(originBitmap, options, resize, orientation)
            inputStream.reset()
            val originSize = inputStream.available()
            val targetSize = if (FConstants.MAX_FILE_SIZE > originSize) originSize else FConstants.MAX_FILE_SIZE
            var quality = 80
            var streamBuff = ByteArrayOutputStream()
            val outputStream = FileOutputStream(outFile)
            do {
                streamBuff.reset()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    resizedBitmap.compress(Bitmap.CompressFormat.WEBP_LOSSLESS, quality, streamBuff)
                } else {
                    @Suppress("DEPRECATION")
                    resizedBitmap.compress(Bitmap.CompressFormat.WEBP, quality, streamBuff)
                }
                quality -= 10
            } while (streamBuff.size() > targetSize && quality > 10)
            streamBuff.writeTo(outputStream)
            streamBuff.close()
            originBitmap.recycle()
            resizedBitmap.recycle()
            outputStream.close()
            return true
        } catch (_: Exception) {
            return false
        }
    }
    fun rotateScaledBitmap(bitMap: Bitmap, options: BitmapFactory.Options, resize: Float, orientation: Int): Bitmap {
        val resizedBitmap = bitMap.scale((options.outWidth / resize).roundToInt(), (options.outHeight / resize).roundToInt(), false)
        if (orientation == 0) {
            return resizedBitmap
        }
        val rotatedBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.width, resizedBitmap.height, Matrix().apply {
            postRotate(orientation.toFloat())
        }, false)

        return rotatedBitmap
    }
    fun getOrientation(inputStream: ByteArrayInputStream): Int {
        val ret = when (ExifInterface(inputStream).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
        inputStream.reset()
        return ret
    }
    private fun calcResize(options: BitmapFactory.Options, limitSize: Int = 2500): Float {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1F
        if (height > limitSize && width > limitSize) {
            val heightRatio = (height.toFloat() / limitSize)
            val widthRatio = (width.toFloat() / limitSize)
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        return inSampleSize
    }

    fun scaledImageToBitmap(resources: Resources, drawable: Int, dstWidth: Int, dstHeight: Int, filter: Boolean = false) = BitmapFactory.decodeResource(resources, drawable).scale(dstWidth, dstHeight, filter)
    fun scaledVectorToBitmap(context: Context, drawable: Int, dstWidth: Int, dstHeight: Int): Bitmap {
        val vectorDrawable = ContextCompat.getDrawable(context, drawable)
        val bitmap = createBitmap(dstWidth, dstHeight)
        val canvas = Canvas(bitmap)
        vectorDrawable?.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable?.draw(canvas)
        return bitmap
    }
    fun urlImageLoad(imageUrl: String): Bitmap {
        val url = URL(imageUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val inputStream = connection.inputStream
        val bitmap = BitmapFactory.decodeStream(inputStream)
        connection.disconnect()
        return bitmap
    }
    fun pathImageLoad(imagePath: String): Bitmap {
        val file = File(imagePath)
        return BitmapFactory.decodeFile(file.absolutePath)
    }
    fun imageLoad(context: Context, drawable: Int): Bitmap {
        val vectorDrawable = ContextCompat.getDrawable(context, drawable)
        val bitmap = createBitmap(vectorDrawable!!.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }
    fun imageLoad(context: Context, imageUri: Uri): Bitmap {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        return BitmapFactory.decodeStream(inputStream)
    }

    fun getMultipartBodyBuilder() = MultipartBody.Builder().setType(MultipartBody.FORM)
    fun getMultipartBodyPart(key: String, file: File) = MultipartBody.Part.createFormData(key, file.name, file.asRequestBody(getFileMediaType(file)))
}