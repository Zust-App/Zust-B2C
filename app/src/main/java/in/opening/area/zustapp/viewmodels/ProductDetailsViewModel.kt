package `in`.opening.area.zustapp.viewmodels

import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.product.Utils
import `in`.opening.area.zustapp.product.model.CreateCartReqModel
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.product.model.convertProductToCreateOrder
import `in`.opening.area.zustapp.productDetails.models.convertToProductSingleItems
import `in`.opening.area.zustapp.repository.ProductRepo
import `in`.opening.area.zustapp.uiModels.ProductDetailsUiState
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(private val productRepo: ProductRepo) : OrderSummaryNetworkVM(productRepo) {
    @Inject
    lateinit var sharedPrefManager: SharedPrefManager
    private var productId: String? = null
    private var merchantId: String? = null
    internal val singleItemUiState = MutableStateFlow<ProductDetailsUiState>(ProductDetailsUiState.InitialUi(false))
    private var job: Job? = null

    private var productVariants: ArrayList<ProductSingleItem>? = null

    private fun attachObservers() {
        job?.cancel()
        job = viewModelScope.launch {
            productRepo.getAllLocalProducts().collectLatest { it ->
                updateAddToCartFlow(it)
                val map = it.associateBy { item ->
                    item.productPriceId
                }
                productVariants?.let { variants ->
                    variants.forEach {
                        if (map.containsKey(it.productPriceId)) {
                            it.itemCountByUser = map[it.productPriceId]?.itemCountByUser ?: 0
                        } else {
                            it.itemCountByUser = 0
                        }
                    }
                }
                singleItemUiState.update { ProductDetailsUiState.Success(productVariants, false) }
            }
        }
    }

    internal fun setProductAndMerchantId(productId: String) {
        this.productId = productId
        this.merchantId = sharedPrefManager.getMerchantId().toString()
    }

    internal fun getProductDetails() = viewModelScope.launch {
        if (merchantId != null && productId != null) {
            singleItemUiState.update {
                ProductDetailsUiState.InitialUi(true)
            }
            when (val response = productRepo.apiRequestManager.getProductDetails(merchantId = merchantId!!.toLong(), productId = productId!!.toLong())) {
                is ResultWrapper.Success -> {
                    response.value.data?.let {
                        productVariants = it.convertToProductSingleItems()
                        attachObservers()
                    } ?: run {
                        singleItemUiState.update {
                            ProductDetailsUiState.ErrorUi(false, "Something went wrong,please try again")
                        }
                    }
                }

                else -> {
                    singleItemUiState.update {
                        ProductDetailsUiState.ErrorUi(false, "Something went wrong,please try again")
                    }
                }
            }

        }
    }

    internal fun updateProductCount(product: ProductSingleItem?, action: ACTION) {
        product?.let {
            viewModelScope.launch {
                productRepo.insertOrUpdateProduct(it, action)
            }
        }
    }

    private fun updateAddToCartFlow(addToCartList: List<ProductSingleItem>) {
        val priceAndTotalItemCount = Utils.calculatePriceAndItemCount(addToCartList as ArrayList<ProductSingleItem>)
        val orderBodyRequest = addToCartList.map {
            it.convertProductToCreateOrder()
        }
        addToCartFlow.tryEmit(CreateCartReqModel(-1, priceAndTotalItemCount.first, orderBodyRequest, priceAndTotalItemCount.second))
    }

}

