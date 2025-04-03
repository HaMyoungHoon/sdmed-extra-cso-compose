package sdmed.extra.cso.views.main.qna

import android.app.Activity
import sdmed.extra.cso.R
import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.interfaces.theme.IBaseColor
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.utils.fCoilLoad
import sdmed.extra.cso.utils.FStorage.getParcelableList
import sdmed.extra.cso.utils.fImageLoad
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.CustomTextFieldData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.customText.customTextField
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.component.vector.FVectorData
import sdmed.extra.cso.views.component.vector.vectorArrowLeft
import sdmed.extra.cso.views.component.vector.vectorCircle
import sdmed.extra.cso.views.component.vector.vectorCross
import sdmed.extra.cso.views.media.picker.MediaPickerActivity
import sdmed.extra.cso.views.theme.FThemeUtil
import java.util.ArrayList

@Composable
fun qnAScreenAdd(thisPK: String = "", title: String = stringResource(R.string.qna_upload),
                 windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                 displayFeatures: List<DisplayFeature> = emptyList(),
                 navigationType: NavigationType = NavigationType.BOTTOM,
                 onDismissRequest: () -> Unit) {
    val dataContext = fBaseScreen<QnAScreenAddVM>({ data, dataContext -> setLayoutCommand(data, dataContext, onDismissRequest) },
        { dataContext -> qnAScreenAddContent(dataContext)},
        windowPanelType, navigationType)
    dataContext.thisPK.value = thisPK
    dataContext.title.value = title
    val dismissRequest by dataContext.dismissRequest.collectAsState()
    val closable by dataContext.closeAble.collectAsState()
    addQnAFilePK(dataContext)
    BackHandler {
        if (closable) {
            dataContext.reSet()
            onDismissRequest()
        }
    }
    LaunchedEffect(dismissRequest) {
        if (dismissRequest) {
            dataContext.dismissRequest.value = false
            onDismissRequest()
        }
    }
}

@Composable
private fun qnAScreenAddContent(dataContext: QnAScreenAddVM) {
    val color = FThemeUtil.safeColorC()
    Box(Modifier.fillMaxSize().background(color.background)) {
        Column(Modifier.fillMaxWidth().padding(5.dp)) {
            topContainer(dataContext)
            contentContainer(dataContext)
            fileUploadContainer(dataContext)
        }
    }
}

@Composable
private fun topContainer(dataContext: QnAScreenAddVM) {
    val color = FThemeUtil.safeColorC()
    val title by dataContext.title.collectAsState()
    Box(Modifier) {
        Row(Modifier) {
            Icon(vectorArrowLeft(FVectorData(color.background, color.foreground)),
                stringResource(R.string.close_desc),
                Modifier.align(Alignment.CenterVertically).clickable { dataContext.relayCommand.execute(QnAScreenAddVM.ClickEvent.CLOSE) }, Color.Unspecified)
            customText(CustomTextData().apply {
                text = title
                textColor = color.foreground
                textAlign = TextAlign.Center
                modifier = Modifier.weight(1F).padding(5.dp)
            })
        }
    }
}
@Composable
private fun contentContainer(dataContext: QnAScreenAddVM) {
    val color = FThemeUtil.safeColorC()
    val thisPK by dataContext.thisPK.collectAsState()
    val postTitle by dataContext.postTitle.collectAsState()
    val content by dataContext.content.collectAsState()
    val uploadBuff by dataContext.uploadItems.collectAsState()
    LaunchedEffect(postTitle, content, uploadBuff) {
        if (postTitle.isNotEmpty() && content.isNotEmpty()) {
            dataContext.isSavable.value = true
        } else if (postTitle.isNotEmpty() && uploadBuff.isNotEmpty()) {
            dataContext.isSavable.value = true
        } else if (thisPK.isNotEmpty() && content.isNotEmpty()) {
            dataContext.isSavable.value = true
        } else if (thisPK.isNotEmpty() && uploadBuff.isNotEmpty()) {
            dataContext.isSavable.value = true
        } else {
            dataContext.isSavable.value = false
        }
    }
    Box(Modifier) {
        Column(Modifier) {
            if (thisPK.isEmpty()) {
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = color.background
                    borderColor = color.primary
                    borderSize = 1.dp
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp, start = 16.dp, end = 16.dp)
                }) {
                    customTextField(CustomTextFieldData().apply {
                        text = postTitle
                        modifier = Modifier.padding(10.dp)
                        onValueChange = {
                            if (it.length <= 20) {
                                dataContext.postTitle.value = it
                            }
                        }
                        decorationBox = { titleDecorationBox(it, postTitle, color) }
                    })
                }
            }
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = color.background
                borderColor = color.primary
                borderSize = 1.dp
                modifier = Modifier.fillMaxWidth().height(200.dp).padding(top = 10.dp, start = 16.dp, end = 16.dp)
            }) {
                customTextField(CustomTextFieldData().apply {
                    text = content
                    modifier = Modifier.fillMaxSize().padding(10.dp)
                    maxLines = 10
                    onValueChange = {
                        if (it.length <= 500) {
                            dataContext.content.value = it
                        }
                    }
                    imeAction = ImeAction.Next
                    decorationBox = { contentDecorationBox(it, content, color) }
                })
            }
        }
    }
}
@Composable
private fun fileUploadContainer(dataContext: QnAScreenAddVM) {
    val color = FThemeUtil.safeColorC()
    val uploadBuff by dataContext.uploadItems.collectAsState()
    val isSavable by dataContext.isSavable.collectAsState()
    Box(Modifier) {
        Column(Modifier.fillMaxWidth()) {
            if (uploadBuff.isNotEmpty()) {
                LazyRow(Modifier.padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)) {
                    items(uploadBuff, { it.thisPK }) { item ->
                        uploadItemContainer(dataContext, item)
                    }
                }
            }
            Row(Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                Arrangement.SpaceEvenly) {
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = color.buttonBackground
                    modifier = Modifier.align(Alignment.CenterVertically).padding(5.dp).clickable { dataContext.relayCommand.execute(QnAScreenAddVM.ClickEvent.ADD) }
                }) {
                    customText(CustomTextData().apply {
                        text = stringResource(R.string.add_file_desc)
                        textColor = color.buttonForeground
                        modifier = Modifier.align(Alignment.Center).padding(5.dp)
                    })
                }
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = if (isSavable) color.buttonBackground else color.disableBackGray
                    modifier = Modifier.align(Alignment.CenterVertically).padding(5.dp).clickable { dataContext.relayCommand.execute(QnAScreenAddVM.ClickEvent.SAVE) }
                }) {
                    customText(CustomTextData().apply {
                        text = stringResource(R.string.save_desc)
                        textColor = if (isSavable) color.buttonForeground else color.disableForeGray
                        modifier = Modifier.align(Alignment.Center).padding(5.dp)
                    })
                }
            }
        }
    }
}
@Composable
private fun uploadItemContainer(dataContext: QnAScreenAddVM, item: MediaPickerSourceModel) {
    val color = FThemeUtil.safeColorC()
    item.relayCommand = dataContext.relayCommand
    Box(Modifier) {
        shapeRoundedBox(ShapeRoundedBoxData().apply {
            backgroundColor = color.transparent
        }) {
            Box(Modifier.align(Alignment.TopEnd).zIndex(100F).clickable { item.onClick(MediaPickerSourceModel.ClickEvent.SELECT) },
                contentAlignment = Alignment.Center) {
                Icon(vectorCircle(FVectorData(color.background, color.primary)),
                    stringResource(R.string.remove_desc),
                    Modifier, Color.Unspecified)
                Icon(vectorCross(FVectorData(color.background, color.primary)),
                    stringResource(R.string.remove_desc),
                    Modifier, Color.Unspecified)
            }
            fImageLoad(item.mediaUrl,
                item.mediaFileType,
                item.mediaName,
                Modifier.width(100.dp).height(100.dp).padding(10.dp),
                ContentScale.Crop)
        }
    }
}

@Composable
private fun addQnAFilePK(dataContext: QnAScreenAddVM) {
    val context = LocalContext.current
    val imageSelect by dataContext.imageSelect.collectAsState()
    val activityResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode != Activity.RESULT_OK) {
            return@rememberLauncherForActivityResult
        }
        val mediaList = it.data?.getParcelableList<MediaPickerSourceModel>(FConstants.MEDIA_LIST) ?: return@rememberLauncherForActivityResult
        if (mediaList.isEmpty()) return@rememberLauncherForActivityResult
        dataContext.reSetImage(mediaList)
    }

    LaunchedEffect(imageSelect) {
        if (imageSelect) {
            addQnAFileSelect(dataContext, context, activityResult)
        }
    }
}
private fun addQnAFileSelect(dataContext: QnAScreenAddVM, context: Context, activityResult: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    val imageSelect = dataContext.imageSelect.value
    if (!imageSelect) {
        return
    }
    checkReadStorage(dataContext) {
        if (it) {
            activityResult.launch(Intent(context, MediaPickerActivity::class.java))
        }
    }
    dataContext.imageSelect.value = false
}

@Composable
private fun titleDecorationBox(innerTextField: @Composable () -> Unit, text: String?, color: IBaseColor) {
    if (text.isNullOrEmpty()) {
        customText(CustomTextData().apply {
            this.text = stringResource(R.string.qna_add_title_hint_desc)
            textColor = color.disableForeGray
            modifier = Modifier.fillMaxWidth()
        })
    } else {
        Column(Modifier.fillMaxWidth()) {
            innerTextField()
        }
    }
}
@Composable
private fun contentDecorationBox(innerTextField: @Composable () -> Unit, text: String?, color: IBaseColor) {
    if (text.isNullOrEmpty()) {
        customText(CustomTextData().apply {
            this.text = stringResource(R.string.qna_add_content_hint_desc)
            textColor = color.disableForeGray
            modifier = Modifier.fillMaxWidth()
        })
    } else {
        Column(Modifier.fillMaxWidth()) {
            innerTextField()
        }
    }
}

private fun setLayoutCommand(data: Any?, dataContext: QnAScreenAddVM, onDismissRequest: () -> Unit) {
    setThisCommand(data, dataContext, onDismissRequest)
    setQnAFileUploadCommand(data, dataContext)
}
private fun setThisCommand(data: Any?, dataContext: QnAScreenAddVM, onDismissRequest: () -> Unit) {
    val eventName = data as? QnAScreenAddVM.ClickEvent ?: return
    when (eventName) {
        QnAScreenAddVM.ClickEvent.CLOSE -> dismiss(dataContext, onDismissRequest)
        QnAScreenAddVM.ClickEvent.ADD -> imageAdd(dataContext)
        QnAScreenAddVM.ClickEvent.SAVE -> save(dataContext)
    }
}
private fun setQnAFileUploadCommand(data: Any?, dataContext: QnAScreenAddVM) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? MediaPickerSourceModel.ClickEvent ?: return
    val dataBuff = data[1] as? MediaPickerSourceModel ?: return
    when (eventName) {
        MediaPickerSourceModel.ClickEvent.SELECT -> dataContext.removeImage(dataBuff)
        MediaPickerSourceModel.ClickEvent.SELECT_LONG -> dataContext.removeImage(dataBuff)
    }
}

private fun dismiss(dataContext: QnAScreenAddVM, onDismissRequest: () -> Unit) {
    dataContext.reSet()
    onDismissRequest()
}
private fun imageAdd(dataContext: QnAScreenAddVM) {
    dataContext.imageSelect.value = true
}
private fun save(dataContext: QnAScreenAddVM) {
    if (!dataContext.isSavable.value) {
        return
    }

    dataContext.startBackgroundService()
    dataContext.toast(dataContext.context.getString(R.string.qna_upload))
    dataContext.loading()
}

private fun checkReadStorage(dataContext: QnAScreenAddVM, callback: (Boolean) -> Unit) {
    dataContext.permissionService.requestReadExternalPermissions(callback)
}