/*
 * AsosPreviews.kt
 * Module: :core:ui   ·   Work package: WP-01
 *
 * Multi-preview annotation: the design's phone size (390×844) and the same screen with
 * large system font, to catch text that overflows.
 */
package uz.asos.core.ui.preview

import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Telefon 390×844", widthDp = 390, heightDp = 844, showBackground = true, backgroundColor = 0xFFF5F8FC)
@Preview(name = "Katta shrift", widthDp = 390, heightDp = 844, fontScale = 1.3f, showBackground = true, backgroundColor = 0xFFF5F8FC)
annotation class AsosPreviews
