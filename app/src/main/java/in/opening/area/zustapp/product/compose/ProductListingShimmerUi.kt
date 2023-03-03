package `in`.opening.area.zustapp.product.compose

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProductListingShimmerUi(modifier: Modifier) {
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

    ShimmerProductListing(brush = brush, modifier)
}

@Composable
fun ShimmerProductListing(brush: Brush, modifier: Modifier) {
    LazyColumn(
        modifier = modifier) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(
                    modifier = Modifier
                        .height(150.dp)
                        .weight(1f)
                        .width(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Spacer(
                    modifier = Modifier
                        .height(150.dp)
                        .weight(1f)
                        .width(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(
                    modifier = Modifier
                        .height(150.dp)
                        .weight(1f)
                        .width(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Spacer(
                    modifier = Modifier
                        .height(150.dp)
                        .weight(1f)
                        .width(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(
                    modifier = Modifier
                        .height(150.dp)
                        .weight(1f)
                        .width(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Spacer(
                    modifier = Modifier
                        .height(150.dp)
                        .weight(1f)
                        .width(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun OtherCategoryShimmerUi(modifier: Modifier) {
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
    ShimmerOtherCategory(brush, modifier.height(130.dp))
}

@Composable
private fun ShimmerOtherCategory(brush: Brush, modifier: Modifier) {
    LazyRow(modifier = modifier) {
        item {
            Column {
                Spacer(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(brush)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Spacer(modifier = Modifier
                    .width(60.dp)
                    .height(12.dp)
                    .background(brush))
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
        item {
            Column {
                Spacer(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(brush)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Spacer(modifier = Modifier
                    .width(60.dp)
                    .height(12.dp)
                    .background(brush))
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
        item {
            Column {
                Spacer(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(brush)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Spacer(modifier = Modifier
                    .width(60.dp)
                    .height(12.dp)
                    .background(brush))
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
        item {
            Column {
                Spacer(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(brush)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Spacer(modifier = Modifier
                    .width(60.dp)
                    .height(12.dp)
                    .background(brush))
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
        item {
            Column {
                Spacer(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(brush)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Spacer(modifier = Modifier
                    .width(60.dp)
                    .height(12.dp)
                    .background(brush))
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}
