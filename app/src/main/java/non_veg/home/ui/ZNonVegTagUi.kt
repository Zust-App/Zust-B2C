package non_veg.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.DrawDashLine
import `in`.opening.area.zustapp.home.components.padding_16Modifier
import `in`.opening.area.zustapp.ui.theme.zustFont


@Composable
fun ZNonVegHomeTagUi() {
    Column(modifier = padding_16Modifier) {
        Spacer(modifier = Modifier.height(70.dp))
        DrawDashLine()
        Spacer(modifier = Modifier.height(20.dp))
        Text(color = colorResource(id = R.color.nobel),
            inlineContent = zustChickenTagText, text = text,
            style = TextStyle(fontFamily = zustFont,
                fontWeight = FontWeight.W700, fontSize = 32.sp))
        Spacer(modifier = Modifier.height(30.dp))
    }
}

const val myId = "inlineContent"
val text = buildAnnotatedString {
    append("Zust - Your\n" + "Trusted Partner ")
    appendInlineContent(myId, "[icon]")
}

val zustChickenTagText = mapOf(
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