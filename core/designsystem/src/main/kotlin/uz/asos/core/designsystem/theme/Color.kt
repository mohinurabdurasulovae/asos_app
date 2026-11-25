/*
 * Color.kt
 * Module: :core:designsystem   ·   Work package: WP-01
 *
 * Colour tokens from the design (Komponentlar.dc.html). Features use these, never raw hex.
 */
package uz.asos.core.designsystem.theme

import androidx.compose.ui.graphics.Color

object AsosColors {
    /** Large surfaces, icons, progress. White small text on it is only 4.4:1. */
    val Brand500 = Color(0xFF2478DE)
    /** Buttons and links (white text passes 4.5:1). */
    val Brand600 = Color(0xFF1C64C0)
    /** Pressed / hover. */
    val Brand700 = Color(0xFF174F99)
    /** Selected states, info cards. */
    val Brand50 = Color(0xFFEAF2FD)
    val Brand100 = Color(0xFFDCE9FB)
    /** Highlighted row (e.g. chapter in progress). */
    val Brand25 = Color(0xFFF3F8FF)
    val Brand200 = Color(0xFFCFE0F7)
    val Brand300 = Color(0xFFBFD7F6)
    /** Light accent on Navy surfaces. */
    val BrandOnNavy = Color(0xFF7FB2F0)
    val BrandOnNavySoft = Color(0xFFA9CBF5)

    /** Main text, dark surfaces. */
    val Navy = Color(0xFF172B4D)
    /** Secondary text (5.4:1 on white). */
    val Text2 = Color(0xFF5E6A80)
    val Text3 = Color(0xFF3A4A66)
    /** Muted text on Navy surfaces. */
    val TextOnNavy = Color(0xFFC9D6EA)
    /** Decoration and icons only, never text. */
    val SoftGray = Color(0xFF8A94A6)

    val Background = Color(0xFFF5F8FC)
    val Surface = Color(0xFFFFFFFF)
    /** Neutral chip / segmented track. */
    val SurfaceMuted = Color(0xFFF0F3F8)
    val SegmentTrack = Color(0xFFE8EEF6)
    val Border = Color(0xFFE3EAF4)
    val BorderSoft = Color(0xFFE8EDF5)
    val Divider = Color(0xFFEEF2F8)
    val Track = Color(0xFFE6ECF5)
    val Outline = Color(0xFFDCE3EE)

    val Success = Color(0xFF1E9E63)
    val SuccessOnLight = Color(0xFF146B45)
    val SuccessText = Color(0xFF14533A)
    val SuccessBg = Color(0xFFEAF7F0)
    val SuccessBgSoft = Color(0xFFF3FAF7)

    val Error = Color(0xFFD9433A)
    val ErrorOnLight = Color(0xFFA1281F)
    val ErrorBg = Color(0xFFFDF2F1)

    val Warning = Color(0xFFD9780A)
    val WarningOnLight = Color(0xFF8A4B00)
    val WarningBg = Color(0xFFFDF1E1)

    val Teal = Color(0xFF0F6B5C)
    val TealIcon = Color(0xFF0F7A68)
    val TealBg = Color(0xFFE3F5F1)

    val Purple = Color(0xFF5B3CC4)
    val PurpleText = Color(0xFF4A2FA8)
    val PurpleBg = Color(0xFFEFEBFC)
    val PurpleBgSoft = Color(0xFFF6F3FE)
    val PurpleBorder = Color(0xFFE3D9FA)

    val Rose = Color(0xFF9A1F44)
    val RoseIcon = Color(0xFFB4234D)
    val RoseBg = Color(0xFFFCE9EE)

    val Orange = Color(0xFFA35A00)
    val OrangeBg = Color(0xFFFDF1E1)

    val Slate = Color(0xFF172B4D)
    val SlateBg = Color(0xFFEBEEF3)
}

/**
 * Colour pair for a `tone` sent by the API (Topic.tone, category tone):
 * brand, teal, orange, purple, pink, navy.
 */
data class AsosTone(val content: Color, val container: Color)

object AsosTones {
    val Brand = AsosTone(AsosColors.Brand600, AsosColors.Brand50)
    val Teal = AsosTone(AsosColors.Teal, AsosColors.TealBg)
    val Orange = AsosTone(AsosColors.WarningOnLight, AsosColors.WarningBg)
    val Purple = AsosTone(AsosColors.Purple, AsosColors.PurpleBg)
    val Pink = AsosTone(AsosColors.Rose, AsosColors.RoseBg)
    val Navy = AsosTone(AsosColors.Navy, AsosColors.SlateBg)
    val Neutral = AsosTone(AsosColors.Text3, AsosColors.SurfaceMuted)

    fun fromName(name: String?): AsosTone = when (name?.lowercase()) {
        "brand", "blue" -> Brand
        "teal", "green" -> Teal
        "orange", "amber", "warning" -> Orange
        "purple", "violet" -> Purple
        "pink", "rose" -> Pink
        "navy", "slate" -> Navy
        else -> Neutral
    }
}
