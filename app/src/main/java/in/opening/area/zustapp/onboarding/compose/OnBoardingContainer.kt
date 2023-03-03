package `in`.opening.area.zustapp.onboarding.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class LoginClick {
    LOGIN
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingContainer(callback: (LoginClick) -> Unit) {
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
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
        val (logoSection, pagingSection, loginBtnSection) = createRefs()
        Column(modifier = Modifier.constrainAs(logoSection) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            Spacer(modifier = Modifier.height(50.dp))
            Image(painter = painterResource(id = R.drawable.grinzy_black), contentDescription = "Zust logo")
        }

        Column(modifier = Modifier.constrainAs(pagingSection) {
            top.linkTo(logoSection.bottom, dp_40)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(loginBtnSection.top, dp_20)
        }) {
            HorizontalPager(count = 3, state = pagerState) {
                OnBoardingSingleItemUi(pagerState.currentPage)
            }
        }

        OutlinedButton(shape = RoundedCornerShape(12.dp), modifier = Modifier
            .fillMaxWidth()
            .constrainAs(loginBtnSection) {
                bottom.linkTo(parent.bottom, dp_40)
                start.linkTo(parent.start, dp_20)
                end.linkTo(parent.end, dp_20)
                width = Dimension.fillToConstraints
            }
            .padding(horizontal = 8.dp), onClick = {
            callback.invoke(LoginClick.LOGIN)
        }, colors = ButtonDefaults.outlinedButtonColors(backgroundColor = colorResource(R.color.new_material_primary),
            contentColor = Color.White)) {
            Text(text = "Login",
                style = Typography_Montserrat.body1,
                modifier = Modifier.padding(vertical = 6.dp))
        }
    }

}