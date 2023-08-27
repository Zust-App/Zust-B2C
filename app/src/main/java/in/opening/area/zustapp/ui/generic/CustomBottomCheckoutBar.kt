package `in`.opening.area.zustapp.ui.generic

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics
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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModel
import ui.colorBlack
import ui.colorWhite


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
                FirebaseAnalytics.logEvents(FirebaseAnalytics.CLICK_ON_VIEW_CART_ERROR)
                if (!response.errorMsg.isNullOrEmpty()) {
                    AppUtility.showToast(context, response.errorMsg)
                } else {
                    AppUtility.showToast(context, response.errors?.getTextMsg())
                }
            }

            is CreateCartResponseUi.CartSuccess -> {
                if (response.value == type) {
                    FirebaseAnalytics.logEvents(FirebaseAnalytics.CLICK_ON_VIEW_CART_SUCCESS)
                    successCallback.invoke(response.data)
                }
            }

            is CreateCartResponseUi.InitialUi -> {

            }
        }

        if (addToCart.totalItemCount > 0) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .shadow(dp_8)
                .clip(RoundedCornerShape(topEnd = dp_8, topStart = dp_8))
                .background(color = colorResource(id = R.color.white), shape = RoundedCornerShape(topEnd = dp_8, topStart = dp_8))) {
                ConstraintLayout(modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topEnd = dp_8, topStart = dp_8))
                    .background(color = colorResource(id = R.color.white), shape = RoundedCornerShape(topEnd = dp_8, topStart = dp_8)))
                {
                    val (itemCount, price, viewCart, progressBar) = createRefs()
                    Text(text = buildString {
                        append(addToCart.totalItemCount)
                        append(" Items")
                    }, modifier = Modifier.constrainAs(itemCount) {
                        start.linkTo(parent.start, dp_16)
                        top.linkTo(viewCart.top)
                        bottom.linkTo(price.top)
                    }, color = colorResource(id = R.color.language_default),
                        style = ZustTypography.bodyMedium)

                    Text(text = stringResource(id = R.string.ruppes) + ProductUtils.roundTo1DecimalPlaces(addToCart.calculatedPrice), modifier = Modifier.constrainAs(price) {
                        start.linkTo(parent.start, dp_16)
                        top.linkTo(itemCount.bottom)
                        bottom.linkTo(viewCart.bottom)
                    }, color = colorBlack,
                        style = ZustTypography.titleLarge, fontSize = 14.sp)

                    Button(onClick = { proceedToCartClick.invoke() }, modifier = Modifier.constrainAs(viewCart) {
                        end.linkTo(parent.end, dp_8)
                        top.linkTo(parent.top, dp_8)
                        bottom.linkTo(parent.bottom, dp_8)
                    }, colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.new_material_primary))) {
                        Spacer(modifier = Modifier.width(dp_12))
                        Text(text = stringResource(R.string.view_cart),
                            color = colorResource(id = R.color.white), style = ZustTypography.bodyMedium)
                        Spacer(modifier = Modifier.width(dp_12))
                        Icon(painter = painterResource(id = R.drawable.arrow_right_icon), contentDescription = "", tint = colorResource(id = R.color.white))
                        Spacer(modifier = Modifier.width(dp_12))
                    }
                    if (cartUiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier
                            .size(dp_24)
                            .constrainAs(progressBar) {
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
}

