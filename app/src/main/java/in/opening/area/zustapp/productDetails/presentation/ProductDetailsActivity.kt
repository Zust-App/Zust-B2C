package `in`.opening.area.zustapp.productDetails.presentation

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.home.ACTION.*
import `in`.opening.area.zustapp.orderSummary.OrderSummaryActivity
import `in`.opening.area.zustapp.payment.PaymentActivity
import `in`.opening.area.zustapp.payment.models.PaymentActivityReqData
import `in`.opening.area.zustapp.product.ProductSelectionListener
import `in`.opening.area.zustapp.product.model.CreateCartData
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.productDetails.compose.ProductDetailTopBarUi
import `in`.opening.area.zustapp.productDetails.compose.ProductDetailMainUi
import `in`.opening.area.zustapp.ui.generic.CustomBottomBarView
import `in`.opening.area.zustapp.uiModels.CreateCartResponseUi
import `in`.opening.area.zustapp.uiModels.VALUE
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.ACTION
import `in`.opening.area.zustapp.viewmodels.ProductDetailsViewModel
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.update

@AndroidEntryPoint
class ProductDetailsActivity : AppCompatActivity(), ProductSelectionListener {
    private val viewModel: ProductDetailsViewModel by viewModels()

    companion object {
        const val PRODUCT_ID = "product_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromIntent()
        setContent {
            Scaffold(topBar = {
                ProductDetailTopBarUi(Modifier) {
                    handleNavigation(it)
                }
            }, bottomBar = {
                CustomBottomBarView(viewModel = viewModel, VALUE.A, {
                    if (!viewModel.isCreateCartOnGoing()) {
                        viewModel.createCartOrderWithServer(VALUE.A)
                    } else {
                        AppUtility.showToast(this, getString(R.string.please_wait))
                    }
                }) {
                    startOrderSummaryActivity(it)
                }
            }, content = { innerPadding ->
                ProductDetailMainUi(viewModel, innerPadding, this)
            })
        }
    }

    private fun getDataFromIntent() {
        if (intent.hasExtra(PRODUCT_ID)) {
            val productId = intent.getStringExtra(PRODUCT_ID)
            productId?.let {
                viewModel.setProductAndMerchantId(it)
                viewModel.getProductDetails()
            }
        } else {
            finish()
        }
    }

    override fun didTapOnIncrementCount(productSingleItem: ProductSingleItem?) {
        if (productSingleItem == null) {
            return
        }
        if (productSingleItem.maxItemPurchaseLimit > 0) {
            if (productSingleItem.itemCountByUser <= productSingleItem.maxItemPurchaseLimit) {
                viewModel.updateProductCount(productSingleItem, ACTION.INCREASE)
            } else {
                AppUtility.showToast(this, "You can't add more than ${productSingleItem.maxItemPurchaseLimit}")
            }
        } else {
            viewModel.updateProductCount(productSingleItem, ACTION.INCREASE)
        }
    }

    override fun didTapOnDecrementCount(productSingleItem: ProductSingleItem?) {
        if (productSingleItem?.itemCountByUser == 0) {
            return
        }
        viewModel.updateProductCount(productSingleItem, ACTION.DECREASE)
    }

    override fun didTapOnContainerClick(productSingleItem: ProductSingleItem?) {

    }

    private fun handleNavigation(action: `in`.opening.area.zustapp.home.ACTION) {
        if (action == NAV_BACK) {
            finish()
        }
    }

    private fun startOrderSummaryActivity(createCartData: CreateCartData) {
        viewModel.createCartUiState.update { CreateCartResponseUi.InitialUi(false) }
        val intent = Intent(this, OrderSummaryActivity::class.java)
        val paymentActivityReqData = PaymentActivityReqData()
        paymentActivityReqData.apply {
            orderId = createCartData.orderId
            itemPrice = createCartData.itemTotalPrice
            deliveryFee = createCartData.deliveryFee
            packagingFee = createCartData.packagingFee
            couponDiscount = createCartData.couponMaxDiscountPrice
            couponString = createCartData.couponCode
        }
        intent.putExtra(PaymentActivity.PAYMENT_MODEL_KEY, paymentActivityReqData)
        startActivity(intent)
    }


}

