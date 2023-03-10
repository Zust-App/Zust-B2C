package `in`.opening.area.zustapp.login

import `in`.opening.area.zustapp.ui.theme.primaryColor
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.material.Icon
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
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
    pushStringAnnotation(tag = "term", annotation = "https://google.com/policy")
    withStyle(style = SpanStyle(color = primaryColor)) {
        append("Terms & Conditions")
    }
    pop()
    append(" and ")
    pushStringAnnotation(tag = "policy", annotation = "https://google.com/terms")
    withStyle(style = SpanStyle(color = primaryColor)) {
        append("Privacy Policy ")
    }
    append("in use of this app")
    pop()
}

@Composable
fun setTextFiledColors(): TextFieldColors {
    return TextFieldDefaults.textFieldColors(
        cursorColor = Color.Black,
        disabledLabelColor = Color.White,
        focusedIndicatorColor = Color.White,
        unfocusedIndicatorColor = Color.White,
        backgroundColor = Color(0xffEFEFEF))
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
