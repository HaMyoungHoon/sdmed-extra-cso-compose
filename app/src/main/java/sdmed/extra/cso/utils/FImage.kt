package sdmed.extra.cso.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.CancellationSignal
import android.util.Size
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.core.net.toFile
import coil.compose.AsyncImage
import sdmed.extra.cso.R
import sdmed.extra.cso.models.common.MediaFileType
import java.io.File
import kotlin.div
import kotlin.math.min

@Composable
fun fImageLoad(blobUrl: String? = null, blobMimeType: String? = null, blobFilename: String? = null, contentScale: ContentScale = ContentScale.Crop) {
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
fun fImageLoad(blobUrl: String? = null, blobMimeType: String? = null, blobFilename: String? = null, modifier: Modifier, contentScale: ContentScale = ContentScale.Crop) {
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
fun fImageLoad(mediaUrl: String? = null, mediaFileType: MediaFileType?, mediaFilename: String? = null, modifier: Modifier, contentScale: ContentScale = ContentScale.Crop, size: Int = 256) {
    val context = FDI.context()
    var imageId = R.drawable.image_no_image_1920
    when (mediaFileType) {
        MediaFileType.IMAGE -> {
            var bitmap by remember { mutableStateOf<Bitmap?>(null) }
            LaunchedEffect(mediaUrl) {
                mediaUrl?.let {
                    FCoroutineUtil.coroutineScopeIO({
                        bitmap = try {
                            val size = Size(size, size)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                ThumbnailUtils.createImageThumbnail(File(mediaUrl), size, null)
                            } else {
                                val resampler = DecodeResampler(size, null)
                                val source = ImageDecoder.createSource(File(mediaUrl))
                                ImageDecoder.decodeBitmap(source, resampler)
                            }
                        } catch (e: Exception) {
                            null
                        }
                    })
                }
            }
            bitmap?.let {
                Image(it.asImageBitmap(),
                    mediaFilename,
                    modifier,
                    contentScale = contentScale,
                    filterQuality = FilterQuality.None)
            } ?: Image(painterResource(R.drawable.image_loading),
                mediaFilename,
                modifier,
                contentScale = contentScale)
            return
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

class DecodeResampler(val size: Size, val signal: CancellationSignal?) : ImageDecoder.OnHeaderDecodedListener {
    override fun onHeaderDecoded(decoder: ImageDecoder, info: ImageDecoder.ImageInfo, source: ImageDecoder.Source) {
        val widthSample = info.size.width / size.width
        val heightSample = info.size.height / size.height
        val sample = min(widthSample, heightSample)
        if (sample > 1) {
            decoder.setTargetSampleSize(sample)
        }
    }
}