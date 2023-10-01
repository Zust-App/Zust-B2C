package `in`.opening.area.zustapp.compose

import androidx.annotation.RawRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import kotlin.math.roundToInt

@Composable
fun ComposeLottie(
    @RawRes rawId: Int, modifier: Modifier? = Modifier,
    speed: Float? = 1f,
) {
    val isLottiePlaying by remember {
        mutableStateOf(true)
    }
    val animationSpeed by remember {
        mutableFloatStateOf(speed ?: 1f)
    }
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(rawId)
    )

    val lottieAnimation by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isLottiePlaying,
        speed = animationSpeed,
        restartOnPlay = false
    )
    if (modifier != null) {
        LottieAnimation(
            composition,
            lottieAnimation,
            modifier = modifier, clipToCompositionBounds = false
        )
    }
}

@Composable
fun ComposeLottieWithoutScope(@RawRes rawId: Int, modifier: Modifier? = Modifier) {
    val isLottiePlaying by remember {
        mutableStateOf(true)
    }
    val animationSpeed by remember {
        mutableStateOf(1f)
    }
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(rawId)
    )

    val lottieAnimation by animateLottieCompositionAsState(
        composition,
        iterations = 5,
        isPlaying = isLottiePlaying,
        speed = animationSpeed,
        restartOnPlay = false
    )

    if (modifier == null) {
        LottieAnimation(
            composition,
            lottieAnimation,
            modifier = Modifier
                .size(250.dp)
        )
    } else {
        LottieAnimation(
            composition,
            lottieAnimation,
            modifier = modifier
        )
    }
}

@Composable
fun ComposeLottieWithCallback(
    @RawRes rawId: Int, modifier: Modifier? = Modifier,
    speed: Float? = 1f,
    onAnimationEnd: () -> Unit, // Callback function for animation end
) {
    val isLottiePlaying by remember {
        mutableStateOf(true)
    }
    val animationSpeed by remember {
        mutableStateOf(speed ?: 1f)
    }
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(rawId)
    )

    val lottieAnimation = animateLottieCompositionAsState(
        composition,
        iterations = 1,
        isPlaying = isLottiePlaying,
        speed = animationSpeed,
        restartOnPlay = false
    )

    if (modifier != null) {
        LottieAnimation(
            composition,
            lottieAnimation.value,
            modifier = modifier,
            clipToCompositionBounds = false
        )

        if (lottieAnimation.progress >= 1.0f) {
            LaunchedEffect(lottieAnimation.progress) {
                if (lottieAnimation.progress >= 1f) {
                    onAnimationEnd.invoke()
                }
            }
        }
    }
}

