package sdmed.extra.cso.views.main.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import sdmed.extra.cso.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.interfaces.theme.IBaseColor
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.menu.MenuItem
import sdmed.extra.cso.models.menu.MenuList
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.models.retrofit.edi.EDIApplyDateModel
import sdmed.extra.cso.models.retrofit.edi.EDIHosBuffModel
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaBuffModel
import sdmed.extra.cso.utils.fCoilLoad
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FStorage.getParcelableList
import sdmed.extra.cso.utils.fImageLoad
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.CustomTextFieldData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.customText.customTextField
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.component.vector.FVectorData
import sdmed.extra.cso.views.component.vector.vectorArrowDown
import sdmed.extra.cso.views.component.vector.vectorArrowUp
import sdmed.extra.cso.views.component.vector.vectorCircle
import sdmed.extra.cso.views.component.vector.vectorCross
import sdmed.extra.cso.views.media.picker.MediaPickerActivity
import sdmed.extra.cso.views.theme.FThemeUtil
import java.util.ArrayList

@Composable
fun homeEDIRequestScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                         displayFeatures: List<DisplayFeature> = emptyList(),
                         navigationType: NavigationType = NavigationType.BOTTOM,
                         navigate: (MenuItem, Boolean) -> Unit) {
    var navigateCalled by remember { mutableStateOf(false) }
    val dataContext = fBaseScreen<HomeEDIRequestScreenVM>( { data, dataContext -> setLayoutCommand(data, dataContext) },
        null,
        windowPanelType, navigationType,
        { dataContext -> homeEDIRequestScreenTwoPane(dataContext, displayFeatures) },
        { dataContext -> homeEDIRequestScreenPhone(dataContext) },
        { dataContext -> homeEDIRequestScreenTablet(dataContext) })
    val navigateEDIList by dataContext.navigateEDIList.collectAsState()
    LaunchedEffect(navigateCalled) {
        if (!navigateCalled) {
            navigateCalled = true
            getData(dataContext)
        }
    }

    addPharmaFilePK(dataContext)
    if (navigateEDIList) {
        navigate(MenuList.menuEDI(), false)
    }
}

@Composable
private fun homeEDIRequestScreenTwoPane(dataContext: HomeEDIRequestScreenVM, displayFeatures: List<DisplayFeature>) {
    val pharmaItems by dataContext.pharmaModel.collectAsState()
    val color = FThemeUtil.safeColorC()
    if (pharmaItems.isEmpty()) {
        Box(Modifier.fillMaxSize().background(color.background)) {
            Column {
                ediDateContainer(dataContext)
                ediHospitalContainer(dataContext, true)
            }
        }
    } else {
        TwoPane({
            Box(Modifier.fillMaxSize().background(color.background)) {
                Column {
                    ediDateContainer(dataContext)
                    ediHospitalContainer(dataContext, true)
                }
            }
        }, {
            Box(Modifier.fillMaxSize().background(color.background)) {
                Column {
                    ediPharmaContainer(dataContext, true)
                }
            }
        },
            HorizontalTwoPaneStrategy(0.5F, 16.dp),
            displayFeatures)
    }
}
@Composable
private fun homeEDIRequestScreenPhone(dataContext: HomeEDIRequestScreenVM) {
    val color = FThemeUtil.safeColorC()
    val scrollState = rememberScrollState()
    Box(Modifier.fillMaxSize().background(color.background).verticalScroll(scrollState)) {
        Column {
            ediDateContainer(dataContext)
            ediHospitalContainer(dataContext)
            ediPharmaContainer(dataContext)
        }
    }
}
@Composable
private fun homeEDIRequestScreenTablet(dataContext: HomeEDIRequestScreenVM) {
    val color = FThemeUtil.safeColorC()
    val scrollState = rememberScrollState()
    Box(Modifier.fillMaxSize().background(color.background).verticalScroll(scrollState)) {
        Column {
            ediDateContainer(dataContext)
            ediHospitalContainer(dataContext)
            ediPharmaContainer(dataContext)
        }
    }
}

@Composable
private fun ediDateContainer(dataContext: HomeEDIRequestScreenVM) {
    val color = FThemeUtil.safeColorC()
    val applyDateModel by dataContext.applyDateModel.collectAsState()
    val selectApplyDate by dataContext.selectApplyDate.collectAsState()
    val isSavable by dataContext.isSavable.collectAsState()
    Box(Modifier.fillMaxWidth().padding(5.dp)) {
        Row(Modifier.fillMaxWidth()) {
            Column(Modifier.weight(1F).align(Alignment.CenterVertically)) {
                customText(CustomTextData().apply {
                    text = stringResource(R.string.edi_date_select_desc)
                    textColor = color.foreground
                    textAlign = TextAlign.Center
                    modifier = Modifier.fillMaxWidth()
                })
                LazyRow(Modifier.align(Alignment.CenterHorizontally)) {
                    items(applyDateModel, { it.thisPK }) { item ->
                        item.relayCommand = dataContext.relayCommand
                        shapeRoundedBox(ShapeRoundedBoxData().apply {
                            backgroundColor = if (selectApplyDate?.thisPK == item.thisPK) color.onPrimary else color.disableBackGray
                            modifier = Modifier.padding(5.dp).clickable { item.onClick(EDIApplyDateModel.ClickEvent.THIS) }
                        }) {
                            customText(CustomTextData().apply {
                                text = item.yearMonth
                                textColor = if (selectApplyDate?.thisPK == item.thisPK) color.primary else color.disableForeGray
                                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
                            })
                        }
                    }
                }
            }
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = if (isSavable) color.buttonBackground else color.disableBackGray
                modifier = Modifier.padding(5.dp).align(Alignment.CenterVertically)
                    .clickable { dataContext.relayCommand.execute(HomeEDIRequestScreenVM.ClickEvent.SAVE) }
            }) {
                customText(CustomTextData().apply {
                    text = stringResource(R.string.save_desc)
                    textColor = if (isSavable) color.buttonForeground else color.disableForeGray
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
                })
            }
        }
    }
}
@Composable
private fun ediHospitalContainer(dataContext: HomeEDIRequestScreenVM, isTwoPane: Boolean = false) {
    val color = FThemeUtil.safeColorC()
    val hospitalOpen by dataContext.hospitalOpen.collectAsState()
    val hospitalModel by dataContext.hospitalModel.collectAsState()
    val selectHospital by dataContext.selectHospital.collectAsState()
    shapeRoundedBox(ShapeRoundedBoxData().apply {
        backgroundColor = color.background
        borderColor = color.primary
        borderSize = 1.dp
        modifier = Modifier.fillMaxWidth().padding(5.dp)
    }) {
        Column(Modifier.align(Alignment.Center)) {
            Row(Modifier.clickable { dataContext.relayCommand.execute(HomeEDIRequestScreenVM.ClickEvent.HOSPITAL_OPEN) }) {
                customText(CustomTextData().apply {
                    text = stringResource(R.string.edi_hospital_select_desc)
                    textColor = color.foreground
                    textAlign = TextAlign.Center
                    modifier = Modifier.padding(10.dp).weight(1F).align(Alignment.CenterVertically)
                })
                if (hospitalOpen) {
                    Icon(vectorArrowDown(FVectorData(color.background, color.foreground)),
                        stringResource(R.string.open_desc),
                        Modifier.padding(end = 5.dp).align(Alignment.CenterVertically),
                        Color.Unspecified)
                } else {
                    Icon(vectorArrowUp(FVectorData(color.background, color.foreground)),
                        stringResource(R.string.close_desc),
                        Modifier.padding(end = 5.dp).align(Alignment.CenterVertically),
                        Color.Unspecified)
                }
            }
            if (hospitalOpen) {
                val lazyModifier = if (isTwoPane) Modifier.fillMaxSize() else Modifier.fillMaxWidth().heightIn(0.dp, 300.dp)
                LazyColumn(lazyModifier) {
                    items(hospitalModel, { it.thisPK }) { item ->
                        item.relayCommand = dataContext.relayCommand
                        shapeRoundedBox(ShapeRoundedBoxData().apply {
                            backgroundColor = if (selectHospital?.thisPK == item.thisPK) color.onPrimary else color.disableBackGray
                            modifier = Modifier.fillMaxWidth().padding(5.dp).clickable { item.onClick(EDIHosBuffModel.ClickEvent.THIS)}
                        }) {
                            customText(CustomTextData().apply {
                                text = item.orgName
                                textColor = if(selectHospital?.thisPK == item.thisPK) color.primary else color.disableForeGray
                                maxLines = 2
                                overflow = TextOverflow.Ellipsis
                                modifier = Modifier.fillMaxSize().padding(10.dp)
                            })
                        }
                    }
                }
            }
        }
    }
}
@Composable
private fun ediPharmaContainer(dataContext: HomeEDIRequestScreenVM, isTwoPane: Boolean = false) {
    val color = FThemeUtil.safeColorC()
    val pharmaViewModel by dataContext.pharmaViewModel.collectAsState()
    val searchString by dataContext.searchString.collectAsState()
    shapeRoundedBox(ShapeRoundedBoxData().apply {
        backgroundColor = color.background
        borderColor = color.primary
        borderSize = 1.dp
        modifier = Modifier.fillMaxWidth().padding(5.dp)
    }) {
        Column(Modifier.align(Alignment.Center)) {
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = color.background
                borderColor = color.primary
                borderSize = 1.dp
                modifier = Modifier.fillMaxWidth().padding(5.dp)
            }) {
                customTextField(CustomTextFieldData().apply {
                    text = searchString
                    modifier = Modifier.padding(10.dp)
                    onValueChange = {
                        dataContext.searchString.value = it
                    }
                    decorationBox = { searchDecorationBox(it, searchString, color) }
                })
            }
            val lazyModifier = if (isTwoPane) Modifier.fillMaxSize() else Modifier.fillMaxWidth().heightIn(0.dp, 300.dp)
            LazyColumn(lazyModifier) {
                items(pharmaViewModel, { it.thisPK }) { item ->
                    item.relayCommand = dataContext.relayCommand
                    val uploadItemCount by item.uploadItemCount.collectAsState()
                    val isOpen by item.isOpen.collectAsState()
                    shapeRoundedBox(ShapeRoundedBoxData().apply {
                        backgroundColor = color.cardBackground
                        modifier = Modifier.fillMaxWidth().padding(5.dp).clickable { item.onClick(EDIPharmaBuffModel.ClickEvent.OPEN)}
                    }) {
                        Column(Modifier.padding(5.dp)) {
                            Row(Modifier, Arrangement.SpaceBetween) {
                                customText(CustomTextData().apply {
                                    text = item.orgName
                                    textColor = color.cardForeground
                                    overflow = TextOverflow.Ellipsis
                                    modifier = Modifier.weight(1F).align(Alignment.CenterVertically)
                                })
                                Row(Modifier.align(Alignment.CenterVertically)) {
                                    customText(CustomTextData().apply {
                                        text = uploadItemCount
                                        textColor = color.foreground
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    })
                                    shapeRoundedBox(ShapeRoundedBoxData().apply {
                                        backgroundColor = color.buttonBackground
                                        modifier = Modifier.padding(5.dp).clickable { item.onClick(EDIPharmaBuffModel.ClickEvent.ADD)}
                                    }) {
                                        customText(CustomTextData().apply {
                                            text = stringResource(R.string.add_file_desc)
                                            textColor = color.buttonForeground
                                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
                                        })
                                    }
                                }
                            }
                            if (isOpen) {
                                val uploadItems by item.uploadItems.collectAsState()
                                LazyRow(Modifier.padding(5.dp)) {
                                    items(uploadItems, { it.thisPK }) { uploadItem ->
                                        uploadItem.relayCommand = dataContext.relayCommand
                                        shapeRoundedBox(ShapeRoundedBoxData().apply {
                                            backgroundColor = color.transparent
                                        }) {
                                            Box(Modifier.align(Alignment.TopEnd).zIndex(100F).clickable { uploadItem.onClick(MediaPickerSourceModel.ClickEvent.SELECT) },
                                                contentAlignment = Alignment.Center) {
                                                Icon(vectorCircle(FVectorData(color.background, color.primary)),
                                                    stringResource(R.string.remove_desc),
                                                    Modifier, Color.Unspecified)
                                                Icon(vectorCross(FVectorData(color.background, color.primary)),
                                                    stringResource(R.string.remove_desc),
                                                    Modifier, Color.Unspecified)
                                            }
                                            fImageLoad(uploadItem.mediaUrl,
                                                uploadItem.mediaFileType,
                                                uploadItem.mediaName,
                                                Modifier.width(100.dp).height(100.dp).padding(10.dp),
                                                ContentScale.Crop)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    LaunchedEffect(searchString) {
        filterItem(dataContext)
    }
}

@Composable
private fun searchDecorationBox(innerTextField: @Composable () -> Unit, text: String?, color: IBaseColor) {
    if (text.isNullOrEmpty()) {
        customText(CustomTextData().apply {
            this.text = stringResource(R.string.pharma_search_desc)
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
private fun addPharmaFilePK(dataContext: HomeEDIRequestScreenVM) {
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
        savableCheck(dataContext)
    }
    LaunchedEffect(addPharmaFilePK) {
        if (addPharmaFilePK != null) {
            addPharmaFileSelect(dataContext, context, activityResult)
        }
    }
}

private fun addPharmaFileSelect(dataContext: HomeEDIRequestScreenVM, context: Context, activityResult: ManagedActivityResultLauncher<Intent, ActivityResult>) {
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

private fun setLayoutCommand(data: Any?, dataContext: HomeEDIRequestScreenVM) {
    setThisCommand(data, dataContext)
    setApplyDateCommand(data, dataContext)
    setHosBuffCommand(data, dataContext)
    setPharmaBuffCommand(data, dataContext)
    setMediaItemCommand(data, dataContext)
}
private fun setThisCommand(data: Any?, dataContext: HomeEDIRequestScreenVM) {
    val eventName = data as? HomeEDIRequestScreenVM.ClickEvent ?: return
    when (eventName) {
        HomeEDIRequestScreenVM.ClickEvent.SAVE -> save(dataContext)
        HomeEDIRequestScreenVM.ClickEvent.HOSPITAL_OPEN -> dataContext.hospitalOpen.value = !dataContext.hospitalOpen.value
    }
}
private fun setApplyDateCommand(data: Any?, dataContext: HomeEDIRequestScreenVM) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? EDIApplyDateModel.ClickEvent ?: return
    val dataBuff = data[1] as? EDIApplyDateModel ?: return
    when (eventName) {
        EDIApplyDateModel.ClickEvent.THIS -> applyDateSelect(dataContext, dataBuff)
    }
}
private fun setHosBuffCommand(data: Any?, dataContext: HomeEDIRequestScreenVM) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? EDIHosBuffModel.ClickEvent ?: return
    val dataBuff = data[1] as? EDIHosBuffModel ?: return
    when (eventName) {
        EDIHosBuffModel.ClickEvent.THIS -> dataContext.hospitalSelect(dataBuff)
    }
}
private fun setPharmaBuffCommand(data: Any?, dataContext: HomeEDIRequestScreenVM) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? EDIPharmaBuffModel.ClickEvent ?: return
    val dataBuff = data[1] as? EDIPharmaBuffModel ?: return
    when (eventName) {
        EDIPharmaBuffModel.ClickEvent.THIS -> { }
        EDIPharmaBuffModel.ClickEvent.OPEN -> dataBuff.isOpen.value = !dataBuff.isOpen.value
        EDIPharmaBuffModel.ClickEvent.ADD -> addImage(dataContext, dataBuff)
    }
}
private fun setMediaItemCommand(data: Any?, dataContext: HomeEDIRequestScreenVM) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? MediaPickerSourceModel.ClickEvent ?: return
    val dataBuff = data[1] as? MediaPickerSourceModel ?: return
    when (eventName) {
        MediaPickerSourceModel.ClickEvent.SELECT -> {
            dataContext.delImage(dataBuff.thisPK)
            savableCheck(dataContext)
        }
        MediaPickerSourceModel.ClickEvent.SELECT_LONG -> {
            dataContext.delImage(dataBuff.thisPK)
            savableCheck(dataContext)
        }
    }
}

private fun savableCheck(dataContext: HomeEDIRequestScreenVM) {
    dataContext.savableCheck()
}
private fun filterItem(dataContext: HomeEDIRequestScreenVM) {
    dataContext.filterItem()
}
private fun getData(dataContext: HomeEDIRequestScreenVM) {
    dataContext.loading()
    FCoroutineUtil.coroutineScope({
        val ret = dataContext.getApplyData()
        dataContext.loading(false)
        if (ret.result != true) {
            dataContext.toast(ret.msg)
        }
    })
}
private fun save(dataContext: HomeEDIRequestScreenVM) {
    if (dataContext.pharmaModel.value.none { x -> x.uploadItems.value.isNotEmpty() }) {
        return
    }
    if (!dataContext.isSavable.value) {
        return
    }
    dataContext.selectApplyDate.value ?: return
    dataContext.selectHospital.value ?: return

    dataContext.toast(R.string.edi_file_upload)
    dataContext.loading()
    dataContext.startBackgroundService()
}

private fun applyDateSelect(dataContext: HomeEDIRequestScreenVM, data: EDIApplyDateModel) {
    FCoroutineUtil.coroutineScope({
        dataContext.applyDateSelect(data)
    })
}

private fun addImage(dataContext: HomeEDIRequestScreenVM, data: EDIPharmaBuffModel) {
    dataContext.addPharmaFilePK.value = data.thisPK
}

private fun checkReadStorage(dataContext: HomeEDIRequestScreenVM, callback: (Boolean) -> Unit) {
    dataContext.permissionService.requestReadExternalPermissions(callback)
}