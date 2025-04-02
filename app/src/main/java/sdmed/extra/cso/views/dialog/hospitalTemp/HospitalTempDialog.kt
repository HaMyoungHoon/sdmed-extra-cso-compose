package sdmed.extra.cso.views.dialog.hospitalTemp

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun hospitalTempDialog(data: HospitalTempModel,
                       windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                       displayFeatures: List<DisplayFeature> = emptyList(),
                       navigationType: NavigationType = NavigationType.BOTTOM,
                       onDismissRequest: () -> Unit,
                       onSelectRequest: (HospitalTempModel) -> Unit = { }) {
    val dataContext = fBaseScreen<HospitalTempDialogVM>( { data, dataContext -> setLayoutCommand(data, dataContext, onSelectRequest) },
        { dataContext -> hospitalTempDialogScreen(dataContext, onDismissRequest) },
        windowPanelType, navigationType)
    dataContext.item.value = data
}

@Composable
private fun hospitalTempDialogScreen(dataContext: HospitalTempDialogVM, onDismissRequest: () -> Unit) {
    val color = FThemeUtil.safeColorC()
    val item by dataContext.item.collectAsState()
    Dialog(onDismissRequest) {
        shapeRoundedBox(ShapeRoundedBoxData().apply {
            backgroundColor = color.background
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp).clickable { dataContext.relayCommand.execute(HospitalTempDialogVM.ClickEvent.THIS) }
        }) {
            Column(Modifier) {
                customText(CustomTextData().apply {
                    text = item.orgName
                    textColor = color.foreground
                    textAlign = TextAlign.Center
                    modifier = Modifier.fillMaxWidth().padding(5.dp)
                })
                customText(CustomTextData().apply {
                    text = item.address
                    textColor = color.foreground
                    textAlign = TextAlign.Center
                    maxLines = 2
                    overflow = TextOverflow.Ellipsis
                    modifier = Modifier.fillMaxWidth().padding(5.dp)
                })
                if (item.phoneNumber.isNotEmpty()) {
                    customText(CustomTextData().apply {
                        text = item.phoneNumber
                        textColor = color.foreground
                        textAlign = TextAlign.Center
                        modifier = Modifier.fillMaxWidth().padding(5.dp).clickable { dataContext.relayCommand.execute(HospitalTempDialogVM.ClickEvent.PHONE_NUMBER) }
                    })
                }
                if (item.websiteUrl.isNotEmpty()) {
                    customText(CustomTextData().apply {
                        text = item.websiteUrl
                        textColor = color.foreground
                        textAlign = TextAlign.Center
                        modifier = Modifier.fillMaxWidth().padding(5.dp).clickable { dataContext.relayCommand.execute(HospitalTempDialogVM.ClickEvent.WEB_SITE) }
                    })
                }
            }
        }
    }
}
private fun setLayoutCommand(data: Any?, dataContext: HospitalTempDialogVM, onSelectRequest: (HospitalTempModel) -> Unit) {
    setThisCommand(data, dataContext, onSelectRequest)
}
private fun setThisCommand(data: Any?, dataContext: HospitalTempDialogVM, onSelectRequest: (HospitalTempModel) -> Unit) {
    val eventName = data as? HospitalTempDialogVM.ClickEvent ?: return
    when (eventName) {
        HospitalTempDialogVM.ClickEvent.THIS -> onSelectRequest(dataContext.item.value)
        HospitalTempDialogVM.ClickEvent.WEB_SITE -> openWebsite(dataContext)
        HospitalTempDialogVM.ClickEvent.PHONE_NUMBER -> openTelephony(dataContext)
    }
}

private fun openWebsite(dataContext: HospitalTempDialogVM) {
    val url = dataContext.item.value.websiteUrl
    if (url.isEmpty()) {
        return
    }

    val buff = if (!url.startsWith("http://") && !url.startsWith("https://")) {
        "https://$url"
    } else {
        url
    }
    dataContext.context.startActivity(Intent(Intent.ACTION_VIEW, buff.toUri()).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}
private fun openTelephony(dataContext: HospitalTempDialogVM) {
    val phoneNumber = dataContext.item.value.phoneNumber
    val buff = FExtensions.regexNumberReplace(phoneNumber)
    if (buff.isNullOrBlank()) {
        return
    }
    dataContext.context.startActivity(Intent(Intent.ACTION_DIAL, "tel:$buff".toUri()).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}