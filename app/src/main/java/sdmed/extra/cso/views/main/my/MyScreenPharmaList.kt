package sdmed.extra.cso.views.main.my

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import sdmed.extra.cso.R
import sdmed.extra.cso.models.retrofit.users.ExtraMyInfoPharma
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun myScreenPharmaList(dataContext: MyScreenVM, heightMax: Dp = Dp.Unspecified) {
    val color = FThemeUtil.safeColorC()
    val pharmaList by dataContext.pharmaList.collectAsState()
    Column(Modifier.padding(5.dp)) {
        customText(CustomTextData().apply {
            text = stringResource(R.string.own_pharma_list_desc)
            textColor = color.foreground
            textSize = FThemeUtil.textUnit(18F)
            modifier = Modifier.align(Alignment.CenterHorizontally)
        })
        LazyColumn(Modifier.fillMaxWidth().heightIn(0.dp, heightMax).background(color.background)) {
            pharmaItems(pharmaList)
        }
    }
}

private fun LazyListScope.pharmaItems(pharmaModel: MutableList<ExtraMyInfoPharma>) {
    items(pharmaModel) { x ->
        itemContainer(x)
    }
}

@Composable
private fun itemContainer(data: ExtraMyInfoPharma) {
    val color = FThemeUtil.safeColorC()
    Card(Modifier.fillMaxWidth()
        .semantics { selected = false }
        .clip(RoundedCornerShape(5.dp))
        .clickable { data.onClick(ExtraMyInfoPharma.ClickEvent.THIS) }
        .clip(RoundedCornerShape(5.dp)),
        RoundedCornerShape(5.dp),
        cardSelectColor()) {
        Column(Modifier.fillMaxWidth().padding(5.dp)) {
            customText(CustomTextData().apply {
                text = data.orgName
                textColor = color.cardParagraph
                textSize = FThemeUtil.textUnit(18F)
                modifier = Modifier.align(Alignment.Start)
                overflow = TextOverflow.Ellipsis
            })
            if (data.address.isNotBlank()) {
                customText(CustomTextData().apply {
                    text = data.address
                    textColor = color.cardForeground
                    textSize = FThemeUtil.textUnit(18F)
                    modifier = Modifier.align(Alignment.Start)
                    maxLines = 2
                    overflow = TextOverflow.Ellipsis
                })
            }
        }
    }
}

@Composable
private fun cardSelectColor(): CardColors {
    val color = FThemeUtil.safeColorC()
    return CardDefaults.cardColors(
        containerColor = color.cardBackground
    )
}

//@Preview
@Composable
fun previewMyScreenPharma() {
    myScreenPharmaList(MyScreenVM().apply { fakeInit() })
}