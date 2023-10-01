package `in`.opening.area.zustapp.login

import `in`.opening.area.zustapp.ui.theme.primaryColor
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import javax.inject.Singleton

const val COUNTRY_CODE_INDIA = "+91 "

@Singleton
class PrefixTransformation(private val prefix: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return prefixFilter(text, prefix)
    }
}

fun prefixFilter(number: AnnotatedString, prefix: String): TransformedText {

    val out = prefix + number.text
    val prefixOffset = prefix.length

    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return offset + prefixOffset
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= prefixOffset) return 0
            return offset - prefixOffset
        }
    }

    return TransformedText(AnnotatedString(out), numberOffsetTranslator)
}

val annotatedStringTAndC = buildAnnotatedString {
    append("By providing my phone number, I hereby agree and accept the ")
    pushStringAnnotation(tag = "term", annotation = APP_PRIVACY_URL)
    withStyle(style = SpanStyle(color = primaryColor)) {
        append("Terms & Conditions")
    }
    pop()
    append(" and ")
    pushStringAnnotation(tag = "policy", annotation = APP_PRIVACY_URL)
    withStyle(style = SpanStyle(color = primaryColor)) {
        append("Privacy Policy ")
    }
    append("in use of this app")
    pop()
}

@Composable
fun setTextFiledColors(): TextFieldColors {
    val containerColor = Color(0xffEFEFEF)
    return TextFieldDefaults.colors(
        focusedContainerColor = containerColor,
        unfocusedContainerColor = containerColor,
        disabledContainerColor = containerColor,
        cursorColor = Color.Black,
        focusedIndicatorColor = Color.White,
        unfocusedIndicatorColor = Color.White,
        disabledLabelColor = Color.White,
    )
}

@Composable
fun SetDrawableIcons(@DrawableRes drawable: Int) {
    return Icon(
        painter = painterResource(id = drawable),
        contentDescription = null
    )
}

@Composable
fun SetDrawableImage(@DrawableRes drawable: Int) {
    return Image(
        painter = painterResource(id = drawable),
        contentDescription = null
    )
}
