package sdmed.extra.cso.views.main.my

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun myScreenSingle(dataContext: MyScreenVM) {
    val color = FThemeUtil.safeColorC()
    val thisData by dataContext.thisData.collectAsState()
    val scrollState = rememberScrollState()
    LaunchedEffect(thisData) {
        dataContext.hosList.value = thisData.hosList
    }
    Column(Modifier.fillMaxWidth().background(color.background)
        .verticalScroll(scrollState)) {
        myScreenTopContainer(dataContext)
        myScreenInfoContainer(dataContext)
        myScreenFileList(dataContext)
        myScreenHospitalList(dataContext, 500.dp)
        myScreenPharmaList(dataContext, 500.dp)
    }
}

//@Preview
@Composable
fun previewMyScreenSingle() {
    myScreenSingle(MyScreenVM().apply { fakeInit() })
}