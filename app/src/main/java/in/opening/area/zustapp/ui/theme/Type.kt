package `in`.opening.area.zustapp.ui.theme

import `in`.opening.area.zustapp.R
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

class FontManager {
    companion object {
        val fontLight = Font(R.font.okra_regular, FontWeight.Light)
        val fontRegular = Font(R.font.okra_regular, FontWeight.Normal)
        val fontMedium = Font(R.font.okra_medium, FontWeight.Medium)
        val fontSemiBold = Font(R.font.okra_semibold, FontWeight.SemiBold)
        val fontBold = Font(R.font.okra_semibold, FontWeight.Bold)
        val okraFontFamily = FontFamily(fontRegular, fontMedium, fontSemiBold, fontBold, fontLight)
    }
}

val okraFontFamily = FontFamily(
    FontManager.fontLight,
    FontManager.fontRegular,
    FontManager.fontMedium,
    FontManager.fontSemiBold,
    FontManager.fontBold)

val montserratFontFamily = FontFamily(
    Font(R.font.montserrat_light, FontWeight.W300),
    Font(R.font.montserrat_regular, FontWeight.W400),
    Font(R.font.montserrat_medium, FontWeight.W500),
    Font(R.font.montserrat_semibold, FontWeight.W600),
    Font(R.font.montserrat_bold, FontWeight.W700))

val anuphanFontFamily = FontFamily(
    Font(R.font.anuphan_light, FontWeight.W300),
    Font(R.font.anuhan_regular, FontWeight.W400),
    Font(R.font.anuphan_medium, FontWeight.W500),
    Font(R.font.anuphan_semibold, FontWeight.W600),
    Font(R.font.anuphan_bold, FontWeight.W700))

val allotropeFontFamily = FontFamily(
    Font(R.font.allotrope_regular, FontWeight.W400),
    Font(R.font.allotrope_medium, FontWeight.W500),
    Font(R.font.allotrope_bold, FontWeight.W700))

val blinkerFontFamily = FontFamily(
    Font(R.font.blinker_regular, FontWeight.W400),
    Font(R.font.blinker_semibold, FontWeight.W500),
    Font(R.font.blinker_bold, FontWeight.W500),
    Font(R.font.blinker_extra_bold, FontWeight.W700))


val openSansFontFamily = FontFamily(Font(R.font.open_sans_light, FontWeight.W300),
    Font(R.font.open_sans_regular, FontWeight.W400), Font(R.font.open_sans_medium, FontWeight.W500),
    Font(R.font.open_sans_semibold, FontWeight.W600), Font(R.font.open_sans_bold, FontWeight.W700))

val Typography_Mons by lazy {
    androidx.compose.material.Typography(
        h1 = TextStyle(
            fontFamily = montserratFontFamily,
            fontSize = 18.sp,
            fontWeight = FontWeight.W700
        ),
        body1 = TextStyle(
            fontFamily = montserratFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.W600
        ),
        body2 = TextStyle(
            fontFamily = montserratFontFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500
        ),
        subtitle1 = TextStyle(fontFamily = montserratFontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.W400
        ))
}

val Typography_OpenSans by lazy {
    androidx.compose.material.Typography(
        h1 = TextStyle(
            fontFamily = anuphanFontFamily,
            fontSize = 18.sp,
            fontWeight = FontWeight.W700
        ),
        body1 = TextStyle(
            fontFamily = openSansFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.W600
        ),
        body2 = TextStyle(
            fontFamily = openSansFontFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500
        ),
        subtitle1 = TextStyle(fontFamily = openSansFontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.W400
        ))
}


val Typography_Okra by lazy {

    androidx.compose.material.Typography(
        h1 = TextStyle(
            fontFamily = anuphanFontFamily,
            fontSize = 18.sp,
            fontWeight = FontWeight.W700
        ),
        body1 = TextStyle(
            fontFamily = okraFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.W600
        ),
        body2 = TextStyle(
            fontFamily = okraFontFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500
        ),
        subtitle1 = TextStyle(fontFamily = okraFontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.W400
        ))
}


val Typography_Anuphan by lazy {
    androidx.compose.material.Typography(
        h1 = TextStyle(
            fontFamily = anuphanFontFamily,
            fontSize = 18.sp,
            fontWeight = FontWeight.W700
        ),
        body1 = TextStyle(
            fontFamily = anuphanFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.W600
        ),
        body2 = TextStyle(
            fontFamily = anuphanFontFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500
        ),
        subtitle1 = TextStyle(fontFamily = anuphanFontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.W400
        ))
}

val Typography_Allotrope by lazy {

    androidx.compose.material.Typography(
        h1 = TextStyle(
            fontFamily = allotropeFontFamily,
            fontSize = 18.sp,
            fontWeight = FontWeight.W600
        ),
        body1 = TextStyle(
            fontFamily = allotropeFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.W500
        ),
        body2 = TextStyle(
            fontFamily = allotropeFontFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500
        ),
        subtitle1 = TextStyle(fontFamily = allotropeFontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.W400
        ))
}

val Typography_Blinker by lazy {

    androidx.compose.material.Typography(
        h1 = TextStyle(
            fontFamily = blinkerFontFamily,
            fontSize = 18.sp,
            fontWeight = FontWeight.W600
        ),
        body1 = TextStyle(
            fontFamily = blinkerFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.W500
        ),
        body2 = TextStyle(
            fontFamily = blinkerFontFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500
        ),
        subtitle1 = TextStyle(fontFamily = blinkerFontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.W400
        ))
}

val ZustTypography = Typography_Allotrope
val zustFont = allotropeFontFamily

val zustTypographySecondary=Typography_Blinker
val zustFontSecondary=Typography_Blinker

//h1 is for title
//body1
//body1
//subtitl1
