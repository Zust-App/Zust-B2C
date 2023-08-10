package non_veg.home.ui

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
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
import `in`.opening.area.zustapp.home.HomePageBannerSection
import `in`.opening.area.zustapp.home.components.CustomIndicator
import `in`.opening.area.zustapp.home.components.KEY_BANNER
import `in`.opening.area.zustapp.home.components.randomBlackIndicator
import `in`.opening.area.zustapp.home.components.randomGreyIndicator
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.AppDeepLinkHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import non_veg.home.model.NonVegHomePageBannerData
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ZNonHomePageBannerUi(data: List<NonVegHomePageBannerData>?) {
    if (data.isNullOrEmpty()) {
        return
    }
    if (data.size == 1) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://dao54xqhg9jfa.cloudfront.net/oms/dd3415e0-d773-bcdf-31c9-b1694bd2d14e/original/WEB_NEW_50.jpeg")
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp).clip(RoundedCornerShape(dp_8))
                .clickable {

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
                animateScrollToPage(
                    page = target,
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                )
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .height(125.dp)) {

        HorizontalPager(count = data.size, state = pagerState) { page ->
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
                .fillMaxWidth().clip(RoundedCornerShape(dp_8)))
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://dao54xqhg9jfa.cloudfront.net/oms/dd3415e0-d773-bcdf-31c9-b1694bd2d14e/original/WEB_NEW_50.jpeg")
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp).clip(RoundedCornerShape(dp_8))
                    .clickable {

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
