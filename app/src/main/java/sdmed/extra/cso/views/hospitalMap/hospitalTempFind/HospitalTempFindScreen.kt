package sdmed.extra.cso.views.hospitalMap.hospitalTempFind

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.interfaces.theme.IBaseColor
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.CustomTextFieldData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.customText.customTextField
import sdmed.extra.cso.views.component.googleMap.GoogleMapStyle
import sdmed.extra.cso.views.component.googleMap.MarkerClusterDataModel
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.component.vector.FVectorData
import sdmed.extra.cso.views.component.vector.vectorArrowLeft
import sdmed.extra.cso.views.dialog.hospitalTemp.hospitalTempDialog
import sdmed.extra.cso.views.dialog.hospitalTemp.hospitalTempListDialog
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun hospitalTempFindScreenTwoPane(dataContext: HospitalTempFindActivityVM,
                                  displayFeatures: List<DisplayFeature>) {
    val hospitalTempItems by dataContext.hospitalTempItems.collectAsState()
    if (hospitalTempItems.isEmpty()) {
        Box(Modifier) {
            Column(Modifier.fillMaxWidth()) {
                topContainer(dataContext)
                searchLoading(dataContext)
                mapMenuContainer(dataContext, true)
                mapContainer(dataContext, true)
            }
        }
    } else {
        TwoPane({
            Box(Modifier) {
                Column(Modifier.fillMaxWidth()) {
                    mapMenuContainer(dataContext, true)
                    mapContainer(dataContext, true)
                }
            }
        }, {
            Box(Modifier) {
                Column(Modifier.fillMaxWidth()) {
                    topContainer(dataContext)
                    searchLoading(dataContext)
                    hospitalListContainer(dataContext)
                }
            }
        },
            HorizontalTwoPaneStrategy(0.5F, 16.dp),
            displayFeatures)
    }
}
@Composable
fun hospitalTempFindScreenPhone(dataContext: HospitalTempFindActivityVM) {
    Box(Modifier) {
        Column(Modifier.fillMaxWidth()) {
            topContainer(dataContext)
            searchLoading(dataContext)
            mapMenuContainer(dataContext)
            mapContainer(dataContext)
            hospitalListContainer(dataContext)
        }
    }
}
@Composable
fun hospitalTempFindScreenTablet(dataContext: HospitalTempFindActivityVM) {
    Box(Modifier) {
        Column(Modifier.fillMaxWidth()) {
            topContainer(dataContext)
            searchLoading(dataContext)
            mapMenuContainer(dataContext)
            mapContainer(dataContext)
            hospitalListContainer(dataContext)
        }
    }
}

@OptIn(FlowPreview::class)
@Composable
private fun topContainer(dataContext: HospitalTempFindActivityVM) {
    val scope = rememberCoroutineScope()
    val color = FThemeUtil.safeColorC()
    val searchBuff by dataContext.searchBuff.collectAsState()
    Box(Modifier) {
        Row(Modifier.fillMaxWidth().padding(5.dp)) {
            Icon(vectorArrowLeft(FVectorData(color.background, color.foreground)),
                stringResource(R.string.close_desc),
                Modifier.align(Alignment.CenterVertically).clickable { dataContext.relayCommand.execute(HospitalTempFindActivityVM.ClickEvent.CLOSE)},
                Color.Unspecified)
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = color.background
                borderColor = color.primary
                borderSize = 1.dp
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, start = 16.dp, end = 16.dp)
            }) {
                customTextField(CustomTextFieldData().apply {
                    text = searchBuff ?: ""
                    modifier = Modifier.padding(10.dp)
                    onValueChange = {
                        if (it.length <= 20) {
                            dataContext.searchBuff.value = it
                        }
                    }
                    decorationBox = { searchDecorationBox(it, searchBuff, color) }
                })
            }
        }
    }
    scope.launch {
        dataContext.searchBuff.debounce(1000).collectLatest {
            it ?: return@collectLatest
            if (dataContext.searchString != it) {
                dataContext.searchString = it
                dataContext.getSearch()
            }
            dataContext.searchLoading.value = false
        }
    }
    scope.launch {
        dataContext.searchBuff.collectLatest {
            it ?: return@collectLatest
            dataContext.searchLoading.value = true
        }
    }
    singleCluster(dataContext)
    listCluster(dataContext)
}
@Composable
private fun mapMenuContainer(dataContext: HospitalTempFindActivityVM, isTwoPane: Boolean = false) {
    val color = FThemeUtil.safeColorC()
    val nearbyAble by dataContext.nearbyAble.collectAsState()
    val selectedHospitalTemp by dataContext.selectedHospitalTemp.collectAsState()
    Box(Modifier) {
        Row(Modifier, Arrangement.SpaceEvenly) {
            if (!isTwoPane) {
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = color.buttonBackground
                    modifier = Modifier.padding(5.dp).clickable { dataContext.relayCommand.execute(HospitalTempFindActivityVM.ClickEvent.MAP_TOGGLE)}
                }) {
                    customText(CustomTextData().apply {
                        text = stringResource(R.string.map_toggle)
                        textColor = color.buttonForeground
                        modifier = Modifier.padding(10.dp)
                    })
                }
            }
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = if (nearbyAble) color.buttonBackground else color.disableBackGray
                modifier = Modifier.padding(5.dp).clickable { dataContext.relayCommand.execute(HospitalTempFindActivityVM.ClickEvent.NEARBY)}
            }) {
                customText(CustomTextData().apply {
                    text = stringResource(R.string.search_nearby)
                    textColor = if (nearbyAble) color.buttonForeground else color.disableForeGray
                    modifier = Modifier.padding(10.dp)
                })
            }
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = if (selectedHospitalTemp != null) color.buttonBackground else color.disableBackGray
                modifier = Modifier.padding(5.dp).clickable { dataContext.relayCommand.execute(HospitalTempFindActivityVM.ClickEvent.SELECT)}
            }) {
                customText(CustomTextData().apply {
                    text = stringResource(R.string.select_desc)
                    textColor = if (selectedHospitalTemp != null) color.buttonForeground else color.disableForeGray
                    modifier = Modifier.padding(10.dp)
                })
            }
        }
    }
}
@OptIn(MapsComposeExperimentalApi::class)
@Composable
private fun mapContainer(dataContext: HospitalTempFindActivityVM, isTwoPane: Boolean = false) {
    val context = LocalContext.current
    var uiSettings by remember { mutableStateOf(MapUiSettings()) }
    var properties by remember { mutableStateOf(MapProperties(
        isMyLocationEnabled = dataContext.isMyLocationEnabled.value,
        mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, GoogleMapStyle.RETRO.resId))) }
    var mergeDescendants by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(FConstants.DEF_LATITUDE, FConstants.DEF_LONGITUDE), FConstants.DEF_ZOOM)
    }
    var mapLoaded by remember { mutableStateOf(false) }
    var clusterMaker by remember { mutableStateOf(mutableListOf(MarkerClusterDataModel())) }
    val hospitalTempItems by dataContext.hospitalTempItems.collectAsState()
    val selectedHospitalTemp by dataContext.selectedHospitalTemp.collectAsState()
    val currentLatLng by dataContext.currentLatLng.collectAsState()
    var boxModifier by remember { mutableStateOf(Modifier.fillMaxSize()) }
    Box(boxModifier) {
        val mapVisible by dataContext.mapVisible.collectAsState()
        var mapModifier by remember { mutableStateOf(Modifier.matchParentSize()) }
        GoogleMap(mapModifier, mergeDescendants, cameraPositionState,
            properties = properties,
            uiSettings = uiSettings,
            onMapLoaded = { mapLoaded = true }) {
            Clustering(clusterMaker,
                onClusterClick = { x ->
                    dataContext.listCluster.value = x
                    false // false 면 이동을 하네
                },
                onClusterItemClick = { x ->
                    dataContext.singleCluster.value = x
                    true // false 면 인포가 뜨네
                },
                clusterItemContent = { x ->
                    Column(Modifier.align(Alignment.Center)) {
                        Icon(painterResource(x.resDrawableId), x.orgName, Modifier.align(Alignment.CenterHorizontally), Color.Unspecified)
                        customText(CustomTextData().apply {
                            text = x.orgName
                            textSize = FThemeUtil.textUnit(10F)
                            textAlign = TextAlign.Center
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        })
                    }
                })
        }
        LaunchedEffect(mapVisible) {
            if (isTwoPane) {
                return@LaunchedEffect
            }
            if (mapVisible) {
                boxModifier = if (hospitalTempItems.isEmpty()) Modifier.fillMaxSize() else Modifier.fillMaxWidth().height(500.dp)
                mapModifier = Modifier.matchParentSize()
            } else {
                boxModifier = Modifier.size(0.dp)
                mapModifier = Modifier.size(0.dp)
            }
        }
    }
    LaunchedEffect(selectedHospitalTemp, mapLoaded) {
        if (mapLoaded) {
            selectedHospitalTemp?.let { x ->
                cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(x.latitude, x.longitude), cameraPositionState.position.zoom)
            }
        }
    }
    LaunchedEffect(currentLatLng, mapLoaded) {
        if (mapLoaded) {
            currentLatLng?.let { x ->
                cameraPositionState.position = CameraPosition.fromLatLngZoom(x, cameraPositionState.position.zoom)
            }
        }
    }
    LaunchedEffect(hospitalTempItems) {
        if (hospitalTempItems.isEmpty()) {
            if (!isTwoPane) {
                boxModifier = Modifier.fillMaxSize()
            }
            clusterMaker.clear()
        } else {
            if (!isTwoPane) {
                boxModifier = Modifier.fillMaxWidth().height(500.dp)
            }
            val buff = mutableListOf<MarkerClusterDataModel>()
            hospitalTempItems.forEach { x ->
                buff.add(x.toMarkerClusterDataModel())
            }
            clusterMaker = buff
        }
    }
}
@Composable
private fun hospitalListContainer(dataContext: HospitalTempFindActivityVM) {
    val hospitalTempItems by dataContext.hospitalTempItems.collectAsState()
    Box(Modifier.fillMaxWidth()) {
        LazyColumn(Modifier) {
            items(hospitalTempItems, { it.thisPK } ) { item ->
                hospitalItemContainer(dataContext, item)
            }
        }
    }
}
@Composable
private fun hospitalItemContainer(dataContext: HospitalTempFindActivityVM, item: HospitalTempModel) {
    item.relayCommand = dataContext.relayCommand
    val color = FThemeUtil.safeColorC()
    val selectedHospitalTemp by dataContext.selectedHospitalTemp.collectAsState()
    shapeRoundedBox(ShapeRoundedBoxData().apply {
        backgroundColor = if(selectedHospitalTemp?.thisPK == item.thisPK) color.onSenary else color.senaryContainer
        modifier = Modifier.fillMaxWidth().padding(5.dp).clickable { item.onClick(HospitalTempModel.ClickEvent.THIS) }
    }) {
        Column(Modifier) {
            customText(CustomTextData().apply {
                text = item.orgName
                textColor = color.quaternary
                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
            })
            customText(CustomTextData().apply {
                text = item.address
                textColor = color.onQuaternary
                maxLines = 2
                overflow = TextOverflow.Ellipsis
                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
            })
            if (item.websiteUrl.isNotEmpty()) {
                customText(CustomTextData().apply {
                    text = item.websiteUrl
                    textColor = color.quaternary
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp).clickable { item.onClick(HospitalTempModel.ClickEvent.WEB_SITE)}
                })
            }
            if (item.phoneNumber.isNotEmpty()) {
                customText(CustomTextData().apply {
                    text = item.phoneNumber
                    textColor = color.onQuaternary
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp).clickable { item.onClick(HospitalTempModel.ClickEvent.PHONE_NUMBER)}
                })
            }
        }
    }
}

@Composable
private fun searchLoading(dataContext: HospitalTempFindActivityVM) {
    val color = FThemeUtil.safeColorC()
    val searchLoading by dataContext.searchLoading.collectAsState()
    if (searchLoading) {
        Box(Modifier.fillMaxSize().background(color.transparent).zIndex(100F)) {
            CircularProgressIndicator(Modifier.align(Alignment.Center).background(color.transparent), color.primary)
        }
    }
}
@Composable
private fun searchDecorationBox(innerTextField: @Composable () -> Unit, text: String?, color: IBaseColor) {
    Box(Modifier.fillMaxWidth()) {
        if (text.isNullOrEmpty()) {
            customText(CustomTextData().apply {
                this.text = stringResource(R.string.search_desc)
                textColor = color.disableForeGray
                modifier = Modifier.fillMaxWidth()
            })
            innerTextField()
        }
    }
}

@Composable
private fun singleCluster(dataContext: HospitalTempFindActivityVM) {
    val singleCluster by dataContext.singleCluster.collectAsState()
    singleCluster?.let { x ->
        hospitalTempDialog(HospitalTempModel().parse(x),
            onDismissRequest = { dataContext.singleCluster.value = null },
            onSelectRequest = { y ->
                dataContext.selectedHospitalTemp.value = if (dataContext.selectedHospitalTemp.value?.thisPK == y.thisPK) {
                    null
                } else {
                    y
                }
            })
    }
}
@Composable
private fun listCluster(dataContext: HospitalTempFindActivityVM) {
    val listCluster by dataContext.listCluster.collectAsState()
    listCluster?.let { x ->
        val data = mutableListOf<HospitalTempModel>()
        x.items.forEach { y -> data.add(HospitalTempModel().parse(y)) }
        hospitalTempListDialog(data,
            onDismissRequest = { dataContext.listCluster.value = null },
            onSelectRequest = { y ->
                dataContext.listCluster.value = null
                dataContext.singleCluster.value = y.toMarkerClusterDataModel()
            })
    }
}