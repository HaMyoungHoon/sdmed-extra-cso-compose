package sdmed.extra.cso.views.dialog.passwordChange

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.window.layout.DisplayFeature
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.interfaces.theme.IBaseColor
import sdmed.extra.cso.models.menu.NavigationType
import sdmed.extra.cso.models.menu.WindowPanelType
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.CustomTextFieldData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.customText.customTextField
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun passwordChangeDialog(windowPanelType: WindowPanelType = WindowPanelType.SINGLE_PANE,
                         displayFeatures: List<DisplayFeature> = emptyList(),
                         navigationType: NavigationType = NavigationType.BOTTOM,
                         onDismissRequest: () -> Unit) {
    val dataContext = fBaseScreen<PasswordChangeDialogVM>( { data, dataContext -> setLayoutCommand(data, dataContext, onDismissRequest) },
        { dataContext -> passwordChangeDialogPhone(dataContext, onDismissRequest) },
        windowPanelType, navigationType)
}

@Composable
private fun passwordChangeDialogPhone(dataContext: PasswordChangeDialogVM, onDismissRequest: () -> Unit) {
    val currentPW by dataContext.currentPW.collectAsState()
    val afterPW by dataContext.afterPW.collectAsState()
    val confirmPW by dataContext.confirmPW.collectAsState()
    val pwUnMatchVisible by dataContext.pwUnMatchVisible.collectAsState()
    val afterPWRuleVisible by dataContext.afterPWRuleVisible.collectAsState()
    val confirmPWRuleVisible by dataContext.confirmPWRuleVisible.collectAsState()
    val changeAble by dataContext.changeAble.collectAsState()
    dataContext.pwUnMatchVisible.value = afterPW != confirmPW
    dataContext.afterPWRuleVisible.value = FExtensions.regexPasswordCheck(afterPW) != true && afterPW.isNotEmpty()
    dataContext.confirmPWRuleVisible.value = FExtensions.regexPasswordCheck(confirmPW) != true && confirmPW.isNotEmpty()
    dataContext.changeAble.value = !pwUnMatchVisible && !afterPWRuleVisible && !confirmPWRuleVisible && currentPW.length > 3 && afterPW.isNotEmpty() && confirmPW.isNotEmpty()
    val color = FThemeUtil.safeColorC()
    Dialog(onDismissRequest) {
        shapeRoundedBox(ShapeRoundedBoxData().apply {
            backgroundColor = color.background
        }) {
            Column(Modifier.align(Alignment.Center).padding(20.dp)) {
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = if (currentPW.isEmpty()) color.disableBackGray else color.background
                    borderColor = if (currentPW.isEmpty()) color.transparent else color.primary
                    borderSize = if (currentPW.isEmpty()) 0.dp else 1.dp
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                }) {
                    customTextField(CustomTextFieldData().apply {
                        text = currentPW
                        modifier = Modifier.padding(10.dp)
                        onValueChange = {
                            if (it.length <= 20) {
                                dataContext.currentPW.value = it
                            }
                        }
                        decorationBox = { pwDecorationBox(it, currentPW, color, R.string.current_pw_desc) }
                        keyboardType = KeyboardType.Password
                        visualTransformation = PasswordVisualTransformation()
                    })
                }
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = if (afterPW.isEmpty()) color.disableBackGray else color.background
                    borderColor = if (afterPW.isEmpty()) color.transparent else color.primary
                    borderSize = if (afterPW.isEmpty()) 0.dp else 1.dp
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                }) {
                    customTextField(CustomTextFieldData().apply {
                        text = afterPW
                        modifier = Modifier.padding(10.dp)
                        onValueChange = {
                            if (it.length <= 20) {
                                dataContext.afterPW.value = it
                            }
                        }
                        decorationBox = { pwDecorationBox(it, afterPW, color, R.string.after_pw_desc) }
                        keyboardType = KeyboardType.Password
                        visualTransformation = PasswordVisualTransformation()
                    })
                }
                if (afterPWRuleVisible) {
                    customTextField(CustomTextFieldData().apply {
                        text = stringResource(R.string.after_pw_rule_check_desc)
                        textColor = color.error
                        modifier = Modifier.padding(start = 16.dp)
                    })
                }
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = if (confirmPW.isEmpty()) color.disableBackGray else color.background
                    borderColor = if (confirmPW.isEmpty()) color.transparent else color.primary
                    borderSize = if (confirmPW.isEmpty()) 0.dp else 1.dp
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                }) {
                    customTextField(CustomTextFieldData().apply {
                        text = confirmPW
                        modifier = Modifier.padding(10.dp)
                        onValueChange = {
                            if (it.length <= 20) {
                                dataContext.confirmPW.value = it
                            }
                        }
                        decorationBox = { pwDecorationBox(it, confirmPW, color, R.string.confirm_desc) }
                        keyboardType = KeyboardType.Password
                        visualTransformation = PasswordVisualTransformation()
                    })
                }
                if (confirmPWRuleVisible) {
                    customTextField(CustomTextFieldData().apply {
                        text = stringResource(R.string.confirm_pw_rule_check_desc)
                        textColor = color.error
                        modifier = Modifier.padding(start = 16.dp)
                    })
                }
                if (pwUnMatchVisible) {
                    customTextField(CustomTextFieldData().apply {
                        text = stringResource(R.string.after_confirm_unmatch_desc)
                        textColor = color.error
                        modifier = Modifier.padding(start = 16.dp)
                    })
                }
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = if (changeAble) color.buttonBackground else color.disableBackGray
                    modifier = Modifier.padding(16.dp).clickable { dataContext.relayCommand.execute(PasswordChangeDialogVM.ClickEvent.CHANGE)}
                }) {
                    customText(CustomTextData().apply {
                        text = stringResource(R.string.password_change_desc)
                        textColor = if (changeAble) color.buttonForeground else color.disableForeGray
                        modifier = Modifier.padding(10.dp)
                    })
                }
            }
        }
    }
}

@Composable
private fun pwDecorationBox(innerTextField: @Composable () -> Unit, text: String, color: IBaseColor, @StringRes id: Int) {
    Box(Modifier.fillMaxWidth()) {
        if (text.isEmpty()) {
            customText(CustomTextData().apply {
                this.text = stringResource(id)
                textSize = FThemeUtil.textUnit(18F)
                textColor = color.disableForeGray
                modifier = Modifier.fillMaxWidth()
            })
        }
        innerTextField()
    }
}

private fun setLayoutCommand(data: Any?, dataContext: PasswordChangeDialogVM, onDismissRequest: () -> Unit) {
    setThisCommand(data, dataContext, onDismissRequest)
}

private fun setThisCommand(data: Any?, dataContext: PasswordChangeDialogVM, onDismissRequest: () -> Unit) {
    val eventName = data as? PasswordChangeDialogVM.ClickEvent ?: return
    when (eventName) {
        PasswordChangeDialogVM.ClickEvent.CHANGE -> {
            if (dataContext.afterPWRuleVisible.value) {
                return
            }
            if (dataContext.confirmPWRuleVisible.value) {
                return
            }
            if (dataContext.pwUnMatchVisible.value) {
                return
            }
            dataContext.loading()
            FCoroutineUtil.coroutineScope({
                val ret = dataContext.putPasswordChange()
                dataContext.loading(false)
                if (ret.result == true) {
                    onDismissRequest()
                    return@coroutineScope
                }
                dataContext.toast(ret.msg)
            })
        }
    }
}