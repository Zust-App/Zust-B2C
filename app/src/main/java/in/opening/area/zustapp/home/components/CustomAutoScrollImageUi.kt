package `in`.opening.area.zustapp.home.components

import `in`.opening.area.zustapp.home.HomePageBannerSection
import `in`.opening.area.zustapp.home.models.HomePageGenericData
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val randomBlackIndicator = Color(0xFF2E2E2E)
val randomGreyIndicator = Color(0xFFD9D9D9)
const val scrollingDelay = 3000L
const val KEY_BANNER = "banner"
fun LazyListScope.customAutoScrollImageUi(imageList: List<HomePageGenericData>?) {
    if (imageList == null) {
        return
    }

    item(key = KEY_BANNER) {
        var selectedPosition by remember {
            mutableStateOf(0)
        }
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(key1 = Unit, block = {
            coroutineScope.launch {
                delay(scrollingDelay)
                if (selectedPosition == 5) {
                    selectedPosition = 0
                } else {
                    selectedPosition += 1
                }
            }
        })
        Column(modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)) {
            HomePageBannerSection(imageList)
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                (0..5).forEach { index ->
                    if (selectedPosition == index) {
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