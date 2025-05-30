package sdmed.extra.cso.views.main.my

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun myScreenDual(dataContext: MyScreenVM, displayFeatures: List<DisplayFeature>) {
    val color = FThemeUtil.safeColorC()
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
}

//@Preview(widthDp = 1100, heightDp = 600)
@Composable
private fun previewMyScreenDual() {
    myScreenDual(MyScreenVM().apply { fakeInit() }, emptyList())
}