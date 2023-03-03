package `in`.opening.area.zustapp.ui.theme

import `in`.opening.area.zustapp.R
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val fontLight = Font(R.font.okra_regular, FontWeight.Light)
val fontRegular = Font(R.font.okra_regular, FontWeight.Normal)
val fontMedium = Font(R.font.okra_medium, FontWeight.Medium)
val fontSemiBold = Font(R.font.okra_semibold, FontWeight.SemiBold)
val fontBold = Font(R.font.okra_semibold, FontWeight.Bold)
val okraFontFamily = FontFamily(fontRegular, fontMedium, fontSemiBold, fontBold, fontLight)


val montserrat = FontFamily(Font(R.font.montserrat_light, FontWeight.W300),
    Font(R.font.montserrat_regular, FontWeight.W400), Font(R.font.montserrat_medium, FontWeight.W500),
    Font(R.font.montserrat_semibold, FontWeight.W600), Font(R.font.montserrat_bold, FontWeight.W700))


val Typography_Montserrat by lazy {
    androidx.compose.material.Typography(
        body1 = TextStyle(
            fontFamily = montserrat,
            fontSize = 16.sp,
            fontWeight = FontWeight.W600
        ),
        body2 = TextStyle(
            fontFamily = montserrat,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500
        ),
        subtitle1 = TextStyle(fontFamily = montserrat,
            fontSize = 12.sp,
            fontWeight = FontWeight.W400
        ))
}




