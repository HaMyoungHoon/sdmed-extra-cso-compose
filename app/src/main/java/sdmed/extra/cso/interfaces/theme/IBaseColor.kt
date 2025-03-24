package sdmed.extra.cso.interfaces.theme

import androidx.compose.ui.graphics.Color

interface IBaseColor {
    val calendarPurple: Color
    val calendarSecondary: Color
    val calendarRed: Color
    val calendarRed2: Color
    val teal200: Color

    val primary: Color
    val secondary: Color
    val tertiary: Color
    val gray: Color
    val black: Color
    val white: Color

    val absoluteBlack: Color
    val absoluteWhite: Color
    val transparent: Color
    val defBackground: Color
    val defForeground: Color
    val defParagraph: Color
    val halfBackground: Color
    val halfForeground: Color
    val defStroke: Color
    val defButtonBackground: Color
    val defButtonForeground: Color
    val defCardBackground: Color
    val defCardForeground: Color
    val defCardParagraph: Color

    val color1F000000: Color
    val color1A000000: Color
    val colorAA000000: Color
    val colorAAFFFFFF: Color

    val recyclerOdd: Color
    val recyclerEven: Color

    val dividerGray: Color
    val disableForeGray: Color
    val disableBackGray: Color

    val ediStateNone: Color
    val ediStateOk: Color
    val ediStateReject: Color
    val ediStatePending: Color
    val ediStatePartial: Color
    val ediBackStateNone: Color
    val ediBackStateOk: Color
    val ediBackStateReject: Color
    val ediBackStatePending: Color
    val ediBackStatePartial: Color

    val qnaStateNone: Color
    val qnaStateOk: Color
    val qnaStateRecep: Color
    val qnaStateReply: Color
    val qnaBackStateNone: Color
    val qnaBackStateOk: Color
    val qnaBackStateRecep: Color
    val qnaBackStateReply: Color
}