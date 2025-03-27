package sdmed.extra.cso.views.main.my

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun myScreenDual(dataContext: MyScreenVM, displayFeatures: List<DisplayFeature>) {
    val color = FThemeUtil.safeColor()
    val thisData by dataContext.thisData.collectAsState()
    val pharmaList by dataContext.pharmaList.collectAsState()
    LaunchedEffect(thisData) {
        dataContext.hosList.value = thisData.hosList
    }
    if (pharmaList.isEmpty()) {
        Column(Modifier.fillMaxSize().background(color.background)) {
            myScreenTopContainer(dataContext)
            myScreenInfoContainer(dataContext)
            myScreenFileList(dataContext)
            myScreenHospitalList(dataContext)
        }
    } else {
        TwoPane({
            Column(Modifier.fillMaxSize().background(color.background)) {
                myScreenTopContainer(dataContext)
                myScreenInfoContainer(dataContext)
                myScreenFileList(dataContext)
                myScreenHospitalList(dataContext)
            }
        }, {
            Column(Modifier.fillMaxSize().background(color.background)) {
                myScreenPharmaList(dataContext)
            }
        }, HorizontalTwoPaneStrategy(0.5F, 5.dp),
            displayFeatures)
    }
    getMyScreenData(dataContext)
}

//@Preview(widthDp = 1100, heightDp = 600)
@Composable
private fun previewMyScreenDual() {
    myScreenDual(MyScreenVM().apply { fakeInit() }, emptyList())
}