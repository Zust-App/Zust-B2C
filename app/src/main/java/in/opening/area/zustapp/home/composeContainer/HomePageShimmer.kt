package `in`.opening.area.zustapp.home.composeContainer

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun HomePageShimmerUi() {
    val shimmerColors = listOf(
        Color(0xffDBDBDB).copy(alpha = 0.8f),
        Color(0xffDBDBDB).copy(alpha = 0.5f),
        Color(0xffDBDBDB).copy(alpha = 1f),
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    ShimmerGridItem(brush = brush)
}

@Composable
fun ShimmerGridItem(brush: Brush) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp,
                vertical = 10.dp),
    ) {
        item {
            Spacer(
                modifier = Modifier
                    .height(12.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Spacer(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Spacer(
                modifier = Modifier
                    .width(80.dp)
                    .height(12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush))
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Spacer(
                modifier = Modifier
                    .width(120.dp)
                    .height(12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush))
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Spacer(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(
                        modifier = Modifier
                            .width(80.dp)
                            .height(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Spacer(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(
                        modifier = Modifier
                            .width(80.dp)
                            .height(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Spacer(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(
                        modifier = Modifier
                            .width(80.dp)
                            .height(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Spacer(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(
                        modifier = Modifier
                            .width(80.dp)
                            .height(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Spacer(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(
                        modifier = Modifier
                            .width(80.dp)
                            .height(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Spacer(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(
                        modifier = Modifier
                            .width(80.dp)
                            .height(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Spacer(
                        modifier = Modifier
                            .height(130.dp)
                            .width(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(
                        modifier = Modifier
                            .width(80.dp)
                            .height(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Spacer(
                        modifier = Modifier
                            .height(130.dp)
                            .width(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(
                        modifier = Modifier
                            .width(80.dp)
                            .height(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Spacer(
                        modifier = Modifier
                            .height(130.dp)
                            .width(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(
                        modifier = Modifier
                            .width(80.dp)
                            .height(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

