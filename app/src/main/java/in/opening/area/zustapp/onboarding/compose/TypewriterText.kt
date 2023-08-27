import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TypewriterText(text: String, modifier: Modifier = Modifier, textSize: TextUnit = 16.sp) {
    var index by remember { mutableStateOf(0) }
    val textToShow = text.take(index + 1)

    LaunchedEffect(Unit) {
        while (index < text.length) {
            delay(80)
            index++
        }
    }
    Text(text = textToShow, modifier = modifier, style = ZustTypography.bodyMedium,
        fontSize = textSize,
        color = colorResource(id = R.color.new_material_primary))
}