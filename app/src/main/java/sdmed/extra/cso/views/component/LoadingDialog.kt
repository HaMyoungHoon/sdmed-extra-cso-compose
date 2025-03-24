package sdmed.extra.cso.views.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import sdmed.extra.cso.R
import sdmed.extra.cso.models.common.LoadingMessageModel
import sdmed.extra.cso.views.component.customText.CustomText
import sdmed.extra.cso.views.theme.FThemeUtil

class LoadingDialog {
    companion object {
        @Composable
        fun screen(loading: State<LoadingMessageModel>) {
            if (loading.value is LoadingMessageModel.Visible) {
                val state = loading.value as LoadingMessageModel.Visible
                spinnerDialogBox(state.msg, state.indicatorColor, state.textColor)
            }
        }
        @Composable
        fun spinnerDialogBox(
            text: String = "",
            indicatorColor: Color? = null,
            textColor: Color? = null
        ) {
            Box(
                Modifier.Companion.fillMaxSize()
                    .background(Color.Companion.Black.copy(alpha = 0.5F))
                    .zIndex(1000F)
            ) {
                Column(
                    Modifier.Companion.align(Alignment.Companion.Center),
                    horizontalAlignment = Alignment.Companion.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.Companion,
                        color = indicatorColor ?: FThemeUtil.baseColor().primary
                    )
                    CustomText()
                }
            }
        }
    }

    @Preview
    @Composable
    fun spinnerDialogPreview() {
        spinnerDialogBox("preview")
    }
}