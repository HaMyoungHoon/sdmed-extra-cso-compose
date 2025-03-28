package sdmed.extra.cso.views.main.my

import sdmed.extra.cso.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun myScreenInfoContainer(dataContext: MyScreenVM) {
    val color = FThemeUtil.safeColorC()
    val thisData by dataContext.thisData.collectAsState()
    Column(Modifier.fillMaxWidth().background(color.background)) {
        Row(Modifier.fillMaxWidth(),
            Arrangement.SpaceBetween) {
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = color.buttonBackground
                modifier = Modifier.wrapContentWidth().padding(5.dp)
                    .clickable { dataContext.relayCommand.execute(MyScreenVM.ClickEvent.PASSWORD_CHANGE) }
            }) {
                customText(CustomTextData().apply {
                    text = stringResource(R.string.password_change_desc)
                    textColor = color.buttonForeground
                    modifier = Modifier.padding(5.dp)
                })
            }
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = color.buttonBackground
                modifier = Modifier.wrapContentWidth().padding(5.dp)
                    .clickable { dataContext.relayCommand.execute(MyScreenVM.ClickEvent.MULTI_LOGIN) }
            }) {
                customText(CustomTextData().apply {
                    text = stringResource(R.string.multi_login)
                    textColor = color.buttonForeground
                    modifier = Modifier.padding(5.dp)
                })
            }
        }
        Row(Modifier.fillMaxWidth().padding(5.dp)) {
            customText(CustomTextData().apply {
                text = stringResource(R.string.last_login_desc)
                textColor = color.foreground
                textSize = FThemeUtil.textUnit(16F)
                modifier = Modifier.padding(end = 5.dp)
            })
            customText(CustomTextData().apply {
                text = thisData.lastLoginDateString
                textSize = FThemeUtil.textUnit(16F)
                textColor = color.foreground
            })
        }
    }
}

//@Preview
@Composable
private fun previewMyScreenInfoContainer() {
    myScreenInfoContainer(MyScreenVM().apply { fakeInit() })
}