package sdmed.extra.cso.views.media.listView

import android.view.MotionEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.common.MediaViewModel
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.fCoilLoad
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.vector.FVectorData
import sdmed.extra.cso.views.component.vector.vectorArrowLeft
import sdmed.extra.cso.views.component.vector.vectorArrowRight
import sdmed.extra.cso.views.component.vector.vectorCircle
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
    val selectedIndex by dataContext.selectedIndex.collectAsState()
    var title by remember { mutableStateOf("") }
    Row(Modifier.fillMaxWidth().padding(10.dp)) {
        Icon(vectorArrowLeft(FVectorData().apply {
            this.tintColor = color.background
            this.fillColor = color.primary
        }), stringResource(R.string.prev_desc),
            Modifier.clickable { dataContext.relayCommand.execute(MediaListViewActivityVM.ClickEvent.PREV) },
            Color.Unspecified)
        customText(CustomTextData().apply {
            text = title
            textColor = color.paragraph
            textAlign = TextAlign.Center
            modifier = Modifier.weight(1F)
        })
        Icon(vectorArrowRight(FVectorData().apply {
            this.tintColor = color.background
            this.fillColor = color.primary
        }), stringResource(R.string.next_desc),
            Modifier.clickable { dataContext.relayCommand.execute(MediaListViewActivityVM.ClickEvent.NEXT) },
            Color.Unspecified)
    }
    LaunchedEffect(selectedIndex) {
        title = if (selectedIndex > dataContext.items.value.count() - 1) {
            ""
        } else {
            dataContext.items.value[selectedIndex].originalFilename.value
        }
    }
}

@Composable
private fun mediaListViewScreenItemContainer(dataContext: MediaListViewActivityVM) {
    val items by dataContext.items.collectAsState()
    val pagerState = rememberPagerState(pageCount = { items.size })
    val selectedIndex by dataContext.selectedIndex.collectAsState()
    HorizontalPager(pagerState, Modifier.fillMaxSize(), userScrollEnabled = false) { page ->
        val item = items.getOrNull(page) ?: items.getOrNull(0) ?: return@HorizontalPager
        if (item.isImage || item.isExcel) {
            mediaListViewScreenImageView(item)
        } else {
            mediaListViewScreenWebView(item)
        }
    }
    LaunchedEffect(selectedIndex) {
        pagerState.animateScrollToPage(selectedIndex)
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
            if (scale != 1F) {
                offset += pan
            } else {
                offset = Offset.Zero
            }
        }
    }, contentAlignment = Alignment.Center) {
        fCoilLoad(item.blobUrl.value,
            item.mimeType.value,
            item.originalFilename.value,
            Modifier.fillMaxWidth().graphicsLayer(scale, scale, 1F, offset.x, offset.y),
            ContentScale.FillWidth)
    }
}

@Composable
private fun mediaListViewScreenWebView(item: MediaViewModel, webViewTouch: ((event: MotionEvent) -> Boolean)? = null) {
    val loadUrl = "${FConstants.WEB_VIEW_PREFIX}${FAmhohwa.urlEncoder(item.blobUrl.value)}"
    var canGoBack by remember { mutableStateOf(false) }
    var webview: WebView? = null
    Box(Modifier.fillMaxSize()) {
        AndroidView({ context ->
            WebView(context).apply {
                webViewClient = object: WebViewClient() {
                    override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                        canGoBack = view?.canGoBack() == true
                    }
                }
                settings.domStorageEnabled = true
                settings.javaScriptEnabled = true
                loadUrl(loadUrl)
                webViewTouch?.let { x ->
                    setOnTouchListener { v, event -> x.invoke(event) }
                }
            }
        }, Modifier.fillMaxSize().padding(bottom = 20.dp), update = { webview = it})
    }
    BackHandler(enabled = canGoBack) {
        if (webview?.canGoBack() == true) {
            webview.goBack()
        }
    }
}

