package sdmed.extra.cso.views.dialog.message

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import sdmed.extra.cso.bases.fBaseScreen
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.component.shape.dividerHorizontal
import sdmed.extra.cso.views.component.shape.dividerVertical
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun messageDialog(data: MessageDialogData) {
    fBaseScreen<MessageDialogVM>({ data, dataContext ->

        }, { dataContext ->
            dataContext.data.value = data
            messageDialogDetail(dataContext)
        })
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun messageDialogDetail(dataContext: MessageDialogVM) {
    val data by dataContext.data.collectAsState()
    val color = FThemeUtil.safeColor()
    BasicAlertDialog({ data.relayCommand?.execute(MessageDialogVM.ClickEvent.CLOSE) },
        Modifier,
        DialogProperties(dismissOnBackPress = data.isCancel, dismissOnClickOutside = data.isCancel)) {
        Column(Modifier.fillMaxWidth().background(color.transparent)) {
            Row(Modifier.fillMaxWidth()) {
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = color.cardBackground
                    modifier = Modifier.weight(1F).align(Alignment.CenterVertically)
                    roundedSize = 0.dp
                }) {
                    customText(CustomTextData().apply {
                        text = data.title
                        textColor = color.cardParagraph
                        textSize = FThemeUtil.textUnit(20F)
                        textAlign = TextAlign.Center
                        modifier = Modifier.fillMaxWidth().padding(10.dp)
                    })
                }
            }
            dividerHorizontal(color.absoluteBlack)
            Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                Arrangement.Start,
                Alignment.CenterVertically) {
                if (data.leftBtnText.isNotBlank()) {
                    shapeRoundedBox(ShapeRoundedBoxData().apply {
                        backgroundColor = color.buttonBackground
                        modifier = Modifier.weight(1F).wrapContentHeight()
                            .align(Alignment.CenterVertically)
                            .clickable { data.relayCommand?.execute(MessageDialogVM.ClickEvent.LEFT)}
                        roundedSize = 0.dp
                    }) {
                        customText(CustomTextData().apply {
                            text = data.leftBtnText
                            textColor = color.buttonForeground
                            textSize = FThemeUtil.textUnit(18F)
                            textAlign = TextAlign.Center
                            modifier = Modifier.fillMaxWidth().padding(10.dp)
                        })
                    }
                }
                if (data.leftBtnText.isNotBlank() && data.rightBtnText.isNotBlank()) {
                    dividerVertical(color.secondary)
                }
                if (data.rightBtnText.isNotBlank()) {
                    shapeRoundedBox(ShapeRoundedBoxData().apply {
                        backgroundColor = color.buttonBackground
                        modifier = Modifier.weight(1F).wrapContentHeight()
                            .align(Alignment.CenterVertically)
                            .clickable { data.relayCommand?.execute(MessageDialogVM.ClickEvent.RIGHT)}
                        roundedSize = 0.dp
                    }) {
                        customText(CustomTextData().apply {
                            text = data.rightBtnText
                            textColor = color.buttonForeground
                            textSize = FThemeUtil.textUnit(18F)
                            textAlign = TextAlign.Center
                            modifier = Modifier.fillMaxWidth().padding(10.dp)
                        })
                    }
                }
            }
        }
    }
}

//@Preview
@Composable
private fun previewMessageDialog() {
    messageDialog(MessageDialogData().apply {
        title = "프리뷰 타이틀"
        leftBtnText = "왼쪽버튼"
        rightBtnText = "오른쪽버튼"
    })
}