package sdmed.extra.cso.views.main.home

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
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
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.interfaces.theme.IBaseColor
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.menu.MenuItem
import sdmed.extra.cso.models.menu.MenuList
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.models.retrofit.edi.EDIApplyDateModel
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaBuffModel
import sdmed.extra.cso.models.retrofit.edi.EDIType
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FStorage.getParcelable
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
import sdmed.extra.cso.views.component.vector.vectorCircle
import sdmed.extra.cso.views.component.vector.vectorCross
import sdmed.extra.cso.views.hospitalMap.hospitalTempFind.HospitalTempFindActivity
import sdmed.extra.cso.views.media.picker.MediaPickerActivity
import sdmed.extra.cso.views.theme.FThemeUtil
import java.util.ArrayList

@Composable
fun homeEDIRequestNewScreen(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                            displayFeatures: List<DisplayFeature> = emptyList(),
                            navigationType: NavigationType = NavigationType.BOTTOM,
                            navigate: (MenuItem, Boolean) -> Unit) {
    var navigateCalled by remember { mutableStateOf(false) }
    val dataContext = fBaseScreen<HomeEDIRequestNewScreenVM>( { data, dataContext -> setLayoutCommand(data, dataContext) },
        null,
        windowPanelType, navigationType,
        { dataContext -> homeEDIRequestNewScreenTwoPane(dataContext, displayFeatures) },
        { dataContext -> homeEDIRequestNewScreenPhone(dataContext) },
        { dataContext -> homeEDIRequestNewScreenTablet(dataContext) })
    val navigateEDIList by dataContext.navigateEDIList.collectAsState()
    LaunchedEffect(navigateCalled) {
        if (!navigateCalled) {
            navigateCalled = true
            getEdiTypeList(dataContext)
            getData(dataContext)
        }
    }
    addHospitalFind(dataContext)
    addPharmaFilePK(dataContext)
    if (navigateEDIList) {
        navigate(MenuList.menuEDI(), false)
    }
}
@Composable
private fun homeEDIRequestNewScreenTwoPane(dataContext: HomeEDIRequestNewScreenVM, displayFeatures: List<DisplayFeature>) {
    // 흠 일단 보류
    val color = FThemeUtil.safeColorC()
    val scrollState = rememberScrollState()
    Box(Modifier.fillMaxSize().background(color.background).verticalScroll(scrollState)) {
        Column {
            ediTypeContainer(dataContext)
            ediDateContainer(dataContext)
            ediHospitalContainer(dataContext)
            ediPharmaContainer(dataContext)
        }
    }
}
@Composable
private fun homeEDIRequestNewScreenPhone(dataContext: HomeEDIRequestNewScreenVM) {
    val color = FThemeUtil.safeColorC()
    val scrollState = rememberScrollState()
    Box(Modifier.fillMaxSize().background(color.background).verticalScroll(scrollState)) {
        Column {
            ediTypeContainer(dataContext)
            ediDateContainer(dataContext)
            ediHospitalContainer(dataContext)
            ediPharmaContainer(dataContext)
        }
    }
}
@Composable
private fun homeEDIRequestNewScreenTablet(dataContext: HomeEDIRequestNewScreenVM) {
    val color = FThemeUtil.safeColorC()
    val scrollState = rememberScrollState()
    Box(Modifier.fillMaxSize().background(color.background).verticalScroll(scrollState)) {
        Column {
            ediTypeContainer(dataContext)
            ediDateContainer(dataContext)
            ediHospitalContainer(dataContext)
            ediPharmaContainer(dataContext)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ediTypeContainer(dataContext: HomeEDIRequestNewScreenVM) {
    val color = FThemeUtil.safeColorC()
    val selectEDITypePosition by dataContext.selectEDITypePosition.collectAsState()
    val ediTypeModel by dataContext.ediTypeModel.collectAsState()
    val isSavable by dataContext.isSavable.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxWidth()) {
        Row(Modifier.align(Alignment.Center)) {
            if (ediTypeModel.isNotEmpty()) {
                ExposedDropdownMenuBox(expanded, { expanded = !expanded }, Modifier) {
                    shapeRoundedBox(ShapeRoundedBoxData().apply {
                        backgroundColor = color.primaryContainer
                        modifier = Modifier.padding(5.dp).menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    }) {
                        Row(Modifier.padding(10.dp)) {
                            customText(CustomTextData().apply {
                                text = stringResource(ediTypeModel[selectEDITypePosition].descResId)
                                textColor = color.primary
                            })
                            Icon(vectorArrowDown(FVectorData(color.background, color.primary)),
                            stringResource(R.string.select_desc), Modifier.width(IntrinsicSize.Min), Color.Unspecified)
                        }
                    }
                    ExposedDropdownMenu(expanded, { expanded = false }, Modifier.exposedDropdownSize()) {
                        ediTypeModel.forEachIndexed { index, type ->
                            DropdownMenuItem({
                                customText(CustomTextData().apply {
                                    text = stringResource(type.descResId)
                                    textColor = color.paragraph
                                })
                            }, {
                                dataContext.selectEDITypePosition.value = index
                                expanded = false
                            })
                        }
                    }
                }
            }
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = if (isSavable) color.buttonBackground else color.disableBackGray
                modifier = Modifier.padding(5.dp).align(Alignment.CenterVertically)
                    .clickable { dataContext.relayCommand.execute(HomeEDIRequestNewScreenVM.ClickEvent.SAVE) }
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
private fun ediDateContainer(dataContext: HomeEDIRequestNewScreenVM) {
    val color = FThemeUtil.safeColorC()
    val applyDateModel by dataContext.applyDateModel.collectAsState()
    val selectApplyDate by dataContext.selectApplyDate.collectAsState()
    Box(Modifier.fillMaxWidth().padding(5.dp)) {
        Column(Modifier.fillMaxWidth().align(Alignment.Center)) {
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
    }
}
@Composable
private fun ediHospitalContainer(dataContext: HomeEDIRequestNewScreenVM) {
    val color = FThemeUtil.safeColorC()
    val tempOrgName by dataContext.tempOrgName.collectAsState()
    val selectHospitalBuff by dataContext.selectHospitalBuff.collectAsState()
    Box(Modifier) {
        Column(Modifier.fillMaxWidth().align(Alignment.Center)) {
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = color.buttonBackground
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, start = 16.dp, end = 16.dp).clickable { dataContext.relayCommand.execute(HomeEDIRequestNewScreenVM.ClickEvent.HOSPITAL_FIND)}
            }) {
                customText(CustomTextData().apply {
                    text = stringResource(R.string.hospital_find_desc)
                    textColor = color.buttonForeground
                    textAlign = TextAlign.Center
                    modifier = Modifier.fillMaxWidth().padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
                })
            }
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = color.background
                borderColor = color.primary
                borderSize = 1.dp
                modifier = Modifier.fillMaxWidth().padding(top = 5.dp, start = 16.dp, end = 16.dp)
            }) {
                Column {
                    customTextField(CustomTextFieldData().apply {
                        text = tempOrgName
                        modifier = Modifier.padding(10.dp)
                        onValueChange = {
                            dataContext.tempOrgName.value = it
                        }
                        decorationBox = { hospitalFindDecorationBox(it, tempOrgName, color) }
                    })
                    if (tempOrgName == selectHospitalBuff.orgName && tempOrgName.isNotEmpty()) {
                        customText(CustomTextData().apply {
                            text = selectHospitalBuff.address
                            textColor = color.foreground
                            modifier = Modifier.padding(bottom = 5.dp, start = 10.dp, end = 10.dp)
                        })
                    }
                }
            }
        }
    }
    LaunchedEffect(tempOrgName) {
        savableCheck(dataContext)
    }
}
@Composable
private fun ediPharmaContainer(dataContext: HomeEDIRequestNewScreenVM) {
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
            LazyColumn(Modifier.fillMaxWidth().heightIn(0.dp, 700.dp)) {
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
                                        Box(Modifier) {
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
    }
    LaunchedEffect(searchString) {
        filterItem(dataContext)
    }
}

@Composable
private fun addHospitalFind(dataContext: HomeEDIRequestNewScreenVM) {
    val context = LocalContext.current
    val hospitalFind by dataContext.hospitalFind.collectAsState()
    val activityResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode != Activity.RESULT_OK) {
            return@rememberLauncherForActivityResult
        }
        val hospitalTempModel = it.data?.getParcelable<HospitalTempModel>(FConstants.HOSPITAL_TEMP) ?: return@rememberLauncherForActivityResult
        dataContext.setHospitalTemp(hospitalTempModel)
    }

    LaunchedEffect(hospitalFind) {
        if (hospitalFind) {
            addHospitalFindSelect(dataContext, context, activityResult)
        }
    }
}
@Composable
private fun addPharmaFilePK(dataContext: HomeEDIRequestNewScreenVM) {
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

@Composable
private fun hospitalFindDecorationBox(innerTextField: @Composable () -> Unit, text: String?, color: IBaseColor) {
    if (text.isNullOrEmpty()) {
        customText(CustomTextData().apply {
            this.text = stringResource(R.string.new_hospital_desc)
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

private fun addHospitalFindSelect(dataContext: HomeEDIRequestNewScreenVM, context: Context, activityResult: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    val hospitalFind = dataContext.hospitalFind.value
    if (!hospitalFind) {
        return
    }

    activityResult.launch(Intent(context, HospitalTempFindActivity::class.java))
    dataContext.hospitalFind.value = false
}
private fun addPharmaFileSelect(dataContext: HomeEDIRequestNewScreenVM, context: Context, activityResult: ManagedActivityResultLauncher<Intent, ActivityResult>) {
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

private fun setLayoutCommand(data: Any?, dataContext: HomeEDIRequestNewScreenVM) {
    setThisCommand(data, dataContext)
    setApplyDateCommand(data, dataContext)
    setPharmaBuffCommand(data, dataContext)
    setMediaItemCommand(data, dataContext)
}
private fun setThisCommand(data: Any?, dataContext: HomeEDIRequestNewScreenVM) {
    val eventName = data as? HomeEDIRequestNewScreenVM.ClickEvent ?: return
    when (eventName) {
        HomeEDIRequestNewScreenVM.ClickEvent.SAVE -> save(dataContext)
        HomeEDIRequestNewScreenVM.ClickEvent.HOSPITAL_FIND -> hospitalFind(dataContext)
    }
}
private fun setApplyDateCommand(data: Any?, dataContext: HomeEDIRequestNewScreenVM) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? EDIApplyDateModel.ClickEvent ?: return
    val dataBuff = data[1] as? EDIApplyDateModel ?: return
    when (eventName) {
        EDIApplyDateModel.ClickEvent.THIS -> {
            applyDateSelect(dataContext, dataBuff)
        }
    }
}
private fun setPharmaBuffCommand(data: Any?, dataContext: HomeEDIRequestNewScreenVM) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? EDIPharmaBuffModel.ClickEvent ?: return
    val dataBuff = data[1] as? EDIPharmaBuffModel ?: return
    when (eventName) {
        EDIPharmaBuffModel.ClickEvent.THIS -> { }
        EDIPharmaBuffModel.ClickEvent.OPEN -> dataBuff.isOpen.value = !dataBuff.isOpen.value
        EDIPharmaBuffModel.ClickEvent.ADD -> addImage(dataContext, dataBuff)
    }
}
private fun setMediaItemCommand(data: Any?, dataContext: HomeEDIRequestNewScreenVM) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? MediaPickerSourceModel.ClickEvent ?: return
    val dataBuff = data[1] as? MediaPickerSourceModel ?: return
    when (eventName) {
        MediaPickerSourceModel.ClickEvent.SELECT ->  {
            dataContext.delImage(dataBuff.thisPK)
            savableCheck(dataContext)
        }
        else -> { }
    }
}

private fun getData(dataContext: HomeEDIRequestNewScreenVM) {
    dataContext.loading()
    FCoroutineUtil.coroutineScope({
        val ret = dataContext.getData()
        dataContext.loading(false)
        if (ret.result != true) {
            dataContext.toast(ret.msg)
            return@coroutineScope
        }
        getPharmaList(dataContext)
    })
}
private fun getPharmaList(dataContext: HomeEDIRequestNewScreenVM) {
    dataContext.loading()
    FCoroutineUtil.coroutineScope({
        val ret = dataContext.getPharmaList()
        dataContext.loading(false)
        if (ret.result != true) {
            dataContext.toast(ret.msg)
            return@coroutineScope
        }
    })
}
private fun getEdiTypeList(dataContext: HomeEDIRequestNewScreenVM) {
    dataContext.ediTypeModel.value = EDIType.allEDITypeList()
}

private fun savableCheck(dataContext: HomeEDIRequestNewScreenVM) {
    dataContext.savableCheck()
}
private fun filterItem(dataContext: HomeEDIRequestNewScreenVM) {
    dataContext.filterItem()
}

private fun save(dataContext: HomeEDIRequestNewScreenVM) {
    if (dataContext.pharmaModel.value.none { x -> x.uploadItems.value.isNotEmpty() }) {
        return
    }
    if (!dataContext.isSavable.value) {
        return
    }
    dataContext.selectApplyDate ?: return
    if (dataContext.tempOrgName.value.isBlank()) {
        return
    }

    dataContext.toast(R.string.edi_file_upload)
    dataContext.loading()
    dataContext.startBackgroundService()
}

private fun applyDateSelect(dataContext: HomeEDIRequestNewScreenVM, data: EDIApplyDateModel) {
    FCoroutineUtil.coroutineScope({
        dataContext.applyDateSelect(data)
    })
}

private fun hospitalFind(dataContext: HomeEDIRequestNewScreenVM) {
    dataContext.hospitalFind.value = true
}

private fun addImage(dataContext: HomeEDIRequestNewScreenVM, data: EDIPharmaBuffModel) {
    dataContext.addPharmaFilePK.value = data.thisPK
}

private fun checkReadStorage(dataContext: HomeEDIRequestNewScreenVM, callback: (Boolean) -> Unit) {
    dataContext.permissionService.requestReadExternalPermissions(callback)
}