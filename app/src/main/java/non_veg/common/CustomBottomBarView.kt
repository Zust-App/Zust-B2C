package non_veg.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_24
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.ProductUtils
import kotlinx.coroutines.flow.update
import non_veg.cart.models.NonVegCartData
import non_veg.cart.uiModel.NonVegCartItemSummaryUiModel
import non_veg.cart.uiModel.NonVegCartUiModel
import non_veg.cart.viewmodel.NonVegCartViewModel
import non_veg.common.model.CartSummaryData

@Composable
fun CustomNonVegBottomBarView(
    viewModel: NonVegCartViewModel,
    proceedToCartClick: () -> Unit,
    cartDataCallback: (NonVegCartData?) -> Unit,
) {
    val createCartUiModel by viewModel.createCartUiModel.collectAsState()//this is from server
    val cartSummaryUiModel by viewModel.cartSummaryUiModel.collectAsState()//this will update the cart value

    val isLoading by rememberUpdatedState(newValue = createCartUiModel.isLoading)

    when (cartSummaryUiModel) {
        is NonVegCartItemSummaryUiModel.Success -> {
            (cartSummaryUiModel as NonVegCartItemSummaryUiModel.Success).data?.let {
                if ((it.itemCountInCart ?: 0) > 0) {
                    FeedDataToNonVegBtmBar(it, isLoading = isLoading) {
                        proceedToCartClick.invoke()
                    }
                }
            }
        }

        is NonVegCartItemSummaryUiModel.InitialUi -> {

        }
    }

    when (createCartUiModel) {
        is NonVegCartUiModel.Success -> {
            viewModel._createCartUiModel.update {
                NonVegCartUiModel.Initial(false)
            }
            cartDataCallback.invoke((createCartUiModel as NonVegCartUiModel.Success).data)
        }

        is NonVegCartUiModel.Initial -> {
        }

        is NonVegCartUiModel.Error -> {
        }
    }
}


@Composable
fun FeedDataToNonVegBtmBar(data: CartSummaryData, isLoading: Boolean, proceedToCartClick: () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(durationMillis = 400)) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(durationMillis = 50)) + fadeOut(),
    ) {
        BottomAppBar(
            backgroundColor = colorResource(id = R.color.white), elevation = dp_12, cutoutShape = RoundedCornerShape(topEnd = dp_8, topStart = dp_8)
        ) {
            ConstraintLayout(modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)) {
                val (itemCount, price, viewCart, progressBar) = createRefs()
                Text(text = buildString {
                    append(data.itemCountInCart)
                    append(" Items")
                }, modifier = Modifier.constrainAs(itemCount) {
                    start.linkTo(parent.start, dp_16)
                    top.linkTo(parent.top, dp_4)
                }, color = colorResource(id = R.color.black), style = ZustTypography.subtitle1)

                Text(text = stringResource(id = R.string.ruppes) + ProductUtils.roundTo1DecimalPlaces(data.itemValueInCart), modifier = Modifier.constrainAs(price) {
                    start.linkTo(parent.start, dp_16)
                    top.linkTo(itemCount.bottom, dp_8)
                    bottom.linkTo(parent.bottom, dp_4)
                }, color = colorResource(id = R.color.black), style = ZustTypography.body1, fontSize = 14.sp)

                Button(onClick = { proceedToCartClick.invoke() }, modifier = Modifier.constrainAs(viewCart) {
                    end.linkTo(parent.end, dp_12)
                    top.linkTo(parent.top, dp_8)
                    bottom.linkTo(parent.bottom, dp_8)
                }, colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.new_material_primary))) {
                    Spacer(modifier = Modifier.width(dp_12))
                    Text(text = stringResource(R.string.view_cart), color = colorResource(id = R.color.white), style = ZustTypography.body1)
                    Spacer(modifier = Modifier.width(dp_12))
                    Icon(painter = painterResource(id = R.drawable.arrow_right_icon), contentDescription = "", tint = colorResource(id = R.color.white))
                    Spacer(modifier = Modifier.width(dp_12))
                }
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(dp_24).constrainAs(progressBar) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }, color = colorResource(id = R.color.new_material_primary))
                }
            }

        }
    }
}

