package it.macgood.yandexcupmusicapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import it.macgood.yandexcupmusicapp.R

private val yandex = FontFamily(
    Font(R.font.yandexsansdisplayregular, FontWeight.W400),
    Font(R.font.yandexsansdisplayregular, FontWeight.W500),
    Font(R.font.yandexsansdisplayregular, FontWeight.W600),
)

val YandexTypographicStyles = Typography(
    displayLarge = TextStyle(
        fontFamily = yandex,
        fontWeight = FontWeight.W500,
        fontSize = 30.sp
    ),
    displayMedium = TextStyle(
        fontFamily = yandex,
        fontWeight = FontWeight.W500,
        fontSize = 24.sp
    ),
    displaySmall = TextStyle(
        fontFamily = yandex,
        fontWeight = FontWeight.W500,
        fontSize = 20.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = yandex,
        fontWeight = FontWeight.W400,
        fontSize = 18.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = yandex,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = yandex,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp
    ),
    titleLarge = TextStyle(
        fontFamily = yandex,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = yandex,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = yandex,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = yandex,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = yandex,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp
    ),
    labelMedium= TextStyle(
        fontFamily = yandex,
        fontWeight = FontWeight.W400,
        fontSize = 10.sp
    )
)
