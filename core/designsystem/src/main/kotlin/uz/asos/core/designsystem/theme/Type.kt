/*
 * Type.kt
 * Module: :core:designsystem   ·   Work package: WP-01
 *
 * Typography.
 *  - Plus Jakarta Sans 400–800: all UI text
 *  - Source Serif 4 (400, 600, 700, italic): official legal text, article numbers, quotes
 * Sizes and weights are the ones used on the design canvas.
 */
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

/** Named text styles from the design. Colour is applied by the caller. */
object AsosTextStyles {
    private val sans = TextStyle(fontFamily = PlusJakartaSans, color = AsosColors.Navy)
    private val serif = TextStyle(fontFamily = SourceSerif4, color = AsosColors.Navy)

    /** Screen title: "Konstitutsiya" (28 / 800, -0.025em). */
    val ScreenTitle = sans.copy(fontSize = 28.sp, lineHeight = 34.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.025).em)
    /** Section heading inside a screen: "Hayotiy misollar" (19 / 800). */
    val SectionTitle = sans.copy(fontSize = 19.sp, lineHeight = 24.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.01).em)
    /** Card heading: "Sodda qilib aytganda" (17 / 800). */
    val CardTitle = sans.copy(fontSize = 17.sp, lineHeight = 22.sp, fontWeight = FontWeight.ExtraBold)
    /** Row title: chapter / section name (15 / 800). */
    val RowTitle = sans.copy(fontSize = 15.sp, lineHeight = 20.sp, fontWeight = FontWeight.ExtraBold)
    /** Row title, lighter: chapter rows (14.5 / 700). */
    val RowTitleRegular = sans.copy(fontSize = 14.5.sp, lineHeight = 19.sp, fontWeight = FontWeight.Bold)
    /** Body text (15 / 500, 1.5). */
    val Body = sans.copy(fontSize = 15.sp, lineHeight = 22.5.sp, fontWeight = FontWeight.Medium)
    /** Smaller body (14 / 500). */
    val BodySmall = sans.copy(fontSize = 14.sp, lineHeight = 21.sp, fontWeight = FontWeight.Medium)
    /** Meta line: "19–21-moddalar" (12.5 / 500). */
    val Meta = sans.copy(fontSize = 12.5.sp, lineHeight = 17.sp, fontWeight = FontWeight.Medium, color = AsosColors.Text2)
    /** Subtitle under a title: "2023-yil tahriri · …" (13 / 600). */
    val Subtitle = sans.copy(fontSize = 13.sp, lineHeight = 18.sp, fontWeight = FontWeight.SemiBold, color = AsosColors.Text2)
    /** Uppercase overline: "II BO‘LIM", "OXIRGI O‘QILGAN" (11.5 / 800, +0.06em). */
    val Overline = sans.copy(fontSize = 11.5.sp, lineHeight = 15.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 0.06.em)
    /** Pill / chip label (13.5 / 700). */
    val Chip = sans.copy(fontSize = 13.5.sp, lineHeight = 18.sp, fontWeight = FontWeight.Bold)
    /** Small tag (12 / 700). */
    val Tag = sans.copy(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Bold)
    /** Link text: "Kazuslar →" (13.5 / 700). */
    val Link = sans.copy(fontSize = 13.5.sp, lineHeight = 18.sp, fontWeight = FontWeight.Bold, color = AsosColors.Brand600)
    /** Primary button (15 / 800). */
    val Button = sans.copy(fontSize = 15.sp, lineHeight = 20.sp, fontWeight = FontWeight.ExtraBold)
    /** Tab bar label (10.5 / 700). */
    val TabLabel = sans.copy(fontSize = 10.5.sp, lineHeight = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.01).em)
    /** Segmented control label (14 / 700; selected 800). */
    val Segment = sans.copy(fontSize = 14.sp, lineHeight = 18.sp, fontWeight = FontWeight.Bold)

    /** "50-modda" display number (serif 46 / 700). */
    val ArticleNumberDisplay = serif.copy(fontSize = 46.sp, lineHeight = 46.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.02).em)
    /** Official text body (serif 19, line 1.6). Size changes with the reader text size. */
    val LegalBody = serif.copy(fontSize = 19.sp, lineHeight = 30.4.sp, fontWeight = FontWeight.Normal)
    /** Part number next to the official text (serif 13 / 700, Brand600). */
    val LegalPartNumber = serif.copy(fontSize = 13.sp, lineHeight = 16.sp, fontWeight = FontWeight.Bold, color = AsosColors.Brand600)
    /** Serif number tiles: "50", "IX" (24 / 700 for big tiles). */
    val SerifNumber = serif.copy(fontSize = 24.sp, lineHeight = 26.sp, fontWeight = FontWeight.Bold)
    /** Roman numeral in a chapter tile (15 / 700). */
    val SerifRoman = serif.copy(fontSize = 15.sp, lineHeight = 18.sp, fontWeight = FontWeight.Bold)
    /** Serif quote / italic example (16 / italic). */
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
