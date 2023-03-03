package `in`.opening.area.zustapp.viewmodels

import `in`.opening.area.zustapp.uiModels.ProductDetailsUiState
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.product.Utils
import `in`.opening.area.zustapp.product.model.CreateCartReqModel
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.product.model.convertProductToCreateOrder
import `in`.opening.area.zustapp.repository.ProductRepo
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager, private val productRepo: ProductRepo) : OrderSummaryNetworkVM(apiRequestManager) {
    internal var singleItemCache: ProductSingleItem? = null
    internal val singleItemUiState = MutableStateFlow<ProductDetailsUiState>(ProductDetailsUiState.InitialUi(false))
    private var job: Job? = null

    internal fun attachObservers() {
        job?.cancel()
        job = viewModelScope.launch {
            productRepo.getAllLocalProducts().collectLatest { it ->
                updateAddToCartFlow(it)
                val requiredItem = it.firstOrNull { it.productPriceId == singleItemCache?.productPriceId }
                singleItemUiState.update { ProductDetailsUiState.Success(requiredItem, false) }
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
        addToCartFlow.tryEmit(CreateCartReqModel(1, priceAndTotalItemCount.first, orderBodyRequest, priceAndTotalItemCount.second))
    }

}

