/*
 * AsosIcons.kt
 * Module: :core:designsystem   ·   Work package: WP-01
 *
 * EVERY icon in the Asos app lives in this one file. They are taken path-for-path from the
 * design canvas (24×24 line icons, rounded caps/joins, 1.8–2 px strokes) and use the
 * Asos palette.
 *
 * How to use:
 *   Monochrome icons are drawn in Navy (#172B4D) and are meant to be tinted:
 *       Icon(AsosIcons.Search, contentDescription = "Qidirish", tint = AsosIconTint.Muted)
 *   Multicolour icons (Verified, CheckCircleFilled, XCircleFilled, CircleOutline, LogoMark,
 *   Google) already carry their palette colours. Draw them with tint = Color.Unspecified:
 *       Icon(AsosIcons.Verified, contentDescription = "Tasdiqlangan", tint = Color.Unspecified)
 *
 * Catalogue with recommended tints: docs/design/ICONS.md  ·  visual sheet: docs/design/icons-preview.html
 * This file is generated from the design. Add new icons here, in the same style.
 */
package uz.asos.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

/** Recommended icon tints from the Asos palette (mirror of theme/Color.kt). */
object AsosIconTint {
    /** #172B4D: Default icon colour, main text. */
    val Navy = Color(0xFF172B4D)
    /** #5E6A80: Secondary icons, inactive tab. */
    val Muted = Color(0xFF5E6A80)
    /** #3A4A66: Tertiary icons. */
    val Text3 = Color(0xFF3A4A66)
    /** #8A94A6: Decoration only, locked states. */
    val SoftGray = Color(0xFF8A94A6)
    /** #1C64C0: Selected tab, links, icon buttons on Brand50. */
    val Active = Color(0xFF1C64C0)
    /** #2478DE: Large brand surfaces, progress. */
    val Brand = Color(0xFF2478DE)
    /** #FFFFFF: Icons on Brand600/Navy surfaces. */
    val OnBrand = Color(0xFFFFFFFF)
    /** #1E9E63: Correct, read, online. */
    val Success = Color(0xFF1E9E63)
    /** #D9433A: Wrong, logout. */
    val Error = Color(0xFFD9433A)
    /** #D9780A: Highlights, partial score. */
    val Warning = Color(0xFFD9780A)
    /** #D9780A: Streak flame. */
    val Streak = Color(0xFFD9780A)
    /** #5B3CC4: Dictionary accent. */
    val Dictionary = Color(0xFF5B3CC4)
    /** #0F7A68: Achievement accent. */
    val Achievement = Color(0xFF0F7A68)
}

object AsosIcons {

    // ---------------------------------------------------------------- Navigation

    /** Back (Orqaga) on every pushed screen. Recommended tint: Navy. */
    val ChevronLeft: ImageVector by lazy {
        icon(
            "ChevronLeft", 24f,
            Layer("m15 6-6 6 6 6", stroke = Color(0xFF172B4D), width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Row / card disclosure. Recommended tint: Muted. */
    val ChevronRight: ImageVector by lazy {
        icon(
            "ChevronRight", 24f,
            Layer("m9 6 6 6-6 6", stroke = Color(0xFF172B4D), width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Primary CTA arrow (Kirish, Home cards). Recommended tint: OnBrand. */
    val ArrowRight: ImageVector by lazy {
        icon(
            "ArrowRight", 24f,
            Layer("M5 12h14M13 6l6 6-6 6", stroke = Color(0xFF172B4D), width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Expand section, show more replies. Recommended tint: Muted. */
    val ChevronDown: ImageVector by lazy {
        icon(
            "ChevronDown", 24f,
            Layer("m6 9 6 6 6-6", stroke = Color(0xFF172B4D), width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Collapse section. Recommended tint: Muted. */
    val ChevronUp: ImageVector by lazy {
        icon(
            "ChevronUp", 24f,
            Layer("m18 15-6-6-6 6", stroke = Color(0xFF172B4D), width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Close quiz (Testni yopish). Recommended tint: Navy. */
    val Close: ImageVector by lazy {
        icon(
            "Close", 24f,
            Layer("M6 6l12 12M18 6 6 18", stroke = Color(0xFF172B4D), width = 2f, cap = StrokeCap.Round),
        )
    }

    /** Send to AI assistant. Recommended tint: OnBrand. */
    val ArrowUp: ImageVector by lazy {
        icon(
            "ArrowUp", 24f,
            Layer("M12 19V5M5 12l7-7 7 7", stroke = Color(0xFF172B4D), width = 2.2f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    // ---------------------------------------------------------------- Status

    /** Verified lawyer (Tasdiqlangan). Multicolour: use tint = Color.Unspecified. */
    val Verified: ImageVector by lazy {
        icon(
            "Verified", 24f,
            Layer("M12 2l2.4 1.8 3-.2.9 2.9 2.5 1.7-1 2.8 1 2.8-2.5 1.7-.9 2.9-3-.2L12 22l-2.4-1.8-3 .2-.9-2.9-2.5-1.7 1-2.8-1-2.8 2.5-1.7.9-2.9 3 .2z", fill = Color(0xFF2478DE)),
            Layer("m8.5 12 2.5 2.5 4.5-5", stroke = Color(0xFFFFFFFF), width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Read (O‘qilgan), correct answer, completed, solved. Multicolour: use tint = Color.Unspecified. */
    val CheckCircleFilled: ImageVector by lazy {
        icon(
            "CheckCircleFilled", 24f,
            Layer("M2 12a10 10 0 1 0 20 0a10 10 0 1 0 -20 0z", fill = Color(0xFF1E9E63)),
            Layer("m7.5 12 3 3 6-6", stroke = Color(0xFFFFFFFF), width = 2.2f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Not started (Boshlanmagan). Multicolour: use tint = Color.Unspecified. */
    val CircleOutline: ImageVector by lazy {
        icon(
            "CircleOutline", 24f,
            Layer("M2.5 12a9.5 9.5 0 1 0 19 0a9.5 9.5 0 1 0 -19 0z", stroke = Color(0xFFD5DDE9), width = 2f),
        )
    }

    /** Wrong answer (Noto‘g‘ri javob). Multicolour: use tint = Color.Unspecified. */
    val XCircleFilled: ImageVector by lazy {
        icon(
            "XCircleFilled", 24f,
            Layer("M2 12a10 10 0 1 0 20 0a10 10 0 1 0 -20 0z", fill = Color(0xFFD9433A)),
            Layer("M8.5 8.5l7 7M15.5 8.5l-7 7", stroke = Color(0xFFFFFFFF), width = 2.2f, cap = StrokeCap.Round),
        )
    }

    // ---------------------------------------------------------------- Communication

    /** Call lawyer / hotline. Recommended tint: Active. */
    val Phone: ImageVector by lazy {
        icon(
            "Phone", 24f,
            Layer("M5 3h3l2 5-2.5 1.5a11 11 0 0 0 5 5L14 12l5 2v3a2 2 0 0 1-2 2A15 15 0 0 1 3 5a2 2 0 0 1 2-2z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Video call. Recommended tint: Active. */
    val Video: ImageVector by lazy {
        icon(
            "Video", 24f,
            Layer("M5 6h9a2 2 0 0 1 2 2v8a2 2 0 0 1 -2 2h-9a2 2 0 0 1 -2 -2v-8a2 2 0 0 1 2 -2z", stroke = Color(0xFF172B4D), width = 1.9f, join = StrokeJoin.Round),
            Layer("m16 10 5-3v10l-5-3z", stroke = Color(0xFF172B4D), width = 1.9f, join = StrokeJoin.Round),
        )
    }

    /** Anonymous posting (Anonim). Recommended tint: Muted. */
    val EyeOff: ImageVector by lazy {
        icon(
            "EyeOff", 24f,
            Layer("M3 3l18 18", stroke = Color(0xFF172B4D), width = 2f, cap = StrokeCap.Round),
            Layer("M10.6 6.1A10 10 0 0 1 12 6c5 0 9 6 9 6a17 17 0 0 1-3 3.5M6.6 6.6C4.2 8.2 3 12 3 12s4 6 9 6a9 9 0 0 0 4.4-1.2", stroke = Color(0xFF172B4D), width = 2f, cap = StrokeCap.Round),
        )
    }

    /** Comments count, "Muhokamada". Recommended tint: Muted. */
    val Comment: ImageVector by lazy {
        icon(
            "Comment", 24f,
            Layer("M21 12a8 8 0 0 1-11.6 7.1L4 20l1-4.6A8 8 0 1 1 21 12z", stroke = Color(0xFF172B4D), width = 1.9f, join = StrokeJoin.Round),
        )
    }

    /** Asos AI yordamchi. Recommended tint: OnBrand. */
    val Sparkles: ImageVector by lazy {
        icon(
            "Sparkles", 24f,
            Layer("M12 3l1.8 5.2L19 10l-5.2 1.8L12 17l-1.8-5.2L5 10l5.2-1.8z", stroke = Color(0xFF172B4D), width = 1.8f, join = StrokeJoin.Round),
            Layer("M19 15l.8 2.2L22 18l-2.2.8L19 21l-.8-2.2L16 18l2.2-.8z", stroke = Color(0xFF172B4D), width = 1.8f, join = StrokeJoin.Round),
        )
    }

    /** Book a time (Vaqt band qilish). Recommended tint: Active. */
    val Calendar: ImageVector by lazy {
        icon(
            "Calendar", 24f,
            Layer("M5 5h14a2 2 0 0 1 2 2v12a2 2 0 0 1 -2 2h-14a2 2 0 0 1 -2 -2v-12a2 2 0 0 1 2 -2z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round),
            Layer("M3 10h18M8 3v4M16 3v4", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round),
        )
    }

    // ---------------------------------------------------------------- Content

    /** File attachment (.docx). Recommended tint: Active. */
    val DocumentLines: ImageVector by lazy {
        icon(
            "DocumentLines", 24f,
            Layer("M14 3H6a1 1 0 0 0-1 1v16a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V8z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M14 3v5h5M9 13h6M9 17h4", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Dictionary (Lug‘at). Recommended tint: Dictionary. */
    val BookOpen: ImageVector by lazy {
        icon(
            "BookOpen", 24f,
            Layer("M2 5h6a4 4 0 0 1 4 4v11a3 3 0 0 0-3-3H2zM22 5h-6a4 4 0 0 0-4 4v11a3 3 0 0 1 3-3h7z", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Official text (Rasmiy matn). Recommended tint: Active. */
    val Document: ImageVector by lazy {
        icon(
            "Document", 24f,
            Layer("M14 3H6a1 1 0 0 0-1 1v16a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V8z", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M14 3v5h5", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Plain summary (Sodda qilib aytganda). Recommended tint: Warning. */
    val Lightbulb: ImageVector by lazy {
        icon(
            "Lightbulb", 24f,
            Layer("M9 18h6M10 21h4M12 3a6 6 0 0 0-3.5 10.9c.6.5 1 1.2 1 2.1h5c0-.9.4-1.6 1-2.1A6 6 0 0 0 12 3z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Achievement: Birinchi qadam. Recommended tint: Active. */
    val Book: ImageVector by lazy {
        icon(
            "Book", 24f,
            Layer("M4 4.5A1.5 1.5 0 0 1 5.5 3H20v15H5.5A1.5 1.5 0 0 0 4 19.5z", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M4 19.5A1.5 1.5 0 0 0 5.5 21H20v-3", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Language setting. Recommended tint: Navy. */
    val Globe: ImageVector by lazy {
        icon(
            "Globe", 24f,
            Layer("M3 12a9 9 0 1 0 18 0a9 9 0 1 0 -18 0z", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round),
            Layer("M3 12h18M12 3c3 3.5 3 14.5 0 18M12 3c-3 3.5-3 14.5 0 18", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round),
        )
    }

    /** Quiz timer, reading time. Recommended tint: Muted. */
    val Clock: ImageVector by lazy {
        icon(
            "Clock", 24f,
            Layer("M3 12a9 9 0 1 0 18 0a9 9 0 1 0 -18 0z", stroke = Color(0xFF172B4D), width = 2f, cap = StrokeCap.Round),
            Layer("M12 7v5l3 2", stroke = Color(0xFF172B4D), width = 2f, cap = StrokeCap.Round),
        )
    }

    // ---------------------------------------------------------------- Actions

    /** Attach file in chat. Recommended tint: Text3. */
    val Attach: ImageVector by lazy {
        icon(
            "Attach", 24f,
            Layer("m20 11-8.5 8.5a5 5 0 0 1-7-7L13 4a3.5 3.5 0 0 1 5 5l-8.5 8.5a2 2 0 0 1-3-3L14 7", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Voice search, chat voice input. Recommended tint: Muted. */
    val Microphone: ImageVector by lazy {
        icon(
            "Microphone", 24f,
            Layer("M12 3h0a3 3 0 0 1 3 3v5a3 3 0 0 1 -3 3h-0a3 3 0 0 1 -3 -3v-5a3 3 0 0 1 3 -3z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round),
            Layer("M5 11a7 7 0 0 0 14 0M12 18v3", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round),
        )
    }

    /** Send message / reply (Yuborish). Recommended tint: OnBrand. */
    val Send: ImageVector by lazy {
        icon(
            "Send", 24f,
            Layer("M21 3 3 10.5l7 3 3 7z", stroke = Color(0xFF172B4D), width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("m10 13.5 4-4", stroke = Color(0xFF172B4D), width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Search fields (Qidirish). Recommended tint: Muted. */
    val Search: ImageVector by lazy {
        icon(
            "Search", 24f,
            Layer("M4 11a7 7 0 1 0 14 0a7 7 0 1 0 -14 0z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round),
            Layer("m20 20-3.5-3.5", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round),
        )
    }

    /** Save / saved (Saqlash, Saqlanganlar). Recommended tint: Navy. */
    val Bookmark: ImageVector by lazy {
        icon(
            "Bookmark", 24f,
            Layer("M6 3h12v18l-6-4-6 4z", stroke = Color(0xFF172B4D), width = 1.8f, join = StrokeJoin.Round),
        )
    }

    /** Saved state of Bookmark (Saqlangan), filled. Recommended tint: Active. */
    val BookmarkFilled: ImageVector by lazy {
        icon(
            "BookmarkFilled", 24f,
            Layer("M6 3h12v18l-6-4-6 4z", fill = Color(0xFF172B4D)),
        )
    }

    /** "Barchasi" community chip. Recommended tint: Navy. */
    val ListAll: ImageVector by lazy {
        icon(
            "ListAll", 24f,
            Layer("M4 6h16M4 12h16M4 18h10", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round),
        )
    }

    /** Vote up. Recommended tint: Muted / Active when voted. */
    val Upvote: ImageVector by lazy {
        icon(
            "Upvote", 24f,
            Layer("M12 4 5 12h4v8h6v-8h4z", fill = Color(0xFF172B4D)),
        )
    }

    /** Vote down. Recommended tint: Muted. */
    val Downvote: ImageVector by lazy {
        icon(
            "Downvote", 24f,
            Layer("M12 20l7-8h-4V4H9v8H5z", stroke = Color(0xFF172B4D), width = 1.9f, join = StrokeJoin.Round),
        )
    }

    /** Share (Ulashish). Recommended tint: Navy. */
    val Share: ImageVector by lazy {
        icon(
            "Share", 24f,
            Layer("M15.5 5a2.5 2.5 0 1 0 5 0a2.5 2.5 0 1 0 -5 0z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round),
            Layer("M3.5 12a2.5 2.5 0 1 0 5 0a2.5 2.5 0 1 0 -5 0z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round),
            Layer("M15.5 19a2.5 2.5 0 1 0 5 0a2.5 2.5 0 1 0 -5 0z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round),
            Layer("m8.2 10.8 7.6-4.4M8.2 13.2l7.6 4.4", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round),
        )
    }

    /** Ask a question (Savol berish). Recommended tint: OnBrand. */
    val Edit: ImageVector by lazy {
        icon(
            "Edit", 24f,
            Layer("M4 20l4-1 11-11a2.1 2.1 0 0 0-3-3L5 16z", stroke = Color(0xFF172B4D), width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Checkbox tick. Recommended tint: OnBrand. */
    val Check: ImageVector by lazy {
        icon(
            "Check", 24f,
            Layer("m5 12 5 5 9-10", stroke = Color(0xFF172B4D), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Sort (Saralash), settings (Sozlamalar). Recommended tint: Navy. */
    val Sliders: ImageVector by lazy {
        icon(
            "Sliders", 24f,
            Layer("M4 6h10M18 6h2M4 12h4M12 12h8M4 18h12", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round),
            Layer("M14 6a2 2 0 1 0 4 0a2 2 0 1 0 -4 0z", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round),
            Layer("M8 12a2 2 0 1 0 4 0a2 2 0 1 0 -4 0z", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round),
            Layer("M16 18a2 2 0 1 0 4 0a2 2 0 1 0 -4 0z", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round),
        )
    }

    /** Continue reading. Recommended tint: OnBrand. */
    val Play: ImageVector by lazy {
        icon(
            "Play", 24f,
            Layer("M8 5v14l11-7z", fill = Color(0xFF172B4D)),
        )
    }

    /** Listen to term (Tinglash). Recommended tint: Active. */
    val VolumeUp: ImageVector by lazy {
        icon(
            "VolumeUp", 24f,
            Layer("M11 5 6 9H3v6h3l5 4z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M15.5 8.5a5 5 0 0 1 0 7M18.5 5.5a9 9 0 0 1 0 13", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Notifications, daily reminders. Recommended tint: Navy. */
    val Bell: ImageVector by lazy {
        icon(
            "Bell", 24f,
            Layer("M6 8a6 6 0 1 1 12 0c0 7 3 8 3 8H3s3-1 3-8", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M10 20a2 2 0 0 0 4 0", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Chiqish. Recommended tint: Error. */
    val Logout: ImageVector by lazy {
        icon(
            "Logout", 24f,
            Layer("M15 4h4a1 1 0 0 1 1 1v14a1 1 0 0 1-1 1h-4M10 8l-4 4 4 4M6 12h10", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    // ---------------------------------------------------------------- Tab bar

    /** Tab: Bosh sahifa; community Uy-joy chip. Recommended tint: Active/Inactive. */
    val Home: ImageVector by lazy {
        icon(
            "Home", 24f,
            Layer("M3 10.5 12 3l9 7.5V20a1 1 0 0 1-1 1h-5v-6h-6v6H4a1 1 0 0 1-1-1z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Tab: Profil; personal info row. Recommended tint: Active/Inactive. */
    val Person: ImageVector by lazy {
        icon(
            "Person", 24f,
            Layer("M8 8a4 4 0 1 0 8 0a4 4 0 1 0 -8 0z", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round),
            Layer("M4 21c0-4 3.6-6.5 8-6.5s8 2.5 8 6.5", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round),
        )
    }

    /** Tab: Konstitutsiya. Recommended tint: Active/Inactive. */
    val TabConstitution: ImageVector by lazy {
        icon(
            "TabConstitution", 24f,
            Layer("M4 4.5A1.5 1.5 0 0 1 5.5 3H20v15H5.5A1.5 1.5 0 0 0 4 19.5z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M4 19.5A1.5 1.5 0 0 0 5.5 21H20v-3", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M9 7.5h7M9 11h5", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Tab: O‘rganish. Recommended tint: Active/Inactive. */
    val TabLearn: ImageVector by lazy {
        icon(
            "TabLearn", 24f,
            Layer("M2 9l10-5 10 5-10 5z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M6 11v5c0 1.5 3 3 6 3s6-1.5 6-3v-5", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M22 9v6", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Tab: Hamjamiyat. Recommended tint: Active/Inactive. */
    val TabCommunity: ImageVector by lazy {
        icon(
            "TabCommunity", 24f,
            Layer("M21 12a8 8 0 0 1-11.6 7.1L4 20l1-4.6A8 8 0 1 1 21 12z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M8.5 12h.01M12 12h.01M15.5 12h.01", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    // ---------------------------------------------------------------- Topics

    /** Community chip: Ta’lim; quiz CTA. Recommended tint: Navy. */
    val GraduationCap: ImageVector by lazy {
        icon(
            "GraduationCap", 24f,
            Layer("M2 9l10-5 10 5-10 5z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M6 11v5c0 1.5 3 3 6 3s6-1.5 6-3v-5", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Community chip: Mehnat. Recommended tint: Navy. */
    val BriefcaseSimple: ImageVector by lazy {
        icon(
            "BriefcaseSimple", 24f,
            Layer("M5 7h14a2 2 0 0 1 2 2v9a2 2 0 0 1 -2 2h-14a2 2 0 0 1 -2 -2v-9a2 2 0 0 1 2 -2z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M9 7V5a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v2", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Community chip: Oila. Recommended tint: Navy. */
    val People: ImageVector by lazy {
        icon(
            "People", 24f,
            Layer("M5.5 8a3.5 3.5 0 1 0 7 0a3.5 3.5 0 1 0 -7 0z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M2.5 20c0-3.5 3-5.5 6.5-5.5s6.5 2 6.5 5.5", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M14.5 9a2.5 2.5 0 1 0 5 0a2.5 2.5 0 1 0 -5 0z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Legal help / Sud. Recommended tint: Active. */
    val Scales: ImageVector by lazy {
        icon(
            "Scales", 24f,
            Layer("M12 3v18M7 21h10M5 7h14", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M5 7l-3 7a3 3 0 0 0 6 0zM19 7l-3 7a3 3 0 0 0 6 0z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Topic: Shaxsiy huquqlar. Recommended tint: Active. */
    val ShieldCheck: ImageVector by lazy {
        icon(
            "ShieldCheck", 24f,
            Layer("M12 3 4 6v6c0 5 3.5 8 8 9 4.5-1 8-4 8-9V6z", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("m9 12 2 2 4-4", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Topic: Siyosiy huquqlar. Recommended tint: Active. */
    val Ballot: ImageVector by lazy {
        icon(
            "Ballot", 24f,
            Layer("M5.5 11h13a1.5 1.5 0 0 1 1.5 1.5v7a1.5 1.5 0 0 1 -1.5 1.5h-13a1.5 1.5 0 0 1 -1.5 -1.5v-7a1.5 1.5 0 0 1 1.5 -1.5z", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M8 11V4h8v7", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("m10 7.5 1.5 1.5L14 6.5", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Topic: Ijtimoiy huquqlar. Recommended tint: Active. */
    val Briefcase: ImageVector by lazy {
        icon(
            "Briefcase", 24f,
            Layer("M5 7h14a2 2 0 0 1 2 2v9a2 2 0 0 1 -2 2h-14a2 2 0 0 1 -2 -2v-9a2 2 0 0 1 2 -2z", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M9 7V5a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v2M3 13h18", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Topic: Burchlar (duties). Recommended tint: Active. */
    val ClipboardCheck: ImageVector by lazy {
        icon(
            "ClipboardCheck", 24f,
            Layer("M7 4h10a2 2 0 0 1 2 2v13a2 2 0 0 1 -2 2h-10a2 2 0 0 1 -2 -2v-13a2 2 0 0 1 2 -2z", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M9 4V3h6v1", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("m9 13 2 2 4-4", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Topic: Oila va yoshlar. Recommended tint: Active. */
    val Family: ImageVector by lazy {
        icon(
            "Family", 24f,
            Layer("M5.5 8a3.5 3.5 0 1 0 7 0a3.5 3.5 0 1 0 -7 0z", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M2.5 20c0-3.5 3-5.5 6.5-5.5s6.5 2 6.5 5.5", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M14.5 9a2.5 2.5 0 1 0 5 0a2.5 2.5 0 1 0 -5 0z", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M17 14.5c2.5 0 4.5 1.5 4.5 4", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Topic: Davlat hokimiyati. Recommended tint: Active. */
    val Government: ImageVector by lazy {
        icon(
            "Government", 24f,
            Layer("M3 21h18M4 10h16M12 3 3 8h18z", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M6 10v8M10 10v8M14 10v8M18 10v8", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    // ---------------------------------------------------------------- Learning

    /** Case score banner. Recommended tint: OnBrand. */
    val Trophy: ImageVector by lazy {
        icon(
            "Trophy", 24f,
            Layer("M8 4h8v5a4 4 0 0 1-8 0z", stroke = Color(0xFF172B4D), width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M8 6H5a3 3 0 0 0 3 4M16 6h3a3 3 0 0 1-3 4M12 13v4M8 21h8", stroke = Color(0xFF172B4D), width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Streak (kunlik seriya). Recommended tint: Streak. */
    val Flame: ImageVector by lazy {
        icon(
            "Flame", 24f,
            Layer("M12 3c1 3.5 5 5.5 5 10a5 5 0 0 1-10 0c0-2 1-3.5 2-4.5 0 2 1 3 2 3 0-3-1-5.5 1-8.5z", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Points, rating, level. Recommended tint: Warning. */
    val Star: ImageVector by lazy {
        icon(
            "Star", 24f,
            Layer("m12 3 2.7 5.6 6.1.9-4.4 4.3 1 6.1L12 17l-5.4 2.9 1-6.1L3.2 9.5l6.1-.9z", fill = Color(0xFF172B4D)),
        )
    }

    /** Achievement: Burchlar bilimdoni. Recommended tint: Achievement. */
    val TrophyLarge: ImageVector by lazy {
        icon(
            "TrophyLarge", 24f,
            Layer("M8 4h8v5a4 4 0 0 1-8 0z", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M8 6H5a3 3 0 0 0 3 4M16 6h3a3 3 0 0 1-3 4M12 13v4M8 21h8M9 17h6", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Locked achievement. Recommended tint: SoftGray. */
    val Lock: ImageVector by lazy {
        icon(
            "Lock", 24f,
            Layer("M7 11h10a2 2 0 0 1 2 2v6a2 2 0 0 1 -2 2h-10a2 2 0 0 1 -2 -2v-6a2 2 0 0 1 2 -2z", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M8 11V8a4 4 0 0 1 8 0v3", stroke = Color(0xFF172B4D), width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    // ---------------------------------------------------------------- Brand

    /** Logo glyph without the tile (watermarks, profile). Recommended tint: Navy / OnBrand. */
    val LogoPillars: ImageVector by lazy {
        icon(
            "LogoPillars", 32f,
            Layer("M4 12 16 4l12 8z", fill = Color(0xFF172B4D)),
            Layer("M7 14h3v10h-3z", fill = Color(0xFF172B4D)),
            Layer("M14.5 14h3v10h-3z", fill = Color(0xFF172B4D)),
            Layer("M22 14h3v10h-3z", fill = Color(0xFF172B4D)),
            Layer("M4 26h24v2.5h-24z", fill = Color(0xFF172B4D)),
        )
    }

    /** Asos logo: app icon, login, design system. Multicolour: use tint = Color.Unspecified. */
    val LogoMark: ImageVector by lazy {
        icon(
            "LogoMark", 32f,
            Layer("M9 0h14a9 9 0 0 1 9 9v14a9 9 0 0 1 -9 9h-14a9 9 0 0 1 -9 -9v-14a9 9 0 0 1 9 -9z", fill = Color(0xFF2478DE)),
            Layer("M7.5 12.5 16 7l8.5 5.5z", fill = Color(0xFFFFFFFF)),
            Layer("M10.1 14h1.2a0.6 0.6 0 0 1 0.6 0.6v7.3a0.6 0.6 0 0 1 -0.6 0.6h-1.2a0.6 0.6 0 0 1 -0.6 -0.6v-7.3a0.6 0.6 0 0 1 0.6 -0.6z", fill = Color(0xFFFFFFFF)),
            Layer("M15.4 14h1.2a0.6 0.6 0 0 1 0.6 0.6v7.3a0.6 0.6 0 0 1 -0.6 0.6h-1.2a0.6 0.6 0 0 1 -0.6 -0.6v-7.3a0.6 0.6 0 0 1 0.6 -0.6z", fill = Color(0xFFFFFFFF)),
            Layer("M20.7 14h1.2a0.6 0.6 0 0 1 0.6 0.6v7.3a0.6 0.6 0 0 1 -0.6 0.6h-1.2a0.6 0.6 0 0 1 -0.6 -0.6v-7.3a0.6 0.6 0 0 1 0.6 -0.6z", fill = Color(0xFFFFFFFF)),
            Layer("M8.1 23.5h15.8a0.6 0.6 0 0 1 0.6 0.6v1a0.6 0.6 0 0 1 -0.6 0.6h-15.8a0.6 0.6 0 0 1 -0.6 -0.6v-1a0.6 0.6 0 0 1 0.6 -0.6z", fill = Color(0xFFFFFFFF)),
        )
    }

    /** OneID login button. Recommended tint: Navy. */
    val OneId: ImageVector by lazy {
        icon(
            "OneId", 24f,
            Layer("M5.5 5h13a2.5 2.5 0 0 1 2.5 2.5v9a2.5 2.5 0 0 1 -2.5 2.5h-13a2.5 2.5 0 0 1 -2.5 -2.5v-9a2.5 2.5 0 0 1 2.5 -2.5z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M7 11a2 2 0 1 0 4 0a2 2 0 1 0 -4 0z", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            Layer("M6 16c.6-1.5 1.7-2.2 3-2.2s2.4.7 3 2.2M14.5 10h4M14.5 13.5h3", stroke = Color(0xFF172B4D), width = 1.9f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }

    /** Google login button (official colours). Multicolour: use tint = Color.Unspecified. */
    val Google: ImageVector by lazy {
        icon(
            "Google", 24f,
            Layer("M21.6 12.2c0-.7-.1-1.4-.2-2H12v3.8h5.4a4.6 4.6 0 0 1-2 3v2.5h3.2c1.9-1.7 3-4.3 3-7.3z", fill = Color(0xFF4285F4)),
            Layer("M12 22c2.7 0 5-.9 6.6-2.4l-3.2-2.5c-.9.6-2 1-3.4 1-2.6 0-4.8-1.8-5.6-4.1H3.1v2.6A10 10 0 0 0 12 22z", fill = Color(0xFF34A853)),
            Layer("M6.4 14c-.2-.6-.3-1.3-.3-2s.1-1.4.3-2V7.4H3.1a10 10 0 0 0 0 9.2z", fill = Color(0xFFFBBC05)),
            Layer("M12 5.9c1.5 0 2.8.5 3.8 1.5l2.9-2.9A10 10 0 0 0 3.1 7.4L6.4 10C7.2 7.7 9.4 5.9 12 5.9z", fill = Color(0xFFEA4335)),
        )
    }
}

private class Layer(
    val pathData: String,
    val fill: Color? = null,
    val stroke: Color? = null,
    val width: Float = 0f,
    val cap: StrokeCap = StrokeCap.Butt,
    val join: StrokeJoin = StrokeJoin.Miter,
)

private fun icon(name: String, viewport: Float, vararg layers: Layer): ImageVector =
    ImageVector.Builder(
        name = "AsosIcons.$name",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = viewport,
        viewportHeight = viewport,
    ).apply {
        layers.forEach { layer ->
            addPath(
                pathData = addPathNodes(layer.pathData),
                fill = layer.fill?.let(::SolidColor),
                stroke = layer.stroke?.let(::SolidColor),
                strokeLineWidth = layer.width,
                strokeLineCap = layer.cap,
                strokeLineJoin = layer.join,
            )
        }
    }.build()
