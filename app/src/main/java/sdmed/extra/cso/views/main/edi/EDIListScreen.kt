package sdmed.extra.cso.views.main.edi

import sdmed.extra.cso.R
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.retrofit.edi.EDIState
import sdmed.extra.cso.models.retrofit.edi.EDIType
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.theme.FThemeUtil
import java.util.UUID

@Composable
fun ediListScreenTopContainer(startDate: String,
                                endDate: String,
                                relayCommand: ICommand? = null) {
    val color = FThemeUtil.safeColor()
    Row(Modifier.fillMaxWidth().background(color.background)) {
        Column(Modifier.clickable { relayCommand?.execute(EDIScreenVM.ClickEvent.START_DATE) }) {
            Card(Modifier.semantics { selected = false }
                .clip(RoundedCornerShape(5.dp))
                .clip(RoundedCornerShape(5.dp))
                .padding(1.dp),
                RoundedCornerShape(5.dp),
                cardSelectColor(false)) {
                customText(CustomTextData().apply {
                    text = startDate
                    textSize = FThemeUtil.textUnit(18F)
                    modifier = Modifier.padding(5.dp)
                })
            }
        }
        Column(Modifier.clickable { relayCommand?.execute(EDIScreenVM.ClickEvent.END_DATE) }) {
            Card(Modifier.semantics { selected = false }
                .clip(RoundedCornerShape(5.dp))
                .clip(RoundedCornerShape(5.dp))
                .padding(1.dp),
                RoundedCornerShape(5.dp),
                cardSelectColor(false)) {
                customText(CustomTextData().apply {
                    text = endDate
                    textSize = FThemeUtil.textUnit(18F)
                    modifier = Modifier.padding(5.dp)
                })
            }
        }
        Column(Modifier.clickable { relayCommand?.execute(EDIScreenVM.ClickEvent.SEARCH) }) {
            Card(Modifier.semantics { selected = false }
                .clip(RoundedCornerShape(5.dp))
                .clip(RoundedCornerShape(5.dp))
                .padding(1.dp),
                RoundedCornerShape(5.dp),
                cardSelectColor(false)) {
                customText(CustomTextData().apply {
                    text = stringResource(R.string.search_desc)
                    textSize = FThemeUtil.textUnit(18F)
                    modifier = Modifier.padding(5.dp)
                })
            }
        }
    }
}
@Composable
fun ediListScreenEdiList(ediItems: List<EDIUploadModel>,
                           lazyListState: LazyListState,
                           relayCommand: ICommand? = null) {
    val color = FThemeUtil.safeColor()
    LazyColumn(Modifier.fillMaxWidth().background(color.background),
        lazyListState) {
        items(ediItems, { it.thisPK }) { x -> itemContainer(x, relayCommand) }
        item { Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars)) }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun itemContainer(data: EDIUploadModel, relayCommand: ICommand? = null) {
    val color = FThemeUtil.safeColor()
    data.relayCommand = relayCommand
    Card(Modifier.fillMaxWidth()
        .semantics { selected = data.isSelected.value }
        .clip(RoundedCornerShape(5.dp))
        .combinedClickable(onClick = { data.onClick(EDIUploadModel.ClickEvent.OPEN) }, onLongClick = { data.onLongClick(EDIUploadModel.ClickEvent.OPEN) })
        .clip(RoundedCornerShape(5.dp)),
        RoundedCornerShape(5.dp),
        cardSelectColor(data.isSelected.value)) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 10.dp)) {
            customText(CustomTextData().apply {
                text = data.getRegDateString()
                textColor = color.cardForeground
                textSize = FThemeUtil.textUnit(18F)
                modifier = Modifier.align(Alignment.CenterVertically)
            })
            Row(Modifier.weight(1F).padding(horizontal = 10.dp).align(Alignment.CenterVertically)) {
                Column(Modifier.align(Alignment.CenterVertically)) {
                    customText(CustomTextData().apply {
                        text = data.orgName
                        textColor = if (data.isDefault) color.foreground else color.cardParagraph
                        textSize = FThemeUtil.textUnit(18F)
                        maxLines = 1
                        overflow = TextOverflow.Ellipsis
                    })
                }
                Column(Modifier.fillMaxWidth().align(Alignment.CenterVertically)) {
                    if (!data.isDefault) {
                        customText(CustomTextData().apply {
                            text = data.tempOrgString
                            textColor = color.cardForeground
                            textSize = FThemeUtil.textUnit(16F)
                            maxLines = 1
                            overflow = TextOverflow.Ellipsis
                        })
                    }
                }
            }
            Column(Modifier) {
                shapeRoundedBox(ShapeRoundedBoxData().apply {
                }) {
                    customText(CustomTextData().apply {
                        text = data.ediState.desc
                        textColor = data.getEdiColor()
                        textSize = FThemeUtil.textUnit(18F)
                    })
                }
            }
        }
    }
}

@Composable
private fun cardSelectColor(isSelected: Boolean): CardColors {
    val color = FThemeUtil.safeColor()
    return CardDefaults.cardColors(
        containerColor = if (isSelected) color.senary
        else color.cardBackground
    )
}

@Preview
@Composable
private fun previewTop() {
    ediListScreenTopContainer("2025-01-01", "2025-01-31")
}
@Preview
@Composable
private fun previewItem() {
    itemContainer(EDIUploadModel().apply {
        regDate = "2025-01-02"
        ediState = EDIState.Reject
        orgName = "떙떙떙 병원 asdf aasd fas asdfa sdf"
        tempOrgName = "ABC 병원"
        ediType = EDIType.NEW
        isSelected.value = true
    })
}
@Preview
@Composable
private fun previewList() {
    val list = mutableListOf<EDIUploadModel>()
    list.add(EDIUploadModel().apply {
        thisPK = UUID.randomUUID().toString()
        regDate = "2025-01-02"
        ediState = EDIState.Reject
        orgName = "신규 병원"
        tempOrgName = "ABC 병원"
        ediType = EDIType.NEW
        isSelected.value = true
    })
    list.add(EDIUploadModel().apply {
        thisPK = UUID.randomUUID().toString()
        regDate = "2025-02-03"
        ediState = EDIState.Pending
        orgName = "이관 병원"
        tempOrgName = "ABC 병원 asdfajsldk fdalksdfjd al"
        ediType = EDIType.TRANSFER
        isSelected.value = false
    })
    list.add(EDIUploadModel().apply {
        thisPK = UUID.randomUUID().toString()
        regDate = "2025-03-04"
        ediState = EDIState.OK
        orgName = "기냥 병원"
        tempOrgName = "ABC 병원"
        ediType = EDIType.DEFAULT
        isSelected.value = false
    })
    val lazyListState = rememberLazyListState()
    ediListScreenEdiList(list, lazyListState)
}