package non_veg.cart.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_20
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.AppUtility
import non_veg.cart.models.NonVegCartData
import non_veg.cart.uiModel.NonVegCartUiModel
import non_veg.cart.viewmodel.NonVegCartViewModel

@Composable
fun NonVegCartMainContainerUi(paddingValues: PaddingValues, viewModel: NonVegCartViewModel) {
    val cartDetailsState = viewModel.cartDetailsState.collectAsState().value
    val context = LocalContext.current

    val cartData = remember { mutableStateOf(NonVegCartData()) }

    when (cartDetailsState) {
        is NonVegCartUiModel.Success -> {
            cartDetailsState.data?.let {
                cartData.value = it
            }
        }

        is NonVegCartUiModel.Initial -> {

        }

        is NonVegCartUiModel.Error -> {
            val errorMessage = cartDetailsState.errorMessage
            AppUtility.showToast(context = context, errorMessage)
        }
    }

    ConstraintLayout(modifier = Modifier
        .padding(paddingValues)
        .fillMaxHeight()
        .fillMaxHeight()
        .background(color = colorResource(id = R.color.screen_surface_color))) {
        val (cartItemsContainer, simpleProgressBar) = createRefs()
        if (cartDetailsState.isLoading && !cartDetailsState.isUpdateApiCall) {
            NonVegCartFullScreenShimmer()
        }

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .constrainAs(cartItemsContainer) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
            }) {
            if (!cartData.value.itemsInCart.isNullOrEmpty()) {
                item {
                    Text(text = "Items Added", style = ZustTypography.h1, modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = dp_16, vertical = dp_12))
                }

                cartData.value.itemsInCart?.forEach { singleItem ->
                    item(singleItem.merchantProductId) {
                        NonVegCartItemUi(singleItem, modifier = nonVegCartItemModifier) { cartItem, action ->
                            if (!cartDetailsState.isLoading) {
                                viewModel.updateUserNonVegCart(cartItem, action)
                            } else {
                                AppUtility.showToast(context = context, "Please wait")
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(dp_20))
                }
                item {
                    NonVegBillingContainer()
                }
                item {
                    Spacer(modifier = Modifier.height(dp_20))
                }
                item {
                    NonVegCancellationPolicyUi()
                }
                item {
                    Spacer(modifier = Modifier.height(dp_20))
                }
            }
        }
        if (cartDetailsState.isLoading && cartDetailsState.isUpdateApiCall) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(colorResource(id = R.color.white).copy(alpha = 0.5f))
                .constrainAs(simpleProgressBar) {
                    top.linkTo(cartItemsContainer.top)
                    end.linkTo(cartItemsContainer.end)
                    bottom.linkTo(cartItemsContainer.bottom)
                    start.linkTo(cartItemsContainer.start)
                }) {
                CircularProgressIndicator(modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.Center),
                    strokeWidth = 4.dp)
            }
        }
    }
}

private val nonVegCartItemModifier = Modifier
    .fillMaxWidth()
    .padding(horizontal = dp_16)
    .wrapContentHeight()


@Composable
private fun NonVegCartFullScreenShimmer() {
    val shimmerColors = listOf(
        Color(0xffDBDBDB).copy(alpha = 0.8f),
        Color(0xffDBDBDB).copy(alpha = 0.5f),
        Color(0xffDBDBDB).copy(alpha = 1f),
    )
    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )
    NonVegCartFullScreenShimmerItem(brush = brush)
}

@Composable
private fun NonVegCartFullScreenShimmerItem(brush: Brush) {
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(horizontal = 16.dp, vertical = 16.dp)) {
        items(count = 5) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()) {
                Spacer(
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


