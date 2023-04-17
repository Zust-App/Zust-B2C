package `in`.opening.area.zustapp.viewmodels

import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.product.Utils
import `in`.opening.area.zustapp.product.model.CreateCartReqModel
import `in`.opening.area.zustapp.product.model.ProductApiResponse
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.product.model.convertProductToCreateOrder
import `in`.opening.area.zustapp.repository.ProductRepo
import `in`.opening.area.zustapp.uiModels.productList.ProductListUi
import `in`.opening.area.zustapp.utility.AppUtility
import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchProductViewModel @Inject constructor(private val productRepo: ProductRepo) : OrderSummaryNetworkVM(productRepo) {

    internal val productListUiState = MutableStateFlow<ProductListUi>(ProductListUi.InitialUi(false))
    internal var searchTextCache: String = ""

    private var job: Job? = null
    suspend fun searchProduct(searchText: String) {
        productListUiState.update {
            ProductListUi.InitialUi(true)
        }
        job?.cancel()
        searchTextCache = searchText
        job = viewModelScope.launch(Dispatchers.IO) {
            val serverResponse = productRepo.apiRequestManager.productSearchApi(searchText)
            val localSavedProducts = productRepo.getAllLocalProducts()
            combine(serverResponse, localSavedProducts) { server, local ->
                parseProductList(server, local)
                updateAddToCartFlow(local)
            }.collect {
                Log.e("collect", it.toString())
            }
        }
    }


    private fun parseProductList(serverList: ResultWrapper<ProductApiResponse>, localList: List<ProductSingleItem>) {
        when (serverList) {
            is ResultWrapper.Success -> {
                if (serverList.value.data != null) {
                    compareLocalAndServerResult(serverList.value, localList)
                } else {
                    productListUiState.update { ProductListUi.ErrorUi(false, errorMsg = serverList.value.message, errors = serverList.value.errors ?: arrayListOf()) }
                }
            }
            is ResultWrapper.GenericError -> {
                productListUiState.update { ProductListUi.ErrorUi(false, errorMsg = serverList.error?.error) }
            }
            is ResultWrapper.NetworkError -> {
                productListUiState.update { ProductListUi.ErrorUi(false, errorMsg = "Something went wrong") }

            }
            is ResultWrapper.UserTokenNotFound -> {
                productListUiState.update { ProductListUi.ErrorUi(false, errors = AppUtility.getAuthErrorArrayList()) }
            }
        }
    }


    private fun compareLocalAndServerResult(serverProduct: ProductApiResponse, dbSavedProducts: List<ProductSingleItem>) {
        if (serverProduct.data == null) {
            productListUiState.update { ProductListUi.ErrorUi(false, errorMsg = "Something went wrong") }
            return
        }
        val localProductMap = dbSavedProducts.associate { it.productPriceId to it.itemCountByUser }

        serverProduct.data.productItems?.forEach {
            if (localProductMap.containsKey(it.productPriceId)) {
                it.itemCountByUser = localProductMap[it.productPriceId] ?: 0
            } else {
                it.itemCountByUser = 0
            }
        }
        productListUiState.update { ProductListUi.ProductListSuccess(false, serverProduct.data.copy(serverProduct.data.productItems)) }
    }


    internal fun updateProductCount(product: ProductSingleItem?, action: ACTION) {
        product?.let {
            viewModelScope.launch {
                val copiedProductSingleItem = it.copy()
                productRepo.insertOrUpdateProduct(copiedProductSingleItem, action)
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