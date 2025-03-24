package sdmed.extra.cso.views.theme

import androidx.compose.ui.graphics.Color
import sdmed.extra.cso.interfaces.theme.IBaseColor

object LightColor: IBaseColor {
    override val calendarPurple = Color(0xFFBB86FC)
    override val calendarSecondary = Color(0xFF15C7FF)
    override val calendarRed = Color(0xFFFF404D)
    override val calendarRed2 = Color(0xFFFFD4C9)
    override val teal200 = Color(0xFF03DAC5)

    override val primary = Color(0xFF8C7851)
    override val secondary = Color(0xFFEADDCF)
    override val tertiary = Color(0xFFF25042)
    override val gray = Color(0xFF9FA4AB)
    override val black = Color(0xFF000000)
    override val white = Color(0xFFFFFFFF)

    override val absoluteBlack = Color(0xFF000000)
    override val absoluteWhite = Color(0xFFFFFFFF)
    override val transparent = Color(0x00000000)
    override val defBackground = Color(0xFFF9F4EF)
    override val defForeground = Color(0xFF020826)
    override val defParagraph = Color(0xFF716040)
    override val halfBackground = Color(0x6FFFFFFF)
    override val halfForeground = Color(0x6F000000)
    override val defStroke = Color(0xFF020826)
    override val defButtonBackground = Color(0xFF8C7851)
    override val defButtonForeground = Color(0xFFFFFFFE)
    override val defCardBackground = Color(0xFFEADDCF)
    override val defCardForeground = Color(0xFF020826)
    override val defCardParagraph = Color(0xFF716040)

    override val color1F000000 = Color(0x1F000000)
    override val color1A000000 = Color(0x1A000000)
    override val colorAA000000 = Color(0xAA000000)
    override val colorAAFFFFFF = Color(0xAAFFFFFF)

    override val recyclerOdd = Color(0xFFFBDD74)
    override val recyclerEven = Color(0xFFF9BC60)
    override val dividerGray = Color(0xFF7F848B)
    override val disableForeGray = Color(0xFF777B7E)
    override val disableBackGray = Color(0xFFD9DDDC)

    override val ediStateNone = defForeground
    override val ediStateOk = Color(0xFFFFFD85)
    override val ediStateReject = Color(0xFFF5516D)
    override val ediStatePending = Color(0xFFC5C6A9)
    override val ediStatePartial = Color(0xFFB293B4)
    override val ediBackStateNone = defBackground
    override val ediBackStateOk = Color(0xFF9BD770)
    override val ediBackStateReject = Color(0xFFF88B9D)
    override val ediBackStatePending = Color(0xFFCCB7CD)
    override val ediBackStatePartial = Color(0xFFFFFC5C)

    override val qnaStateNone = defForeground
    override val qnaStateOk = Color(0xFFFFFD85)
    override val qnaStateRecep = Color(0xFF4DD17F)
    override val qnaStateReply = Color(0xFF2D3047)
    override val qnaBackStateNone = defBackground
    override val qnaBackStateOk = Color(0xFF9BD770)
    override val qnaBackStateRecep = Color(0xFFC5C6A9)
    override val qnaBackStateReply = Color(0xFFB293B4)
}