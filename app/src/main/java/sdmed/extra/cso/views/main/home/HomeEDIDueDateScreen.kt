package sdmed.extra.cso.views.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.fDate.FDateTime
import sdmed.extra.cso.fDate.FDayOfWeek
import sdmed.extra.cso.models.menu.MenuItem
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaDueDateModel
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun homeEDIDueDateScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                         displayFeatures: List<DisplayFeature> = emptyList(),
                         navigationType: NavigationType = NavigationType.BOTTOM,
                         navigate: (MenuItem, Boolean) -> Unit) {
    var navigateCalled by remember { mutableStateOf(false) }
    val dataContext = fBaseScreen<HomeEDIDueDateScreenVM>( { data, dataContext -> setLayoutCommand(data, dataContext, navigate) },
        null,
        windowPanelType, navigationType,
        { dataContext -> homeEDIDueDateScreenTwoPane(dataContext, displayFeatures) },
        { dataContext -> homeEDIDueDateScreenPhone(dataContext) },
        { dataContext -> homeEDIDueDateScreenTablet(dataContext) })
    LaunchedEffect(navigateCalled) {
        if (!navigateCalled) {
            navigateCalled = true
            getList(dataContext)
        }
    }
}

@Composable
private fun homeEDIDueDateScreenTwoPane(dataContext: HomeEDIDueDateScreenVM, displayFeatures: List<DisplayFeature>) {
    val color = FThemeUtil.safeColorC()
    Box(Modifier.fillMaxSize().background(color.background)) {
        Column(Modifier.fillMaxWidth()) {
            topContainer(dataContext)
            itemListContainer(dataContext)
        }
    }
}
@Composable
private fun homeEDIDueDateScreenPhone(dataContext: HomeEDIDueDateScreenVM) {
    val color = FThemeUtil.safeColorC()
    Box(Modifier.fillMaxSize().background(color.background)) {
        Column(Modifier.fillMaxWidth()) {
            topContainer(dataContext, false)
            itemListContainer(dataContext, false)
        }
    }
}
@Composable
private fun homeEDIDueDateScreenTablet(dataContext: HomeEDIDueDateScreenVM) {
    val color = FThemeUtil.safeColorC()
    Box(Modifier.fillMaxSize().background(color.background)) {
        Column(Modifier.fillMaxWidth()) {
            topContainer(dataContext)
            itemListContainer(dataContext)
        }
    }
}

@Composable
private fun topContainer(dataContext: HomeEDIDueDateScreenVM, isWide: Boolean = true) {
    val color = FThemeUtil.safeColorC()
    val startDate by dataContext.startDate.collectAsState()
    val endDate by dataContext.endDate.collectAsState()
    val startDateSelect by dataContext.startDateSelect.collectAsState()
    val endDateSelect by dataContext.endDateSelect.collectAsState()

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
                    modifier = Modifier.padding(start = 10.dp).clickable { dataContext.relayCommand.execute(HomeEDIDueDateScreenVM.ClickEvent.START_DATE)}
                }) {
                    customText(CustomTextData().apply {
                        text = startDate
                        textColor = color.buttonForeground
                        modifier = Modifier.padding(10.dp)
                    })
                }
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = color.buttonBackground
                    modifier = Modifier.padding(start = 10.dp).clickable { dataContext.relayCommand.execute(HomeEDIDueDateScreenVM.ClickEvent.END_DATE)}
                }) {
                    customText(CustomTextData().apply {
                        text = endDate
                        textColor = color.buttonForeground
                        modifier = Modifier.padding(10.dp)
                    })
                }
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = color.buttonBackground
                    modifier = Modifier.padding(start = 10.dp).clickable { dataContext.relayCommand.execute(HomeEDIDueDateScreenVM.ClickEvent.SEARCH)}
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

    if (startDateSelect) {
        startDateSelect(dataContext)
    }
    if (endDateSelect) {
        endDateSelect(dataContext)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun itemListContainer(dataContext: HomeEDIDueDateScreenVM, isWide: Boolean = true) {
    val items by dataContext.items.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshState = rememberPullToRefreshState()
    PullToRefreshBox(isRefreshing, {
        isRefreshing = true
        getList(dataContext) {
            isRefreshing = false
        }
    }, Modifier, refreshState) {
        LazyColumn(Modifier.padding(10.dp)) {
            if (isWide) {
                items(items.chunked(2), { it.first().thisPK }) { items ->
                    Row(Modifier, Arrangement.SpaceBetween) {
                        items.forEach { item ->
                            Box(Modifier.weight(1F).padding(8.dp), Alignment.Center) {
                                itemContainer(dataContext, item)
                            }
                        }
                        if (items.size == 1) {
                            Spacer(Modifier.weight(1F))
                        }
                    }
                }
            } else {
                items(items, { it.thisPK }) { item ->
                    itemContainer(dataContext, item)
                }
            }
        }
    }
}

@Composable
private fun itemContainer(dataContext: HomeEDIDueDateScreenVM, item: EDIPharmaDueDateModel) {
    val color = FThemeUtil.safeColorC()
    Row(Modifier.fillMaxWidth()) {
        customText(CustomTextData().apply {
            text = item.yearMonthDay
            textColor = item.parseColor
            modifier = Modifier.padding(end = 2.dp)
        })
        customText(CustomTextData().apply {
            text = "("
            textColor = item.parseColor
        })
        customText(CustomTextData().apply {
            text = item.dayOfTheWeek
            textColor = item.parseColor
        })
        customText(CustomTextData().apply {
            text = ")"
            textColor = item.parseColor
        })
        customText(CustomTextData().apply {
            text = item.orgName
            textColor = color.primary
            modifier = Modifier.weight(1F).padding(start = 2.dp, end = 2.dp)
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun startDateSelect(dataContext: HomeEDIDueDateScreenVM) {
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
private fun endDateSelect(dataContext: HomeEDIDueDateScreenVM) {
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

private fun setLayoutCommand(data: Any?, dataContext: HomeEDIDueDateScreenVM, navigate: (MenuItem, Boolean) -> Unit) {
    val eventName = data as? HomeEDIDueDateScreenVM.ClickEvent ?: return
    when (eventName) {
        HomeEDIDueDateScreenVM.ClickEvent.START_DATE -> startDate(dataContext)
        HomeEDIDueDateScreenVM.ClickEvent.END_DATE -> endDate(dataContext)
        HomeEDIDueDateScreenVM.ClickEvent.SEARCH -> getList(dataContext)
    }
}

private fun getList(dataContext: HomeEDIDueDateScreenVM, end: () -> Unit = { }) {
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
private fun startDate(dataContext: HomeEDIDueDateScreenVM) {
    dataContext.startDateSelect.value = true
}
private fun endDate(dataContext: HomeEDIDueDateScreenVM) {
    dataContext.endDateSelect.value = true
}