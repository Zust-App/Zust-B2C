package `in`.opening.area.zustapp.compose

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*

@Composable
fun ComposeLottie(
    @RawRes rawId: Int, modifier: Modifier? = Modifier,
    speed: Float? = 1f,
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