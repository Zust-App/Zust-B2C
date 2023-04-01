import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import `in`.opening.area.zustapp.utility.AppUtility.Companion.WEB_URL
import `in`.opening.area.zustapp.utility.openBrowser
import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun WebsiteLinkText(modifier: Modifier) {
    val context = LocalContext.current
    val annotatedText = with(AnnotatedString.Builder()) {
        append(WEB_URL)
        addStringAnnotation(
            tag = "URL",
            annotation = WEB_URL,
            start = 6,
            end = 21
        )
        toAnnotatedString()
    }
    Text(
        text = annotatedText,
        modifier = modifier.clickable(onClick = {
            context.openBrowser(WEB_URL)
        }),
        textAlign = TextAlign.Center,
        style = Typography_Montserrat.body2.copy(
            color = colorResource(id = R.color.new_material_primary),
            textDecoration = TextDecoration.Underline
        ),
        color = colorResource(id = R.color.app_black),
        fontWeight = FontWeight.W500)
}
