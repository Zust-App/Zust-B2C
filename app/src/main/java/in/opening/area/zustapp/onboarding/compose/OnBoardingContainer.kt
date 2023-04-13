package `in`.opening.area.zustapp.onboarding.compose

import WebsiteLinkText
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.ComposeLottie
import `in`.opening.area.zustapp.ui.theme.*
import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.delay


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
        val (lottieAnimation) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.zust_white_text),
            contentDescription = "Zust App",
            modifier = Modifier
                .clip(CircleShape)
                .constrainAs(lottieAnimation) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .width(200.dp)
                .height(200.dp)
                .scale(scale.value),
            contentScale = ContentScale.FillWidth,
        )

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
    return slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeIn(animationSpec = tween(durationMillis = duration)) with slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> -height } + fadeOut(animationSpec = tween(durationMillis = duration))
}




