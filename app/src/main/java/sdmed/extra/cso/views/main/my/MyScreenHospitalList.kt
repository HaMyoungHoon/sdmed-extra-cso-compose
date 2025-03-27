package sdmed.extra.cso.views.main.my

import sdmed.extra.cso.R
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
import androidx.compose.runtime.LaunchedEffect
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
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.retrofit.hospitals.HospitalModel
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun myScreenHospitalList(dataContext: MyScreenVM, heightMax: Dp = Dp.Unspecified) {
    val color = FThemeUtil.safeColor()
    val hospitalList by dataContext.hosList.collectAsState()
    val selectHospital by dataContext.selectedHos.collectAsState()
    LaunchedEffect(selectHospital) {
        dataContext.pharmaList.value = selectHospital.pharmaList
    }
    Column(Modifier.padding(5.dp)) {
        customText(CustomTextData().apply {
            text = stringResource(R.string.own_hospital_list_desc)
            textColor = color.foreground
            textSize = FThemeUtil.textUnit(18F)
            modifier = Modifier.align(Alignment.CenterHorizontally)
        })
        LazyColumn(Modifier.fillMaxWidth().heightIn(0.dp, heightMax).background(color.background)) {
            hospitalItems(hospitalList, selectHospital, dataContext.relayCommand)
        }
    }
}

private fun LazyListScope.hospitalItems(hospitalModel: MutableList<HospitalModel>, selectHospital: HospitalModel, relayCommand: ICommand) {
    items(hospitalModel) { x ->
        itemContainer(x, selectHospital.thisPK == x.thisPK, relayCommand)
    }
}

@Composable
private fun itemContainer(data: HospitalModel, select: Boolean = false, relayCommand: ICommand) {
    val color = FThemeUtil.safeColor()
    data.relayCommand = relayCommand
    Card(Modifier.fillMaxWidth()
        .semantics { selected = select }
        .clip(RoundedCornerShape(5.dp))
        .clickable { data.onClick(HospitalModel.ClickEvent.THIS) }
        .clip(RoundedCornerShape(5.dp)),
        RoundedCornerShape(5.dp),
        cardSelectColor(select)) {
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
                    overflow = TextOverflow.Ellipsis
                })
            }
        }
    }
}

@Composable
private fun cardSelectColor(isSelected: Boolean): CardColors {
    val color = FThemeUtil.safeColor()
    return CardDefaults.cardColors(
        containerColor = if (isSelected) color.septenary
        else color.cardBackground
    )
}

//@Preview
@Composable
fun previewMyScreenHospital() {
    myScreenHospitalList(MyScreenVM().apply { fakeInit() })
}