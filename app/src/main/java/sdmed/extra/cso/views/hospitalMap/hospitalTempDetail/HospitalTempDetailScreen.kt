package sdmed.extra.cso.views.hospitalMap.hospitalTempDetail

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel
import sdmed.extra.cso.models.retrofit.hospitals.PharmacyTempModel
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.googleMap.GoogleMapStyle
import sdmed.extra.cso.views.component.googleMap.MarkerClusterDataModel
import sdmed.extra.cso.views.component.googleMap.MarkerClusterType
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.component.vector.FVectorData
import sdmed.extra.cso.views.component.vector.vectorArrowLeft
import sdmed.extra.cso.views.dialog.hospitalTemp.hospitalTempDialog
import sdmed.extra.cso.views.dialog.pharmacyTemp.pharmacyTempDialog
import sdmed.extra.cso.views.dialog.pharmacyTemp.pharmacyTempListDialog
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun hospitalTempDetailTwoPane(dataContext: HospitalTempDetailActivityVM,
                              displayFeatures: List<DisplayFeature>) {
    val pharmacyTempItems by dataContext.pharmacyTempItems.collectAsState()
    if (pharmacyTempItems.isEmpty()) {
        Box(Modifier) {
            Column(Modifier.fillMaxWidth()) {
                topContainer(dataContext, true)
                mapContainer(dataContext, true)
            }
        }
    } else {
        TwoPane({
            Box(Modifier) {
                Column(Modifier.fillMaxWidth()) {
                    mapContainer(dataContext, true)
                }
            }
        }, {
            Box(Modifier) {
                Column(Modifier.fillMaxWidth()) {
                    topContainer(dataContext, true)
                    pharmacyListContainer(dataContext)                }
            }
        },
            HorizontalTwoPaneStrategy(0.5F, 16.dp),
            displayFeatures)
    }
}
@Composable
fun hospitalTempDetailPhone(dataContext: HospitalTempDetailActivityVM) {
    Box(Modifier) {
        Column(Modifier.fillMaxWidth()) {
            topContainer(dataContext)
            mapContainer(dataContext)
            pharmacyListContainer(dataContext)
        }
    }
}
@Composable
fun hospitalTempDetailTablet(dataContext: HospitalTempDetailActivityVM) {
    Box(Modifier) {
        Column(Modifier.fillMaxWidth()) {
            topContainer(dataContext)
            mapContainer(dataContext)
            pharmacyListContainer(dataContext)
        }
    }
}

@Composable
private fun topContainer(dataContext: HospitalTempDetailActivityVM, isTwoPane: Boolean = false) {
    val color = FThemeUtil.safeColorC()
    val hospitalTempItem by dataContext.hospitalTempItem.collectAsState()
    Box(Modifier) {
        Row(Modifier.fillMaxWidth().padding(5.dp), Arrangement.SpaceBetween) {
            Row(Modifier.align(Alignment.CenterVertically)) {
                Icon(vectorArrowLeft(FVectorData(color.background, color.foreground)),
                    stringResource(R.string.close_desc),
                    Modifier.align(Alignment.CenterVertically).clickable { dataContext.relayCommand.execute(HospitalTempDetailActivityVM.ClickEvent.CLOSE)},
                    Color.Unspecified)
                if (hospitalTempItem.thisPK.isNotEmpty()) {
                    customText(CustomTextData().apply {
                        text = hospitalTempItem.orgName
                        textColor = color.foreground
                        modifier = Modifier.align(Alignment.CenterVertically).padding(start = 5.dp, end = 5.dp)
                        overflow = TextOverflow.Ellipsis
                    })
                }
            }
            Row(Modifier.align(Alignment.CenterVertically)) {
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = color.buttonBackground
                    modifier = Modifier.padding(5.dp).clickable { dataContext.relayCommand.execute(HospitalTempDetailActivityVM.ClickEvent.PHARMACY_TOGGLE) }
                }) {
                    customText(CustomTextData().apply {
                        text = stringResource(R.string.pharmacy_toggle)
                        textColor = color.buttonForeground
                        modifier = Modifier.padding(10.dp)
                    })
                }
                if (!isTwoPane) {
                    shapeRoundedBox(ShapeRoundedBoxData().apply {
                        backgroundColor = color.buttonBackground
                        modifier = Modifier.padding(5.dp).clickable { dataContext.relayCommand.execute(HospitalTempDetailActivityVM.ClickEvent.MAP_TOGGLE) }
                    }) {
                        customText(CustomTextData().apply {
                            text = stringResource(R.string.map_toggle)
                            textColor = color.buttonForeground
                            modifier = Modifier.padding(10.dp)
                        })
                    }
                }
            }
        }
    }
    singleCluster(dataContext)
    listCluster(dataContext)
}
@OptIn(MapsComposeExperimentalApi::class)
@Composable
private fun mapContainer(dataContext: HospitalTempDetailActivityVM, isTwoPane: Boolean = false) {
    val context = LocalContext.current
    var uiSettings by remember { mutableStateOf(MapUiSettings()) }
    var properties by remember { mutableStateOf(MapProperties(
        mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, GoogleMapStyle.RETRO.resId))) }
    var mergeDescendants by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(FConstants.DEF_LATITUDE, FConstants.DEF_LONGITUDE), FConstants.DEF_ZOOM)
    }
    var mapLoaded by remember { mutableStateOf(false) }
    var clusterMaker by remember { mutableStateOf(mutableListOf(MarkerClusterDataModel())) }
    val hospitalTempItem by dataContext.hospitalTempItem.collectAsState()
    val pharmacyTempItems by dataContext.pharmacyTempItems.collectAsState()
    val pharmacyToggle by dataContext.pharmacyToggle.collectAsState()
    val selectPharmacyTemp by dataContext.selectPharmacyTemp.collectAsState()
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
                boxModifier = if (pharmacyTempItems.isEmpty()) Modifier.fillMaxSize() else Modifier.fillMaxWidth().height(500.dp)
                mapModifier = Modifier.matchParentSize()
            } else {
                boxModifier = Modifier.size(0.dp)
                mapModifier = Modifier.size(0.dp)
            }
        }
    }
    LaunchedEffect(selectPharmacyTemp, mapLoaded) {
        if (mapLoaded) {
            selectPharmacyTemp?.let { x ->
                cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(x.latitude, x.longitude), cameraPositionState.position.zoom)
            }
        }
    }
    LaunchedEffect(hospitalTempItem, mapLoaded) {
        if (mapLoaded) {
            hospitalTempItem.let { x ->
                if (x.thisPK.isNotEmpty()) {
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(hospitalTempItem.latitude, hospitalTempItem.longitude), cameraPositionState.position.zoom)
                }
            }
        }
    }
    LaunchedEffect(pharmacyTempItems, pharmacyToggle) {
        if (pharmacyTempItems.isEmpty()) {
            if (!isTwoPane) {
                boxModifier = Modifier.fillMaxSize()
            }
            val buff = mutableListOf<MarkerClusterDataModel>()
            if (hospitalTempItem.thisPK.isNotEmpty()) {
                buff.add(hospitalTempItem.toMarkerClusterDataModel())
            }
            clusterMaker = buff
        } else {
            if (!isTwoPane) {
                boxModifier = Modifier.fillMaxWidth().height(500.dp)
            }
            val buff = mutableListOf<MarkerClusterDataModel>()
            buff.add(hospitalTempItem.toMarkerClusterDataModel())
            if (pharmacyToggle) {
                pharmacyTempItems.forEach { x ->
                    buff.add(x.toMarkerClusterDataModel())
                }
            }
            clusterMaker = buff
        }
    }
}
@Composable
private fun pharmacyListContainer(dataContext: HospitalTempDetailActivityVM) {
    val pharmacyTempItems by dataContext.pharmacyTempItems.collectAsState()
    Box(Modifier.fillMaxWidth()) {
        LazyColumn(Modifier) {
            items(pharmacyTempItems, { it.thisPK }) { item ->
                pharmacyItemContainer(dataContext, item)
            }
        }
    }
}
@Composable
private fun pharmacyItemContainer(dataContext: HospitalTempDetailActivityVM, item: PharmacyTempModel) {
    item.relayCommand = dataContext.relayCommand
    val color = FThemeUtil.safeColorC()
    val selectedHospitalTemp by dataContext.selectPharmacyTemp.collectAsState()
    shapeRoundedBox(ShapeRoundedBoxData().apply {
        backgroundColor = if(selectedHospitalTemp?.thisPK == item.thisPK) color.onSenary else color.senaryContainer
        modifier = Modifier.fillMaxWidth().padding(5.dp).clickable { item.onClick(PharmacyTempModel.ClickEvent.THIS) }
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
            if (item.phoneNumber.isNotEmpty()) {
                customText(CustomTextData().apply {
                    text = item.phoneNumber
                    textColor = color.onQuaternary
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp).clickable { item.onClick(PharmacyTempModel.ClickEvent.PHONE_NUMBER)}
                })
            }
        }
    }
}

@Composable
private fun singleCluster(dataContext: HospitalTempDetailActivityVM) {
    val singleCluster by dataContext.singleCluster.collectAsState()
    singleCluster?.let { x ->
        when (x.markerType) {
            MarkerClusterType.NONE -> dataContext.singleCluster.value = null
            MarkerClusterType.HOSPITAL -> {
                hospitalTempDialog(HospitalTempModel().parse(x),
                    onDismissRequest = { dataContext.singleCluster.value = null })
            }
            MarkerClusterType.PHARMACY -> {
                pharmacyTempDialog(PharmacyTempModel().parse(x),
                    onDismissRequest = { dataContext.singleCluster.value = null },
                    onSelectRequest = { y ->
                        dataContext.selectPharmacyTemp.value = if (dataContext.selectPharmacyTemp.value?.thisPK == y.thisPK) {
                            null
                        } else {
                            y
                        }
                    })
            }
        }
    }
}
@Composable
private fun listCluster(dataContext: HospitalTempDetailActivityVM) {
    val listCluster by dataContext.listCluster.collectAsState()
    listCluster?.let { x ->
        val data = mutableListOf<PharmacyTempModel>()
        x.items.forEach { y -> data.add(PharmacyTempModel().parse(y)) }
        pharmacyTempListDialog(data,
            onDismissRequest = { dataContext.listCluster.value = null },
            onSelectRequest = { y ->
                dataContext.listCluster.value = null
                dataContext.singleCluster.value = y.toMarkerClusterDataModel()
            })
    }
}