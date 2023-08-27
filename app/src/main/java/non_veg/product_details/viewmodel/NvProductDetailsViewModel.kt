package non_veg.product_details.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import non_veg.cart.uiModel.NonVegCartItemSummaryUiModel
import non_veg.cart.viewmodel.NonVegCartViewModel
import non_veg.common.model.CartSummaryData
import non_veg.listing.models.NonVegItemListModel
import non_veg.product_details.uimodel.NvProductDetailsUiState
import non_veg.storage.NonVegItemLocalModel
import non_veg.storage.dao.NonVegAddToCartDao
import javax.inject.Inject

@HiltViewModel
class NvProductDetailsViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager, private val nonVegAddToCartDao: NonVegAddToCartDao) : NonVegCartViewModel(apiRequestManager, nonVegAddToCartDao) {

    internal var productId: Int = -1
    internal var productPriceId: Int = -1
    private val _nvProductDetailUiModel = MutableStateFlow<NvProductDetailsUiState>(NvProductDetailsUiState.Initial(false))
    internal val nvProductDetailUiModel: SharedFlow<NvProductDetailsUiState> get() = _nvProductDetailUiModel


    internal fun getNonVegProductDetail() = viewModelScope.launch(Dispatchers.IO) {
        if (nonVegMerchantId == -2) {
            return@launch
        }
        if (nonVegMerchantId == -1) {
            nonVegMerchantId = sharedPrefManager.getNonVegMerchantId()
        }
        if (nonVegMerchantId == -1) {
            nonVegMerchantId = -2
            return@launch
        }
        if (productId == -1 && productPriceId == -1) {
            return@launch
        }
        _nvProductDetailUiModel.update {
            NvProductDetailsUiState.Initial(true)
        }
        val localNvProductDetails = getProductDetailsFromLocal()
        val serverProductDetails = getProductDetailsFromServer()
        combine(localNvProductDetails, serverProductDetails) { local, server ->
            parseLocalAndServerResult(local, server)
        }.collectLatest {

        }
    }

    private fun parseLocalAndServerResult(local: List<NonVegItemLocalModel>, response: ResultWrapper<NonVegItemListModel>) {
        when (response) {
            is ResultWrapper.Success -> {
                response.value.data?.let { data ->
                    val localNvItemsMap = local.associateBy { it.productPriceId }
                    val mergedProductList = data.map { serverProduct ->
                        serverProduct.copy(
                            quantityOfItemInCart = localNvItemsMap[serverProduct.productPriceId]?.quantity ?: 0
                        )
                    }
                    nonVegCartSummaryData(local)
                    _nvProductDetailUiModel.update {
                        NvProductDetailsUiState.Success(false, mergedProductList)
                    }
                } ?: run {
                    _nvProductDetailUiModel.update {
                        NvProductDetailsUiState.Error(false, "Something went wrong")
                    }
                }
            }

            else -> {
                _nvProductDetailUiModel.update {
                    NvProductDetailsUiState.Error(false, "Something went wrong")
                }
            }
        }
    }

    private fun nonVegCartSummaryData(localCart: List<NonVegItemLocalModel>) {
        var itemCountInCart = 0
        var itemValueInCart = 0.0
        itemsInCart = localCart
        localCart.forEach {
            if (it.quantity > 0) {
                itemCountInCart += it.quantity
                itemValueInCart += (it.price) * it.quantity
            }
        }
        _cartSummaryUiModel.update {
            NonVegCartItemSummaryUiModel.Success(CartSummaryData(itemCountInCart = itemCountInCart, itemValueInCart = itemValueInCart))
        }
    }

    private fun getProductDetailsFromServer() = flow {
        emit(apiRequestManager.getNonVegProductDetails(productPriceId = productPriceId, productId = productId, merchantId = nonVegMerchantId))
    }

    private fun getProductDetailsFromLocal() = nonVegAddToCartDao.getAllNonVegItemFromLocal(nonVegMerchantId)
}