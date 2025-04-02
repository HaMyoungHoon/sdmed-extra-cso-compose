package sdmed.extra.cso.views.dialog.hospitalTemp

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import java.util.ArrayList

@Composable
fun hospitalTempListDialog(data: List<HospitalTempModel>,
                           windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                           displayFeatures: List<DisplayFeature> = emptyList(),
                           navigationType: NavigationType = NavigationType.BOTTOM,
                           onDismissRequest: () -> Unit,
                           onSelectRequest: (HospitalTempModel) -> Unit = { }) {
    val dataContext = fBaseScreen<HospitalTempListDialogVM>( { data, dataContext -> setLayoutCommand(data, dataContext, onSelectRequest) },
        { dataContext -> hospitalTempListDialogScreen(dataContext, onDismissRequest) },
        windowPanelType, navigationType)
    dataContext.items.value = data.toMutableList()
}

@Composable
private fun hospitalTempListDialogScreen(dataContext: HospitalTempListDialogVM, onDismissRequest: () -> Unit) {
    val color = FThemeUtil.safeColorC()
    val items by dataContext.items.collectAsState()
    Dialog(onDismissRequest) {
        shapeRoundedBox(ShapeRoundedBoxData().apply {
            backgroundColor = color.background
            modifier = Modifier.fillMaxWidth().heightIn(0.dp, 400.dp).padding(start = 20.dp, end = 20.dp)
        }) {
            LazyColumn(Modifier) {
                items(items, { it.thisPK }) { item ->
                    item.relayCommand = dataContext.relayCommand
                    shapeRoundedBox(ShapeRoundedBoxData().apply {
                        backgroundColor = color.cardBackground
                        modifier = Modifier.padding(5.dp).clickable { item.onClick(HospitalTempModel.ClickEvent.THIS) }
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
                                    modifier = Modifier.fillMaxWidth().padding(5.dp).clickable { item.onClick(HospitalTempModel.ClickEvent.PHONE_NUMBER) }
                                })
                            }
                            if (item.websiteUrl.isNotEmpty()) {
                                customText(CustomTextData().apply {
                                    text = item.websiteUrl
                                    textColor = color.foreground
                                    textAlign = TextAlign.Center
                                    modifier = Modifier.fillMaxWidth().padding(5.dp).clickable { item.onClick(HospitalTempModel.ClickEvent.WEB_SITE) }
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun setLayoutCommand(data: Any?, dataContext: HospitalTempListDialogVM, onSelectRequest: (HospitalTempModel) -> Unit) {
    setHospitalCommand(data, dataContext, onSelectRequest)
}

private fun setHospitalCommand(data: Any?, dataContext: HospitalTempListDialogVM, onSelectRequest: (HospitalTempModel) -> Unit) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? HospitalTempModel.ClickEvent ?: return
    val dataBuff = data[1] as? HospitalTempModel ?: return
    when (eventName) {
        HospitalTempModel.ClickEvent.THIS -> onSelectRequest(dataBuff)
        HospitalTempModel.ClickEvent.WEB_SITE -> openWebsite(dataBuff, dataContext)
        HospitalTempModel.ClickEvent.PHONE_NUMBER -> openTelephony(dataBuff, dataContext)
    }
}

private fun openWebsite(data: HospitalTempModel, dataContext: HospitalTempListDialogVM) {
    val url = data.websiteUrl
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
private fun openTelephony(data: HospitalTempModel, dataContext: HospitalTempListDialogVM) {
    val phoneNumber = data.phoneNumber
    val buff = FExtensions.regexNumberReplace(phoneNumber)
    if (buff.isNullOrBlank()) {
        return
    }
    dataContext.context.startActivity(Intent(Intent.ACTION_DIAL, "tel:$buff".toUri()).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}