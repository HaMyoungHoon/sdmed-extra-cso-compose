package sdmed.extra.cso.views.main.qna

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.models.common.MediaViewParcelModel
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.models.retrofit.qna.QnAFileModel
import sdmed.extra.cso.models.retrofit.qna.QnAHeaderModel
import sdmed.extra.cso.models.retrofit.qna.QnAReplyFileModel
import sdmed.extra.cso.models.retrofit.qna.QnAReplyModel
import sdmed.extra.cso.utils.FCoil
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FStorage.putParcelable
import sdmed.extra.cso.utils.FStorage.putParcelableList
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.component.vector.FVectorData
import sdmed.extra.cso.views.component.vector.vectorArrowDown
import sdmed.extra.cso.views.component.vector.vectorArrowLeft
import sdmed.extra.cso.views.component.vector.vectorArrowUp
import sdmed.extra.cso.views.main.edi.EDIScreenDetailVM
import sdmed.extra.cso.views.media.listView.MediaListViewActivity
import sdmed.extra.cso.views.media.singleView.MediaViewActivity
import sdmed.extra.cso.views.theme.FThemeUtil
import java.util.ArrayList

@Composable
fun qnAScreenDetail(selectItem: QnAHeaderModel,
                    windowPanelType: WindowPanelType,
                    displayFeatures: List<DisplayFeature>,
                    navigationType: NavigationType,
                    onDismissRequest: () -> Unit,
                    onAddRequest: (String?, String?) -> Unit) {
    val dataContext = fBaseScreen<QnAScreenDetailVM>( { data, dataContext -> setLayoutCommand(data, dataContext, onDismissRequest) },
        null,
        windowPanelType, navigationType,
        { dataContext -> qnAScreenDetailTwoPane(dataContext, displayFeatures) },
        { dataContext -> qnAScreenDetailPhone(dataContext) },
        { dataContext -> qnAScreenDetailTablet(dataContext) })
    val thisPK by dataContext.thisPK.collectAsState()
    val addQnATitle by dataContext.addQnATitle.collectAsState()
    dataContext.thisPK.value = selectItem.thisPK
    BackHandler() {
        dismiss(dataContext, onDismissRequest)
    }
    LaunchedEffect(thisPK) {
        if (thisPK.isNotEmpty()) {
            getData(dataContext)
        }
    }
    LaunchedEffect(addQnATitle) {
        if (!addQnATitle.isNullOrBlank()) {
            onAddRequest(thisPK, addQnATitle)
            dataContext.addQnATitle.value = null
        }
    }
}

@Composable
private fun qnAScreenDetailTwoPane(dataContext: QnAScreenDetailVM, displayFeatures: List<DisplayFeature>) {
    val color = FThemeUtil.safeColorC()
    Box(Modifier.background(color.background).fillMaxWidth()) {
        Column(Modifier.align(Alignment.Center)) {
            topContainer(dataContext)
            contentContainer(dataContext)
            replyListContainer(dataContext)
            bottomContainer(dataContext)
        }
    }
}
@Composable
private fun qnAScreenDetailPhone(dataContext: QnAScreenDetailVM) {
    val color = FThemeUtil.safeColorC()
    shapeRoundedBox(ShapeRoundedBoxData().apply {
        backgroundColor = color.background
        borderColor = color.primary
        borderSize = 1.dp
        modifier = Modifier.padding(5.dp).fillMaxWidth()
    }) {
        Column(Modifier.align(Alignment.Center)) {
            topContainer(dataContext, false)
            contentContainer(dataContext, false)
            replyListContainer(dataContext, false)
            bottomContainer(dataContext, false)
        }
    }
}
@Composable
private fun qnAScreenDetailTablet(dataContext: QnAScreenDetailVM) {
    val color = FThemeUtil.safeColorC()
    shapeRoundedBox(ShapeRoundedBoxData().apply {
        backgroundColor = color.background
        borderColor = color.primary
        borderSize = 1.dp
        modifier = Modifier.padding(5.dp).fillMaxWidth()
    }) {
        Column(Modifier.align(Alignment.Center)) {
            topContainer(dataContext)
            contentContainer(dataContext)
            replyListContainer(dataContext)
            bottomContainer(dataContext)
        }
    }
}

@Composable
private fun topContainer(dataContext: QnAScreenDetailVM, isWide: Boolean = true) {
    val color = FThemeUtil.safeColorC()
    val headerModel by dataContext.headerModel.collectAsState()
    Box(Modifier) {
        val rowModifier = if (isWide) Modifier.align(Alignment.Center) else Modifier.fillMaxWidth()
        Row(rowModifier) {
            Icon(vectorArrowLeft(FVectorData(color.background, color.foreground)),
                stringResource(R.string.close_desc),
                Modifier.align(Alignment.CenterVertically).clickable { dataContext.relayCommand.execute(QnAScreenDetailVM.ClickEvent.CLOSE)},
                Color.Unspecified)
            customText(CustomTextData().apply {
                text = headerModel.title
                textColor = color.foreground
                textAlign = TextAlign.Center
                modifier = Modifier.weight(1F).align(Alignment.CenterVertically)
            })
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = headerModel.qnaState.parseQnABackColor()
                modifier = Modifier.padding(5.dp)
            }) {
                customText(CustomTextData().apply {
                    text = headerModel.qnaState.desc
                    textColor = headerModel.qnaState.parseQnAColor()
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                })
            }
        }
    }
}

@Composable
private fun contentContainer(dataContext: QnAScreenDetailVM, isWide: Boolean = true) {
    val color = FThemeUtil.safeColorC()
    val headerModel by dataContext.headerModel.collectAsState()
    val contentModel by dataContext.contentModel.collectAsState()
    val collapseContent by dataContext.collapseContent.collectAsState()
    shapeRoundedBox(ShapeRoundedBoxData().apply {
        backgroundColor = color.cardBackground
        borderColor = color.foreground
        borderSize = 1.dp
        modifier = if (isWide) {
            Modifier.padding(10.dp)
        } else {
            Modifier.fillMaxWidth().padding(10.dp)
        }
    }) {
        val modifier = if (isWide) Modifier.align(Alignment.Center) else Modifier.fillMaxWidth()
        Column(modifier.padding(5.dp)) {
            Row(Modifier.fillMaxWidth().clickable { dataContext.relayCommand.execute(QnAScreenDetailVM.ClickEvent.COLLAPSE)}) {
                if (collapseContent) {
                    Icon(vectorArrowDown(FVectorData(color.background, color.foreground)),
                    stringResource(R.string.open_desc),
                    Modifier.padding(end = 5.dp),
                    Color.Unspecified)
                } else {
                    Icon(vectorArrowUp(FVectorData(color.background, color.foreground)),
                    stringResource(R.string.close_desc),
                    Modifier.padding(end = 5.dp),
                    Color.Unspecified)
                }
                customText(CustomTextData().apply {
                    text = headerModel.regDateString
                    textColor = color.foreground
                })
            }
            if (collapseContent) {
                customText(CustomTextData().apply {
                    text = contentModel.htmlContentString.toString()
                    textColor = color.foreground
                    maxLines = 10
                })
                contentFileListContainer(dataContext, isWide)
            }
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun contentFileListContainer(dataContext: QnAScreenDetailVM, isWide: Boolean = true) {
    val contentModel by dataContext.contentModel.collectAsState()
    if (contentModel.fileList.isNotEmpty()) {
        val color = FThemeUtil.safeColorC()
        val pagerState = rememberPagerState(pageCount = { contentModel.fileList.size })
        var scrollEnabled by remember { mutableStateOf(true) }
        HorizontalPager(pagerState, Modifier.fillMaxWidth(), userScrollEnabled = scrollEnabled) { page ->
            val item = contentModel.fileList.getOrNull(page) ?: contentModel.fileList.getOrNull(0) ?: return@HorizontalPager
            item.relayCommand = dataContext.relayCommand
            Column(Modifier) {
                val imageModifier = if (isWide) Modifier.fillMaxWidth() else Modifier.height(150.dp).fillMaxWidth()
                FCoil.load(item.blobUrl,
                    item.mimeType,
                    item.originalFilename,
                    imageModifier.combinedClickable(onClick = { item.onClick(QnAFileModel.ClickEvent.SHORT) },
                        onLongClick = { item.onClick(QnAFileModel.ClickEvent.LONG) }),
                    ContentScale.FillWidth)
                customText(CustomTextData().apply {
                    text = "${page + 1}/${contentModel.fileList.size}"
                    textColor = color.foreground
                    textAlign = TextAlign.Center
                    modifier = Modifier.fillMaxWidth()
                })
            }
        }
    }
}
@Composable
private fun replyListContainer(dataContext: QnAScreenDetailVM, isWide: Boolean = true) {
    val contentModel by dataContext.contentModel.collectAsState()
    Box(Modifier) {
        val modifier = if (isWide) Modifier.align(Alignment.Center) else Modifier.fillMaxWidth()
        LazyColumn(modifier) {
            items(contentModel.replyList, { it.thisPK }) { item ->
                replyContainer(dataContext, item, isWide)
            }
        }
    }
}
@Composable
private fun replyContainer(dataContext: QnAScreenDetailVM, item: QnAReplyModel, isWide: Boolean = true) {
    val color = FThemeUtil.safeColorC()
    val open by item.open.collectAsState()
    item.relayCommand = dataContext.relayCommand
    shapeRoundedBox(ShapeRoundedBoxData().apply {
        backgroundColor = color.cardBackground
        borderColor = color.foreground
        borderSize = 1.dp
        modifier = Modifier.padding(10.dp)
    }) {
        Column(Modifier.padding(5.dp).align(Alignment.Center)) {
            Row(Modifier.clickable { item.onClick(QnAReplyModel.ClickEvent.OPEN) }) {
                customText(CustomTextData().apply {
                    text = item.regDateString
                    textAlign = TextAlign.Center
                    textColor = color.foreground
                })
                customText(CustomTextData().apply {
                    text = item.name
                    textColor = color.foreground
                    textAlign = TextAlign.Center
                    modifier = Modifier.weight(1F)
                })
                if (open) {
                    Icon(vectorArrowDown(FVectorData(color.background, color.foreground)),
                        stringResource(R.string.open_desc),
                        Modifier.padding(end = 5.dp),
                        Color.Unspecified)
                } else {
                    Icon(vectorArrowUp(FVectorData(color.background, color.foreground)),
                        stringResource(R.string.close_desc),
                        Modifier.padding(end = 5.dp),
                        Color.Unspecified)
                }
            }
            if (open) {
                customText(CustomTextData().apply {
                    text = item.htmlContentString.toString()
                    textColor = color.foreground
                    maxLines = 10
                })
                replyFileListContainer(dataContext, item, isWide)
            }
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun replyFileListContainer(dataContext: QnAScreenDetailVM, reply: QnAReplyModel, isWide: Boolean = true) {
    if (reply.fileList.isNotEmpty()) {
        val color = FThemeUtil.safeColorC()
        val pagerState = rememberPagerState(pageCount = { reply.fileList.size })
        var scrollEnabled by remember { mutableStateOf(true) }
        HorizontalPager(pagerState, Modifier.fillMaxSize(), userScrollEnabled = scrollEnabled) { page ->
            val item = reply.fileList.getOrNull(page) ?: reply.fileList.getOrNull(0) ?: return@HorizontalPager
            item.relayCommand = dataContext.relayCommand
            Column(Modifier) {
                val imageModifier = if (isWide) Modifier.fillMaxWidth() else Modifier.height(150.dp).fillMaxWidth()
                FCoil.load(item.blobUrl,
                    item.mimeType,
                    item.originalFilename,
                    imageModifier.combinedClickable(onClick = { item.onClick(QnAReplyFileModel.ClickEvent.SHORT) },
                        onLongClick = { item.onClick(QnAReplyFileModel.ClickEvent.LONG) }),
                    ContentScale.FillWidth)
                customText(CustomTextData().apply {
                    text = "${page + 1}/${reply.fileList.size}"
                    textColor = color.foreground
                    textAlign = TextAlign.Center
                    modifier = Modifier.fillMaxWidth()
                })
            }
        }
    }
}
@Composable
private fun bottomContainer(dataContext: QnAScreenDetailVM, isWide: Boolean = true) {
    val headerModel by dataContext.headerModel.collectAsState()
    if (headerModel.qnaState.isEditable()) {
        val color = FThemeUtil.safeColorC()
        Box(Modifier) {
            Row(Modifier.fillMaxWidth().align(Alignment.Center),
                Arrangement.SpaceEvenly) {
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = color.buttonBackground
                    modifier = Modifier.align(Alignment.CenterVertically).padding(5.dp).clickable { dataContext.relayCommand.execute(QnAScreenDetailVM.ClickEvent.ADD) }
                }) {
                    customText(CustomTextData().apply {
                        text = stringResource(R.string.qna_add_desc)
                        textColor = color.buttonForeground
                        modifier = Modifier.align(Alignment.Center).padding(5.dp)
                    })
                }
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = color.buttonBackground
                    modifier = Modifier.align(Alignment.CenterVertically).padding(5.dp).clickable { dataContext.relayCommand.execute(QnAScreenDetailVM.ClickEvent.COMP) }
                }) {
                    customText(CustomTextData().apply {
                        text = stringResource(R.string.qna_comp_desc)
                        textColor = color.buttonForeground
                        modifier = Modifier.align(Alignment.Center).padding(5.dp)
                    })
                }
            }
        }
    }
}

private fun setLayoutCommand(data: Any?, dataContext: QnAScreenDetailVM, onDismissRequest: () -> Unit) {
    setThisCommand(data, dataContext, onDismissRequest)
    setQnAFileCommand(data, dataContext)
    setQnAReplyCommand(data, dataContext)
    setQnAReplyFileCommand(data, dataContext)
}
private fun setThisCommand(data: Any?, dataContext: QnAScreenDetailVM, onDismissRequest: () -> Unit) {
    val eventName = data as? QnAScreenDetailVM.ClickEvent ?: return
    when (eventName) {
        QnAScreenDetailVM.ClickEvent.CLOSE -> dismiss(dataContext, onDismissRequest)
        QnAScreenDetailVM.ClickEvent.COLLAPSE -> dataContext.collapseContent.value = !dataContext.collapseContent.value
        QnAScreenDetailVM.ClickEvent.ADD -> addQnAReply(dataContext)
        QnAScreenDetailVM.ClickEvent.COMP -> compQnA(dataContext)
    }
}
private fun setQnAFileCommand(data: Any?, dataContext: QnAScreenDetailVM) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? QnAFileModel.ClickEvent ?: return
    val dataBuff = data[1] as? QnAFileModel ?: return
    when (eventName) {
        QnAFileModel.ClickEvent.LONG -> qnAFileLong(dataContext, dataBuff)
        QnAFileModel.ClickEvent.SHORT -> qnAFileShort(dataContext, dataBuff)
    }
}
private fun setQnAReplyCommand(data: Any?, dataContext: QnAScreenDetailVM) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? QnAReplyModel.ClickEvent ?: return
    val dataBuff = data[1] as? QnAReplyModel ?: return
    when (eventName) {
        QnAReplyModel.ClickEvent.OPEN -> dataBuff.open.value = !dataBuff.open.value
    }
}
private fun setQnAReplyFileCommand(data: Any?, dataContext: QnAScreenDetailVM) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? QnAReplyFileModel.ClickEvent ?: return
    val dataBuff = data[1] as? QnAReplyFileModel ?: return
    when (eventName) {
        QnAReplyFileModel.ClickEvent.LONG -> qnaReplyFileLong(dataContext, dataBuff)
        QnAReplyFileModel.ClickEvent.SHORT -> qnaReplyFileShort(dataContext, dataBuff)
    }
}

private fun getData(dataContext: QnAScreenDetailVM) {
    dataContext.loading()
    FCoroutineUtil.coroutineScope({
        val ret = dataContext.getData()
        dataContext.loading(false)
        if (ret.result != true) {
            dataContext.toast(ret.msg)
        }
    })
}

private fun dismiss(dataContext: QnAScreenDetailVM, onDismissRequest: () -> Unit) {
    onDismissRequest()
    dataContext.reSet()
}
private fun addQnAReply(dataContext: QnAScreenDetailVM) {
    dataContext.addQnATitle.value = dataContext.headerModel.value.title
}
private fun compQnA(dataContext: QnAScreenDetailVM) {
    dataContext.loading()
    FCoroutineUtil.coroutineScope({
        val ret = dataContext.postData()
        dataContext.loading(false)
        if (ret.result == true) {
            getData(dataContext)
            return@coroutineScope
        }
        dataContext.toast(ret.msg)
    })
}
private fun qnAFileLong(dataContext: QnAScreenDetailVM, item: QnAFileModel) {
    val context = dataContext.context
    context.startActivity(Intent(context, MediaListViewActivity::class.java).apply {
        putParcelableList(FConstants.MEDIA_LIST, dataContext.getMediaViewQnAFiles())
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}
private fun qnAFileShort(dataContext: QnAScreenDetailVM, item: QnAFileModel) {
    val context = dataContext.context
    context.startActivity(Intent(context, MediaViewActivity::class.java).apply {
        putParcelable(FConstants.MEDIA_ITEM, MediaViewParcelModel().parse(item))
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}
private fun qnaReplyFileLong(dataContext: QnAScreenDetailVM, item: QnAReplyFileModel) {
    val context = dataContext.context
    context.startActivity(Intent(context, MediaListViewActivity::class.java).apply {
        putParcelableList(FConstants.MEDIA_LIST, dataContext.getMediaViewQnAFiles())
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}
private fun qnaReplyFileShort(dataContext: QnAScreenDetailVM, item: QnAReplyFileModel) {
    val context = dataContext.context
    context.startActivity(Intent(context, MediaViewActivity::class.java).apply {
        putParcelable(FConstants.MEDIA_ITEM, MediaViewParcelModel().parse(item))
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}