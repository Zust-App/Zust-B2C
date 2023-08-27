package non_veg.home.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.generic.KEY_SEARCH
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_8
import kotlinx.coroutines.delay

fun LazyListScope.homeGenericPageSearchDefaultUi(hintTexts: List<String>, callback: () -> Unit) {
    item(key = KEY_SEARCH) {
        var textIndex by remember { mutableIntStateOf(0) }
        var animatedAlpha by remember { mutableFloatStateOf(1f) }
        val animatedColor by animateColorAsState(
            targetValue = colorResource(id = R.color.new_hint_color),
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .clickable {
                    callback.invoke()
                }
                .border(width = 1.dp, shape = RoundedCornerShape(dp_12), color = colorResource(id = R.color.indicator_color))
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            Text(
                text = hintTexts[textIndex],
                style = ZustTypography.bodyMedium,
                color = animatedColor.copy(alpha = animatedAlpha),
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.new_search_icon),
                contentDescription = "Search",
                tint = colorResource(id = R.color.new_material_primary)
            )
        }

        LaunchedEffect(Unit) {
            val delayInMillis = 3000L
            while (true) {
                delay(delayInMillis)
                textIndex = (textIndex + 1) % hintTexts.size // Cycle through the texts
                animatedAlpha = 1f
                if (textIndex == hintTexts.size - 1) {
                    break
                }
            }
        }
    }
}