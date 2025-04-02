package sdmed.extra.cso.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import sdmed.extra.cso.R
import sdmed.extra.cso.models.common.MediaFileType

object FImage {
    @Composable
    fun load(blobUrl: String? = null, blobMimeType: String? = null, blobFilename: String? = null, contentScale: ContentScale = ContentScale.Crop) {
        val mimeType = FContentsType.getExtMimeType(blobMimeType)
        if (blobUrl.isNullOrEmpty() || !FImageUtils.isImage(mimeType)) {
            Image(painterResource(FImageUtils.getDefaultImage(blobMimeType)),
                blobFilename,
                Modifier.fillMaxSize(),
                contentScale = contentScale)
        } else {
            AsyncImage(blobUrl,
                blobFilename,
                Modifier.fillMaxSize(),
                contentScale = contentScale)
        }
    }
    @Composable
    fun load(blobUrl: String? = null, blobMimeType: String? = null, blobFilename: String? = null, modifier: Modifier, contentScale: ContentScale = ContentScale.Crop) {
        val mimeType = FContentsType.getExtMimeType(blobMimeType)
        if (blobUrl.isNullOrEmpty() || !FImageUtils.isImage(mimeType)) {
            Image(painterResource(FImageUtils.getDefaultImage(blobMimeType)),
                blobFilename,
                modifier,
                contentScale = contentScale)
        } else {
            AsyncImage(blobUrl,
                blobFilename,
                modifier,
                contentScale = contentScale)
        }
    }
    @Composable
    fun load(mediaUri: Uri? = null, mediaFileType: MediaFileType?, mediaFilename: String? = null, modifier: Modifier, contentScale: ContentScale = ContentScale.Crop) {
        val context = FDI.context()
        var imageId = R.drawable.image_no_image_1920
        when (mediaFileType) {
            MediaFileType.IMAGE -> {
                mediaUri?.let {
                    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
                    FCoroutineUtil.coroutineScopeIO({
                        val inputStream = context.contentResolver.openInputStream(mediaUri)
                        bitmap = BitmapFactory.decodeStream(inputStream)
                        inputStream?.close()
                    })
                    bitmap?.let {
                        Image(it.asImageBitmap(),
                            mediaFilename,
                            modifier,
                            contentScale = contentScale,
                            filterQuality = FilterQuality.None)
                    }
                    return
                }
            }
            MediaFileType.VIDEO -> { }
            MediaFileType.UNKNOWN -> {  }
            MediaFileType.PDF -> { imageId = R.drawable.image_pdf }
            MediaFileType.EXCEL -> { imageId = R.drawable.image_excel }
            MediaFileType.ZIP -> { imageId = R.drawable.image_zip }
            else -> { }
        }
        Image(painterResource(imageId),
            mediaFilename,
            modifier,
            contentScale = contentScale)
    }
}