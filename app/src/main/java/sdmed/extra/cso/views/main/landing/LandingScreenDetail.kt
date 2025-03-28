package sdmed.extra.cso.views.main.landing

import sdmed.extra.cso.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import sdmed.extra.cso.bases.FMainApplication
import sdmed.extra.cso.models.menu.MenuItem
import sdmed.extra.cso.models.menu.MenuList
import sdmed.extra.cso.models.retrofit.FRetrofitVariable
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.utils.FVersionControl
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.dialog.message.MessageDialogData
import sdmed.extra.cso.views.dialog.message.messageDialog
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun landingScreenDetail(dataContext: LandingScreenVM, navigate: (MenuItem, Boolean)-> Unit) {
    val startVisible by dataContext.startVisible.collectAsState()
    val tokenCheck by dataContext.tokenCheck.collectAsState()
    tokenCheck(dataContext)
    if (tokenCheck) {
        navigate(MenuList.menuEDI(), true)
    }
    val color = FThemeUtil.safeColor()
    Box(Modifier.fillMaxSize()) {
        Image(painterResource(R.drawable.landing_background),
            stringResource(R.string.landing_desc),
            Modifier.fillMaxSize().align(Alignment.Center),
            Alignment.Center,
            ContentScale.Crop
        )

        customText(CustomTextData().apply {
            text = stringResource(R.string.app_name)
            textSize = FThemeUtil.textUnit(30F)
            textColor = color.absoluteWhite
            fontWeight = FontWeight.ExtraBold
            textAlign = TextAlign.Center
            modifier = Modifier.fillMaxWidth().padding(top = 40.dp)
        })

        if (startVisible) {
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = color.background
                modifier = Modifier.fillMaxWidth().padding(bottom = 36.dp, start = 16.dp, end = 16.dp)
                    .align(Alignment.BottomCenter)
                    .clickable { dataContext.relayCommand.execute(LandingScreenVM.ClickEvent.START)}
            }) {
                customText(CustomTextData().apply {
                    text = stringResource(R.string.start_desc)
                    textSize = FThemeUtil.textUnit(24F)
                    textColor = color.primary
                    modifier = Modifier.fillMaxWidth().padding(5.dp)
                    textAlign = TextAlign.Center
                })
            }
        }
    }
}
private fun tokenCheck(dataContext: LandingScreenVM) {
    if (!FAmhohwa.tokenCheck(dataContext)) {
        FAmhohwa.tokenRefresh(dataContext, { ret ->
            if (ret.result == true) {
                dataContext.tokenCheck.value = true
            } else {
                dataContext.startVisible.value = true
            }
        })
    } else {
        dataContext.tokenCheck.value = true
    }
}

//@Preview
@Composable
private fun previewLandingDetail() {
    landingScreenDetail(LandingScreenVM(), {a, b -> })
}