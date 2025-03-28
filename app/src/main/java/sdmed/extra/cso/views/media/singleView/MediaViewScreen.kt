package sdmed.extra.cso.views.media.singleView

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import sdmed.extra.cso.R
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.common.MediaViewModel
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FCoil
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.vector.FVectorData
import sdmed.extra.cso.views.component.vector.vectorArrowLeft
import sdmed.extra.cso.views.component.vector.vectorCircle
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun mediaViewScreen(dataContext: MediaViewActivityVM) {
    val color = FThemeUtil.safeColorC()
    val item by dataContext.item.collectAsState()
    Column(Modifier.windowInsetsPadding(WindowInsets.statusBars.only(WindowInsetsSides.Top))
        .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom))
        .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Horizontal))
        .fillMaxSize().background(color.background)) {
        mediaViewTopContainer(dataContext)
        if (item.isImage) {
            mediaViewImageView(dataContext)
        }  else if(item.isExcel) {
            mediaViewScreenExcel(item)
        } else {
            mediaViewWebView(dataContext)
        }
    }
}

@Composable
private fun mediaViewTopContainer(dataContext: MediaViewActivityVM) {
    val color = FThemeUtil.safeColorC()
    Row(Modifier.fillMaxWidth().padding(5.dp)) {
        Icon(vectorArrowLeft(FVectorData().apply {
            this.tintColor = color.background
            this.fillColor = color.primary
        }), stringResource(R.string.back_btn_close_desc),
            Modifier.clickable { dataContext.relayCommand.execute(MediaViewActivityVM.ClickEvent.CLOSE) })
        customText(CustomTextData().apply {
            text = stringResource(R.string.media_view_title_desc)
            textColor = color.paragraph
            textAlign = TextAlign.Center
            modifier = Modifier.weight(1F)
        })
    }
}

@Composable
private fun mediaViewImageView(dataContext: MediaViewActivityVM) {
    val color = FThemeUtil.safeColorC()
    val item by dataContext.item.collectAsState()
    var scale by remember { mutableStateOf(1F) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    Box(Modifier.fillMaxSize().background(color.gray),
        contentAlignment = Alignment.Center) {
        FCoil.load(item.blobUrl.value,
            item.mimeType.value,
            item.originalFilename.value,
            Modifier.fillMaxWidth().graphicsLayer(scale, scale, 1F, offset.x, offset.y),
            ContentScale.FillWidth)
        Box(Modifier.zIndex(100F).align(Alignment.TopEnd).width(200.dp).height(200.dp).pointerInput(Unit) {
            detectTransformGestures { _, pan, zoom, _ ->
                scale = (scale * zoom).coerceIn(1F, 5F)
                if (scale != 1F) {
                    offset += pan
                } else {
                    offset = Offset.Zero
                }
            }
        }) {
            customText(CustomTextData().apply {
                text = "zoom"
                textColor = color.absoluteWhite
                textAlign = TextAlign.Center
                modifier = Modifier.align(Alignment.Center)
            })
            Icon(vectorCircle(FVectorData(color.transparent, color.scrim)),
                stringResource(R.string.media_view_title_desc),
                Modifier.align(Alignment.TopEnd).fillMaxSize())
        }
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
private fun mediaViewScreenExcel(item: MediaViewModel) {

}

@Composable
private fun mediaViewWebView(dataContext: MediaViewActivityVM) {
    val item by dataContext.item.collectAsState()
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
            }
        }, Modifier.fillMaxSize(), update = { webview = it})
    }
    BackHandler(enabled = canGoBack) {
        if (webview?.canGoBack() == true) {
            webview?.goBack()
        }
    }
}

//@Preview
@Composable
private fun previewMediaViewScreen() {
    mediaViewScreen(MediaViewActivityVM().apply { fakeInit() })
}