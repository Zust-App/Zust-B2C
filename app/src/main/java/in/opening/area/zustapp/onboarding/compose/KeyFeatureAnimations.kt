package `in`.opening.area.zustapp.onboarding.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_6
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.delay


@Composable
fun KeyFeatureAnimations(textList: List<String>) {
    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (currentIndex < textList.size - 1) {
            delay(2000)
            currentIndex += 1
        }
    }

    val displayedTexts = textList.subList(0, currentIndex + 1)

    LazyColumn(modifier = Modifier.height(250.dp)) {
        itemsIndexed(displayedTexts) { index, text ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                KeyFeatureItem(text = text,
                    canShowDivider = index < displayedTexts.size - 1)
            }
        }
    }
}

@Composable
fun KeyFeatureItem(text: String, canShowDivider: Boolean) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (icon, name, divider) = createRefs()
        val isChecked by remember { mutableStateOf(true) }
        val emptyIcon = painterResource(id = R.drawable.ic_outline_circle_24)
        val checkedIcon = painterResource(id = R.drawable.ic_outline_check_circle_outline_24)
        Crossfade(
            isChecked,
            modifier = Modifier.constrainAs(icon) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            },
            animationSpec = tween(500)) { checked ->
            Icon(
                painter = if (checked) checkedIcon else emptyIcon,
                contentDescription = if (checked) "Checked icon" else "Empty icon",
                tint = if (checked) colorResource(id = R.color.light_green) else Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(icon) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )
        }

        Text(text, modifier = Modifier.constrainAs(name) {
            start.linkTo(icon.end, dp_6)
            end.linkTo(parent.end)
            top.linkTo(icon.top)
            bottom.linkTo(icon.bottom)
            width = Dimension.fillToConstraints
        }, style = ZustTypography.body2)

        val animatedHeight by remember { mutableStateOf(20f) }

        if (canShowDivider) {
            Divider(
                modifier = Modifier
                    .constrainAs(divider) {
                        top.linkTo(icon.bottom)
                        start.linkTo(icon.start)
                        end.linkTo(icon.end)
                    }
                    .height(animatedHeight.dp)
                    .width(0.5.dp),
                color = Color.Gray
            )
        }

    }
}
