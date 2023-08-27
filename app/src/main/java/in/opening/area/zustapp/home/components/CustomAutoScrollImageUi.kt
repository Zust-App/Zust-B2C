package `in`.opening.area.zustapp.home.components

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.CustomHorizontalIndicator
import `in`.opening.area.zustapp.home.HomePageBannerSection
import `in`.opening.area.zustapp.home.models.HomePageGenericData
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.AppDeepLinkHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import zustbase.custom.pressClickEffect
import kotlin.math.absoluteValue

val randomBlackIndicator = Color(0xFF2E2E2E)
val randomGreyIndicator = Color(0xFFD9D9D9)
const val scrollingDelay = 3000L
const val KEY_BANNER = "banner"

@OptIn(ExperimentalPagerApi::class)
fun LazyListScope.customAutoScrollImageUi(imageList: List<HomePageGenericData>?) {
    if (imageList == null) {
        return
    }

    item(key = KEY_BANNER) {
        val context = LocalContext.current
        if (imageList.size == 1) {
            if (imageList[0].imageUrl == null) {
                return@item
            }
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageList[0].imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(dp_8))
                    .pressClickEffect {
                        AppDeepLinkHandler.handleOfferLink(context, imageList[0].deepLink)
                    }
            )
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
            .padding(horizontal = dp_8)
            .background(shape = RoundedCornerShape(dp_8), color = colorResource(id = R.color.white))
            .height(150.dp)
            .clip(RoundedCornerShape(dp_8))) {
            HorizontalPager(count = imageList.size, state = pagerState) { page ->
                if (imageList[page].imageUrl.isNullOrEmpty()) {
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
                        .data(imageList[page].imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(dp_8))
                        .pressClickEffect {
                            AppDeepLinkHandler.handleOfferLink(context, imageList[page].deepLink)
                        }
                )
            }

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                (imageList.indices).forEach { index ->
                    if (pagerState.currentPage == index) {
                        CustomIndicator(randomBlackIndicator)
                    } else {
                        CustomIndicator(randomGreyIndicator)
                    }
                }
            }
        }

    }
}

@Composable
fun CustomIndicator(color: Color) {
    Canvas(modifier = Modifier.size(12.dp)) {
        drawCircle(
            color = color,
            radius = 10f,
            center = Offset(8f, 8f)
        )
    }
}