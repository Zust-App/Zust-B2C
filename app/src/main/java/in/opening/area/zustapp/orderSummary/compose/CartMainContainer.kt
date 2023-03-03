package `in`.opening.area.zustapp.orderSummary.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.CustomAnimatedProgressBar
import `in`.opening.area.zustapp.extensions.collectAsStateLifecycleAware
import `in`.opening.area.zustapp.orderSummary.OrderItemsClickListeners
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.productDetails.presentation.ProductDetailsActivity
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import `in`.opening.area.zustapp.uiModels.orderSummary.OrderSummaryUi
import `in`.opening.area.zustapp.viewmodels.OrderSummaryViewModel
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp

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
                if (cartResponse.data.productsAlreadyInCart.isNotEmpty()) {
                    item {
                        ExpectedDeliveryTimeTag(orderSummaryViewModel)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item {
                        Text(text = "Items Added",
                            style = Typography_Montserrat.body1,
                            color = colorResource(id = R.color.app_black))
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    if (cartResponse.data.productsAlreadyInCart.isNotEmpty()) {
                        item {
                            Box(modifier = modifierType1)
                        }
                    }
                    cartResponse.data.productsAlreadyInCart.forEachIndexed { index, cartItem ->
                        item {
                            SelectedCartVerticalItemUi(cartItem, modifierType4, false, {
                                listeners.didTapOnIncreaseProductItemAmount(it)
                            }, {
                                listeners.didTapOnDecreaseProductItemAmount(it)
                            }) {
                                startProductDetailPage(it, context)
                            }
                            if (index < cartResponse.data.productsAlreadyInCart.lastIndex) {
                                Divider(modifier = Modifier
                                    .height(0.2.dp)
                                    .padding(horizontal = 20.dp)
                                    .background(color = Color(0xffA0A0A0)))
                            }
                        }
                    }
                    if (cartResponse.data.productsAlreadyInCart.isNotEmpty()) {
                        item {
                            Box(modifier = modifierType2)
                        }
                    }
                    if (!cartResponse.data.suggestedProducts.isNullOrEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(28.dp))
                            Text(text = "Before you checkout", style = Typography_Montserrat.body1, color = colorResource(id = R.color.app_black))
                            Spacer(modifier = Modifier.height(24.dp))
                            UpSellingUi(cartResponse.data.suggestedProducts, listeners)
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        CartBillingDetails(orderSummaryViewModel)
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        DeliveryPartnerTipUi(orderSummaryViewModel)
                    }
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(text = "Cancellation Policy",
                            style = Typography_Montserrat.body1,
                            color = colorResource(id = R.color.app_black))
                        CancellationPolicyUi(orderSummaryViewModel)
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

private fun startProductDetailPage(productSingleItem: ProductSingleItem, context: Context) {
    val productDetailIntent = Intent(context, ProductDetailsActivity::class.java)
    productDetailIntent.putExtra(ProductDetailsActivity.PRODUCT_KEY, productSingleItem)
    context.startActivity(productDetailIntent)
}