package uz.asos.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.R

val PlusJakartaSans = FontFamily(
    Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans_medium, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans_semibold, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold),
    Font(R.font.plus_jakarta_sans_extrabold, FontWeight.ExtraBold),
)

val SourceSerif4 = FontFamily(
    Font(R.font.source_serif_4_regular, FontWeight.Normal),
    Font(R.font.source_serif_4_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.source_serif_4_semibold, FontWeight.SemiBold),
    Font(R.font.source_serif_4_bold, FontWeight.Bold),
)

object AsosTextStyles {
    private val sans = TextStyle(fontFamily = PlusJakartaSans, color = AsosColors.Navy)
    private val serif = TextStyle(fontFamily = SourceSerif4, color = AsosColors.Navy)

    val ScreenTitle = sans.copy(fontSize = 28.sp, lineHeight = 34.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.025).em)
    val SectionTitle = sans.copy(fontSize = 19.sp, lineHeight = 24.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.01).em)
    val CardTitle = sans.copy(fontSize = 17.sp, lineHeight = 22.sp, fontWeight = FontWeight.ExtraBold)
    val RowTitle = sans.copy(fontSize = 15.sp, lineHeight = 20.sp, fontWeight = FontWeight.ExtraBold)
    val RowTitleRegular = sans.copy(fontSize = 14.5.sp, lineHeight = 19.sp, fontWeight = FontWeight.Bold)
    val Body = sans.copy(fontSize = 15.sp, lineHeight = 22.5.sp, fontWeight = FontWeight.Medium)
    val BodySmall = sans.copy(fontSize = 14.sp, lineHeight = 21.sp, fontWeight = FontWeight.Medium)
    val Meta = sans.copy(fontSize = 12.5.sp, lineHeight = 17.sp, fontWeight = FontWeight.Medium, color = AsosColors.Text2)
    val Subtitle = sans.copy(fontSize = 13.sp, lineHeight = 18.sp, fontWeight = FontWeight.SemiBold, color = AsosColors.Text2)
    val Overline = sans.copy(fontSize = 11.5.sp, lineHeight = 15.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 0.06.em)
    val Chip = sans.copy(fontSize = 13.5.sp, lineHeight = 18.sp, fontWeight = FontWeight.Bold)
    val Tag = sans.copy(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Bold)
    val Link = sans.copy(fontSize = 13.5.sp, lineHeight = 18.sp, fontWeight = FontWeight.Bold, color = AsosColors.Brand600)
    val Button = sans.copy(fontSize = 15.sp, lineHeight = 20.sp, fontWeight = FontWeight.ExtraBold)
    val TabLabel = sans.copy(fontSize = 10.5.sp, lineHeight = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.01).em)
    val Segment = sans.copy(fontSize = 14.sp, lineHeight = 18.sp, fontWeight = FontWeight.Bold)

    val ArticleNumberDisplay = serif.copy(fontSize = 46.sp, lineHeight = 46.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.02).em)
    val LegalBody = serif.copy(fontSize = 19.sp, lineHeight = 30.4.sp, fontWeight = FontWeight.Normal)
    val LegalPartNumber = serif.copy(fontSize = 13.sp, lineHeight = 16.sp, fontWeight = FontWeight.Bold, color = AsosColors.Brand600)
    val SerifNumber = serif.copy(fontSize = 24.sp, lineHeight = 26.sp, fontWeight = FontWeight.Bold)
    val SerifRoman = serif.copy(fontSize = 15.sp, lineHeight = 18.sp, fontWeight = FontWeight.Bold)
    val SerifQuote = serif.copy(fontSize = 16.sp, lineHeight = 24.sp, fontStyle = FontStyle.Italic)
}

internal val AsosMaterialTypography = Typography(
    displaySmall = AsosTextStyles.ArticleNumberDisplay,
    headlineMedium = AsosTextStyles.ScreenTitle,
    titleLarge = AsosTextStyles.SectionTitle,
    titleMedium = AsosTextStyles.CardTitle,
    titleSmall = AsosTextStyles.RowTitle,
    bodyLarge = AsosTextStyles.Body,
    bodyMedium = AsosTextStyles.BodySmall,
    bodySmall = AsosTextStyles.Meta,
    labelLarge = AsosTextStyles.Button,
    labelMedium = AsosTextStyles.Chip,
    labelSmall = AsosTextStyles.TabLabel,
)
