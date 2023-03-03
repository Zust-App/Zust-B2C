import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.extensions.collectAsStateLifecycleAware
import `in`.opening.area.zustapp.product.model.CreateCartData
import `in`.opening.area.zustapp.product.model.CreateCartReqModel
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.uiModels.CreateCartResponseUi
import `in`.opening.area.zustapp.uiModels.VALUE
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.OrderSummaryNetworkVM
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModel


@Composable
fun CustomBottomBarViewV2(viewModel: ViewModel, type: VALUE, proceedToCartClick: () -> Unit,
                          successCallback: (CreateCartData) -> Unit) {
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

        if (addToCart.totalItemCount > 0) {
            ConstraintLayout(modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.green))
                .height(50.dp)
                .clickable {
                    proceedToCartClick.invoke()
                }
            ) {
                val (itemCount, price, viewCart, progressBar, viewCartIcon) = createRefs()
                Text(text = addToCart.totalItemCount.toString() + " Items |", modifier = Modifier.constrainAs(itemCount) {
                    start.linkTo(parent.start, dp_12)
                    top.linkTo(parent.top, dp_4)
                    bottom.linkTo(parent.bottom, dp_4)
                }, color = Color.White, style = Typography_Montserrat.body2)

                Text(text = stringResource(id = R.string.ruppes) + addToCart.calculatedPrice, modifier = Modifier.constrainAs(price) {
                    start.linkTo(itemCount.end, dp_6)
                    top.linkTo(parent.top, dp_4)
                    bottom.linkTo(parent.bottom, dp_4)
                }, color = Color.White, style = Typography_Montserrat.body2)

                Text(text = "View Cart", modifier = Modifier.constrainAs(viewCart) {
                    end.linkTo(parent.end, dp_12)
                    top.linkTo(parent.top, dp_4)
                    bottom.linkTo(parent.bottom, dp_4)
                }, color = Color.White, style = Typography_Montserrat.body2)

                Icon(painter = painterResource(id = R.drawable.ic_outline_shopping_cart_24), contentDescription = "", modifier = Modifier.constrainAs(viewCartIcon) {
                    end.linkTo(viewCart.start, dp_8)
                    top.linkTo(parent.top, dp_4)
                    bottom.linkTo(parent.bottom, dp_4)
                }, tint = Color.White)

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
