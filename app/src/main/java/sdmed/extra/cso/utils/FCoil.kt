package sdmed.extra.cso.utils

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.ImageDecoderDecoder
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
        .components {
            add(ImageDecoderDecoder.Factory())
        }
        .build()
    @Composable
    fun load(blobUrl: String? = null, blobMimeType: String? = null, blobFilename: String? = null, contentScale: ContentScale = ContentScale.Crop) {
        val context = LocalContext.current
        val mimeType = FContentsType.getExtMimeType(blobMimeType)
        if (blobUrl.isNullOrEmpty() || !FImageUtils.isImage(mimeType)) {
            Image(painterResource(FImageUtils.getDefaultImage(blobMimeType)),
                blobFilename,
                Modifier.fillMaxSize(),
                contentScale = contentScale)
        } else {
            AsyncImage(ImageRequest.Builder(context)
                .data(blobUrl)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
                blobFilename,
                imageLoader(context),
                Modifier.fillMaxSize(),
                contentScale = contentScale)
        }
    }
    @Composable
    fun load(blobUrl: String? = null, blobMimeType: String? = null, blobFilename: String? = null, modifier: Modifier, contentScale: ContentScale = ContentScale.Crop) {
        val context = LocalContext.current
        val mimeType = FContentsType.getExtMimeType(blobMimeType)
        if (blobUrl.isNullOrEmpty() || !FImageUtils.isImage(mimeType)) {
            Image(painterResource(FImageUtils.getDefaultImage(blobMimeType)),
                blobFilename,
                modifier,
                contentScale = contentScale)
        } else {
            AsyncImage(ImageRequest.Builder(context)
                .data(blobUrl)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
                blobFilename,
                imageLoader(context),
                modifier,
                contentScale = contentScale)
        }
    }
}

//@Preview
@Composable
private fun previewNullImage() {
    FCoil.load()
}

//@Preview
@Composable
private fun previewExcelImage() {
    FCoil.load("", FContentsType.type_xlsx)
}

//@Preview
@Composable
private fun previewPdf() {
    FCoil.load("", FContentsType.type_pdf)
}