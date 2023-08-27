package `in`.opening.area.zustapp.home.components

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.DrawDashLine
import `in`.opening.area.zustapp.ui.theme.zustFont
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.opening.area.zustapp.ui.theme.dp_8

const val KEY_HOME_PAGE_BRAND = "brand"
fun LazyListScope.homePageBrandPromiseUi(isMainHomePage: Boolean = false) {
    item(key = KEY_HOME_PAGE_BRAND) {
        Column(modifier = padding_16Modifier) {
            Spacer(modifier = Modifier.height(70.dp))
            DrawDashLine()
            Spacer(modifier = Modifier.height(20.dp))
            if (!isMainHomePage) {
                Text(color = colorResource(id = R.color.nobel),
                    inlineContent = inlineContent, text = text, style = TextStyle(fontFamily = zustFont,
                        fontWeight = FontWeight.W700, fontSize = 32.sp))
                Spacer(modifier = Modifier.height(30.dp))
            } else {
                Text(color = colorResource(id = R.color.nobel), text = text1, style = TextStyle(fontFamily = zustFont,
                        fontWeight = FontWeight.W700, fontSize = 32.sp))
                Spacer(modifier = Modifier.height(dp_8))
                Text(color = colorResource(id = R.color.nobel),
                    inlineContent = inlineContent1, text = craftedText, style = TextStyle(fontFamily = zustFont,
                        fontWeight = FontWeight.W500, fontSize = 14.sp))
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

}

const val myId = "inlineContent"
val text = buildAnnotatedString {
    append("We deliver with\n" +
            "a promise of \n" +
            "Freshness ")
    appendInlineContent(myId, "[icon]")
}
val text1 = buildAnnotatedString {
    append("Your all in one\nDoorstep Delivery\nApp")
}
val craftedText = buildAnnotatedString {
    append("Crafted with ")
    appendInlineContent(myId, "[icon]")
    append(" in Patna, India")
}
val inlineContent = mapOf(
    Pair(
        // This tells the [CoreText] to replace the placeholder string "[icon]" by
        // the composable given in the [InlineTextContent] object.
        myId,
        InlineTextContent(
            // Placeholder tells text layout the expected size and vertical alignment of
            // children composable.
            Placeholder(
                width = 20.sp,
                height = 20.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline
            )
        ) {
            Icon(painterResource(id = R.drawable.love), "love", tint = Color(0xffE74C3C))
        }
    )
)

val inlineContent1 = mapOf(
    Pair(
        // This tells the [CoreText] to replace the placeholder string "[icon]" by
        // the composable given in the [InlineTextContent] object.
        myId,
        InlineTextContent(
            // Placeholder tells text layout the expected size and vertical alignment of
            // children composable.
            Placeholder(
                width = 14.sp,
                height = 14.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextBottom
            )
        ) {
            Icon(painterResource(id = R.drawable.love), "love", tint = Color(0xffE74C3C))
        }
    )
)