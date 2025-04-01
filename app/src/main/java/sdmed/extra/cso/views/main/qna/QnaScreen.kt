package sdmed.extra.cso.views.main.qna

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import sdmed.extra.cso.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.interfaces.theme.IBaseColor
import sdmed.extra.cso.models.common.PageNumberModel
import sdmed.extra.cso.models.common.PaginationModel
import sdmed.extra.cso.models.menu.MenuItem
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.models.retrofit.qna.QnAHeaderModel
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.CustomTextFieldData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.customText.customTextField
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.component.vector.FVectorData
import sdmed.extra.cso.views.component.vector.vectorCircle
import sdmed.extra.cso.views.component.vector.vectorDoubleLeft
import sdmed.extra.cso.views.component.vector.vectorDoubleRight
import sdmed.extra.cso.views.component.vector.vectorPlus
import sdmed.extra.cso.views.theme.FThemeUtil
import java.util.ArrayList

@Composable
fun qnaScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
              displayFeatures: List<DisplayFeature> = emptyList(),
              navigationType: NavigationType = NavigationType.BOTTOM,
              navigate: (MenuItem, Boolean) -> Unit) {
    var navigateCalled by remember { mutableStateOf(false) }
    val dataContext = fBaseScreen<QnaScreenVM>({ data, dataContext -> setLayoutCommand(data, dataContext) },
        null,
        windowPanelType, navigationType,
        { dataContext -> qnaScreenTwoPane(dataContext, windowPanelType, displayFeatures, navigationType) },
        { dataContext -> qnaScreenPhone(dataContext, windowPanelType, displayFeatures, navigationType) },
        { dataContext -> qnaScreenTablet(dataContext, windowPanelType, displayFeatures, navigationType) }, )
    LaunchedEffect(navigateCalled) {
        if (!navigateCalled) {
            navigateCalled = true
            getList(dataContext)
        }
    }
}

@Composable
private fun qnaScreenTwoPane(dataContext: QnaScreenVM,
                             windowPanelType: WindowPanelType,
                             displayFeatures: List<DisplayFeature>,
                             navigationType: NavigationType) {
    val color = FThemeUtil.safeColorC()
    val selectItem by dataContext.selectItem.collectAsState()
    if (selectItem == null) {
        Column(Modifier.fillMaxSize().background(color.background)) {
            qnaAddContainer(dataContext, windowPanelType, displayFeatures, navigationType)
            topContainer(dataContext)
            searchLoading(dataContext)
            Column(Modifier) {
                Box(Modifier.weight(1F)) {
                    itemListContainer(dataContext)
                }
                pageListContainer(dataContext)
            }
        }
    } else {
        TwoPane({
            Box(Modifier.fillMaxSize().background(color.background)) {
                Column(Modifier.fillMaxWidth()) {
                    qnaAddContainer(dataContext, windowPanelType, displayFeatures, navigationType)
                    topContainer(dataContext, false)
                    searchLoading(dataContext)
                    Column(Modifier) {
                        Box(Modifier.weight(1F)) {
                            itemListContainer(dataContext, false)
                        }
                        pageListContainer(dataContext)
                    }
                }
            }
        }, {
            selectItem?.let {
                qnAScreenDetail(it, windowPanelType, displayFeatures, navigationType,
                    {dataContext.selectItem.value = null}) { qnaPK, qnaTitle ->
                    dataContext.addQnA.value = false
                    dataContext.replyQnA.value = Pair(qnaPK, qnaTitle)
                }
            }
        },
            HorizontalTwoPaneStrategy(0.5F, 16.dp),
            displayFeatures)
    }
}
@Composable
private fun qnaScreenPhone(dataContext: QnaScreenVM,
                           windowPanelType: WindowPanelType,
                           displayFeatures: List<DisplayFeature>,
                           navigationType: NavigationType) {
    val color = FThemeUtil.safeColorC()
    val selectItem by dataContext.selectItem.collectAsState()
    Box(Modifier.fillMaxSize().background(color.background)) {
        Column(Modifier.fillMaxWidth()) {
            qnaAddContainer(dataContext, windowPanelType, displayFeatures, navigationType)
            selectItem?.let {
                qnAScreenDetail(it, windowPanelType, displayFeatures, navigationType,
                    {dataContext.selectItem.value = null}) { qnaPK, qnaTitle ->
                    dataContext.addQnA.value = false
                    dataContext.replyQnA.value = Pair(qnaPK, qnaTitle)
                }
            }
            topContainer(dataContext, false)
            searchLoading(dataContext)
            Column(Modifier) {
                Box(Modifier.weight(1F)) {
                    itemListContainer(dataContext, false)
                }
                pageListContainer(dataContext, false)
            }
        }
    }
}
@Composable
private fun qnaScreenTablet(dataContext: QnaScreenVM,
                            windowPanelType: WindowPanelType,
                            displayFeatures: List<DisplayFeature>,
                            navigationType: NavigationType) {
    val color = FThemeUtil.safeColorC()
    val selectItem by dataContext.selectItem.collectAsState()
    Box(Modifier.fillMaxSize().background(color.background)) {
        Column(Modifier.fillMaxWidth()) {
            qnaAddContainer(dataContext, windowPanelType, displayFeatures, navigationType)
            selectItem?.let {
                qnAScreenDetail(it, windowPanelType, displayFeatures, navigationType,
                    {dataContext.selectItem.value = null}) { qnaPK, qnaTitle ->
                    dataContext.addQnA.value = false
                    dataContext.replyQnA.value = Pair(qnaPK, qnaTitle)
                }
            }
            topContainer(dataContext)
            searchLoading(dataContext)
            Column(Modifier) {
                Box(Modifier.weight(1F)) {
                    itemListContainer(dataContext)
                }
                pageListContainer(dataContext)
            }
        }
    }
}

@Composable
private fun qnaAddContainer(dataContext: QnaScreenVM, windowPanelType: WindowPanelType, displayFeatures: List<DisplayFeature>, navigationType: NavigationType) {
    val addQnA by dataContext.addQnA.collectAsState()
    val replyQnA by dataContext.replyQnA.collectAsState()
    if (replyQnA.first != null && replyQnA.second != null) {
        qnAScreenAdd(replyQnA.first ?: "", replyQnA.second ?: "", windowPanelType = windowPanelType, displayFeatures = displayFeatures, navigationType = navigationType) {
            dataContext.replyQnA.value = Pair(null, null)
        }
    } else if (addQnA) {
        qnAScreenAdd(windowPanelType = windowPanelType, displayFeatures = displayFeatures, navigationType = navigationType) {
            dataContext.addQnA.value = false
        }
    }
}
@OptIn(FlowPreview::class)
@Composable
private fun topContainer(dataContext: QnaScreenVM, isWide: Boolean = true) {
    val scope = rememberCoroutineScope()
    val color = FThemeUtil.safeColorC()
    val searchBuff by dataContext.searchBuff.collectAsState()
    Box(Modifier) {
        val colModifier = if (isWide) Modifier.align(Alignment.Center) else Modifier.fillMaxWidth()
        Column(colModifier.padding(10.dp)) {
            Row(Modifier) {
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = color.buttonBackground
                }) {
                    Icon(vectorPlus(FVectorData(color.background, color.buttonForeground)),
                        stringResource(R.string.add_desc),
                        Modifier.align(Alignment.Center).clickable { dataContext.relayCommand.execute(QnaScreenVM.ClickEvent.ADD)},
                        Color.Unspecified)
                }
                customText(CustomTextData().apply {
                    text = stringResource(R.string.qna_desc)
                    textColor = color.foreground
                    textAlign = TextAlign.Center
                    modifier = Modifier.weight(1F)
                })
            }
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = color.background
                borderColor = color.primary
                borderSize = 1.dp
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, start = 16.dp, end = 16.dp)
            }) {
                customTextField(CustomTextFieldData().apply {
                    text = searchBuff ?: ""
                    modifier = Modifier.padding(10.dp)
                    onValueChange = {
                        if (it.length <= 20) {
                            dataContext.searchBuff.value = it
                        }
                    }
                    decorationBox = { searchDecorationBox(it, searchBuff, color) }
                })
            }
        }
    }
    scope.launch {
        dataContext.searchBuff.debounce(1000).collectLatest {
            it ?: return@collectLatest
            if (dataContext.searchString != it) {
                dataContext.searchString = it
                getList(dataContext)
            }
            dataContext.searchLoading.value = false
        }
    }
    scope.launch {
        dataContext.searchBuff.collectLatest {
            it ?: return@collectLatest
            dataContext.searchLoading.value = true
        }
    }
}
@Composable
private fun searchLoading(dataContext: QnaScreenVM) {
    val color = FThemeUtil.safeColorC()
    val searchLoading by dataContext.searchLoading.collectAsState()
    if (searchLoading) {
        Box(Modifier.fillMaxSize().background(color.transparent).zIndex(100F)) {
            CircularProgressIndicator(Modifier.align(Alignment.Center).background(color.transparent), color.primary)
        }
    }
}
@Composable
private fun itemListContainer(dataContext: QnaScreenVM, isWide: Boolean = true) {
    val items by dataContext.qnaModel.collectAsState()
    LazyColumn(Modifier.fillMaxWidth()) {
        items(items, { it.thisPK }) { item ->
            qnaItemContainer(dataContext, item)
        }
    }
}
@Composable
private fun qnaItemContainer(dataContext: QnaScreenVM, item: QnAHeaderModel) {
    val color = FThemeUtil.safeColorC()
    val selectItem by dataContext.selectItem.collectAsState()
    item.relayCommand = dataContext.relayCommand
    shapeRoundedBox(ShapeRoundedBoxData().apply {
        backgroundColor = if (selectItem?.thisPK == item.thisPK) color.onSenary else color.senaryContainer
        modifier = Modifier.padding(5.dp).clickable { item.onClick(QnAHeaderModel.ClickEvent.THIS) }
    }) {
        Row(Modifier.fillMaxWidth().clickable { item.onClick(QnAHeaderModel.ClickEvent.THIS) }) {
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = item.qnaState.parseQnABackColor()
                modifier = Modifier.padding(5.dp)
            }) {
                customText(CustomTextData().apply {
                    text = item.qnaState.desc
                    textColor = item.qnaState.parseQnAColor()
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                })
            }
            customText(CustomTextData().apply {
                text = item.name
                textColor = color.foreground
                modifier = Modifier.align(Alignment.CenterVertically).weight(1F)
            })

            customText(CustomTextData().apply {
                text = item.regDateString
                textColor = color.foreground
                modifier = Modifier.align(Alignment.CenterVertically)
            })
        }
    }
}

@Composable
private fun pageListContainer(dataContext: QnaScreenVM, isWide: Boolean = true) {
    val color = FThemeUtil.safeColorC()
    val paginationModel by dataContext.paginationModel.collectAsState()
    paginationModel.relayCommand = dataContext.relayCommand
    val previousPage by dataContext.previousPage.collectAsState()
    val page by dataContext.page.collectAsState()
    val pages by paginationModel.pages.collectAsState()
    val first by paginationModel.first.collectAsState()
    val last by paginationModel.last.collectAsState()
    val itemCountOnScreen = if (isWide) 2 else 5
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = screenWidth / itemCountOnScreen * 2
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    Box(Modifier.fillMaxWidth()) {
        Row(Modifier.align(Alignment.Center)) {
            Icon(vectorDoubleLeft(FVectorData(color.transparent, if (first) color.disableForeGray else color.primary)),
                stringResource(R.string.left_desc),
                Modifier.clickable { paginationModel.onClick(PaginationModel.ClickEvent.FIRST) }, Color.Unspecified)
            LazyRow(Modifier.widthIn(0.dp, itemWidth), listState) {
                items(pages, { it.pageNumber }) { item ->
                    pageItemContainer(dataContext, item)
                }
            }
            Icon(vectorDoubleRight(FVectorData(color.transparent, if (last) color.disableForeGray else color.primary)),
                stringResource(R.string.right_desc),
                Modifier.clickable { paginationModel.onClick(PaginationModel.ClickEvent.LAST) }, Color.Unspecified)
        }
    }
    if (page != previousPage) {
        scope.launch {
            dataContext.previousPage.value = page
            listState.animateScrollToItem(page)
        }
        pages[previousPage].unSelectThis()
    }
    LaunchedEffect(page) {
        if (pages.isNotEmpty()) {
            pages[page].selectThis()
        }
        paginationModel.first.value = page == 0
        paginationModel.last.value = page == pages.size - 1
    }
}
@Composable
private fun pageItemContainer(dataContext: QnaScreenVM, item: PageNumberModel) {
    item.relayCommand = dataContext.relayCommand
    val color = FThemeUtil.safeColorC()
    val isSelect by item.isSelect.collectAsState()
    Box(Modifier.zIndex(100F).clickable { item.onClick(PageNumberModel.ClickEvent.THIS)}, contentAlignment = Alignment.Center) {
        customText(CustomTextData().apply {
            text = item.pageNumber.toString()
            textColor = if (isSelect) color.buttonForeground else color.foreground
            textSize = FThemeUtil.textUnit(12F)
            textAlign = TextAlign.Center
            modifier = Modifier.zIndex(100F)
        })
        Icon(vectorCircle(FVectorData(color.transparent, if (isSelect) color.buttonBackground else color.transparent)),
            stringResource(R.string.select_desc), Modifier, Color.Unspecified)
    }
}

@Composable
private fun searchDecorationBox(innerTextField: @Composable () -> Unit, text: String?, color: IBaseColor) {
    if (text.isNullOrEmpty()) {
        customText(CustomTextData().apply {
            this.text = stringResource(R.string.search_desc)
            textColor = color.disableForeGray
            modifier = Modifier.fillMaxWidth()
        })
    } else {
        Column(Modifier.fillMaxWidth()) {
            innerTextField()
        }
    }
}

private fun setLayoutCommand(data: Any?, dataContext: QnaScreenVM) {
    setThisCommand(data, dataContext)
    setQnaCommand(data, dataContext)
    setPageCommand(data, dataContext)
}
private fun setThisCommand(data: Any?, dataContext: QnaScreenVM) {
    val eventName = data as? QnaScreenVM.ClickEvent ?: return
    when (eventName) {
        QnaScreenVM.ClickEvent.ADD -> dataContext.addQnA.value = true
    }
}
private fun setQnaCommand(data: Any?, dataContext: QnaScreenVM) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? QnAHeaderModel.ClickEvent ?: return
    val dataBuff = data[1] as? QnAHeaderModel ?: return
    when (eventName) {
        QnAHeaderModel.ClickEvent.THIS -> {
            if (dataContext.selectItem.value != dataBuff) {
                dataContext.selectItem.value = dataBuff
            } else {
                dataContext.selectItem.value = null
            }
        }
    }
}
private fun setPageCommand(data: Any?, dataContext: QnaScreenVM) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? PaginationModel.ClickEvent ?: return
    val dataBuff = data[1] as? PaginationModel ?: return
    when (eventName) {
        PaginationModel.ClickEvent.FIRST -> {
            if (dataBuff.first.value) {
                return
            }
            dataContext.page.value = 0
            addList(dataContext)
        }
        PaginationModel.ClickEvent.PREV -> { }
        PaginationModel.ClickEvent.NEXT -> { }
        PaginationModel.ClickEvent.LAST -> {
            if (dataBuff.last.value) {
                return
            }
            dataContext.page.value = dataBuff.totalPages - 1
            addList(dataContext)
        }
    }
}
private fun setPageItemCommand(data: Any?, dataContext: QnaScreenVM) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? PageNumberModel.ClickEvent ?: return
    val dataBuff = data[1] as? PageNumberModel ?: return
    when (eventName) {
        PageNumberModel.ClickEvent.THIS -> {
            if (dataContext.page.value == dataBuff.pageNumber - 1) {
                return
            }
            dataContext.page.value = dataBuff.pageNumber - 1
            addList(dataContext)
        }
    }
}

private fun getList(dataContext: QnaScreenVM) {
    dataContext.loading()
    FCoroutineUtil.coroutineScope({
        val ret = dataContext.getList()
        dataContext.loading(false)
        if (ret.result != true) {
            dataContext.toast(ret.msg)
        }
    })
}
private fun addList(dataContext: QnaScreenVM) {
    dataContext.loading()
    FCoroutineUtil.coroutineScope({
        val ret = if (dataContext.searchString.isBlank()) {
            dataContext.addList()
        } else {
            dataContext.addLike()
        }
        dataContext.loading(false)
        if (ret.result != true) {
            dataContext.toast(ret.msg)
        }
    })
}