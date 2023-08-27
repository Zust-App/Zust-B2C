package `in`.opening.area.zustapp.onboarding.compose

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import `in`.opening.area.zustapp.BuildConfig
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_16


enum class LoginClick {
    LOGIN
}

private val exploringText = arrayListOf(
    "Super fast delivery",
    "All in one grocery app",
    "Promise freshness",
    "More than 1000 items",
    "Budget friendly",
    "Connecting farmers",
    "Customer Satisfaction",
    "Always Available")

@Composable
fun OnBoardingContainer(callback: (LoginClick) -> Unit) {
    val scale = remember { Animatable(0f) }
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(color = colorResource(id = R.color.new_material_primary))) {
        val (lottieAnimation, appVersion) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.zust_app_white_text),
            contentDescription = "Zust App",
            modifier = Modifier
                .clip(CircleShape)
                .constrainAs(lottieAnimation) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .width(240.dp)
                .height(240.dp)
                .scale(scale.value),
            contentScale = ContentScale.FillWidth,
        )

        Text(text = "v" + BuildConfig.VERSION_NAME, modifier = Modifier.constrainAs(appVersion) {
            bottom.linkTo(parent.bottom, dp_16)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, style = ZustTypography.bodyMedium, color = colorResource(R.color.white))

        LaunchedEffect(key1 = Unit, block = {
            animateImageScale(scale)
            callback.invoke(LoginClick.LOGIN)
        })
    }
}

private suspend fun animateImageScale(scale: Animatable<Float, AnimationVector1D>) {
    scale.animateTo(
        targetValue = 1.2f, // target scale value
        animationSpec = tween(
            durationMillis = 1000, // animation duration
            easing = FastOutSlowInEasing // animation easing function
        )
    )
    scale.animateTo(
        targetValue = 1f, // target scale value
        animationSpec = tween(
            durationMillis = 500, // animation duration
            easing = FastOutSlowInEasing // animation easing function
        )
    )
}


@ExperimentalAnimationApi
fun addAnimation(duration: Int = 800): ContentTransform {
    return (slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeIn(animationSpec = tween(durationMillis = duration))).togetherWith(slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> -height } + fadeOut(animationSpec = tween(durationMillis = duration)))
}




