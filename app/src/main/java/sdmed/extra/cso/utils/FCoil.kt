package sdmed.extra.cso.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import sdmed.extra.cso.R
import sdmed.extra.cso.models.common.MediaFileType

@OptIn(ExperimentalCoilApi::class)
fun fCoilClearCache(context: Context? = null) {
    val imageLoader = ImageLoader.Builder(context ?: FDI.context()).build()
    imageLoader.diskCache?.clear()
    imageLoader.memoryCache?.clear()
}
fun fCoilImageRequest(context: Context, data: Any?) = ImageRequest.Builder(context)
    .data(data)
    .size(300)
    .diskCachePolicy(CachePolicy.WRITE_ONLY)
    .memoryCachePolicy(CachePolicy.READ_ONLY)
    .build()
fun fCoilImageLoader(context: Context) = ImageLoader.Builder(context)
    .crossfade(true)
    .bitmapConfig(Bitmap.Config.RGB_565)
    .allowRgb565(true)
    .diskCache {
        DiskCache.Builder()
            .directory(context.cacheDir)
            .maxSizePercent(0.05)
            .build()
    }
    .memoryCache {
        MemoryCache.Builder(context)
            .maxSizePercent(0.25)
            .build()
    }
    .memoryCachePolicy(CachePolicy.ENABLED)
    .diskCachePolicy(CachePolicy.ENABLED)
    .respectCacheHeaders(false)
    .components {
        add(ImageDecoderDecoder.Factory())
    }
    .build()
@Composable
fun fCoilLoad(blobUrl: String? = null, blobMimeType: String? = null, blobFilename: String? = null, contentScale: ContentScale = ContentScale.Crop) {
    val context = FDI.context()
    val mimeType = FContentsType.getExtMimeType(blobMimeType)
    if (blobUrl.isNullOrEmpty() || !FImageUtils.isImage(mimeType)) {
        Image(painterResource(FImageUtils.getDefaultImage(blobMimeType)),
            blobFilename,
            Modifier.fillMaxSize(),
            contentScale = contentScale)
    } else {
        AsyncImage(fCoilImageRequest(context, blobUrl),
            blobFilename,
            fCoilImageLoader(context),
            Modifier.fillMaxSize(),
            contentScale = contentScale)
    }
}
@Composable
fun fCoilLoad(blobUrl: String? = null, blobMimeType: String? = null, blobFilename: String? = null, modifier: Modifier, contentScale: ContentScale = ContentScale.Crop) {
    val context = FDI.context()
    val mimeType = FContentsType.getExtMimeType(blobMimeType)
    if (blobUrl.isNullOrEmpty() || !FImageUtils.isImage(mimeType)) {
        Image(painterResource(FImageUtils.getDefaultImage(blobMimeType)),
            blobFilename,
            modifier,
            contentScale = contentScale)
    } else {
        AsyncImage(fCoilImageRequest(context, blobUrl),
            blobFilename,
            fCoilImageLoader(context),
            modifier,
            contentScale = contentScale)
    }
}
@Composable
fun fCoilLoad(mediaUri: Uri? = null, mediaFileType: MediaFileType?, mediaFilename: String? = null, modifier: Modifier, contentScale: ContentScale = ContentScale.Crop) {
    val context = FDI.context()
    var imageId = R.drawable.image_no_image_1920
    when (mediaFileType) {
        MediaFileType.IMAGE -> {
            AsyncImage(fCoilImageRequest(context, mediaUri),
                mediaFilename,
                fCoilImageLoader(context),
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