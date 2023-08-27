package zustbase.basepage.ui

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import `in`.opening.area.zustapp.home.components.CustomIndicator
import `in`.opening.area.zustapp.home.components.randomBlackIndicator
import `in`.opening.area.zustapp.home.components.randomGreyIndicator
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.AppDeepLinkHandler.Companion.handleOfferLink
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import zustbase.basepage.models.ServicePageSingleItemData
import zustbase.custom.pressClickEffect
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ZustBaseAutoScrollUi(data: List<ServicePageSingleItemData>?) {
    if (data.isNullOrEmpty()) {
        return
    }
    val context = LocalContext.current
    if (data.size == 1) {
        if (data[0].imageUrl == null) {
            return
        }
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(data[0].imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 100.dp)
                .wrapContentHeight()
                .clip(RoundedCornerShape(dp_8))
                .pressClickEffect {
                    handleOfferLink(context, data[0].deepLink)
                }
        )
        return
    }
    val pagerState = rememberPagerState()
    LaunchedEffect(key1 = pagerState.currentPage) {
        launch {
            delay(4000)
            with(pagerState) {
                val target = if (currentPage < pageCount - 1) currentPage + 1 else 0
                tween<Float>(
                    durationMillis = 500,
                    easing = LinearOutSlowInEasing
                )
                animateScrollToPage(page = target)
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .height(140.dp)) {

        HorizontalPager(count = data.size, state = pagerState) { page ->
            if (data[page].imageUrl.isNullOrEmpty()) {
                return@HorizontalPager
            }
            Box(modifier = Modifier
                .graphicsLayer {
                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                    lerp(
                        start = 0.85f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale

                    }
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )

                }
                .fillMaxWidth()
                .clip(RoundedCornerShape(dp_8)))
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data[page].imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(dp_8))
                    .pressClickEffect {
                        handleOfferLink(context, data[page].deepLink)
                    }
            )
        }

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            (data.indices).forEach { index ->
                if (pagerState.currentPage == index) {
                    CustomIndicator(randomBlackIndicator)
                } else {
                    CustomIndicator(randomGreyIndicator)
                }
            }
        }
    }
}
