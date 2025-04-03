package sdmed.extra.cso.views.media.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import sdmed.extra.cso.R
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.utils.fCoilLoad
import sdmed.extra.cso.utils.fImageLoad
import sdmed.extra.cso.views.component.customText.CustomTextData
import sdmed.extra.cso.views.component.customText.customText
import sdmed.extra.cso.views.component.vector.FVectorData
import sdmed.extra.cso.views.component.vector.vectorArrowDown
import sdmed.extra.cso.views.component.vector.vectorCheck
import sdmed.extra.cso.views.component.vector.vectorCircle
import sdmed.extra.cso.views.component.vector.vectorCross
import sdmed.extra.cso.views.theme.FThemeUtil

@Composable
fun mediaPickerScreen(content: @Composable BoxScope.() -> Unit) {
    val color = FThemeUtil.safeColorC()
    Box(Modifier.windowInsetsPadding(WindowInsets.statusBars.only(WindowInsetsSides.Top))
        .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom))
        .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Horizontal))
        .fillMaxSize().background(color.background)) {
        content()
    }
}
@Composable
fun mediaPickerScreenPhone(dataContext: MediaPickerActivityVM) {
    mediaPickerScreen {
        Column(Modifier) {
            topContainer(dataContext)
            mediaContainer(dataContext)
            mediaListContainer(dataContext)
        }
    }
}
@Composable
fun mediaPickerScreenTablet(dataContext: MediaPickerActivityVM) {
    mediaPickerScreen {
        Column(Modifier) {
            topContainer(dataContext)
            mediaContainer(dataContext)
            mediaListContainer(dataContext)
        }
    }
}
@Composable
fun mediaPickerScreenTwoPane(dataContext: MediaPickerActivityVM, displayFeatures: List<DisplayFeature>) {
    val mediaUrl by dataContext.mediaUrl.collectAsState()
    mediaPickerScreen {
        mediaUrl?.let {
            Column(Modifier) {
                topContainer(dataContext)
                mediaListContainer(dataContext)
            }
        } ?: TwoPane({
            Column(Modifier) {
                topContainer(dataContext)
                mediaListContainer(dataContext)
            }},
            {
                mediaContainer(dataContext, true)
            }, HorizontalTwoPaneStrategy(0.5F, 5.dp), displayFeatures)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun topContainer(dataContext: MediaPickerActivityVM) {
    val color = FThemeUtil.safeColorC()
    val boxPosition by dataContext.boxesPosition.collectAsState()
    val confirmEnable by dataContext.confirmEnable.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val boxes by dataContext.boxes.collectAsState()
    LaunchedEffect(boxPosition) {
        dataContext.selectItem(boxPosition)
    }

    Box(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(10.dp),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically) {
            Icon(vectorCross(FVectorData(color.background, color.primary)), stringResource(R.string.close_desc),
                Modifier.clickable { dataContext.relayCommand.execute(MediaPickerActivityVM.ClickEvent.CLOSE) },
                Color.Unspecified)
            if (boxes.isNotEmpty()) {
                ExposedDropdownMenuBox(expanded, { expanded = !expanded },
                    Modifier) {
                    TextField(value = boxes[boxPosition],
                        textStyle = exposedTextStyle(),
                        onValueChange =  {},
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).background(color.background),
                        readOnly = true,
                        singleLine = true,
                        trailingIcon = { Icon(vectorArrowDown(FVectorData(color.background, color.primary)),
                            stringResource(R.string.select_desc),
                            Modifier,
                            Color.Unspecified) },
                        colors = exposedDropColor()
                    )
                    ExposedDropdownMenu(expanded, { expanded = false}) {
                        boxes.forEachIndexed { index, box ->
                            DropdownMenuItem({
                                customText(CustomTextData().apply {
                                    text = box
                                    textColor = color.paragraph
                                })
                            }, {
                                dataContext.boxesPosition.value = index
                                expanded = false
                            })
                        }
                    }
                }
            }

            val tintColor = if (confirmEnable) color.background else color.background
            val fillColor = if (confirmEnable) color.primary else color.disableForeGray
            val contentDescription = stringResource(R.string.confirm_desc)
            val tint = Color.Unspecified
            Box(Modifier.align(Alignment.CenterVertically).clickable {
                dataContext.relayCommand.execute(MediaPickerActivityVM.ClickEvent.CONFIRM)
            }, contentAlignment = Alignment.Center) {
                Icon(vectorCircle(FVectorData(tintColor, fillColor)), contentDescription, Modifier, tint)
                Icon(vectorCheck(FVectorData(tintColor, fillColor)), contentDescription, Modifier, tint)
            }
        }
    }
}
@Composable
private fun mediaContainer(dataContext: MediaPickerActivityVM, isFull: Boolean = false) {
    val mediaUrl by dataContext.mediaUrl.collectAsState()
    val mediaFileType by dataContext.mediaFileType.collectAsState()
    val mediaName by dataContext.mediaName.collectAsState()
    mediaUrl?.let {
        val modifier = if (isFull) Modifier.fillMaxSize() else Modifier.fillMaxWidth().height(300.dp)
        Box(modifier) {
            fImageLoad(it,
                mediaFileType,
                mediaName,
                Modifier.fillMaxSize(),
                ContentScale.FillWidth,
                512)
        }
    }
}
@Composable
private fun mediaListContainer(dataContext: MediaPickerActivityVM) {
    val color = FThemeUtil.safeColorC()
    val items by dataContext.items.collectAsState()
    val gridState = rememberLazyGridState()
    LazyVerticalGrid(GridCells.Fixed(3), Modifier.fillMaxSize(), gridState) {
        itemsIndexed(items, { index, item -> item.thisPK }) { index, item ->
            item.relayCommand = dataContext.relayCommand
            Box(Modifier.fillMaxWidth().aspectRatio(1F)
                .clickable { item.onClick(MediaPickerSourceModel.ClickEvent.SELECT) }) {
                val lastClick by item.lastClick.collectAsState()
                if (lastClick) {
                    Box(Modifier.fillMaxSize().zIndex(99F).background(color.scrim))
                }
                fImageLoad(item.mediaUrl,
                    item.mediaFileType,
                    item.mediaName,
                    Modifier.fillMaxSize(),
                    ContentScale.Crop)
                val num by item.num.collectAsState()
                val solid by item.solid.collectAsState()
                if (num != null && solid != null) {
                    Box(Modifier.align(Alignment.TopEnd).zIndex(100F), contentAlignment = Alignment.Center) {
                        customText(CustomTextData().apply {
                            text = num.toString()
                            textColor = color.absoluteWhite
                            textSize = FThemeUtil.textUnit(12F)
                            textAlign = TextAlign.Center
                            modifier = Modifier.zIndex(100F)
                        })
                        Icon(vectorCircle(FVectorData(color.transparent, Color(solid!!))),
                            stringResource(R.string.select_desc), Modifier, Color.Unspecified)
                    }
                }
            }
        }
    }
}

@Composable
private fun exposedTextStyle(): TextStyle = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 20.sp,
    lineHeight = 0.sp,
    letterSpacing = 0.sp
)
@Composable
private fun exposedDropColor(): TextFieldColors {
    val color = FThemeUtil.safeColorC()
    return TextFieldDefaults.colors(
        focusedTextColor = color.primary,
        unfocusedTextColor = color.primary,
        disabledTextColor = color.disableForeGray,
        errorTextColor = color.error,
        focusedContainerColor = color.primaryContainer,
        unfocusedContainerColor = color.primaryContainer,
        disabledContainerColor = color.paragraph,
        errorContainerColor = color.errorContainer,
        cursorColor = color.paragraph,
        errorCursorColor = color.error,
        selectionColors = null,
        focusedIndicatorColor = color.transparent,
        unfocusedIndicatorColor = color.transparent,
        disabledIndicatorColor = color.disableForeGray,
        errorIndicatorColor = color.errorContainer,
        focusedLeadingIconColor = color.primary,
        unfocusedLeadingIconColor = color.primary,
        disabledLeadingIconColor = color.disableForeGray,
        errorLeadingIconColor = color.error,
        focusedTrailingIconColor = color.primary,
        unfocusedTrailingIconColor = color.primary,
        disabledTrailingIconColor = color.disableForeGray,
        errorTrailingIconColor = color.error,
        focusedLabelColor = color.primary,
        unfocusedLabelColor = color.primary,
        disabledLabelColor = color.disableForeGray,
        errorLabelColor = color.error,
        focusedPlaceholderColor = color.primary,
        unfocusedPlaceholderColor = color.primary,
        disabledPlaceholderColor = color.disableForeGray,
        errorPlaceholderColor = color.error,
        focusedPrefixColor = color.primary,
        unfocusedPrefixColor = color.primary,
        disabledPrefixColor = color.disableForeGray,
        errorPrefixColor = color.error,
        focusedSuffixColor = color.primary,
        unfocusedSuffixColor = color.primary,
        disabledSuffixColor = color.disableForeGray,
        errorSuffixColor = color.error,
    )
}


//@Preview
@Composable
private fun previewTopContainer() {
    topContainer(MediaPickerActivityVM().apply {
        fakeInit()
    })
}