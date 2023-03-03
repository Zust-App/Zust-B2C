package `in`.opening.area.zustapp.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect

@Composable
 fun DrawDashLine() {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f)
    Canvas(
        Modifier.fillMaxWidth()
    ) {
        drawLine(
            color = Color(0xffA0A0A0),
            strokeWidth = 1f,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )
    }
}