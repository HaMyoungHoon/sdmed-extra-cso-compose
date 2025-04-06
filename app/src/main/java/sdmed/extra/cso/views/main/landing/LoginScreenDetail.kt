package sdmed.extra.cso.views.main.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import sdmed.extra.cso.R
import sdmed.extra.cso.interfaces.theme.IBaseColor
import sdmed.extra.cso.models.menu.MenuItem
import sdmed.extra.cso.models.menu.MenuList
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.CustomTextFieldData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.customText.customTextField
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.theme.FThemeUtil

private val headerHeight = 48.dp
@Composable
fun loginScreenDetail(dataContext: LoginScreenVM,
                      navigate: (MenuItem, Boolean) -> Unit) {
    val color = FThemeUtil.safeColorC()
    val loginEnd by dataContext.loginEnd.collectAsState()
    var navigateCalled by remember { mutableStateOf(false) }
    LaunchedEffect(loginEnd) {
        if (loginEnd && !navigateCalled) {
            navigateCalled = true
            dataContext.reSet()
            navigate(MenuList.menuEDI(), true)
        }
    }
    Box(Modifier.fillMaxSize().background(color.background)) {
        loginHeader()
        loginBody(dataContext)
        Box(Modifier.align(Alignment.BottomCenter)) {
            loginTail(dataContext)
        }
    }
}

@Composable
private fun loginHeader() {
    Box(Modifier.fillMaxWidth().height(headerHeight)) {
        customText(CustomTextData().apply {
            text = stringResource(R.string.login_title_desc)
            textAlign = TextAlign.Center
            modifier = Modifier.align(Alignment.Center)
        })
    }
}
@Composable
private fun loginBody(dataContext: LoginScreenVM) {
    val id = dataContext.id.collectAsState()
    val pw = dataContext.pw.collectAsState()
    val multiSignItems = dataContext.multiSignItems.collectAsState()
    val color = FThemeUtil.safeColorC()
    Box(Modifier.fillMaxWidth().padding(top = headerHeight * 2)) {
        Column(Modifier.fillMaxWidth()) {
            shapeRoundedBox(ShapeRoundedBoxData().apply {
                backgroundColor = if (id.value.isEmpty()) color.disableBackGray else color.background
                borderColor = if (id.value.isEmpty()) color.transparent else color.primary
                borderSize = if (id.value.isEmpty()) 0.dp else 1.dp
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)
            }) {
                customTextField(CustomTextFieldData().apply {
                    text = id.value
                    textSize = FThemeUtil.textUnit(18F)
                    textColor = color.foreground
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
                    textColor = color.foreground
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
            if (multiSignItems.value.isNotEmpty()) {
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = color.quinary
                    modifier = Modifier.align(Alignment.End).padding(top = 12.dp, start = 16.dp, end = 16.dp)
                        .clickable { dataContext.relayCommand.execute(LoginScreenVM.ClickEvent.MULTI_LOGIN) }
                }) {
                    customText(CustomTextData().apply {
                        text = stringResource(R.string.multi_login)
                        textSize = FThemeUtil.textUnit(16F)
                        textColor = color.buttonForeground
                        modifier = Modifier.padding(5.dp)
                    })
                }
            }
        }
    }
}
@Composable
private fun loginTail(dataContext: LoginScreenVM) {
    val fillDataState = dataContext.fillDataState.collectAsState()
    val color = FThemeUtil.safeColorC()
    Box(Modifier.fillMaxWidth()) {
        shapeRoundedBox(ShapeRoundedBoxData().apply {
            backgroundColor = if (fillDataState.value) color.buttonBackground else color.disableBackGray
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp, start = 16.dp, end = 16.dp)
                .clickable { dataContext.relayCommand.execute(LoginScreenVM.ClickEvent.SIGN_IN)}
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
        }
        innerTextField()
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
        }
        innerTextField()
    }
}

//@Preview
@Composable
private fun previewScreen() {
    loginScreenDetail(LoginScreenVM().apply { fakeInit() }, { a, b -> })
}