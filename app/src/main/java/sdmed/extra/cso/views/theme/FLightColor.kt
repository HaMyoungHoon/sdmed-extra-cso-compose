package sdmed.extra.cso.views.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import sdmed.extra.cso.interfaces.theme.IBaseColor

object FLightColor: IBaseColor {
    override val primary = Color(0xFF8C7851)
    override val onPrimary = Color(0xFFE0D5C6)
    override val primaryContainer = Color(0xFFF1E0C6)
    override val onPrimaryContainer = Color(0xFF8C7851)
    override val inversePrimary = Color(0xFF5A3D2A)
    override val secondary = Color(0xFFEADDCF)
    override val onSecondary = Color(0xFF6A5D4D)
    override val secondaryContainer = Color(0xFFC9C1A9)
    override val onSecondaryContainer = Color(0xFFEADDCF)
    override val tertiary = Color(0xFFF25042)
    override val onTertiary = Color(0xFF6F6B6F)
    override val tertiaryContainer = Color(0xFFFAD1D0)
    override val onTertiaryContainer = Color(0xFFF25042)
    override val quaternary = Color(0xFF4A90E2)
    override val onQuaternary = Color(0xFF8A9DCA)
    override val quaternaryContainer = Color(0xFFBDD8F0)
    override val onQuaternaryContainer = Color(0xFF4A90E2)
    override val quinary = Color(0xFF50BFA5)
    override val onQuinary = Color(0xFF77A59D)
    override val quinaryContainer = Color(0xFF99D0BD)
    override val onQuinaryContainer = Color(0xFF50BFA5)
    override val senary = Color(0xFFFFC857)
    override val onSenary = Color(0xFF5A4F4F)
    override val senaryContainer = Color(0xFFFFE6A2)
    override val onSenaryContainer = Color(0xFFFFC857)
    override val septenary = Color(0xFFB47EB3)
    override val onSeptenary = Color(0xFF9C8E9A)
    override val septenaryContainer = Color(0xFFDBC6D7)
    override val onSeptenaryContainer = Color(0xFFB47EB3)
    override val octonary = Color(0xFFE57373)
    override val onOctonary = Color(0xFF6B5B5B)
    override val octonaryContainer = Color(0xFFF1C1C1)
    override val onOctonaryContainer = Color(0xFFE57373)
    override val nonary = Color(0xFF90A4AE)
    override val onNonary = Color(0xFF4F4F4F)
    override val nonaryContainer = Color(0xFFB0BCC3)
    override val onNonaryContainer = Color(0xFF90A4AE)
    override val denary = Color(0xFFF5E6CC)
    override val onDenary = Color(0xFF6A5D4F)
    override val denaryContainer = Color(0xFFF9F0D7)
    override val onDenaryContainer = Color(0xFFF5E6CC)

    override val foreground = Color(0xFF020826)
    override val paragraph = Color(0xFF716040)
    override val background = Color(0xFFF9F4EF)
    override val onBackground = Color(0xFF020826)
    override val surface = Color(0xFFF5F5F5)
    override val onSurface = Color(0xFF020826)
    override val surfaceVariant = Color(0xFFE4E4E4)
    override val onSurfaceVariant = Color(0xFF020826)
    override val surfaceTint = Color(0xFF8C7851)
    override val inverseSurface = Color(0xFF716040)
    override val inverseOnSurface = Color(0xFFF0F0F0)
    override val error = Color(0xFFB00020)
    override val onError = Color(0xFFF0F0F0)
    override val errorContainer = Color(0xFFFFD6D6)
    override val onErrorContainer = Color(0xFFB00020)
    override val outline = Color(0xFF020826)
    override val outlineVariant = Color(0xFF3D4A62)
    override val scrim = Color(0x80020826)

    override val buttonBackground = Color(0xFF8C7851)
    override val buttonForeground = Color(0xFFFFFFFE)
    override val cardBackground = Color(0xFFEADDCF)
    override val cardForeground = Color(0xFF020826)
    override val cardParagraph = Color(0xFF716040)

    override val gray = Color(0xFF9FA4AB)
    override val black = Color(0xFF000000)
    override val white = Color(0xFFFFFFFF)
    override val absoluteBlack = Color(0xFF000000)
    override val absoluteWhite = Color(0xFFFFFFFF)
    override val transparent = Color(0x00000000)
    override val dividerGray = Color(0xFF7F848B)
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
        return lightColorScheme(
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