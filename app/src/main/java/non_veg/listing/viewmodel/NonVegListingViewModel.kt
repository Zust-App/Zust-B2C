package non_veg.listing.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import `in`.opening.area.zustapp.viewmodels.ACTION
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import non_veg.cart.models.CreateCartItem
import non_veg.cart.models.CreateCartReqBody
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
class NonVegListingViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager, private val nonVegAddToCartDao: NonVegAddToCartDao) : NonVegCartViewModel(apiRequestManager, nonVegAddToCartDao) {


    private val _nonVegProductUiModel = MutableStateFlow<NonVegProductListingUiModel>(NonVegProductListingUiModel.Empty(false))
    val nonVegProductListUiModel: StateFlow<NonVegProductListingUiModel> get() = _nonVegProductUiModel

    private var job: Job? = null
    private var merchantNonVegId: Int = -1
    internal fun getAndMappedLocalCartWithServerItems(categoryId: Int) {
        _nonVegProductUiModel.update {
            NonVegProductListingUiModel.Empty(true)
        }
        job?.cancel()
        merchantNonVegId = sharedPrefManager.getNonVegMerchantId()
        job = viewModelScope.launch {
            val itemsFromLocalNonVegCart = getAllNonVegFromLocal(merchantId = merchantNonVegId)
            val itemsFromServerSide = apiRequestManager.getNonVegProductByCategoryAndMerchantId(categoryId = categoryId, merchantNonVegId)
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

    internal fun handleNonVegCartInsertOrUpdate(nonVegListingSingleItem: NonVegListingSingleItem, action: ACTION) {
        handleNonVegCartInsertionOrUpdate(nonVegListingSingleItem, action)
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
}