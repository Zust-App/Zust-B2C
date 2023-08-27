package `in`.opening.area.zustapp.orderSummary.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.CustomAnimatedProgressBar
import `in`.opening.area.zustapp.extensions.collectAsStateLifecycleAware
import `in`.opening.area.zustapp.orderSummary.OrderItemsClickListeners
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.uiModels.orderSummary.OrderSummaryUi
import `in`.opening.area.zustapp.utility.startProductDetailPage
import `in`.opening.area.zustapp.viewmodels.OrderSummaryViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import non_veg.payment.ui.ViewSpacer20

@Composable
fun CartMainContainer(orderSummaryViewModel: OrderSummaryViewModel, listeners: OrderItemsClickListeners, paddingValue: PaddingValues) {
    val cartItemData by orderSummaryViewModel.addedCartUiState.collectAsStateLifecycleAware(initial = OrderSummaryUi.InitialUi(false))

    val context = LocalContext.current
    var isLoading by remember {
        mutableStateOf(false)
    }
    if (isLoading) {
        CustomAnimatedProgressBar(modifier = Modifier)
    }

    LazyColumn(modifier = Modifier
        .padding(paddingValue)
        .fillMaxWidth()
        .padding(horizontal = 20.dp)) {
        when (val cartResponse = cartItemData) {
            is OrderSummaryUi.SummarySuccess -> {
                if (cartResponse.data.productsAlreadyInCart.isNotEmpty() || cartResponse.data.totalItemCount > 0) {
                    item {
                        Text(text = "Items Added", style = ZustTypography.titleMedium, modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = dp_12))
                    }
                    items(cartResponse.data.productsAlreadyInCart) { cartItem ->
                        SelectedCartVerticalItemUi(cartItem, modifierType4, false, {
                            listeners.didTapOnIncreaseProductItemAmount(it)
                        }, {
                            listeners.didTapOnDecreaseProductItemAmount(it)
                        }) {
                            context.startProductDetailPage(it)
                        }
                    }

                    if (!cartResponse.data.suggestedProducts.isNullOrEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(28.dp))
                            Text(text = "Before you checkout", style = ZustTypography.bodyMedium, color = colorResource(id = R.color.app_black))
                            Spacer(modifier = Modifier.height(24.dp))
                            UpSellingUi(cartResponse.data.suggestedProducts, listeners)
                        }
                    }

                    item {
                        ViewSpacer20()
                        CartBillingDetails(orderSummaryViewModel)
                    }
                    item {
                        ViewSpacer20()
                        DeliveryPartnerTipUi(orderSummaryViewModel)
                    }
                    item {
                        ViewSpacer20()
                        CancellationPolicyUi(orderSummaryViewModel)
                        Spacer(modifier = Modifier.height(28.dp))
                    }
                } else {
                    listeners.finishActivity()
                }
            }

            is OrderSummaryUi.InitialUi -> {
                isLoading = cartResponse.isLoading
            }
        }
    }
}

