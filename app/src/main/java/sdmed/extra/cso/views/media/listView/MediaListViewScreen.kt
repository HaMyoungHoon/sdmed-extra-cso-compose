package sdmed.extra.cso.views.media.listView

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.common.MediaViewModel
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FCoil
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.vector.FVectorData
import sdmed.extra.cso.views.component.vector.vectorArrowLeft
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun mediaListViewScreen(dataContext: MediaListViewActivityVM) {
    val color = FThemeUtil.safeColorC()
    Column(Modifier.windowInsetsPadding(WindowInsets.statusBars.only(WindowInsetsSides.Top))
        .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom))
        .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Horizontal))
        .fillMaxSize().background(color.background)) {
        mediaListViewScreenTopContainer(dataContext)
        mediaListViewScreenItemContainer(dataContext)
    }
}

@Composable
private fun mediaListViewScreenTopContainer(dataContext: MediaListViewActivityVM) {
    val color = FThemeUtil.safeColorC()
    Row(Modifier.fillMaxWidth().padding(5.dp)) {
        Icon(vectorArrowLeft(FVectorData().apply {
            this.tintColor = color.background
            this.fillColor = color.primary
        }), stringResource(R.string.back_btn_close_desc),
            Modifier.clickable { dataContext.relayCommand.execute(MediaListViewActivityVM.ClickEvent.CLOSE) })
        customText(CustomTextData().apply {
            text = stringResource(R.string.media_view_title_desc)
            textColor = color.paragraph
            textAlign = TextAlign.Center
            modifier = Modifier.weight(1F)
        })
    }
}

@Composable
private fun mediaListViewScreenItemContainer(dataContext: MediaListViewActivityVM) {
    val items by dataContext.items.collectAsState()
    val pagerState = rememberPagerState(pageCount = { items.size })
    HorizontalPager(pagerState, Modifier.fillMaxSize()) { page ->
        val item = items.getOrNull(page) ?: items.getOrNull(0) ?: return@HorizontalPager
        if (item.isImage) {
            mediaListViewScreenImageView(item)
        } else {
            mediaListViewScreenWebView(item)
        }
    }
}

@Composable
private fun mediaListViewScreenImageView(item: MediaViewModel) {
    val color = FThemeUtil.safeColorC()
    var scale by remember { mutableStateOf(1F) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    Box(Modifier.fillMaxSize().background(color.gray).pointerInput(Unit) {
        detectTransformGestures { _, pan, zoom, _ ->
            scale = (scale * zoom).coerceIn(1F, 5F)
            offset += pan
        }
    }, contentAlignment = Alignment.Center) {
        FCoil.load(item.blobUrl.value,
            item.mimeType.value,
            item.originalFilename.value,
            Modifier.fillMaxWidth().graphicsLayer(scale, scale, 1F, offset.x, offset.y),
            ContentScale.FillWidth)
        customText(CustomTextData().apply {
            text = item.originalFilename.value
            textColor = color.disableForeGray
            textSize = FThemeUtil.textUnit(18F)
            maxLines = 2
            modifier = Modifier.align(Alignment.BottomStart).padding(10.dp)
            overflow = TextOverflow.Ellipsis
        })
    }
}

@Composable
private fun mediaListViewScreenWebView(item: MediaViewModel) {
    val loadUrl = "${FConstants.WEB_VIEW_PREFIX}${FAmhohwa.urlEncoder(item.blobUrl.value)}"
    AndroidView({ context ->
        WebView(context).apply {
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true
            loadUrl(loadUrl)
        }
    }, Modifier.fillMaxSize())
}

