package zustbase.analysis.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.opening.area.zustapp.ui.theme.blue200
import `in`.opening.area.zustapp.ui.theme.blue400
import `in`.opening.area.zustapp.ui.theme.blue500
import `in`.opening.area.zustapp.ui.theme.blueBG
import `in`.opening.area.zustapp.ui.theme.card
import `in`.opening.area.zustapp.ui.theme.deepGold
import `in`.opening.area.zustapp.ui.theme.whiteText


@Composable
fun CountDownView(progress: Float, width: Dp, height: Dp) {
    val gradientBrush = Brush.linearGradient(listOf(blue500, blue200, blue400))

    Canvas(
        modifier = Modifier
            .size(width = width, height = height)
            .background(color = blueBG)
    ) {
        val centerX = size.width / 2
        val centerY = size.height / 2

    }
}
