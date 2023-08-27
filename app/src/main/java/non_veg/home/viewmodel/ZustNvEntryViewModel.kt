package non_veg.home.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.address.model.getDisplayString
import `in`.opening.area.zustapp.locationManager.UserLocationDetails
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import zustbase.orderDetail.models.ZustAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import non_veg.cart.models.CreateCartItem
import non_veg.cart.uiModel.NonVegCartItemSummaryUiModel
import non_veg.cart.viewmodel.NonVegCartViewModel
import non_veg.common.model.CartSummaryData
import non_veg.home.uiModel.NvHomePageCombinedUiModel
import non_veg.listing.models.NonVegListingSingleItem
import non_veg.storage.NonVegItemLocalModel
import non_veg.storage.dao.NonVegAddToCartDao
import javax.inject.Inject

@HiltViewModel
class ZustNvEntryViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager, private val nonVegAddToCartDao: NonVegAddToCartDao) : NonVegCartViewModel(apiRequestManager, nonVegAddToCartDao) {

    private val _nonVegHomePageUiModel = MutableStateFlow<NvHomePageCombinedUiModel>(NvHomePageCombinedUiModel.Initial(false))
    val nonVegHomePageUiModel: StateFlow<NvHomePageCombinedUiModel> get() = _nonVegHomePageUiModel

    internal val userLocationFlow = MutableStateFlow(UserLocationDetails())


    //this function handle all the location and home page data functionality
    internal fun getUserSavedAddress() {
        val address = sharedPrefManager.getUserAddress()
        updateAddressItem(address)
        address?.let {
            if (address.latitude != null && address.latitude != 0.0 && address.pinCode != null) {
                getNonVegHomePageData(address.pinCode, address.latitude, address.longitude)
            } else {
                _nonVegHomePageUiModel.value = NvHomePageCombinedUiModel.Error(true, "Something went wrong Invalid address")
            }
        }
    }

    override fun updateAddressItem(zustAddress: ZustAddress?) {
        userLocationFlow.update {
            UserLocationDetails(zustAddress?.latitude, zustAddress?.longitude, zustAddress?.getDisplayString())
        }
    }


    private fun getNonVegHomePageData(pinCode: String, lat: Double?, lng: Double?) = viewModelScope.launch {
        _nonVegHomePageUiModel.update {
            NvHomePageCombinedUiModel.Initial(true)
        }
        when (val response = apiRequestManager.getNonVegHomePageData(pinCode, lat = lat, lng = lng)) {
            is ResultWrapper.Success -> {
                if (response.value.data != null) {
                    response.value.data.merchantDetails?.let {
                        sharedPrefManager.saveNonVegMerchantId(it.id)
                    }
                    _nonVegHomePageUiModel.update {
                        NvHomePageCombinedUiModel.Success(response.value.data, false)
                    }
                } else {
                    _nonVegHomePageUiModel.update {
                        NvHomePageCombinedUiModel.Error(false, "We are currently not available", ApiRequestManager.NOT_COVERAGE_ERROR_CODE)
                    }
                }
            }

            is ResultWrapper.NetworkError -> {
                sharedPrefManager.removeNonVegMerchantId()
                _nonVegHomePageUiModel.update {
                    NvHomePageCombinedUiModel.Error(false, "Something went wrong please try again")
                }
            }

            is ResultWrapper.GenericError -> {
                sharedPrefManager.removeNonVegMerchantId()
                _nonVegHomePageUiModel.update {
                    NvHomePageCombinedUiModel.Error(false, "Something went wrong Please try again")
                }
            }

            else -> {
                sharedPrefManager.removeNonVegMerchantId()
                _nonVegHomePageUiModel.update {
                    NvHomePageCombinedUiModel.Error(false, "Something went wrong Please try again")
                }
            }
        }
    }

    internal fun getUserLatestLocalCartDetails() = viewModelScope.launch(Dispatchers.IO) {
        supervisorScope {
            nonVegMerchantId = sharedPrefManager.getNonVegMerchantId()
            if (nonVegMerchantId == -1) {
                return@supervisorScope
            }
            val localCartItems = async { nonVegAddToCartDao.getAllNonVegItemFromLocals(merchantId = nonVegMerchantId) }
            localCartItems.await().also { data ->
                if (data.isNotEmpty()) {
                    val reqData = data.map { it.productPriceId }
                    when (val response = apiRequestManager.getUserLatestLocalCartDetails(reqData, merchantId = nonVegMerchantId)) {
                        is ResultWrapper.Success -> {
                            response.value.data?.let {
                                resetLocalCartData(it, data)
                            } ?: run {
                                startCollectingLocalInfoItems()
                            }
                        }

                        else -> {
                            startCollectingLocalInfoItems()
                        }
                    }
                } else {
                    startCollectingLocalInfoItems()
                }
            }
        }
    }

    private fun resetLocalCartData(nonVegCartData: List<NonVegListingSingleItem>, reqData: List<NonVegItemLocalModel>) = viewModelScope.launch(Dispatchers.IO) {
        cartItemsForRequest.clear()
        if (nonVegCartData.isNotEmpty()) {
            val reqLocalDataMap = reqData.associateBy { it.productPriceId }
            val nonVegLocalCartItems = nonVegCartData.map {
                if (it.price > 0) {
                    cartItemsForRequest.add(CreateCartItem(mrp = it.mrp ?: 0.0, price = it.price, productPriceId = it.productPriceId, quantity = reqLocalDataMap[it.productPriceId]?.quantity ?: 0))
                }
                NonVegItemLocalModel(it.productPriceId, it.merchantId, reqLocalDataMap[it.productPriceId]?.quantity ?: 0, it.price, it.mrp ?: 0.0, it.productId, it.categoryId)
            }
            nonVegAddToCartDao.deleteAllAndInsertAll(nonVegLocalCartItems)
        }
        startCollectingLocalInfoItems()
    }

    private fun startCollectingLocalInfoItems() = viewModelScope.launch(Dispatchers.IO) {
        nonVegAddToCartDao.getAllNonVegItemFromLocal(merchantId = nonVegMerchantId).collectLatest {
            nonVegCartSummaryData(it)
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
}