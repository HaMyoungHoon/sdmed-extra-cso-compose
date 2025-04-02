package sdmed.extra.cso.views.main.my

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import sdmed.extra.cso.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.fDate.FDateTime
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.models.retrofit.users.UserTrainingModel
import sdmed.extra.cso.utils.FCoil
import sdmed.extra.cso.utils.FStorage.getParcelableList
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.media.picker.MediaPickerActivity
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun myScreenTrainingCertificate(item: List<UserTrainingModel>,
                                windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                                displayFeatures: List<DisplayFeature> = emptyList(),
                                navigationType: NavigationType = NavigationType.BOTTOM,
                                onDismissRequest: () -> Unit) {
    val dataContext = fBaseScreen<MyScreenTrainingCertificateVM>( { data, dataContext -> setLayoutCommand(data, dataContext, onDismissRequest) },
        null,
        windowPanelType, navigationType,
        { dataContext -> myScreenTrainingCertificateTwoPane(dataContext, displayFeatures, onDismissRequest) },
        { dataContext -> myScreenTrainingCertificatePhone(dataContext, onDismissRequest) },
        { dataContext -> myScreenTrainingCertificateTablet(dataContext, onDismissRequest) })
    val context = LocalContext.current
    val imageSelect by dataContext.imageSelect.collectAsState()
    val trainingDateSelect by dataContext.trainingDateSelect.collectAsState()
    val activityResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode != Activity.RESULT_OK) {
            return@rememberLauncherForActivityResult
        }
        val mediaList = it.data?.getParcelableList<MediaPickerSourceModel>(FConstants.MEDIA_LIST) ?: return@rememberLauncherForActivityResult
        if (mediaList.isEmpty()) return@rememberLauncherForActivityResult
        dataContext.setUploadBuff(mediaList)
    }
    dataContext.trainingList.value = item.toMutableList()
    LaunchedEffect(imageSelect) {
        if (imageSelect) {
            imageSelect(dataContext, context, activityResult)
        }
    }
    if (trainingDateSelect) {
        trainingDateSelect(dataContext)
    }
}
private fun imageSelect(dataContext: MyScreenTrainingCertificateVM, context: Context, activityResult: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    checkReadStorage(dataContext) {
        if (it) {
            activityResult.launch(Intent(context, MediaPickerActivity::class.java).apply {
                putExtra(FConstants.MEDIA_MAX_COUNT, 1)
            })
        }
    }
    dataContext.imageSelect.value = false
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun trainingDateSelect(dataContext: MyScreenTrainingCertificateVM) {
    val color = FThemeUtil.safeColorC()
    val datePickerState = rememberDatePickerState()
    DatePickerDialog( { dataContext.trainingDateSelect.value = false},
        { customText(CustomTextData().apply {
            text = stringResource(R.string.confirm_desc)
            textColor = color.paragraph
            modifier = Modifier.clickable {
                if (datePickerState.selectedDateMillis == null) {
                    return@clickable
                }
                datePickerState.selectedDateMillis?.let {
                    dataContext.trainingDate.value = FDateTime().setThis(it).toString("yyyy-MM-dd")
                    dataContext.trainingDateSelect.value = false
                }
            }
        })}, Modifier.background(color.background)) {
        DatePicker(datePickerState, Modifier.background(color.background))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun myScreenTrainingCertificateTwoPane(dataContext: MyScreenTrainingCertificateVM, displayFeatures: List<DisplayFeature>, onDismissRequest: () -> Unit) {
    val color = FThemeUtil.safeColorC()
    ModalBottomSheet(onDismissRequest) {
        Column(Modifier.background(color.background)) {
            topContainer(dataContext, onDismissRequest)
            uploadContainer(dataContext)
            itemListContainer(dataContext)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun myScreenTrainingCertificateTablet(dataContext: MyScreenTrainingCertificateVM, onDismissRequest: () -> Unit) {
    val color = FThemeUtil.safeColorC()
    ModalBottomSheet(onDismissRequest) {
        Column(Modifier.background(color.background)) {
            topContainer(dataContext, onDismissRequest)
            uploadContainer(dataContext)
            itemListContainer(dataContext)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun myScreenTrainingCertificatePhone(dataContext: MyScreenTrainingCertificateVM, onDismissRequest: () -> Unit) {
    val color = FThemeUtil.safeColorC()
    ModalBottomSheet(onDismissRequest) {
        Column(Modifier.fillMaxWidth().background(color.background)) {
            topContainer(dataContext, onDismissRequest)
            uploadContainer(dataContext)
            itemListContainer(dataContext)
        }
    }
}

@Composable
private fun topContainer(dataContext: MyScreenTrainingCertificateVM, onDismissRequest: () -> Unit) {
    val color = FThemeUtil.safeColorC()
    Row(Modifier.fillMaxWidth(),
        Arrangement.Center,
        Alignment.CenterVertically) {
        customText(CustomTextData().apply {
            text = stringResource(R.string.training_certificate_add)
            textColor = color.paragraph
            textAlign = TextAlign.Center
            modifier = Modifier.align(Alignment.CenterVertically).weight(1F)
        })
    }
}
@Composable
private fun uploadContainer(dataContext: MyScreenTrainingCertificateVM) {
    val color = FThemeUtil.safeColorC()
    val isSavable by dataContext.isSavable.collectAsState()
    val uploadBuff by dataContext.uploadBuff.collectAsState()
    val trainingDate by dataContext.trainingDate.collectAsState()
    Column(Modifier.fillMaxWidth()) {
        uploadBuff?.let { buff ->
            Row(Modifier.fillMaxWidth(),
                Arrangement.Center,
                Alignment.CenterVertically) {
                FCoil.load(buff.mediaPath,
                    buff.mediaFileType,
                    buff.mediaName,
                    Modifier.width(100.dp).height(100.dp).padding(10.dp),
                    ContentScale.Crop)
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = color.background
                    modifier = Modifier.clickable { dataContext.relayCommand.execute(MyScreenTrainingCertificateVM.ClickEvent.TRAINING_DATE)}
                }) {
                    customText(CustomTextData().apply {
                        text = trainingDate
                        textColor = color.foreground
                    })
                }
            }
        }
        Row(Modifier.fillMaxWidth(),
            Arrangement.Center,
            Alignment.CenterVertically) {
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = color.buttonBackground
                modifier = Modifier.clickable { dataContext.relayCommand.execute(MyScreenTrainingCertificateVM.ClickEvent.ADD)}.padding(10.dp)
            }) {
                customText(CustomTextData().apply {
                    text = stringResource(R.string.add_desc)
                    textColor = color.buttonForeground
                    modifier = Modifier.padding(5.dp)
                })
            }
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = if (isSavable) color.buttonBackground else color.disableBackGray
                modifier = Modifier.clickable { dataContext.relayCommand.execute(MyScreenTrainingCertificateVM.ClickEvent.SAVE)}.padding(10.dp)
            }) {
                customText(CustomTextData().apply {
                    text = stringResource(R.string.save_desc)
                    textColor = if (isSavable) color.buttonForeground else color.buttonBackground
                    modifier = Modifier.padding(5.dp)
                })
            }
        }
    }
}
@Composable
private fun itemListContainer(dataContext: MyScreenTrainingCertificateVM) {
    val trainingList by dataContext.trainingList.collectAsState()
    val color = FThemeUtil.safeColorC()
    LazyColumn(Modifier.fillMaxWidth()) {
        items(trainingList, { it.thisPK}) { x ->
            Row(Modifier.fillMaxWidth()) {
                FCoil.load(x.blobUrl,
                    x.mimeType,
                    x.originalFilename,
                    Modifier.height(100.dp).width(100.dp))
                Column(Modifier.align(Alignment.CenterVertically)) {
                    customText(CustomTextData().apply {
                        text = x.trainingDateString
                        textColor = color.paragraph
                    })
                    customText(CustomTextData().apply {
                        text = x.originalFilename
                        textColor = color.paragraph
                    })
                }
            }
        }
    }
}

private fun setLayoutCommand(data: Any?, dataContext: MyScreenTrainingCertificateVM, onDismissRequest: () -> Unit) {
    setThisCommand(data, dataContext, onDismissRequest)
}

private fun setThisCommand(data: Any?, dataContext: MyScreenTrainingCertificateVM, onDismissRequest: () -> Unit) {
    val eventName = data as? MyScreenTrainingCertificateVM.ClickEvent ?: return
    when (eventName) {
        MyScreenTrainingCertificateVM.ClickEvent.CLOSE -> onDismissRequest()
        MyScreenTrainingCertificateVM.ClickEvent.TRAINING_DATE -> { dataContext.trainingDateSelect.value = true }
        MyScreenTrainingCertificateVM.ClickEvent.ADD -> { dataContext.imageSelect.value = true }
        MyScreenTrainingCertificateVM.ClickEvent.SAVE ->  dataContext.startBackground()
    }
}

private fun checkReadStorage(dataContext: MyScreenTrainingCertificateVM, callback: (Boolean) -> Unit) {
    dataContext.permissionService.requestReadExternalPermissions(callback)
}