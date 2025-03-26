package sdmed.extra.cso.interfaces.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

interface IBaseColor {
    val primary: Color
    val onPrimary: Color
    val primaryContainer: Color
    val onPrimaryContainer: Color
    val inversePrimary: Color
    val secondary: Color
    val onSecondary: Color
    val secondaryContainer: Color
    val onSecondaryContainer: Color
    val tertiary: Color
    val onTertiary: Color
    val tertiaryContainer: Color
    val onTertiaryContainer: Color
    val quaternary: Color
    val onQuaternary: Color
    val quaternaryContainer: Color
    val onQuaternaryContainer: Color
    val quinary: Color
    val onQuinary: Color
    val quinaryContainer: Color
    val onQuinaryContainer: Color
    val senary: Color
    val onSenary: Color
    val senaryContainer: Color
    val onSenaryContainer: Color
    val septenary: Color
    val onSeptenary: Color
    val septenaryContainer: Color
    val onSeptenaryContainer: Color
    val octonary: Color
    val onOctonary: Color
    val octonaryContainer: Color
    val onOctonaryContainer: Color
    val nonary: Color
    val onNonary: Color
    val nonaryContainer: Color
    val onNonaryContainer: Color
    val denary: Color
    val onDenary: Color
    val denaryContainer: Color
    val onDenaryContainer: Color

    val foreground: Color
    val paragraph: Color
    val background: Color
    val onBackground: Color
    val surface: Color
    val onSurface: Color
    val surfaceVariant: Color
    val onSurfaceVariant: Color
    val surfaceTint: Color
    val inverseSurface: Color
    val inverseOnSurface: Color
    val error: Color
    val onError: Color
    val errorContainer: Color
    val onErrorContainer: Color
    val outline: Color
    val outlineVariant: Color
    val scrim: Color

    val buttonBackground: Color
    val buttonForeground: Color
    val cardBackground: Color
    val cardForeground: Color
    val cardParagraph: Color

    val gray: Color
    val black: Color
    val white: Color
    val absoluteBlack: Color
    val absoluteWhite: Color
    val transparent: Color
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

    fun materialTheme(): ColorScheme
}