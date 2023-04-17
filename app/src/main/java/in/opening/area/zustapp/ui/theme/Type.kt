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


val openSansFontFamily = FontFamily(Font(R.font.open_sans_light, FontWeight.W300),
    Font(R.font.open_sans_regular, FontWeight.W400), Font(R.font.open_sans_medium, FontWeight.W500),
    Font(R.font.open_sans_semibold, FontWeight.W600), Font(R.font.open_sans_bold, FontWeight.W700))

val Typography_Mons by lazy {
    androidx.compose.material.Typography(
        body1 = TextStyle(
            fontFamily = zustFont,
            fontSize = 16.sp,
            fontWeight = FontWeight.W600
        ),
        body2 = TextStyle(
            fontFamily = zustFont,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500
        ),
        subtitle1 = TextStyle(fontFamily = zustFont,
            fontSize = 12.sp,
            fontWeight = FontWeight.W400
        ))
}

val Typography_OpenSans by lazy {
    androidx.compose.material.Typography(
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

val ZustTypography = Typography_OpenSans
val zustFont = openSansFontFamily


