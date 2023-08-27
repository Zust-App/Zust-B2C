package zustbase

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ZLoader() {
    val infiniteTransition = rememberInfiniteTransition()
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(rotationAngle)

        Canvas(
            modifier = Modifier.size(150.dp),
            onDraw = {
                val fontSize = 48.sp
                val zText = "Z"

                drawIntoCanvas { canvas ->
                    val textPaint = androidx.compose.ui.graphics.Paint().asFrameworkPaint().apply {
                        isAntiAlias = true
                        color = Color.Black.toArgb()
                        textSize = fontSize.toPx()
                        textAlign = android.graphics.Paint.Align.CENTER
                        style = android.graphics.Paint.Style.FILL
                    }
                    canvas.nativeCanvas.drawText(
                        zText,
                        size.width / 2,
                        size.height / 2 + fontSize.toPx() / 3,
                        textPaint
                    )
                }
            }
        )
    }
}
