package `in`.opening.area.zustapp.product

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.ComposeTopAppBarProductList
import `in`.opening.area.zustapp.extensions.showBottomSheetIsNotPresent
import `in`.opening.area.zustapp.home.ACTION.*
import `in`.opening.area.zustapp.orderSummary.OrderSummaryActivity
import `in`.opening.area.zustapp.payment.PaymentActivity.Companion.PAYMENT_MODEL_KEY
import `in`.opening.area.zustapp.payment.models.PaymentActivityReqData
import `in`.opening.area.zustapp.product.model.CreateCartData
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.product.others.OtherCategoryUi
import `in`.opening.area.zustapp.productDetails.ProductDetailsCallback
import `in`.opening.area.zustapp.profile.SuggestProductBtmSheet
import `in`.opening.area.zustapp.search.SearchProductActivity
import `in`.opening.area.zustapp.ui.generic.CustomBottomBarView
import `in`.opening.area.zustapp.uiModels.CreateCartResponseUi
import `in`.opening.area.zustapp.uiModels.VALUE
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.startProductDetailPage
import `in`.opening.area.zustapp.viewmodels.ACTION
import `in`.opening.area.zustapp.viewmodels.ProductListingViewModel
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ui.colorBlack
import ui.colorWhite
import ui.linearGradientGroceryBrush
import ui.linearGradientNonVegBrush
import zustbase.utility.showSuggestProductSheet

@AndroidEntryPoint
class ProductListingActivity : AppCompatActivity(), ProductSelectionListener, ProductDetailsCallback {
    private val productListingViewModel: ProductListingViewModel by viewModels()
    private var categoryId: Int? = null
    private var categoryName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromIntent()
        setContent {
            Scaffold(topBar = {
                ComposeTopAppBarProductList(Modifier.background(color = colorWhite),
                    buildString {
                        append(categoryName ?: "")
                        append(" Category")
                    }, "", color = colorBlack, R.drawable.new_search_icon) {
                    if (it == NAV_BACK) {
                        finish()
                    } else if (it == SEARCH_PRODUCT) {
                        startSearchActivity()
                    }
                }
            }, bottomBar = {
                CustomBottomBarView(viewModel = productListingViewModel, VALUE.A, {
                    if (!productListingViewModel.isCreateCartOnGoing()) {
                        productListingViewModel.createCartOrderWithServer(VALUE.A)
                    } else {
                        AppUtility.showToast(this, "Please wait")
                    }
                }) {
                    startOrderSummaryActivity(it)
                }
            }, content = { innerPadding ->
                SetUpContent(innerPadding)
            })

        }
        lifecycleScope.launch {
            if (categoryId != null) {
                productListingViewModel.updateCategoryIdBasedOnSelection(categoryId!!)
                productListingViewModel.getMergedResponseOfProduct(categoryId!!)
            }
        }
    }

    private fun getDataFromIntent() {
        if (intent != null && intent.hasExtra(CATEGORY_ID)) {
            categoryId = intent.getIntExtra(CATEGORY_ID, -1)
        }
        if (intent != null && intent.hasExtra(CATEGORY_NAME)) {
            categoryName = intent.getStringExtra(CATEGORY_NAME)
            productListingViewModel.updateHeaderData(categoryName)
        }
        if (categoryId == null) {
            finish()
        }
    }


    @Composable
    private fun SetUpContent(innerPadding: PaddingValues) {
        ConstraintLayout(modifier = Modifier
            .fillMaxHeight()
            .padding(innerPadding)
            .background(color = colorResource(id = R.color.screen_surface_color))
            .fillMaxWidth()) {
            val (otherCategoryUi, productList) = createRefs()
            OtherCategoryUi(otherCategoryUi, productListingViewModel)
            ProductListingContainer(this, productList, productListingViewModel,
                this@ProductListingActivity, otherCategoryUi)
        }
    }

    override fun didTapOnIncrementCount(productSingleItem: ProductSingleItem?) {
        if (productSingleItem == null) {
            return
        }
        if (productSingleItem.maxItemPurchaseLimit > 0) {
            if (productSingleItem.itemCountByUser < productSingleItem.maxItemPurchaseLimit) {
                productListingViewModel.updateProductCount(productSingleItem, ACTION.INCREASE)
            } else {
                AppUtility.showToast(this, "You can't add more than ${productSingleItem.maxItemPurchaseLimit}")
            }
        } else {
            productListingViewModel.updateProductCount(productSingleItem, ACTION.INCREASE)
        }
    }

    override fun didTapOnDecrementCount(productSingleItem: ProductSingleItem?) {
        if (productSingleItem?.itemCountByUser == 0) {
            return
        }
        productListingViewModel.updateProductCount(productSingleItem, ACTION.DECREASE)
    }

    override fun didTapOnContainerClick(productSingleItem: ProductSingleItem?) {
        if (productSingleItem == null) {
            return
        }
        this.startProductDetailPage(productSingleItem)
    }

    private fun startOrderSummaryActivity(createCartData: CreateCartData) {
        productListingViewModel.createCartUiState.update { CreateCartResponseUi.InitialUi(false) }
        val intent = Intent(this, OrderSummaryActivity::class.java)
        val paymentActivityReqData = PaymentActivityReqData()
        paymentActivityReqData.apply {
            orderId = createCartData.orderId
            itemPrice = createCartData.itemTotalPrice
            deliveryFee = createCartData.deliveryFee
            packagingFee = createCartData.packagingFee
            couponDiscount = createCartData.couponMaxDiscountPrice
            couponString = createCartData.couponCode
            isFreeDelivery = createCartData.isFreeDelivery
            expectedDelivery = createCartData.expectedDelivery
        }
        intent.putExtra(PAYMENT_MODEL_KEY, paymentActivityReqData)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    companion object {
        const val CATEGORY_ID = "category_id"
        const val CATEGORY_NAME = "category_name"
    }

    override fun didTapOnViewCartBtn() {

    }

    override fun startOrderSummary(cartData: CreateCartData) {
        startOrderSummaryActivity(cartData)
    }

    override fun openSuggestProduct() {
        this.showSuggestProductSheet()
    }

    private fun startSearchActivity() {
        val searchIntent = Intent(this, SearchProductActivity::class.java)
        startActivity(searchIntent)
    }
}

