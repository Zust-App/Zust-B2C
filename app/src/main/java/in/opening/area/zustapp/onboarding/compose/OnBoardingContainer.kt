package `in`.opening.area.zustapp.onboarding.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.ComposeLottie
import `in`.opening.area.zustapp.ui.theme.*
import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    "Promise freshness",
    "More than 1000 items",
    "Budget friendly",
    "All in one grocery app",
    "Fresh Vegetables",
    "Connecting farmers",
    "Customer Satisfaction",
    "Always Available")

@Composable
fun OnBoardingContainer(callback: (LoginClick) -> Unit) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(color = colorResource(id = R.color.white))
        .padding(horizontal = 24.dp)) {
        val topGuideline = createGuidelineFromTop(0.4f)
        val bottomGuideline = createGuidelineFromBottom(0.7f)
        val (loginBtn, contentContainer) = createRefs()
        Column(modifier = Modifier.constrainAs(contentContainer) {
            top.linkTo(topGuideline, dp_24)
            bottom.linkTo(bottomGuideline, dp_40)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }) {
            Image(painter = painterResource(id = R.drawable.zust_app_black_text), contentDescription = "Zust App", modifier = Modifier
                .width(140.dp)
                .height(60.dp))
            Spacer(modifier = Modifier.height(24.dp))
            TypewriterText(text = "Just in 45 Minutes delivery",
                modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(40.dp))
            AnimatedTextContent(exploringText)
        }
        val (website) = createRefs()
        OutlinedButton(shape = RoundedCornerShape(12.dp), modifier = Modifier
            .fillMaxWidth()
            .constrainAs(loginBtn) {
                bottom.linkTo(website.top, dp_8)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
            .padding(horizontal = 8.dp), onClick = {
            callback.invoke(LoginClick.LOGIN)
        }, colors = ButtonDefaults.outlinedButtonColors(backgroundColor = colorResource(R.color.new_material_primary),
            contentColor = Color.White)) {
            Text(text = "Login", style = Typography_Montserrat.body1, modifier = Modifier.padding(vertical = 6.dp))
        }

        Text(text = "www.zustapp.com", modifier = Modifier.constrainAs(website) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom, dp_24)
            width = Dimension.fillToConstraints
        }, textAlign = TextAlign.Center,
            style = Typography_Montserrat.subtitle1,
            color = colorResource(id = R.color.app_black),
            fontWeight = FontWeight.W500)


    }
}


@Composable
fun TypewriterText(text: String, modifier: Modifier = Modifier, textSize: TextUnit = 24.sp) {
    var index by remember { mutableStateOf(0) }
    val textToShow = text.take(index + 1)

    LaunchedEffect(Unit) {
        while (index < text.length) {
            delay(80)
            index++
        }
    }
    Text(text = textToShow, modifier = modifier, style = Typography_Montserrat.body1,
        fontSize = textSize,
        color = colorResource(id = R.color.new_material_primary))
}


@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun AnimatedTextContent(textList: List<String>) {
    var currentIndex by remember { mutableStateOf(0) }

    val currentText by derivedStateOf { textList[currentIndex] }

    AnimatedContent(targetState = currentText, transitionSpec = {
        addAnimation().using(SizeTransform(clip = false))
    }) { targetCount ->
        Text(text = targetCount, style = Typography_Montserrat.body2,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            color = colorResource(id = R.color.app_black))
    }
    LaunchedEffect(currentIndex) {
        delay(2000)
        currentIndex = (currentIndex + 1) % textList.size
    }
}


@ExperimentalAnimationApi
fun addAnimation(duration: Int = 800): ContentTransform {
    return slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeIn(animationSpec = tween(durationMillis = duration)) with slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> -height } + fadeOut(animationSpec = tween(durationMillis = duration))
}




