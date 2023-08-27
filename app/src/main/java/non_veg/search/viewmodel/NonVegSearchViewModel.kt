package non_veg.search.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.viewmodels.ACTION
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import non_veg.cart.uiModel.NonVegCartItemSummaryUiModel
import non_veg.cart.viewmodel.NonVegCartViewModel
import non_veg.common.model.CartSummaryData
import non_veg.listing.models.NonVegItemListModel
import non_veg.listing.models.NonVegListingSingleItem
import non_veg.listing.uiModel.NonVegProductListingUiModel
import non_veg.storage.NonVegItemLocalModel
import non_veg.storage.dao.NonVegAddToCartDao
import javax.inject.Inject

@HiltViewModel
class NonVegSearchViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager, private val nonVegAddToCartDao: NonVegAddToCartDao) : NonVegCartViewModel(apiRequestManager, nonVegAddToCartDao) {
    internal val _nonVegProductUiModel = MutableStateFlow<NonVegProductListingUiModel>(NonVegProductListingUiModel.Empty(false))
    internal val nonVegProductListUiModel: StateFlow<NonVegProductListingUiModel> get() = _nonVegProductUiModel

    var searchTextCache: String = ""

    private var job: Job? = null
    internal fun searchProduct(searchText: String) {
        job?.cancel()
        searchTextCache = searchText.trim()
        job?.cancel()
        _nonVegProductUiModel.update {
            NonVegProductListingUiModel.Empty(true)
        }
        if (nonVegMerchantId == -2) {
            return
        }
        if (nonVegMerchantId == -1) {
            nonVegMerchantId = sharedPrefManager.getNonVegMerchantId()
        }
        if (nonVegMerchantId == -1) {
            nonVegMerchantId = -2
            return
        }
        job = viewModelScope.launch {
            val itemsFromLocalNonVegCart = getAllNonVegFromLocal(merchantId = nonVegMerchantId)
            val itemsFromServerSide = apiRequestManager.searchNonVegProduct(merchantId = nonVegMerchantId, searchInput = searchText)
            combine(itemsFromLocalNonVegCart, itemsFromServerSide) { localCart, serverProduct ->
                mergeLocalAndServerResult(localCart, serverProduct)
            }.collect()
        }
    }

    private fun mergeLocalAndServerResult(
        localCart: List<NonVegItemLocalModel>,
        serverProductResponse: ResultWrapper<NonVegItemListModel>,
    ) {
        when (serverProductResponse) {
            is ResultWrapper.Success -> {
                val mapOfLocalData = localCart.associateBy { it.productPriceId }
                val mergedProductList = serverProductResponse.value.data?.map { serverProduct ->
                    serverProduct.copy(
                        quantityOfItemInCart = mapOfLocalData[serverProduct.productPriceId]?.quantity ?: 0
                    )
                }
                nonVegCartSummaryData(localCart)
                _nonVegProductUiModel.update {
                    NonVegProductListingUiModel.Success(mergedProductList, false)
                }
            }

            else -> {
                _nonVegProductUiModel.update {
                    NonVegProductListingUiModel.Error("Error occured", false)
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

    internal fun handleNonVegCartInsertOrUpdate(nonVegListingSingleItem: NonVegListingSingleItem, action: ACTION) {
        handleNonVegCartInsertionOrUpdate(nonVegListingSingleItem, action)
    }

}