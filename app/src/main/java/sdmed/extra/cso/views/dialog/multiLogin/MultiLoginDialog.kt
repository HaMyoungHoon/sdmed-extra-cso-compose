package sdmed.extra.cso.views.dialog.multiLogin

import sdmed.extra.cso.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.models.eventbus.EventList
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.models.retrofit.FRetrofitVariable
import sdmed.extra.cso.models.retrofit.users.UserMultiLoginModel
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FEventBus
import sdmed.extra.cso.utils.FStorage
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.theme.FThemeUtil
import java.util.ArrayList

@Composable
fun multiLoginDialog(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                     displayFeatures: List<DisplayFeature> = emptyList(),
                     navigationType: NavigationType = NavigationType.BOTTOM,
                     isAddVisible: Boolean = false,
                     addLoginRequest: () -> Unit = { },
                     onDismissRequest: () -> Unit) {
    val context = LocalContext.current
    val dataContext = fBaseScreen<MultiLoginDialogVM>( { data, dataContext -> setLayoutCommand(data, dataContext, onDismissRequest) },
        null,
        windowPanelType, navigationType,
        { dataContext -> multiLoginTwoPane(dataContext, displayFeatures, onDismissRequest) },
        { dataContext -> multiLoginPhone(dataContext, onDismissRequest) },
        { dataContext -> multiLoginTablet(dataContext, onDismissRequest) })
    FStorage.getMultiLoginData(context)?.let {
        dataContext.items.value = it.toMutableList()
    }
    dataContext.isAddVisible.value = isAddVisible
    val addLogin by dataContext.addLogin.collectAsState()
    if (addLogin) {
        addLoginRequest()
        dataContext.addLogin.value = false
        onDismissRequest()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun multiLoginTwoPane(dataContext: MultiLoginDialogVM, displayFeatures: List<DisplayFeature>, onDismissRequest: () -> Unit) {
    val color = FThemeUtil.safeColorC()
    ModalBottomSheet(onDismissRequest) {
        Column(Modifier.background(color.background)) {
            topContainer(dataContext)
            itemListContainer(dataContext)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun multiLoginTablet(dataContext: MultiLoginDialogVM, onDismissRequest: () -> Unit) {
    val color = FThemeUtil.safeColorC()
    ModalBottomSheet(onDismissRequest) {
        Column(Modifier.background(color.background)) {
            topContainer(dataContext)
            itemListContainer(dataContext)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun multiLoginPhone(dataContext: MultiLoginDialogVM, onDismissRequest: () -> Unit) {
    val color = FThemeUtil.safeColorC()
    ModalBottomSheet(onDismissRequest) {
        Column(Modifier.fillMaxWidth().background(color.background)) {
            topContainer(dataContext)
            itemListContainer(dataContext)
        }
    }
}

@Composable
private fun topContainer(dataContext: MultiLoginDialogVM) {
    val color = FThemeUtil.safeColorC()
    val addVisible by dataContext.isAddVisible.collectAsState()
    Column(Modifier.fillMaxWidth()) {
        customText(CustomTextData().apply {
            text = stringResource(R.string.multi_login)
            textColor = color.paragraph
            textAlign = TextAlign.Center
            modifier = Modifier.fillMaxWidth()
        })
        if (addVisible) {
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = color.buttonBackground
                modifier = Modifier.fillMaxWidth().padding(10.dp).clickable { dataContext.relayCommand.execute(MultiLoginDialogVM.ClickEvent.ADD)}
            }) {
                customText(CustomTextData().apply {
                    text = stringResource(R.string.add_desc)
                    textColor = color.buttonForeground
                    textAlign = TextAlign.Center
                    modifier = Modifier.padding(10.dp).fillMaxWidth()
                })
            }
        }
    }
}
@Composable
private fun itemListContainer(dataContext: MultiLoginDialogVM) {
    val color = FThemeUtil.safeColorC()
    val items by dataContext.items.collectAsState()
    LazyColumn(Modifier.fillMaxWidth().padding(10.dp)) {
        items(items, { it.thisPK }) { x ->
            x.relayCommand = dataContext.relayCommand
            Row(Modifier.fillMaxWidth()) {
                Column(Modifier.align(Alignment.CenterVertically).weight(1F)) {
                    customText(CustomTextData().apply {
                        text = x.id
                        textColor = color.paragraph
                    })
                    customText(CustomTextData().apply {
                        text = x.name
                        textColor = color.foreground
                    })
                }
                if (!x.isLogin) {
                    shapeRoundedBox(ShapeRoundedBoxData().apply {
                        backgroundColor = color.buttonBackground
                        modifier = Modifier.clickable { x.onClick(UserMultiLoginModel.ClickEvent.THIS) }.padding(10.dp)
                    }) {
                        customText(CustomTextData().apply {
                            text = stringResource(R.string.login_btn_desc)
                            textColor = color.buttonForeground
                            modifier = Modifier.padding(5.dp)
                        })
                    }
                }
            }
        }
    }
}

private fun setLayoutCommand(data: Any?, dataContext: MultiLoginDialogVM, onDismissRequest: () -> Unit) {
    setThisCommand(data, dataContext, onDismissRequest)
    setItemCommand(data, dataContext, onDismissRequest)
}
private fun setThisCommand(data: Any?, dataContext: MultiLoginDialogVM, onDismissRequest: () -> Unit) {
    val eventName = data as? MultiLoginDialogVM.ClickEvent ?: return
    when (eventName) {
        MultiLoginDialogVM.ClickEvent.CLOSE -> onDismissRequest()
        MultiLoginDialogVM.ClickEvent.ADD -> addLogin(dataContext)
    }
}
private fun setItemCommand(data: Any?, dataContext: MultiLoginDialogVM, onDismissRequest: () -> Unit) {
    if (data !is ArrayList<*> || data.size <= 1) return
    val eventName = data[0] as? UserMultiLoginModel.ClickEvent ?: return
    val dataBuff = data[1] as? UserMultiLoginModel ?: return
    when (eventName) {
        UserMultiLoginModel.ClickEvent.THIS -> {
            dataContext.loading()
            FCoroutineUtil.coroutineScope({
                val ret = dataContext.multiSign(dataBuff.token)
                dataContext.loading(false)
                if (ret.result == true) {
                    FRetrofitVariable.token.value = ret.data
                    FStorage.setAuthToken(dataContext.context, ret.data)
                    FAmhohwa.addLoginData(dataContext.context)
                    FEventBus.emit(EventList.MultiLoginEvent)
                    onDismissRequest()
                    return@coroutineScope
                }
                dataContext.toast(ret.msg)
            })
        }
    }
}

private fun addLogin(dataContext: MultiLoginDialogVM) {
    dataContext.addLogin.value = true
}