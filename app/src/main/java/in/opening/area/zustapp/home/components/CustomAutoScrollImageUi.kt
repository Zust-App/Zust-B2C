package `in`.opening.area.zustapp.home.components

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.CustomHorizontalIndicator
import `in`.opening.area.zustapp.home.HomePageBannerSection
import `in`.opening.area.zustapp.home.models.HomePageGenericData
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
            .height(150.dp)) {

            HorizontalPager(count = imageList.size, state = pagerState) {
                HomePageBannerSection(imageList[pagerState.currentPage])
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