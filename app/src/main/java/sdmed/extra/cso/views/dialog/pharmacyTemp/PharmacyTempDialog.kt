package sdmed.extra.cso.views.dialog.pharmacyTemp

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import sdmed.extra.cso.models.retrofit.hospitals.PharmacyTempModel
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun pharmacyTempDialog(data: PharmacyTempModel,
                       windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                       displayFeatures: List<DisplayFeature> = emptyList(),
                       navigationType: NavigationType = NavigationType.BOTTOM,
                       onDismissRequest: () -> Unit,
                       onSelectRequest: (PharmacyTempModel) -> Unit = { }) {
    val dataContext = fBaseScreen<PharmacyTempDialogVM>( { data, dataContext -> setLayoutCommand(data, dataContext, onSelectRequest) },
        { dataContext -> pharmacyTempDialogScreen(dataContext, onDismissRequest) },
        windowPanelType, navigationType)
    dataContext.item.value = data
}

@Composable
private fun pharmacyTempDialogScreen(dataContext: PharmacyTempDialogVM, onDismissRequest: () -> Unit) {
    val color = FThemeUtil.safeColorC()
    val item by dataContext.item.collectAsState()
    Dialog(onDismissRequest) {
        shapeRoundedBox(ShapeRoundedBoxData().apply {
            backgroundColor = color.background
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp).clickable { dataContext.relayCommand.execute(PharmacyTempDialogVM.ClickEvent.THIS) }
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
                        modifier = Modifier.fillMaxWidth().padding(5.dp).clickable { dataContext.relayCommand.execute(PharmacyTempDialogVM.ClickEvent.PHONE_NUMBER) }
                    })
                }
            }
        }
    }
}

private fun setLayoutCommand(data: Any?, dataContext: PharmacyTempDialogVM, onSelectRequest: (PharmacyTempModel) -> Unit) {
    setThisCommand(data, dataContext, onSelectRequest)
}
private fun setThisCommand(data: Any?, dataContext: PharmacyTempDialogVM, onSelectRequest: (PharmacyTempModel) -> Unit) {
    val eventName = data as? PharmacyTempDialogVM.ClickEvent ?: return
    when (eventName) {
        PharmacyTempDialogVM.ClickEvent.THIS -> onSelectRequest(dataContext.item.value)
        PharmacyTempDialogVM.ClickEvent.PHONE_NUMBER -> openTelephony(dataContext)
    }
}

private fun openTelephony(dataContext: PharmacyTempDialogVM) {
    val phoneNumber = dataContext.item.value.phoneNumber
    val buff = FExtensions.regexNumberReplace(phoneNumber)
    if (buff.isNullOrBlank()) {
        return
    }
    dataContext.context.startActivity(Intent(Intent.ACTION_DIAL, "tel:$buff".toUri()).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}