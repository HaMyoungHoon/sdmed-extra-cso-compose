package sdmed.extra.cso.views.main.price

import sdmed.extra.cso.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import sdmed.extra.cso.models.retrofit.medicines.MedicineModel
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
import sdmed.extra.cso.views.theme.FThemeUtil
import java.util.ArrayList

@Composable
fun priceScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                displayFeatures: List<DisplayFeature> = emptyList(),
                navigationType: NavigationType = NavigationType.BOTTOM,
                navigate: (MenuItem, Boolean) -> Unit) {
    var navigateCalled by remember { mutableStateOf(false) }
    val dataContext = fBaseScreen<PriceScreenVM>({ data, dataContext -> setLayoutCommand(data, dataContext) },
        null,
        windowPanelType, navigationType,
        { dataContext -> priceScreenDual(dataContext, displayFeatures) },
        { dataContext -> priceScreenPhone(dataContext) },
        { dataContext -> priceScreenTablet(dataContext) })
    LaunchedEffect(navigateCalled) {
        if (!navigateCalled) {
            navigateCalled = true
            getList(dataContext)
        }
    }
}

@Composable
private fun priceScreenDual(dataContext: PriceScreenVM, displayFeatures: List<DisplayFeature>) {
    val color = FThemeUtil.safeColorC()
    Box(Modifier.fillMaxSize().background(color.background)) {
        Column(Modifier.fillMaxSize().align(Alignment.Center)) {
            topContainer(dataContext)
            searchLoading(dataContext)
            Column(Modifier) {
                itemListContainer(dataContext)
                pageListContainer(dataContext)
            }
        }
    }
}
@Composable
private fun priceScreenTablet(dataContext: PriceScreenVM) {
    val color = FThemeUtil.safeColorC()
    Box(Modifier.fillMaxSize().background(color.background)) {
        Column(Modifier.fillMaxSize().align(Alignment.Center)) {
            topContainer(dataContext)
            searchLoading(dataContext)
            Column(Modifier) {
                itemListContainer(dataContext)
                pageListContainer(dataContext)
            }
        }
    }
}
@Composable
private fun priceScreenPhone(dataContext: PriceScreenVM) {
    val color = FThemeUtil.safeColorC()
    Box(Modifier.fillMaxSize().background(color.background)) {
        Column(Modifier.fillMaxSize().align(Alignment.Center)) {
            topContainer(dataContext)
            Box(Modifier) {
                searchLoading(dataContext)
                Column(Modifier.fillMaxSize()) {
                    Box(Modifier.weight(1F)) {
                        itemListContainer(dataContext, false)
                    }
                    pageListContainer(dataContext, false)
                }
            }
        }
    }
}
@OptIn(FlowPreview::class)
@Composable
private fun topContainer(dataContext: PriceScreenVM) {
    val scope = rememberCoroutineScope()
    val color = FThemeUtil.safeColorC()
    val searchBuff by dataContext.searchBuff.collectAsState()
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
private fun searchLoading(dataContext: PriceScreenVM) {
    val color = FThemeUtil.safeColorC()
    val searchLoading by dataContext.searchLoading.collectAsState()
    if (searchLoading) {
        Box(Modifier.fillMaxSize().background(color.transparent).zIndex(100F)) {
            CircularProgressIndicator(Modifier.align(Alignment.Center).background(color.transparent), color.primary)
        }
    }
}
@Composable
private fun itemListContainer(dataContext: PriceScreenVM, isWide: Boolean = true) {
    val medicineModel by dataContext.medicineModel.collectAsState()
    dataContext.size.value = if (isWide) 40 else 20
    LazyColumn(Modifier.fillMaxWidth().padding(10.dp)) {
        if (isWide) {
            items(medicineModel.chunked(2), { it.first().thisPK }) { items ->
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    items.forEach { item ->
                        Box(Modifier.weight(1F).aspectRatio(1F).padding(8.dp),
                            Alignment.Center) {
                            medicineItemContainer(dataContext, item)
                        }
                    }
                    if (items.size == 1) {
                        Spacer(Modifier.weight(1F))
                    }
                }
            }
        } else {
            items(medicineModel, { it.thisPK }) { item ->
                medicineItemContainer(dataContext, item)
            }
        }
    }
}
@Composable
private fun medicineItemContainer(dataContext: PriceScreenVM, item: MedicineModel) {
    val color = FThemeUtil.safeColorC()
    item.relayCommand = dataContext.relayCommand
    Box(Modifier) {
        Column(Modifier.fillMaxWidth()) {
            customText(CustomTextData().apply {
                text = item.orgName
                textColor = color.paragraph
            })
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                customText(CustomTextData().apply {
                    text = stringResource(R.string.client_name)
                    textColor = color.foreground
                })
                customText(CustomTextData().apply {
                    text = item.clientName ?: ""
                    textColor = color.primary
                    modifier = Modifier.weight(1F)
                })
                customText(CustomTextData().apply {
                    text = item.kdCode
                    textColor = color.foreground
                })
            }
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                customText(CustomTextData().apply {
                    text = stringResource(R.string.maker_name)
                    textColor = color.foreground
                })
                customText(CustomTextData().apply {
                    text = item.makerName ?: ""
                    textColor = color.primary
                    modifier = Modifier.weight(1F)
                })
                customText(CustomTextData().apply {
                    text = item.maxPriceString
                    textColor = color.quaternary
                })
                customText(CustomTextData().apply {
                    text = stringResource(R.string.price_unit)
                    textColor = color.foreground
                })
                customText(CustomTextData().apply {
                    text = item.standard
                    textColor = color.foreground
                })
            }
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                customText(CustomTextData().apply {
                    text = item.etc1
                    textColor = color.quaternary
                })
            }
        }
    }
}
@Composable
private fun pageListContainer(dataContext: PriceScreenVM, isWide: Boolean = true) {
    val color = FThemeUtil.safeColorC()
    val paginationModel by dataContext.paginationModel.collectAsState()
    paginationModel.relayCommand = dataContext.relayCommand
    val previousPage by dataContext.previousPage.collectAsState()
    val page by dataContext.page.collectAsState()
    val pages by paginationModel.pages.collectAsState()
    val first by paginationModel.first.collectAsState()
    val last by paginationModel.last.collectAsState()
    val itemCountOnScreen = if (isWide) 10 else 5
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = screenWidth / itemCountOnScreen * 2
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    Box(Modifier.fillMaxWidth()) {
        Row(Modifier.align(Alignment.Center)) {
            Icon(vectorDoubleLeft(FVectorData(color.transparent, if (first) color.disableForeGray else color.primary)),
                stringResource(R.string.left_desc),
                Modifier.clickable { paginationModel.onClick(PaginationModel.ClickEvent.FIRST) }, Color.Unspecified)
            LazyRow(Modifier.width(itemWidth), listState) {
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
private fun pageItemContainer(dataContext: PriceScreenVM, item: PageNumberModel) {
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

private fun setLayoutCommand(data: Any?, dataContext: PriceScreenVM) {
    setThisCommand(data, dataContext)
    setItemCommand(data, dataContext)
    setPageCommand(data, dataContext)
    setPageItemCommand(data, dataContext)
}
private fun setThisCommand(data: Any?, dataContext: PriceScreenVM) {

}
private fun setItemCommand(data: Any?, dataContext: PriceScreenVM) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? MedicineModel.ClickEvent ?: return
    val dataBuff = data[1] as? MedicineModel ?: return
    when (eventName) {
        MedicineModel.ClickEvent.THIS -> { }
    }
}
private fun setPageCommand(data: Any?, dataContext: PriceScreenVM) {
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
private fun setPageItemCommand(data: Any?, dataContext: PriceScreenVM) {
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

private fun getList(dataContext: PriceScreenVM) {
    dataContext.loading()
    FCoroutineUtil.coroutineScope({
        val ret = if (dataContext.searchString.isBlank()) {
            dataContext.getList()
        } else {
            dataContext.getLike()
        }
        dataContext.loading(false)
        if (ret.result != true) {
            dataContext.toast(ret.msg)
        }
    })
}
private fun addList(dataContext: PriceScreenVM) {
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