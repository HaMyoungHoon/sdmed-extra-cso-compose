package sdmed.extra.cso.views.main.my

import sdmed.extra.cso.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun myScreenTopContainer(dataContext: MyScreenVM) {
    val color = FThemeUtil.safeColorC()
    val thisData by dataContext.thisData.collectAsState()
    Row(Modifier.fillMaxWidth().background(color.background)) {
        customText(CustomTextData().apply {
            text = thisData.id
            textSize = FThemeUtil.textUnit(18F)
            textColor = color.foreground
            modifier = Modifier.align(Alignment.CenterVertically).padding(5.dp)
        })
        customText(CustomTextData().apply {
            text = thisData.name
            textSize = FThemeUtil.textUnit(18F)
            textColor = color.foreground
            textAlign = TextAlign.Center
            modifier = Modifier.align(Alignment.CenterVertically).weight(1F).padding(5.dp)
        })
        shapeRoundedBox(ShapeRoundedBoxData().apply {
            backgroundColor = color.buttonBackground
            modifier = Modifier.align(Alignment.CenterVertically).padding(5.dp)
                .clickable { dataContext.relayCommand.execute(MyScreenVM.ClickEvent.LOGOUT)}
        }) {
            customText(CustomTextData().apply {
                text = stringResource(R.string.logout_desc)
                textSize = FThemeUtil.textUnit(18F)
                textColor = color.buttonForeground
                modifier = Modifier.padding(5.dp)
            })
        }
    }
}

//@Preview
@Composable
private fun previewMyScreenTopContainer() {
    myScreenTopContainer(MyScreenVM().apply {
        fakeInit()
    })
}