package sdmed.extra.cso.views.dialog.loginDialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.interfaces.theme.IBaseColor
import sdmed.extra.cso.models.eventbus.EventList
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.models.retrofit.FRetrofitVariable
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FEventBus
import sdmed.extra.cso.utils.FStorage
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.CustomTextFieldData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.customText.customTextField
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun loginDialog(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                displayFeatures: List<DisplayFeature> = emptyList(),
                navigationType: NavigationType = NavigationType.BOTTOM,
                onDismissRequest: () -> Unit) {
    val dataContext = fBaseScreen<LoginDialogVM>( { data, dataContext -> setLayoutCommand(data, dataContext, onDismissRequest) },
        { dataContext -> loginDialogPhone(dataContext, onDismissRequest) },
        windowPanelType, navigationType)
}

@Composable
private fun loginDialogPhone(dataContext: LoginDialogVM, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest) {
        loginBody(dataContext)
    }
}

@Composable
private fun loginBody(dataContext: LoginDialogVM) {
    val id = dataContext.id.collectAsState()
    val pw = dataContext.pw.collectAsState()
    val fillDataState = dataContext.fillDataState.collectAsState()
    val color = FThemeUtil.safeColorC()
    shapeRoundedBox(ShapeRoundedBoxData().apply {
        backgroundColor = color.background
    }) {
        Column(Modifier.align(Alignment.Center).padding(20.dp)) {
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = if (id.value.isEmpty()) color.disableBackGray else color.background
                borderColor = if (id.value.isEmpty()) color.transparent else color.primary
                borderSize = if (id.value.isEmpty()) 0.dp else 1.dp
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)
            }) {
                customTextField(CustomTextFieldData().apply {
                    text = id.value
                    textSize = FThemeUtil.textUnit(18F)
                    modifier = Modifier.padding(10.dp)
                    onValueChange = {
                        if (it.length <= 20) {
                            dataContext.id.value = it
                        }
                    }
                    decorationBox = { idDecorationBox(it, id.value, color) }
                })
            }
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = if (pw.value.isEmpty()) color.disableBackGray else color.background
                borderColor = if (pw.value.isEmpty()) color.transparent else color.primary
                borderSize = if (pw.value.isEmpty()) 0.dp else 1.dp
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp, start = 16.dp, end = 16.dp)
            }) {
                customTextField(CustomTextFieldData().apply {
                    text = pw.value
                    textSize = FThemeUtil.textUnit(18F)
                    modifier = Modifier.padding(10.dp)
                    onValueChange = {
                        if (it.length <= 20) {
                            dataContext.pw.value = it
                        }
                    }
                    decorationBox = { pwDecorationBox(it, pw.value, color) }
                    keyboardType = KeyboardType.Password
                    visualTransformation = PasswordVisualTransformation()
                })
            }
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = if (fillDataState.value) color.buttonBackground else color.disableBackGray
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 10.dp, start = 16.dp, end = 16.dp)
                    .clickable { dataContext.relayCommand.execute(LoginDialogVM.ClickEvent.SIGN_IN)}
            }) {
                customText(CustomTextData().apply {
                    text = stringResource(R.string.login_btn_desc)
                    textSize = FThemeUtil.textUnit(18F)
                    textColor = if (fillDataState.value) color.buttonForeground else color.disableForeGray
                    modifier = Modifier.fillMaxWidth().padding(15.dp)
                    textAlign = TextAlign.Center
                })
            }
        }
    }
}

@Composable
private fun idDecorationBox(innerTextField: @Composable () -> Unit, text: String, color: IBaseColor) {
    Box(Modifier.fillMaxWidth()) {
        if (text.isEmpty()) {
            customText(CustomTextData().apply {
                this.text = stringResource(R.string.login_id_edit_desc)
                textSize = FThemeUtil.textUnit(18F)
                textColor = color.disableForeGray
                modifier = Modifier.fillMaxWidth()
            })
            innerTextField()
        }
    }
}
@Composable
private fun pwDecorationBox(innerTextField: @Composable () -> Unit, text: String, color: IBaseColor) {
    Box(Modifier.fillMaxWidth()) {
        if (text.isEmpty()) {
            customText(CustomTextData().apply {
                this.text = stringResource(R.string.pw_hint_desc)
                textSize = FThemeUtil.textUnit(18F)
                textColor = color.disableForeGray
                modifier = Modifier.fillMaxWidth()
            })
            innerTextField()
        }
    }
}

private fun setLayoutCommand(data: Any?, dataContext: LoginDialogVM, onDismissRequest: () -> Unit) {
    setThisCommand(data, dataContext, onDismissRequest)
}
private fun setThisCommand(data: Any?, dataContext: LoginDialogVM, onDismissRequest: () -> Unit) {
    val eventName = data as? LoginDialogVM.ClickEvent ?: return
    when (eventName) {
        LoginDialogVM.ClickEvent.SIGN_IN -> {
            if (!dataContext.fillDataState.value) {
                return
            }
            FCoroutineUtil.coroutineScope({
                val ret = dataContext.signIn()
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