package sdmed.extra.cso.views.main.my

import sdmed.extra.cso.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import sdmed.extra.cso.utils.fCoilLoad
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun myScreenFileList(dataContext: MyScreenVM) {
    val color = FThemeUtil.safeColorC()
    val thisData by dataContext.thisData.collectAsState()
    LazyRow(Modifier.fillMaxWidth().background(color.background).padding(5.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        item {
            Column(Modifier.widthIn(100.dp, 120.dp)) {
                Box(Modifier.width(100.dp).height(100.dp).align(Alignment.CenterHorizontally)
                    .clickable { dataContext.relayCommand.execute(MyScreenVM.ClickEvent.IMAGE_TRAINING)}) {
                    fCoilLoad(thisData.trainingUrl, thisData.trainingMimeType, thisData.trainingFilename)
                }
                customText(CustomTextData().apply {
                    text = thisData.trainingDate
                    textColor = color.foreground
                    textSize = FThemeUtil.textUnit(16F)
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                })
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                    backgroundColor = color.buttonBackground
                    modifier = Modifier
                        .clickable { dataContext.relayCommand.execute(MyScreenVM.ClickEvent.TRAINING_CERTIFICATE_ADD)}
                }) {
                    customText(CustomTextData().apply {
                        text = stringResource(R.string.training_certificate_add)
                        textColor = color.buttonForeground
                        textSize = FThemeUtil.textUnit(16F)
                        modifier = Modifier.padding(5.dp)
                    })
                }
            }
        }
        item {
            Column(Modifier.widthIn(100.dp, 120.dp)) {
                Box(Modifier.width(100.dp).height(100.dp).align(Alignment.CenterHorizontally)
                    .clickable { dataContext.relayCommand.execute(MyScreenVM.ClickEvent.IMAGE_TAXPAYER)}) {
                    fCoilLoad(thisData.taxPayerUrl, thisData.taxPayerMimeType, thisData.taxPayerFilename)
                }
                customText(CustomTextData().apply {
                    text = thisData.companyName
                    textColor = color.foreground
                    textSize = FThemeUtil.textUnit(16F)
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                })
                customText(CustomTextData().apply {
                    text = thisData.companyNumber
                    textColor = color.foreground
                    textSize = FThemeUtil.textUnit(16F)
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                })
            }
        }
        item {
            Column(Modifier.widthIn(100.dp, 120.dp)) {
                Box(Modifier.width(100.dp).height(100.dp).align(Alignment.CenterHorizontally)
                    .clickable { dataContext.relayCommand.execute(MyScreenVM.ClickEvent.IMAGE_BANK_ACCOUNT)}) {
                    fCoilLoad(thisData.csoReportUrl, thisData.csoReportMimeType, thisData.csoReportFilename)
                }
                customText(CustomTextData().apply {
                    text = thisData.csoReportNumber
                    textColor = color.foreground
                    textSize = FThemeUtil.textUnit(16F)
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                })
            }
        }
        item {
            Column(Modifier.widthIn(100.dp, 120.dp)) {
                Box(Modifier.width(100.dp).height(100.dp).align(Alignment.CenterHorizontally)
                    .clickable { dataContext.relayCommand.execute(MyScreenVM.ClickEvent.IMAGE_CSO_REPORT)}) {
                    fCoilLoad(thisData.bankAccountUrl, thisData.bankAccountMimeType, thisData.bankAccountFilename)
                }
                customText(CustomTextData().apply {
                    text = thisData.bankAccount
                    textColor = color.foreground
                    textSize = FThemeUtil.textUnit(16F)
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                    maxLines = 3
                })
            }
        }
        item {
            Column(Modifier.widthIn(100.dp, 120.dp)) {
                Box(Modifier.width(100.dp).height(100.dp).align(Alignment.CenterHorizontally)
                    .clickable { dataContext.relayCommand.execute(MyScreenVM.ClickEvent.IMAGE_MARKETING_CONTRACT)}) {
                    fCoilLoad(thisData.marketingContractUrl, thisData.marketingContractMimeType, thisData.marketingContractFilename)
                }
                customText(CustomTextData().apply {
                    text = thisData.contractDateString
                    textColor = color.foreground
                    textSize = FThemeUtil.textUnit(16F)
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                })
            }
        }
    }
}

//@Preview
@Composable
private fun previewMyScreenFileList() {
    myScreenFileList(MyScreenVM().apply { fakeInit() })
}