package `in`.opening.area.zustapp.ui.generic

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.product.model.CreateCartData
import `in`.opening.area.zustapp.product.model.CreateCartReqModel
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.uiModels.CreateCartResponseUi
import `in`.opening.area.zustapp.uiModels.VALUE
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.ProductUtils
import `in`.opening.area.zustapp.viewmodels.OrderSummaryNetworkVM
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModel


@Composable
fun CustomBottomBarView(
    viewModel: ViewModel, type: VALUE,
    proceedToCartClick: () -> Unit,
    successCallback: (CreateCartData) -> Unit,
) {
    val context: Context = LocalContext.current

    if (viewModel is OrderSummaryNetworkVM) {
        val addToCart by viewModel.addToCartFlow.collectAsState(initial = CreateCartReqModel())
        val cartUiState by viewModel.createCartUiState.collectAsState(initial = CreateCartResponseUi.InitialUi(false))

        when (val response = cartUiState) {
            is CreateCartResponseUi.ErrorUi -> {
                if (!response.errorMsg.isNullOrEmpty()) {
                    AppUtility.showToast(context, response.errorMsg)
                } else {
                    AppUtility.showToast(context, response.errors?.getTextMsg())
                }
            }
            is CreateCartResponseUi.CartSuccess -> {
                if (response.value == type) {
                    successCallback.invoke(response.data)
                }
            }
            is CreateCartResponseUi.InitialUi -> {

            }
        }

        AnimatedVisibility(
            visible = addToCart.totalItemCount > 0,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = 400)
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(durationMillis = 50)
            ) + fadeOut(),
        ) {
            //new_material_primary
            BottomAppBar(
                modifier = Modifier
                    .clickable {
                        proceedToCartClick.invoke()
                    }
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, 0.dp, 0.dp)),
                backgroundColor = colorResource(id = R.color.new_material_primary),
            ) {
                ConstraintLayout(modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)) {
                    val (itemCount, price, viewCart, progressBar, viewCartIcon) = createRefs()
                    Text(text = addToCart.totalItemCount.toString() + " Items", modifier = Modifier.constrainAs(itemCount) {
                        start.linkTo(parent.start, dp_16)
                        top.linkTo(parent.top, dp_4)
                    }, color = colorResource(id = R.color.white),
                        style = ZustTypography.subtitle1)

                    Text(text = stringResource(id = R.string.ruppes) +
                            ProductUtils.roundTo1DecimalPlaces(addToCart.calculatedPrice),
                        modifier = Modifier.constrainAs(price) {
                            start.linkTo(parent.start, dp_16)
                            top.linkTo(itemCount.bottom, dp_4)
                            bottom.linkTo(parent.bottom, dp_4)
                        }, color = colorResource(id = R.color.white),
                        style = ZustTypography.body1,
                        fontSize = 14.sp)

                    Text(text = stringResource(R.string.view_cart),
                        modifier = Modifier.constrainAs(viewCart) {
                            end.linkTo(viewCartIcon.start, dp_12)
                            top.linkTo(parent.top, dp_20)
                            bottom.linkTo(parent.bottom, dp_20)
                        }, color = colorResource(id = R.color.white),
                        style = ZustTypography.body1)

                    Icon(painter = painterResource(id = R.drawable.arrow_right_icon),
                        contentDescription = "", modifier = Modifier.constrainAs(viewCartIcon) {
                            end.linkTo(parent.end, dp_20)
                            top.linkTo(parent.top, dp_20)
                            bottom.linkTo(parent.bottom, 20.dp)
                        }, tint = colorResource(id = R.color.white))

                    if (cartUiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.constrainAs(progressBar) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }, color = colorResource(id = R.color.white))
                    }
                }

            }
        }

    }
}

