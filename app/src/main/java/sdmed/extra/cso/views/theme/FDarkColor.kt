package sdmed.extra.cso.views.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import sdmed.extra.cso.interfaces.theme.IBaseColor

object FDarkColor: IBaseColor {
    override val primary = Color(0xFF8BD3DD)
    override val onPrimary = Color(0xFF003B3C)
    override val primaryContainer = Color(0xFF3A7C81)
    override val onPrimaryContainer = Color(0xFFE1F5F6)
    override val inversePrimary = Color(0xFF3A7C81)
    override val secondary = Color(0xFFF3D2C1)
    override val onSecondary = Color(0xFF4E2F1D)
    override val secondaryContainer = Color(0xFFB56F4A)
    override val onSecondaryContainer = Color(0xFFBE8E77)
    override val tertiary = Color(0xFFF582AE)
    override val onTertiary = Color(0xFF6A1D3D)
    override val tertiaryContainer = Color(0xFFC84C77)
    override val onTertiaryContainer = Color(0xFFF3B1C6)
    override val quaternary = Color(0xFF5E81AC)
    override val onQuaternary = Color(0xFF1E2A47)
    override val quaternaryContainer = Color(0xFF3A5D83)
    override val onQuaternaryContainer = Color(0xFFBEC6D4)
    override val quinary = Color(0xFF81C995)
    override val onQuinary = Color(0xFF2A4C35)
    override val quinaryContainer = Color(0xFF5A8B5E)
    override val onQuinaryContainer = Color(0xFF8B9F89)
    override val senary = Color(0xFFFFB86C)
    override val onSenary = Color(0xFF2F3D2C)
    override val senaryContainer = Color(0xFFB5833A)
    override val onSenaryContainer = Color(0xFF7A5F3D)
    override val septenary = Color(0xFFC792EA)
    override val onSeptenary = Color(0xFF4E2269)
    override val septenaryContainer = Color(0xFF9C4DCE)
    override val onSeptenaryContainer = Color(0xFFD6A0D7)
    override val octonary = Color(0xFFE05A47)
    override val onOctonary = Color(0xFF3A2B2A)
    override val octonaryContainer = Color(0xFF9C3C2F)
    override val onOctonaryContainer = Color(0xFF9E7A72)
    override val nonary = Color(0xFF4C566A)
    override val onNonary = Color(0xFF2D3B46)
    override val nonaryContainer = Color(0xFF5F6A75)
    override val onNonaryContainer = Color(0xFFBCC0C5)
    override val denary = Color(0xFFECEFF4)
    override val onDenary = Color(0xFF3B4252)
    override val denaryContainer = Color(0xFFB1BCC7)
    override val onDenaryContainer = Color(0xFF7A8B94)

    override val foreground = Color(0xFFFFFFFE)
    override val paragraph = Color(0xFF94A1B2)
    override val background = Color(0xFF16161A)
    override val onBackground = Color(0xFFE4E4E7)
    override val surface = Color(0xFF25262C)
    override val onSurface = Color(0xFFE4E4E7)
    override val surfaceVariant = Color(0xFF35373D)
    override val onSurfaceVariant = Color(0xFFE0E0E3)
    override val surfaceTint = Color(0xFF4D7C8A)
    override val inverseSurface = Color(0xFFE4E4E7)
    override val inverseOnSurface = Color(0xFF16161A)
    override val error = Color(0xFFB00020)
    override val onError = Color(0xFFFFFFFF)
    override val errorContainer = Color(0xFF37002B)
    override val onErrorContainer = Color(0xFFEF9A9A)
    override val outline = Color(0xFF010101)
    override val outlineVariant = Color(0xFF303030)
    override val scrim = Color(0x99000000)

    override val buttonBackground = Color(0xFFFF8906)
    override val buttonForeground = Color(0xFFFFFFF0)
    override val cardBackground = Color(0xFF0F0E17)
    override val cardForeground = Color(0xFFFFFFFE)
    override val cardParagraph = Color(0xFFA7A9BE)

    override val gray = Color(0xFF9FA4AB)
    override val black = Color(0xFFFFFFFF)
    override val white = Color(0xFF000000)
    override val absoluteBlack = Color(0xFF000000)
    override val absoluteWhite = Color(0xFFFFFFFF)
    override val transparent = Color(0x00000000)
    override val dividerGray = Color(0xFF9FA4AB)
    override val disableForeGray = Color(0xFF777B7E)
    override val disableBackGray = Color(0xFFD9DDDC)

    override val ediStateNone = foreground
    override val ediStateOk = Color(0xFFFFFD85)
    override val ediStateReject = Color(0xFFF5516D)
    override val ediStatePending = Color(0xFFC5C6A9)
    override val ediStatePartial = Color(0xFFB293B4)
    override val ediBackStateNone = background
    override val ediBackStateOk = Color(0xFF9BD770)
    override val ediBackStateReject = Color(0xFFF88B9D)
    override val ediBackStatePending = Color(0xFFCCB7CD)
    override val ediBackStatePartial = Color(0xFFFFFC5C)

    override val qnaStateNone = foreground
    override val qnaStateOk = Color(0xFFFFFD85)
    override val qnaStateRecep = Color(0xFF4DD17F)
    override val qnaStateReply = Color(0xFF2D3047)
    override val qnaBackStateNone = background
    override val qnaBackStateOk = Color(0xFF9BD770)
    override val qnaBackStateRecep = Color(0xFFC5C6A9)
    override val qnaBackStateReply = Color(0xFFB293B4)

    override fun materialTheme(): ColorScheme {
        return darkColorScheme(
            primary,
            onPrimary,
            primaryContainer,
            onPrimaryContainer,
            inversePrimary,
            secondary,
            onSecondary,
            secondaryContainer,
            onSecondaryContainer,
            tertiary,
            onTertiary,
            tertiaryContainer,
            onTertiaryContainer,
            background,
            onBackground,
            surface,
            onSurface,
            surfaceVariant,
            onSurfaceVariant,
            surfaceTint,
            inverseSurface,
            inverseOnSurface,
            error,
            onError,
            errorContainer,
            onErrorContainer,
            outline,
            outlineVariant,
            scrim
        )
    }
}