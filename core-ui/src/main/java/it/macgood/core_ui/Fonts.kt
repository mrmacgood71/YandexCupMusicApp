package it.macgood.core_ui

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

object Fonts {
    val SfProDisplay = FontFamily(
        Font(R.font.sf_pro_display_light_italic, FontWeight.Light, FontStyle.Italic),
        Font(R.font.sf_pro_display_regular, FontWeight.Normal),
        Font(R.font.sf_pro_display_medium, FontWeight.Medium),
        Font(R.font.sf_pro_display_bold, FontWeight.Bold)
    )
}