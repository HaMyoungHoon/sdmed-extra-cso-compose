package sdmed.extra.cso.utils

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest

object FCoil {
    fun imageLoader(context: Context) = ImageLoader.Builder(context)
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
        .build()
    @Composable
    fun load(blobUrl: String? = null, blobMimeType: String? = null, blobFilename: String? = null) {
        val context = LocalContext.current
        val mimeType = FContentsType.getExtMimeType(blobMimeType)
        if (blobUrl.isNullOrEmpty() || !FImageUtils.isImage(mimeType)) {
            Image(painterResource(FImageUtils.getDefaultImage(blobMimeType)),
                blobFilename)
        } else {
            AsyncImage(ImageRequest.Builder(context)
                .data(blobUrl)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
                blobFilename,
                imageLoader(context))
        }
    }
}

@Preview
@Composable
fun previewNullImage() {
    FCoil.load()
}
@Preview
@Composable
fun previewExcelImage() {
    FCoil.load("", FContentsType.type_xlsx)
}
@Preview
@Composable
fun previewPdf() {
    FCoil.load("", FContentsType.type_pdf)
}