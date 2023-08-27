package ui

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val colorWhite = Color(0xffFfFfff)
val colorWhite1 = Color(0xffF5F5f5)
val colorWhite2 = Color(0xfff9f9f9)
val activityBackgroundLight = Color(0xfffdfdfd)
val colorBlack = Color(0xE51E1E1E)
val groceryHeader = Color(0xffF4E0BF)
val nonVegColorHeader = Color(0xFFAB444A)
val gradientColorsGrocery = listOf(Color(0xFFF0F0D8), Color(0xFFF0F0D8))
val gradientColorsNonVeg = listOf(Color(0xFFAB444A), Color(0xFFAB444A))

val linearGradientGroceryBrush = Brush.linearGradient(
    colors = gradientColorsGrocery
)


val linearGradientNonVegBrush = Brush.linearGradient(
    colors = gradientColorsNonVeg
)
