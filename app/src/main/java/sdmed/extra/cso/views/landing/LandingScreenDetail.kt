package sdmed.extra.cso.views.landing

import sdmed.extra.cso.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sdmed.extra.cso.bases.FMainApplication
import sdmed.extra.cso.models.retrofit.FRetrofitVariable
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FVersionControl
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.dialog.message.MessageDialog
import sdmed.extra.cso.views.dialog.message.MessageDialogData
import sdmed.extra.cso.views.theme.FThemeUtil

class LandingScreenDetail {
    @Composable
    fun screen(viewModel: LandingScreenVM) {
        val startVisible = viewModel.startVisible.collectAsState()
        val updateVisible = viewModel.updateVisible.collectAsState()
        versionCheck(viewModel)
        val color = FThemeUtil.safeColor()
        Box(Modifier.fillMaxSize()) {
            Image(painterResource(R.drawable.landing_background),
                stringResource(R.string.landing_desc),
                Modifier.align(Alignment.Center),
                Alignment.Center,
                ContentScale.Crop
            )

            if (updateVisible.value) {
                MessageDialog().screen(MessageDialogData().apply {
                    title = stringResource(R.string.new_version_update_desc)
                    leftBtnText = stringResource(R.string.update_desc)
                    rightBtnText = ""
                    isCancel = false
                    relayCommand = viewModel.relayCommand
                })
            }

            customText(CustomTextData().apply {
                text = stringResource(R.string.app_name)
                textSize = FThemeUtil.textUnit(30F)
                textColor = color.absoluteWhite
                fontWeight = FontWeight.ExtraBold
                textAlign = TextAlign.Center
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp)
            })

            if (startVisible.value) {
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = color.background
                    modifier = Modifier.fillMaxWidth().padding(bottom = 36.dp, start = 16.dp, end = 16.dp)
                        .align(Alignment.BottomCenter)
                        .clickable { viewModel.relayCommand.execute(LandingScreenVM.ClickEvent.START)}
                }) {
                    customText(CustomTextData().apply {
                        text = stringResource(R.string.start_desc)
                        textSize = FThemeUtil.textUnit(24F)
                        textColor = color.primary
                        modifier = Modifier.fillMaxWidth().padding(5.dp)
                        textAlign = TextAlign.Center
                    })
                }
            }
        }
    }

    private fun versionCheck(viewModel: LandingScreenVM) {
        if (viewModel.checking) {
            return
        }
        viewModel.loading()
        viewModel.checking = true
        FCoroutineUtil.coroutineScope({
            val ret = viewModel.versionCheck()
            viewModel.loading(false)
            viewModel.checking = false
            if (ret.result != true) {
                viewModel.toast(ret.msg)
                return@coroutineScope
            }
            if (!ret.data.isNullOrEmpty()) {
                val versionModel = ret.data?.firstOrNull { it.able } ?: return@coroutineScope
                val currentVersion = FMainApplication.ins.getVersionNameString()
                val check = FVersionControl.checkVersion(versionModel, currentVersion)
                if (check == FVersionControl.VersionResultType.NEED_UPDATE) {
                    viewModel.updateVisible.value = true
                    return@coroutineScope
                }
                versionCheckEnd(viewModel)
            }
        })
    }
    private fun versionCheckEnd(viewModel: LandingScreenVM) {
        if (FRetrofitVariable.token.value.isNullOrBlank()) {
            viewModel.startVisible.value = true
            return
        }
    }
    @Preview
    @Composable
    fun previewLandingDetail() {
        screen(LandingScreenVM())
    }
}