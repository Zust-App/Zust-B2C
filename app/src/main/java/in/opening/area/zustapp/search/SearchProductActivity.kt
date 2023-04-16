package `in`.opening.area.zustapp.search

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.compose.NoProductFoundErrorPage
import `in`.opening.area.zustapp.orderSummary.OrderSummaryActivity
import `in`.opening.area.zustapp.orderSummary.compose.SelectedCartVerticalItemUi
import `in`.opening.area.zustapp.payment.PaymentActivity
import `in`.opening.area.zustapp.payment.models.PaymentActivityReqData
import `in`.opening.area.zustapp.product.ProductSelectionListener
import `in`.opening.area.zustapp.product.model.CreateCartData
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.profile.SuggestProductBtmSheet
import `in`.opening.area.zustapp.search.compose.SearchBarUi
import `in`.opening.area.zustapp.search.compose.SearchResultVerticalLayout
import `in`.opening.area.zustapp.ui.generic.CustomBottomBarView
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_20
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.uiModels.CreateCartResponseUi
import `in`.opening.area.zustapp.uiModels.VALUE
import `in`.opening.area.zustapp.uiModels.productList.ProductListUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.startProductDetailPage
import `in`.opening.area.zustapp.viewmodels.ACTION
import `in`.opening.area.zustapp.viewmodels.SearchProductViewModel
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.update

@AndroidEntryPoint
class SearchProductActivity : AppCompatActivity(), ProductSelectionListener {
    private val searchProductViewModel: SearchProductViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                bottomBar = {
                    CustomBottomBarView(viewModel = searchProductViewModel, VALUE.A, {
                        searchProductViewModel.createCartOrderWithServer(VALUE.A)
                    }) {
                        startOrderSummaryActivity(it)
                    }
                },
                topBar = {
                    ComposeCustomTopAppBar(Modifier, "Search") {
                        if (it == `in`.opening.area.zustapp.home.ACTION.NAV_BACK) {
                            finish()
                        }
                    }
                },
                backgroundColor = colorResource(id = R.color.screen_surface_color),
                content = { paddingValue ->
                    SearchProductMainContainer(paddingValue, Modifier)
                },
            )
        }
    }

    @Composable
    private fun SearchProductMainContainer(paddingValue: PaddingValues, modifier: Modifier) {
        val context = LocalContext.current
        var showProgressBar by remember {
            mutableStateOf(false)
        }
        val productSearchData by searchProductViewModel.productListUiState.collectAsState(ProductListUi.InitialUi(false))

        ConstraintLayout(modifier = modifier
            .fillMaxWidth()
            .padding(paddingValue)
            .fillMaxHeight()) {
            val (progressBar) = createRefs()
            val (searchSection, searchResult) = createRefs()
            val (resultTitleText) = createRefs()
            showProgressBar = productSearchData.isLoading

            when (val data = productSearchData) {
                is ProductListUi.ProductListSuccess -> {
                    Text(text = buildString {
                        append("Showing ")
                        append((data.data.productItems?.size ?: 0))
                        append(" Result for `${searchProductViewModel.searchTextCache}`")
                    }, modifier = modifier.constrainAs(resultTitleText) {
                        top.linkTo(searchSection.bottom, dp_4)
                        start.linkTo(parent.start, dp_20)
                        end.linkTo(parent.end, dp_20)
                        width = Dimension.fillToConstraints
                    }, style = ZustTypography.body1)
                    if (data.data.productItems.isNullOrEmpty()) {
                        NoProductFoundErrorPage(layoutScope = this, topReference = searchSection) {
                            showSuggestProductSheet()
                        }
                    } else {
                        if (!data.data.productItems.isNullOrEmpty()) {
                            LazyColumn(modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp,
                                    vertical = 8.dp)
                                .constrainAs(searchResult) {
                                    top.linkTo(resultTitleText.bottom, dp_16)
                                    end.linkTo(parent.end)
                                    start.linkTo(parent.start)
                                    bottom.linkTo(parent.bottom)
                                    height = Dimension.fillToConstraints
                                }) {
                                items(data.data.productItems!!) {
                                    SearchResultVerticalLayout(it, searchResultModifier,
                                        { product ->
                                            didTapOnIncrementCount(product)
                                        }, { product ->
                                            didTapOnDecrementCount(product)
                                        }) { product ->
                                        context.startProductDetailPage(product)
                                    }
                                }
                            }
                        }
                    }
                }
                is ProductListUi.ErrorUi -> {
                    AppUtility.showToast(context, "Something went wrong")
                }
                is ProductListUi.InitialUi -> {

                }
            }

            if (showProgressBar) {
                CircularProgressIndicator(modifier = modifier.constrainAs(progressBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                })
            }

            SearchBarUi(modifier = modifier.constrainAs(searchSection) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }, modifier1 = modifier,
                viewModel = searchProductViewModel)
        }
    }


    override fun didTapOnIncrementCount(productSingleItem: ProductSingleItem?) {
        searchProductViewModel.updateProductCount(productSingleItem, ACTION.INCREASE)
    }

    override fun didTapOnDecrementCount(productSingleItem: ProductSingleItem?) {
        searchProductViewModel.updateProductCount(productSingleItem, ACTION.DECREASE)
    }

    override fun didTapOnContainerClick(productSingleItem: ProductSingleItem?) {
        if (productSingleItem != null) {
            this.startProductDetailPage(productSingleItem)
        }
    }


    private fun startOrderSummaryActivity(createCartData: CreateCartData) {
        searchProductViewModel.createCartUiState.update { CreateCartResponseUi.InitialUi(false) }
        val intent = Intent(this, OrderSummaryActivity::class.java)
        val paymentActivityReqData = PaymentActivityReqData()
        paymentActivityReqData.apply {
            orderId = createCartData.orderId
            itemPrice = createCartData.itemTotalPrice
            deliveryFee = createCartData.deliveryFee
            packagingFee = createCartData.packagingFee
            couponDiscount = createCartData.couponMaxDiscountPrice
        }
        intent.putExtra(PaymentActivity.PAYMENT_MODEL_KEY, paymentActivityReqData)
        startActivity(intent)
    }

    companion object {
        const val SEARCH_THRESHOLD = 1
    }

    private fun showSuggestProductSheet() {
        val suggestProductSheet: SuggestProductBtmSheet = SuggestProductBtmSheet.newInstance()
        suggestProductSheet.show(supportFragmentManager, "suggest_product")
    }
}

private val searchResultModifier = Modifier
    .fillMaxWidth()
    .wrapContentHeight()
    .padding(bottom = 8.dp)
    .background(color = Color(0xffffffff))
    .padding(start = 12.dp,
        end = 16.dp,
        top = 12.dp,
        bottom = 12.dp)
