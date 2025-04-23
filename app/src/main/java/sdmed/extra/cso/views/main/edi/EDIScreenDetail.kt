package sdmed.extra.cso.views.main.edi

import sdmed.extra.cso.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.common.MediaViewParcelModel
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaFileModel
import sdmed.extra.cso.models.retrofit.edi.ExtraEDIListResponse
import sdmed.extra.cso.models.retrofit.edi.ExtraEDIPharma
import sdmed.extra.cso.models.retrofit.edi.ExtraEDIResponse
import sdmed.extra.cso.utils.fCoilLoad
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FStorage.getParcelableList
import sdmed.extra.cso.utils.FStorage.putParcelable
import sdmed.extra.cso.utils.FStorage.putParcelableList
import sdmed.extra.cso.utils.fImageLoad
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.component.vector.FVectorData
import sdmed.extra.cso.views.component.vector.vectorArrowLeft
import sdmed.extra.cso.views.component.vector.vectorCircle
import sdmed.extra.cso.views.component.vector.vectorCross
import sdmed.extra.cso.views.hospitalMap.hospitalTempDetail.HospitalTempDetailActivity
import sdmed.extra.cso.views.media.listView.MediaListViewActivity
import sdmed.extra.cso.views.media.picker.MediaPickerActivity
import sdmed.extra.cso.views.media.singleView.MediaViewActivity
import sdmed.extra.cso.views.theme.FThemeUtil
import java.util.ArrayList

@Composable
fun ediScreenDetail(selectedItem: ExtraEDIListResponse,
                    windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                    displayFeatures: List<DisplayFeature> = emptyList(),
                    navigationType: NavigationType = NavigationType.BOTTOM,
                    onDismissRequest: () -> Unit) {
    val dataContext = fBaseScreen<EDIScreenDetailVM>( { data, dataContext -> setLayoutCommand(data, dataContext, onDismissRequest) },
        null,
        windowPanelType, navigationType,
        { dataContext -> ediScreenDetailTwoPane(dataContext, displayFeatures) },
        { dataContext -> ediScreenDetailPhone(dataContext) },
        { dataContext -> ediScreenDetailTablet(dataContext) })
    val closeAble by dataContext.closeAble.collectAsState()
    val thisPK by dataContext.thisPK.collectAsState()
    dataContext.thisPK.value = selectedItem.thisPK
    addPharmaFilePK(dataContext)
    hospitalTempDetail(dataContext)
    BackHandler() {
        if (closeAble) {
            onDismissRequest()
            dataContext.reSet()
        }
    }
    LaunchedEffect(thisPK) {
        if (thisPK.isNotEmpty()) {
            getData(dataContext)
        }
    }
}

@Composable
private fun ediScreenDetailTwoPane(dataContext: EDIScreenDetailVM, displayFeatures: List<DisplayFeature>) {
    val color = FThemeUtil.safeColorC()
    Box(Modifier.background(color.background).fillMaxWidth()) {
        Column(Modifier.fillMaxWidth()) {
            topContainer(dataContext)
            pharmaListContainer(dataContext)
            responseListContainer(dataContext)
        }
    }
}
@Composable
private fun ediScreenDetailPhone(dataContext: EDIScreenDetailVM) {
    val color = FThemeUtil.safeColorC()
    shapeRoundedBox(ShapeRoundedBoxData().apply {
        backgroundColor = color.background
        borderColor = color.primary
        borderSize = 1.dp
        modifier = Modifier.padding(5.dp).fillMaxWidth()
    }) {
        Column(Modifier.fillMaxWidth()) {
            topContainer(dataContext, false)
            pharmaListContainer(dataContext, false)
            responseListContainer(dataContext, false)
        }
    }
}
@Composable
private fun ediScreenDetailTablet(dataContext: EDIScreenDetailVM) {
    val color = FThemeUtil.safeColorC()
    shapeRoundedBox(ShapeRoundedBoxData().apply {
        backgroundColor = color.background
        borderColor = color.primary
        borderSize = 1.dp
        modifier = Modifier.padding(5.dp).fillMaxWidth()
    }) {
        Column(Modifier.align(Alignment.Center)) {
            topContainer(dataContext)
            pharmaListContainer(dataContext)
            responseListContainer(dataContext)
        }
    }
}

@Composable
private fun topContainer(dataContext: EDIScreenDetailVM, isWide: Boolean = true) {
    val color = FThemeUtil.safeColorC()
    val item by dataContext.item.collectAsState()
    item.relayCommand = dataContext.relayCommand
    Box(Modifier.padding(10.dp)) {
        val rowModifier = if (isWide) Modifier.align(Alignment.Center) else Modifier.fillMaxWidth()
        Row(rowModifier) {
            Icon(vectorArrowLeft(FVectorData(color.background, color.foreground)),
                stringResource(R.string.close_desc), Modifier.clickable { dataContext.relayCommand.execute(EDIScreenDetailVM.ClickEvent.CLOSE) },
                Color.Unspecified)
            customText(CustomTextData().apply {
                text = item.orgViewName
                textColor = if (item.tempHospitalPK.isEmpty()) color.paragraph else color.septenary
                textAlign = TextAlign.Center
                modifier = if (item.tempHospitalPK.isNotEmpty()) {
                    Modifier.weight(1F).clickable { dataContext.relayCommand.execute(EDIScreenDetailVM.ClickEvent.HOSPITAL_DETAIL) }
                } else {
                    Modifier.weight(1F)
                }
            })
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = item.getEdiBackColor()
            }) {
                customText(CustomTextData().apply {
                    text = item.ediState.desc
                    textColor = item.getEdiColor()
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
                })
            }
        }
    }
}
@Composable
private fun pharmaListContainer(dataContext: EDIScreenDetailVM, isWide: Boolean = true) {
    val item by dataContext.item.collectAsState()
    Box(Modifier) {
        val modifier = if (isWide) Modifier.align(Alignment.Center) else Modifier.fillMaxWidth()
        LazyColumn(modifier) {
            items(item.pharmaList, { it.thisPK }) { item ->
                pharmaItemContainer(dataContext, item, isWide)
            }
        }
    }
}
@Composable
private fun pharmaItemContainer(dataContext: EDIScreenDetailVM, item: ExtraEDIPharma, isWide: Boolean = true) {
    val color = FThemeUtil.safeColorC()
    val isOpen by item.isOpen.collectAsState()
    item.relayCommand = dataContext.relayCommand
    shapeRoundedBox(ShapeRoundedBoxData().apply {
        backgroundColor = color.cardBackground
        modifier = Modifier.padding(10.dp)
    }) {
        Column(Modifier.padding(5.dp).align(Alignment.Center)) {
            Row(Modifier.fillMaxWidth().clickable { item.onClick(ExtraEDIPharma.ClickEvent.OPEN) }) {
                if (item.isCarriedOver) {
                    customText(CustomTextData().apply {
                        text = stringResource(R.string.carried_over_desc)
                        textColor = color.paragraph
                    })
                }
                customText(CustomTextData().apply {
                    text = item.getYearMonth()
                    textColor = color.foreground
                })
                customText(CustomTextData().apply {
                    text = item.orgName
                    textColor = color.paragraph
                    modifier = Modifier.weight(1F).padding(start = 5.dp)
                })
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = item.ediState.parseEDIBackColor()
                }) {
                    customText(CustomTextData().apply {
                        text = item.ediState.desc
                        textColor = item.ediState.parseEDIColor()
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                    })
                }
            }
            if (isOpen) {
                pharmaFileContainer(dataContext, item.fileList, isWide)
                if (item.isAddable) {
                    pharmaFileUploadContainer(dataContext, item)
                }
            }
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun pharmaFileContainer(dataContext: EDIScreenDetailVM, items: MutableList<EDIUploadPharmaFileModel>, isWide: Boolean = true) {
    val color = FThemeUtil.safeColorC()
    val pagerState = rememberPagerState(pageCount = { items.size })
    var scrollEnabled by remember { mutableStateOf(true) }
    HorizontalPager(pagerState, Modifier.padding(top = 10.dp).fillMaxSize(), userScrollEnabled = scrollEnabled) { page ->
        val item = items.getOrNull(page) ?: items.getOrNull(0) ?: return@HorizontalPager
        item.relayCommand = dataContext.relayCommand
        Column(Modifier) {
            val imageModifier = if (isWide) Modifier.fillMaxWidth() else Modifier.height(300.dp).fillMaxWidth()
            fCoilLoad(item.blobUrl,
                item.mimeType,
                item.originalFilename,
                imageModifier.combinedClickable(onClick = { item.onClick(EDIUploadPharmaFileModel.ClickEvent.SHORT) },
                    onLongClick = { item.onClick(EDIUploadPharmaFileModel.ClickEvent.LONG) }),
                ContentScale.FillWidth)
            customText(CustomTextData().apply {
                text = "${page + 1}/${items.size}"
                textColor = color.foreground
                textAlign = TextAlign.Center
                modifier = Modifier.fillMaxWidth()
            })
        }
    }
}
@Composable
private fun pharmaFileUploadContainer(dataContext: EDIScreenDetailVM, item: ExtraEDIPharma) {
    val ediUploadItem by dataContext.item.collectAsState()
    if (!ediUploadItem.ediState.isEditable()) {
        return
    }
    val color = FThemeUtil.safeColorC()
    val uploadItems by item.uploadItems.collectAsState()
    val isSavable by item.isSavable.collectAsState()
    Box(Modifier) {
        Column(Modifier.fillMaxWidth()) {
            if (uploadItems.isNotEmpty()) {
                LazyRow(Modifier.padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)) {
                    items(uploadItems, { it.thisPK }) { item ->
                        pharmaFileUploadItemContainer(dataContext, item)
                    }
                }
            }
            Row(Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                Arrangement.SpaceEvenly) {
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = color.buttonBackground
                    modifier = Modifier.align(Alignment.CenterVertically).padding(5.dp).clickable { item.onClick(ExtraEDIPharma.ClickEvent.ADD) }
                }) {
                    customText(CustomTextData().apply {
                        text = stringResource(R.string.add_file_desc)
                        textColor = color.buttonForeground
                        modifier = Modifier.align(Alignment.Center).padding(5.dp)
                    })
                }
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = if (isSavable) color.buttonBackground else color.disableBackGray
                    modifier = Modifier.align(Alignment.CenterVertically).padding(5.dp).clickable { item.onClick(ExtraEDIPharma.ClickEvent.SAVE) }
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
private fun pharmaFileUploadItemContainer(dataContext: EDIScreenDetailVM, item: MediaPickerSourceModel) {
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
            fImageLoad(item.mediaUri,
                item.mediaFileType,
                item.mediaName,
                Modifier.width(100.dp).height(100.dp).padding(10.dp),
                ContentScale.Crop)
        }
    }
}
@Composable
private fun responseListContainer(dataContext: EDIScreenDetailVM, isWide: Boolean = true) {
    val color = FThemeUtil.safeColorC()
    val item by dataContext.item.collectAsState()
    Box(Modifier) {
        LazyColumn(Modifier) {
            items(item.responseList, { it.thisPK }) { item ->
                responseItemContainer(dataContext, item)
            }
        }
    }
}
@Composable
private fun responseItemContainer(dataContext: EDIScreenDetailVM, item: ExtraEDIResponse) {
    val color = FThemeUtil.safeColorC()
    val isOpen by item.isOpen.collectAsState()
    item.relayCommand = dataContext.relayCommand
    shapeRoundedBox(ShapeRoundedBoxData().apply {
        backgroundColor = color.quinary
        modifier = Modifier.padding(10.dp)
    }) {
        Column(Modifier.padding(5.dp).clickable { item.onClick(ExtraEDIResponse.ClickEvent.OPEN) }) {
            Row(Modifier) {
                customText(CustomTextData().apply {
                    text = item.getResponseDate()
                    textColor = color.foreground
                    modifier = Modifier.align(Alignment.CenterVertically)
                })
                customText(CustomTextData().apply {
                    text = item.pharmaName
                    textColor = color.paragraph
                    modifier = Modifier.weight(1F).align(Alignment.CenterVertically).padding(start = 5.dp)
                })
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = item.ediState.parseEDIBackColor()
                }) {
                    customText(CustomTextData().apply {
                        text = item.ediState.desc
                        textColor = item.ediState.parseEDIColor()
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
                    })
                }
            }
            if (isOpen) {
                customText(CustomTextData().apply {
                    text = item.etc
                    textColor = color.foreground
                    maxLines = 3
                    overflow = TextOverflow.Ellipsis
                })
            }
        }
    }
}


@Composable
private fun addPharmaFilePK(dataContext: EDIScreenDetailVM) {
    val context = LocalContext.current
    val addPharmaFilePK by dataContext.addPharmaFilePK.collectAsState()
    val activityResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode != Activity.RESULT_OK) {
            return@rememberLauncherForActivityResult
        }
        val pharmaBuffPK = it.data?.getStringExtra(FConstants.MEDIA_TARGET_PK) ?: return@rememberLauncherForActivityResult
        val mediaList = it.data?.getParcelableList<MediaPickerSourceModel>(FConstants.MEDIA_LIST) ?: return@rememberLauncherForActivityResult
        if (pharmaBuffPK.isEmpty()) return@rememberLauncherForActivityResult
        if (mediaList.isEmpty()) return@rememberLauncherForActivityResult
        dataContext.reSetImage(pharmaBuffPK, mediaList)
    }
    LaunchedEffect(addPharmaFilePK) {
        if (addPharmaFilePK != null) {
            addPharmaFileSelect(dataContext, context, activityResult)
        }
    }
}

private fun addPharmaFileSelect(dataContext: EDIScreenDetailVM, context: Context, activityResult: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    val addPharmaFilePK = dataContext.addPharmaFilePK.value
    if (addPharmaFilePK == null) {
        return
    }

    checkReadStorage(dataContext) {
        if (it) {
            activityResult.launch(Intent(context, MediaPickerActivity::class.java).apply {
                putExtra(FConstants.MEDIA_TARGET_PK, addPharmaFilePK)
            })
        }
    }
    dataContext.addPharmaFilePK.value = null
}

@Composable
private fun hospitalTempDetail(dataContext: EDIScreenDetailVM) {
    val context = LocalContext.current
    val hospitalTempDetail by dataContext.hospitalTempDetail.collectAsState()
    val activityResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }
    LaunchedEffect(hospitalTempDetail) {
        if (hospitalTempDetail) {
            hospitalTempDetail(dataContext, context, activityResult)
        }
    }
}
private fun hospitalTempDetail(dataContext: EDIScreenDetailVM, context: Context, activityResult: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    val hospitalTempDetail = dataContext.hospitalTempDetail.value
    if (!hospitalTempDetail) {
        return
    }
    val tempHospitalPK = dataContext.item.value.tempHospitalPK
    if (tempHospitalPK.isEmpty()) {
        dataContext.hospitalTempDetail.value = false
        return
    }

    activityResult.launch(Intent(context, HospitalTempDetailActivity::class.java).apply {
        putExtra(FConstants.HOSPITAL_PK, tempHospitalPK)
    })

    dataContext.hospitalTempDetail.value = false
}

private fun setLayoutCommand(data: Any?, dataContext: EDIScreenDetailVM, onDismissRequest: () -> Unit) {
    setThisCommand(data, dataContext, onDismissRequest)
    setEDIPharmaCommand(data, dataContext)
    setEDIPharmaFileCommand(data, dataContext)
    setEDIResponseCommand(data)
    setEDIPharmaFileUploadCommand(data, dataContext)
}
private fun setThisCommand(data: Any?, dataContext: EDIScreenDetailVM, onDismissRequest: () -> Unit) {
    val eventName = data as? EDIScreenDetailVM.ClickEvent ?: return
    when (eventName) {
        EDIScreenDetailVM.ClickEvent.CLOSE -> dismiss(dataContext, onDismissRequest)
        EDIScreenDetailVM.ClickEvent.HOSPITAL_DETAIL -> hospitalDetail(dataContext)
    }
}
private fun setEDIPharmaCommand(data: Any?, dataContext: EDIScreenDetailVM) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? ExtraEDIPharma.ClickEvent ?: return
    val dataBuff = data[1] as? ExtraEDIPharma ?: return
    when (eventName) {
        ExtraEDIPharma.ClickEvent.OPEN -> dataBuff.isOpen.value = !dataBuff.isOpen.value
        ExtraEDIPharma.ClickEvent.ADD -> addPharmaFile(dataContext, dataBuff)
        ExtraEDIPharma.ClickEvent.SAVE -> savePharmaFile(dataContext, dataBuff)
    }
}
private fun setEDIPharmaFileCommand(data: Any?, dataContext: EDIScreenDetailVM) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? EDIUploadPharmaFileModel.ClickEvent ?: return
    val dataBuff = data[1] as? EDIUploadPharmaFileModel ?: return
    when (eventName) {
        EDIUploadPharmaFileModel.ClickEvent.LONG -> pharmaFileLongSelect(dataContext, dataBuff)
        EDIUploadPharmaFileModel.ClickEvent.SHORT -> pharmaFileShortSelect(dataContext, dataBuff)
    }
}
private fun setEDIPharmaFileUploadCommand(data: Any?, dataContext: EDIScreenDetailVM) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? MediaPickerSourceModel.ClickEvent ?: return
    val dataBuff = data[1] as? MediaPickerSourceModel ?: return
    when (eventName) {
        MediaPickerSourceModel.ClickEvent.SELECT -> dataContext.delImage(dataBuff.thisPK)
        MediaPickerSourceModel.ClickEvent.SELECT_LONG -> dataContext.delImage(dataBuff.thisPK)
    }
}
private fun setEDIResponseCommand(data: Any?) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? ExtraEDIResponse.ClickEvent ?: return
    val dataBuff = data[1] as? ExtraEDIResponse ?: return
    when (eventName) {
        ExtraEDIResponse.ClickEvent.OPEN -> dataBuff.isOpen.value = !dataBuff.isOpen.value
    }
}

private fun getData(dataContext: EDIScreenDetailVM) {
    dataContext.loading()
    FCoroutineUtil.coroutineScope({
        val ret = dataContext.getData()
        dataContext.loading(false)
        if (ret.result != true) {
            dataContext.toast(ret.msg)
        }
    })
}
private fun dismiss(dataContext: EDIScreenDetailVM, onDismissRequest: () -> Unit) {
    onDismissRequest()
    dataContext.reSet()
}
private fun hospitalDetail(dataContext: EDIScreenDetailVM) {
    dataContext.hospitalTempDetail.value = true
}

private fun addPharmaFile(dataContext: EDIScreenDetailVM, dataBuff: ExtraEDIPharma) {
    if (!dataBuff.isAddable) {
        return
    }
    checkExternalStorage(dataContext)
    checkReadStorage(dataContext) {
        dataContext.addPharmaFilePK.value = dataBuff.thisPK
    }
}
private fun savePharmaFile(dataContext: EDIScreenDetailVM, dataBuff: ExtraEDIPharma) {
    if (!dataBuff.isAddable) {
        return
    }
    if (!dataBuff.isSavable.value) {
        return
    }
    dataContext.startBackgroundService(dataBuff)
    dataContext.toast(dataContext.context.getString(R.string.edi_file_upload))
    dataContext.loading()
}
private fun pharmaFileLongSelect(dataContext: EDIScreenDetailVM, dataBuff: EDIUploadPharmaFileModel) {
    val context = dataContext.context
    context.startActivity(Intent(context, MediaListViewActivity::class.java).apply {
        putParcelableList(FConstants.MEDIA_LIST, dataContext.getMediaViewPharmaFiles(dataBuff))
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}
private fun pharmaFileShortSelect(dataContext: EDIScreenDetailVM, dataBuff: EDIUploadPharmaFileModel) {
    val context = dataContext.context
    context.startActivity(Intent(context, MediaViewActivity::class.java).apply {
        putParcelable(FConstants.MEDIA_ITEM, MediaViewParcelModel().parse(dataBuff))
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}
private fun checkExternalStorage(dataContext: EDIScreenDetailVM) {
    dataContext.permissionService.externalStorage()
}
private fun checkReadStorage(dataContext: EDIScreenDetailVM, callback: (Boolean) -> Unit) {
    dataContext.permissionService.requestReadExternalPermissions(callback)
}