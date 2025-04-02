package sdmed.extra.cso.views.main.home

import sdmed.extra.cso.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.models.menu.MenuItem
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.vector.FVectorData
import sdmed.extra.cso.views.component.vector.vectorDueDate
import sdmed.extra.cso.views.component.vector.vectorEDI
import sdmed.extra.cso.views.component.vector.vectorEDINew
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun homeScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
               displayFeatures: List<DisplayFeature> = emptyList(),
               navigationType: NavigationType = NavigationType.BOTTOM,
               navigate: (MenuItem, Boolean) -> Unit) {
    val dataContext = fBaseScreen<HomeScreenVM>({ data, dataContext -> setLayoutCommand(data, dataContext) },
        { dataContext -> homeScreenPhone(dataContext, windowPanelType, displayFeatures, navigationType, navigate)},
        windowPanelType, navigationType)
}
@Composable
private fun homeScreenPhone(dataContext: HomeScreenVM, windowPanelType: WindowPanelType,
                            displayFeatures: List<DisplayFeature>,
                            navigationType: NavigationType,
                            navigate: (MenuItem, Boolean) -> Unit) {
    val color = FThemeUtil.safeColorC()
    Box(Modifier.fillMaxSize().background(color.background)) {
        Column(Modifier) {
            topContainer(dataContext)
            contentContainer(dataContext, windowPanelType, displayFeatures, navigationType, navigate)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun topContainer(dataContext: HomeScreenVM) {
    val color = FThemeUtil.safeColorC()
    val tabIndex by dataContext.tabIndex.collectAsState()
    SecondaryTabRow(tabIndex, Modifier.fillMaxWidth(), color.primaryContainer, color.primary, {
        TabRowDefaults.SecondaryIndicator(Modifier.tabIndicatorOffset(tabIndex, matchContentSize = true))
    }) {
        Tab(tabIndex == 0, { dataContext.tabIndex.value = 0 }) {
            Column(Modifier) {
                Icon(vectorEDI(FVectorData(color.background, if (tabIndex == 0) color.primary else color.disableForeGray)),
                    stringResource(R.string.edi_request_desc),
                    Modifier.align(Alignment.CenterHorizontally), Color.Unspecified)
                customText(CustomTextData().apply {
                    text = stringResource(R.string.edi_request_desc)
                    textColor = if (tabIndex == 0) color.primary else color.disableForeGray
                    textAlign = TextAlign.Center
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                })
            }
        }
        Tab(tabIndex == 1, { dataContext.tabIndex.value = 1 }) {
            Column(Modifier) {
                vectorEDINew(Modifier.align(Alignment.CenterHorizontally),
                    FVectorData(color.background, if (tabIndex == 1) color.primary else color.disableForeGray), 24.dp)
                customText(CustomTextData().apply {
                    text = stringResource(R.string.edi_new_desc)
                    textColor = if (tabIndex == 1) color.primary else color.disableForeGray
                    textAlign = TextAlign.Center
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                })
            }
        }
        Tab(tabIndex == 2, { dataContext.tabIndex.value = 2 }) {
            Column(Modifier) {
                Icon(vectorDueDate(FVectorData(color.background, if (tabIndex == 2) color.primary else color.disableForeGray)),
                    stringResource(R.string.edi_due_date_desc),
                    Modifier.align(Alignment.CenterHorizontally), Color.Unspecified)
                customText(CustomTextData().apply {
                    text = stringResource(R.string.edi_due_date_desc)
                    textColor = if (tabIndex == 2) color.primary else color.disableForeGray
                    textAlign = TextAlign.Center
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                })
            }
        }
    }
}
@Composable
fun contentContainer(dataContext: HomeScreenVM,
                     windowPanelType: WindowPanelType,
                     displayFeatures: List<DisplayFeature>,
                     navigationType: NavigationType,
                     navigate: (MenuItem, Boolean) -> Unit) {
    val tabIndex by dataContext.tabIndex.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 3 })
    HorizontalPager(pagerState, Modifier.fillMaxSize()) { page ->
        when (page) {
            0 -> homeEDIRequestScreen(windowPanelType, displayFeatures, navigationType, navigate)
            1 -> homeEDIRequestNewScreen(windowPanelType, displayFeatures, navigationType, navigate)
            2 -> homeEDIDueDateScreen(windowPanelType, displayFeatures, navigationType, navigate)
        }
    }

    LaunchedEffect(tabIndex) {
        pagerState.animateScrollToPage(tabIndex)
    }
    LaunchedEffect(pagerState.currentPage) {
        dataContext.tabIndex.value = pagerState.currentPage
    }
}

private fun setLayoutCommand(data: Any?, dataContext: HomeScreenVM) {
}