package sdmed.extra.cso.views.main.edi

import sdmed.extra.cso.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.fDate.FDateTime
import sdmed.extra.cso.models.menu.MenuItem
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FLog
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.theme.FThemeUtil
import java.util.ArrayList

@Composable
fun ediScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
              displayFeatures: List<DisplayFeature> = emptyList(),
              navigationType: NavigationType = NavigationType.BOTTOM,
              navigate: (MenuItem, Boolean) -> Unit) {
    var navigateCalled by remember { mutableStateOf(false) }
    val dataContext = fBaseScreen<EDIScreenVM>( { data, dataContext -> setLayoutCommand(data, dataContext) },
        null,
        windowPanelType, navigationType,
        { dataContext -> ediScreenTwoPane(dataContext, windowPanelType, displayFeatures, navigationType) },
        { dataContext -> ediScreenPhone(dataContext, windowPanelType, displayFeatures, navigationType) },
        { dataContext -> ediScreenTablet(dataContext, windowPanelType, displayFeatures, navigationType) })
    val startDateSelect by dataContext.startDateSelect.collectAsState()
    val endDateSelect by dataContext.endDateSelect.collectAsState()
    LaunchedEffect(navigateCalled) {
        if (!navigateCalled) {
            navigateCalled = true
            checkExternalStorage(dataContext)
            getList(dataContext)
        }
    }
    if (startDateSelect) {
        startDateSelect(dataContext)
    }
    if (endDateSelect) {
        endDateSelect(dataContext)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun startDateSelect(dataContext: EDIScreenVM) {
    val color = FThemeUtil.safeColorC()
    val datePickerState = rememberDatePickerState()
    DatePickerDialog( { dataContext.startDateSelect.value = false},
        { customText(CustomTextData().apply {
            text = stringResource(R.string.confirm_desc)
            textColor = color.paragraph
            modifier = Modifier.clickable {
                if (datePickerState.selectedDateMillis == null) {
                    return@clickable
                }
                datePickerState.selectedDateMillis?.let {
                    dataContext.startDate.value = FDateTime().setThis(it).toString("yyyy-MM-dd")
                    dataContext.startDateSelect.value = false
                }
            }
        })}, Modifier.background(color.background)) {
        DatePicker(datePickerState, Modifier.background(color.background))
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun endDateSelect(dataContext: EDIScreenVM) {
    val color = FThemeUtil.safeColorC()
    val datePickerState = rememberDatePickerState()
    DatePickerDialog( { dataContext.endDateSelect.value = false},
        { customText(CustomTextData().apply {
            text = stringResource(R.string.confirm_desc)
            textColor = color.paragraph
            modifier = Modifier.clickable {
                if (datePickerState.selectedDateMillis == null) {
                    return@clickable
                }
                datePickerState.selectedDateMillis?.let {
                    dataContext.endDate.value = FDateTime().setThis(it).toString("yyyy-MM-dd")
                    dataContext.endDateSelect.value = false
                }
            }
        })}, Modifier.background(color.background)) {
        DatePicker(datePickerState, Modifier.background(color.background))
    }
}


@Composable
private fun ediScreenTwoPane(dataContext: EDIScreenVM,
                             windowPanelType: WindowPanelType,
                             displayFeatures: List<DisplayFeature>,
                             navigationType: NavigationType) {
    val color = FThemeUtil.safeColorC()
    val selectItem by dataContext.selectItem.collectAsState()
    if (selectItem == null) {
        Box(Modifier.background(color.background)) {
            Column(Modifier.fillMaxWidth()) {
                topContainer(dataContext, false)
                itemListContainer(dataContext)
            }
        }
    } else {
        TwoPane({
            Box(Modifier.background(color.background)) {
                Column(Modifier.fillMaxWidth()) {
                    topContainer(dataContext, false)
                    itemListContainer(dataContext)
                }
            }
        }, {
            selectItem?.let { x ->
                ediScreenDetail(x, windowPanelType, displayFeatures, navigationType) {
                    dataContext.selectItem.value = null
                }
            }
        },
            HorizontalTwoPaneStrategy(0.5F, 16.dp),
            displayFeatures)
    }
}
@Composable
private fun ediScreenPhone(dataContext: EDIScreenVM,
                           windowPanelType: WindowPanelType,
                           displayFeatures: List<DisplayFeature>,
                           navigationType: NavigationType) {
    val color = FThemeUtil.safeColorC()
    val selectItem by dataContext.selectItem.collectAsState()
    Box(Modifier.background(color.background)) {
        Column(Modifier.fillMaxWidth()) {
            selectItem?.let { x ->
                ediScreenDetail(x, windowPanelType, displayFeatures, navigationType) {
                    dataContext.selectItem.value = null
                }
            }
            topContainer(dataContext, false)
            itemListContainer(dataContext)
        }
    }
}
@Composable
private fun ediScreenTablet(dataContext: EDIScreenVM,
                            windowPanelType: WindowPanelType,
                            displayFeatures: List<DisplayFeature>,
                            navigationType: NavigationType) {
    val color = FThemeUtil.safeColorC()
    val selectItem by dataContext.selectItem.collectAsState()
    Box(Modifier.background(color.background)) {
        Column(Modifier.fillMaxWidth()) {
            selectItem?.let { x ->
                ediScreenDetail(x, windowPanelType, displayFeatures, navigationType) {
                    dataContext.selectItem.value = null
                }
            }
            topContainer(dataContext)
            itemListContainer(dataContext)
        }
    }
}


@Composable
private fun topContainer(dataContext: EDIScreenVM, isWide: Boolean = true) {
    val color = FThemeUtil.safeColorC()
    val startDate by dataContext.startDate.collectAsState()
    val endDate by dataContext.endDate.collectAsState()
    Box(Modifier.fillMaxWidth()) {
        val colModifier = if (isWide) Modifier.align(Alignment.Center) else Modifier.fillMaxWidth()
        Column(colModifier) {
            customText(CustomTextData().apply {
                text = stringResource(R.string.search_range_desc)
                textColor = color.foreground
                modifier = Modifier.padding(start = 10.dp)
            })
            Row(Modifier) {
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = color.buttonBackground
                    modifier = Modifier.padding(start = 10.dp).clickable { dataContext.relayCommand.execute(EDIScreenVM.ClickEvent.START_DATE)}
                }) {
                    customText(CustomTextData().apply {
                        text = startDate
                        textColor = color.buttonForeground
                        modifier = Modifier.padding(10.dp)
                    })
                }
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = color.buttonBackground
                    modifier = Modifier.padding(start = 10.dp).clickable { dataContext.relayCommand.execute(EDIScreenVM.ClickEvent.END_DATE)}
                }) {
                    customText(CustomTextData().apply {
                        text = endDate
                        textColor = color.buttonForeground
                        modifier = Modifier.padding(10.dp)
                    })
                }
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = color.buttonBackground
                    modifier = Modifier.padding(start = 10.dp).clickable { dataContext.relayCommand.execute(EDIScreenVM.ClickEvent.SEARCH)}
                }) {
                    customText(CustomTextData().apply {
                        text = stringResource(R.string.search_desc)
                        textColor = color.buttonForeground
                        modifier = Modifier.padding(10.dp)
                    })
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun itemListContainer(dataContext: EDIScreenVM) {
    val items by dataContext.items.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshState = rememberPullToRefreshState()
    PullToRefreshBox(isRefreshing, {
        isRefreshing = true
        getList(dataContext) {
            isRefreshing = false
        }
    }, Modifier, refreshState) {
        LazyColumn(Modifier.fillMaxWidth()) {
            items(items, { it.thisPK }) { item ->
                ediItemContainer(dataContext, item)
            }
        }
    }
}
@Composable
private fun ediItemContainer(dataContext: EDIScreenVM, item: EDIUploadModel) {
    val color = FThemeUtil.safeColorC()
    val selectedItem by dataContext.selectItem.collectAsState()
    item.relayCommand = dataContext.relayCommand
    shapeRoundedBox(ShapeRoundedBoxData().apply {
        backgroundColor = if(selectedItem?.thisPK == item.thisPK) color.onSenary else color.senaryContainer
        modifier = Modifier.padding(5.dp).clickable { item.onClick(EDIUploadModel.ClickEvent.OPEN) }
    }) {
        Row(Modifier.fillMaxWidth().padding(10.dp)) {
            customText(CustomTextData().apply {
                text = item.getRegDateString()
                textColor = color.foreground
                modifier = Modifier.align(Alignment.CenterVertically)
            })
            Row(Modifier.weight(1F).padding(start = 10.dp).align(Alignment.CenterVertically)) {
                customText(CustomTextData().apply {
                    text = item.orgName
                    textColor = if (item.isDefault) color.foreground else color.senary
                })
                if (!item.isDefault) {
                    customText(CustomTextData().apply {
                        text = item.tempOrgString
                        textColor = color.foreground
                    })
                }
            }
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = item.getEdiBackColor()
                modifier = Modifier.align(Alignment.CenterVertically)
            }) {
                customText(CustomTextData().apply {
                    text = item.ediState.desc
                    textColor = item.getEdiColor()
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                })
            }
        }
    }
}

private fun setLayoutCommand(data: Any?, dataContext: EDIScreenVM) {
    setThisCommand(data, dataContext)
    setEDICommand(data, dataContext)
}
private fun setThisCommand(data: Any?, dataContext: EDIScreenVM) {
    val eventName = data as? EDIScreenVM.ClickEvent ?: return
    when (eventName) {
        EDIScreenVM.ClickEvent.START_DATE -> startDateOpen(dataContext)
        EDIScreenVM.ClickEvent.END_DATE -> endDateOpen(dataContext)
        EDIScreenVM.ClickEvent.SEARCH -> getList(dataContext)
    }
}
private fun setEDICommand(data: Any?, dataContext: EDIScreenVM) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? EDIUploadModel.ClickEvent ?: return
    val dataBuff = data[1] as? EDIUploadModel ?: return
    when (eventName) {
        EDIUploadModel.ClickEvent.OPEN -> {
            if (dataContext.selectItem.value != dataBuff) {
                dataContext.selectItem.value = dataBuff
            } else {
                dataContext.selectItem.value = null
            }
        }
    }
}

private fun startDateOpen(dataContext: EDIScreenVM) {
    dataContext.startDateSelect.value = true
}
private fun endDateOpen(dataContext: EDIScreenVM) {
    dataContext.endDateSelect.value = true
}

private fun getList(dataContext: EDIScreenVM, end: () -> Unit = { }) {
    dataContext.loading()
    FCoroutineUtil.coroutineScope({
        val ret = dataContext.getList()
        dataContext.loading(false)
        if (ret.result != true) {
            dataContext.toast(ret.msg)
        }
        end()
    })
}
private fun checkExternalStorage(dataContext: EDIScreenVM) {
    dataContext.permissionService.externalStorage()
}
private fun checkReadStorage(dataContext: EDIScreenVM, callback: (Boolean) -> Unit) {
    dataContext.permissionService.requestReadExternalPermissions(callback)
}