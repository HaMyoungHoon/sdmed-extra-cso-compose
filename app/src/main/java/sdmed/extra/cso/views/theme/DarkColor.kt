package sdmed.extra.cso.views.theme

import androidx.compose.ui.graphics.Color
import sdmed.extra.cso.interfaces.theme.IBaseColor

object DarkColor: IBaseColor {
    override val calendarPurple = Color(0xFFBB86FC)
    override val calendarSecondary = Color(0xFFFFC715)
    override val calendarRed = Color(0xFFFF404D)
    override val calendarRed2 = Color(0xFFFFD4C9)
    override val teal200 = Color(0xFF03DAC5)

    override val primary = Color(0xFF8BD3DD)
    override val secondary = Color(0xFFF3D2C1)
    override val tertiary = Color(0xFFF582AE)
    override val gray = Color(0xFF9FA4AB)
    override val black = Color(0xFFFFFFFF)
    override val white = Color(0xFF000000)

    override val absoluteBlack = Color(0xFF000000)
    override val absoluteWhite = Color(0xFFFFFFFF)
    override val transparent = Color(0x00000000)
    override val defBackground = Color(0xFF16161A)
    override val defForeground = Color(0xFFFFFFFE)
    override val defParagraph = Color(0xFF94A1B2)
    override val halfBackground = Color(0x6FFFFFFF)
    override val halfForeground = Color(0x6F000000)
    override val defStroke = Color(0xFF010101)
    override val defButtonBackground = Color(0xFFFF8906)
    override val defButtonForeground = Color(0xFFFFFFF0)
    override val defCardBackground = Color(0xFF0F0E17)
    override val defCardForeground = Color(0xFFFFFFFE)
    override val defCardParagraph = Color(0xFFA7A9BE)

    override val color1F000000 = Color(0x1F000000)
    override val color1A000000 = Color(0x1A000000)
    override val colorAA000000 = Color(0xAA000000)
    override val colorAAFFFFFF = Color(0xAAFFFFFF)

    override val recyclerOdd = Color(0xFF232946)
    override val recyclerEven = Color(0xFF2D334A)
    override val dividerGray = Color(0xFF9FA4AB)
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