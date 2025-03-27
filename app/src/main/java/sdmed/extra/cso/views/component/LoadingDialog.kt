package sdmed.extra.cso.views.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import sdmed.extra.cso.models.common.LoadingMessageModel
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.theme.FThemeUtil
import sdmed.extra.cso.views.theme.FLightColor

@Composable
fun loadingDialog(loading: LoadingMessageModel) {
    val color = FThemeUtil.safeColor()
    val state = loading as? LoadingMessageModel.Visible ?: return
    spinnerDialogBox(CustomTextData().apply {
        text = state.msg
        textColor = color.primary
    })
}
@Composable
private fun spinnerDialogBox(customTextData: CustomTextData) {
    val color = FThemeUtil.safeColor()
    Box(Modifier.fillMaxSize()
        .background(color.transparent)
        .zIndex(1000F)) {
        Column(Modifier.align(Alignment.Companion.Center), Arrangement.Center, Alignment.CenterHorizontally) {
            CircularProgressIndicator(Modifier, color.primary)
            customText(customTextData)
        }
    }
}

//@Preview
@Composable
private fun previewLoadingDialog() {
    spinnerDialogBox(CustomTextData().apply {
        text = "loading"
        textColor = FLightColor.primary
    })
}