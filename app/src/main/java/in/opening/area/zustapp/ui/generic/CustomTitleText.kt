package `in`.opening.area.zustapp.ui.generic

import `in`.opening.area.zustapp.ui.theme.ZustTypography
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun CustomTitleText(title: String, fontSize: TextUnit? = 24.sp) {
    Text(
        text = title, style = ZustTypography.body1,
        fontSize = fontSize ?: 24.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
}



