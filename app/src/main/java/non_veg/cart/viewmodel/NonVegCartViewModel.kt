package non_veg.cart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.address.model.getDisplayString
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import zustbase.orderDetail.models.Address
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import `in`.opening.area.zustapp.viewmodels.ACTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import non_veg.cart.models.CreateCartItem
import non_veg.cart.models.CreateCartReqBody
import non_veg.cart.models.CreateFinalCartReqBody
import non_veg.cart.models.ItemsInCart
import non_veg.cart.models.NonVegCartData
import non_veg.cart.models.UpdateNonVegCartItemReqBody
import non_veg.cart.uiModel.NonVegCartItemSummaryUiModel
import non_veg.cart.uiModel.NonVegCartUiModel
import non_veg.common.model.CartSummaryData
import non_veg.listing.models.NonVegListingSingleItem
import non_veg.listing.models.convertToNonVegCartItem
import non_veg.storage.NonVegItemLocalModel
import non_veg.storage.dao.NonVegAddToCartDao
import javax.inject.Inject

@HiltViewModel
open class NonVegCartViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager, private val nonVegAddToCartDao: NonVegAddToCartDao) : ViewModel() {

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    private val _cartDetailsState = MutableStateFlow<NonVegCartUiModel>(NonVegCartUiModel.Initial(false))
    internal val cartDetailsState: StateFlow<NonVegCartUiModel> get() = _cartDetailsState

    internal val _createCartUiModel = MutableStateFlow<NonVegCartUiModel>(NonVegCartUiModel.Initial(false))
    internal val createCartUiModel: StateFlow<NonVegCartUiModel> get() = _createCartUiModel

    internal val _cartSummaryUiModel = MutableStateFlow<NonVegCartItemSummaryUiModel>(NonVegCartItemSummaryUiModel.InitialUi)
    internal val cartSummaryUiModel: StateFlow<NonVegCartItemSummaryUiModel> get() = _cartSummaryUiModel

    private var currentCartIdFromServer: Int? = null

    internal var itemsInCart: List<NonVegItemLocalModel>? = null
    internal val addressLineData = MutableStateFlow("")
    internal var addressItemCache: Address? = null

    private val cartItemsForRequest = arrayListOf<CreateCartItem>()
    private var nonVegMerchantId: Int = -1//default -2 is reset means already try to set the value >-1 but not found
    internal fun getNonVegCartDetails(cartId: Int) = viewModelScope.launch(Dispatchers.Default) {
        currentCartIdFromServer = cartId
        _cartDetailsState.update {
            NonVegCartUiModel.Initial(true)
        }
        nonVegMerchantId = sharedPrefManager.getNonVegMerchantId()
        when (val response = apiRequestManager.getNonVegCartDetails(cartId = cartId, merchantId = nonVegMerchantId)) {
            is ResultWrapper.Success -> {
                if (response.value.data != null) {
                    _cartSummaryUiModel.update {
                        NonVegCartItemSummaryUiModel.Success(CartSummaryData(itemCountInCart = response.value.data.noOfItemsInCart,
                            itemValueInCart = response.value.data.itemPrice,
                            deliveryFee = response.value.data.deliveryFee,
                            serviceFee = response.value.data.serviceCharge,
                            packagingFee = response.value.data.packagingFee))
                    }
                    cartItemsForRequest.clear()
                    response.value.data.itemsInCart?.forEach {
                        if (it.price > 0 && it.quantity > 0) {
                            cartItemsForRequest.add(CreateCartItem(mrp = it.mrp, price = it.price, productPriceId = it.merchantProductId, quantity = it.quantity))
                        }
                    }
                    _cartDetailsState.value = NonVegCartUiModel.Success(response.value.data, false)
                }
            }

            else -> {
                _cartDetailsState.value = NonVegCartUiModel.Error("An error occurred", false)
            }
        }
    }

    internal fun updateUserNonVegCart(cartItem: ItemsInCart, action: ACTION) = viewModelScope.launch(Dispatchers.IO) {
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
        if (currentCartIdFromServer == null || currentCartIdFromServer == -1) {
            return@launch
        }
        var quantity = cartItem.quantity
        var actionType = action
        if (action == ACTION.INCREASE && quantity >= 0) {
            quantity += 1
        } else if (action == ACTION.DECREASE && (quantity - 1) <= 0) {
            actionType = ACTION.DELETE
        } else if (action == ACTION.DECREASE) {
            actionType = ACTION.DECREASE
            quantity -= 1
        }

        val updateNonVegCartReqBody = UpdateNonVegCartItemReqBody(cartId = currentCartIdFromServer!!,
            merchantId = nonVegMerchantId,
            mrp = cartItem.mrp,
            price = cartItem.price,
            productPriceId = cartItem.merchantProductId,
            quantity = quantity, updateType = actionType.name
        )
        _cartDetailsState.update {
            NonVegCartUiModel.Initial(true, isUpdateApiCall = true)
        }
        when (val response = apiRequestManager.updateUserNonVegCart(updateNonVegCartReqBody)) {
            is ResultWrapper.Success -> {
                if (response.value.data != null) {
                    insertAllNewCartItemFromServerInLocal(response.value.data)
                    _cartSummaryUiModel.update {
                        NonVegCartItemSummaryUiModel.Success(CartSummaryData(itemCountInCart = response.value.data.noOfItemsInCart,
                            itemValueInCart = response.value.data.itemPrice,
                            deliveryFee = response.value.data.deliveryFee,
                            serviceFee = response.value.data.serviceCharge,
                            packagingFee = response.value.data.packagingFee))
                    }
                    _cartDetailsState.value = NonVegCartUiModel.Success(response.value.data, false)
                } else {
                    _cartDetailsState.value = NonVegCartUiModel.Error("An error occurred", false)
                }
            }

            else -> {
                _cartDetailsState.value = NonVegCartUiModel.Error("An error occurred", false)
            }
        }
    }

    internal fun createNonVegCart() = viewModelScope.launch(Dispatchers.IO) {
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
        if (itemsInCart == null) {
            return@launch
        }
        _createCartUiModel.value = NonVegCartUiModel.Initial(true)
        val cartItems = arrayListOf<CreateCartItem>()
        itemsInCart?.forEach {
            if (it.price > 0 && it.quantity > 0) {
                cartItems.add(CreateCartItem(mrp = it.mrp, price = it.price, productPriceId = it.productPriceId, quantity = it.quantity))
            }
        }
        val createCartReqBody = CreateCartReqBody(cartItems, merchantId = nonVegMerchantId)
        when (val response = apiRequestManager.createNonVegCart(createCartReqBody)) {
            is ResultWrapper.Success -> {
                _createCartUiModel.value = NonVegCartUiModel.Success(response.value.data, false)
            }

            is ResultWrapper.NetworkError -> {
                _createCartUiModel.value = NonVegCartUiModel.Error("An error occurred", false)
            }

            is ResultWrapper.GenericError -> {
                _createCartUiModel.value = NonVegCartUiModel.Error("An error occurred", false)
            }

            is ResultWrapper.UserTokenNotFound -> {
                _createCartUiModel.value = NonVegCartUiModel.Error("An error occurred", false)
            }
        }
    }

    internal fun getAllNonVegFromLocal(merchantId: Int) = nonVegAddToCartDao.getAllNonVegItemFromLocal(merchantId)

    internal fun handleNonVegCartInsertionOrUpdate(
        singleNonVegItem: NonVegListingSingleItem,
        action: ACTION,
    ) = viewModelScope.launch(Dispatchers.IO) {
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
        val nonVegCartItem = singleNonVegItem.convertToNonVegCartItem(merchantId = nonVegMerchantId)
        if (action == ACTION.INCREASE) {
            nonVegAddToCartDao.insertNonVegItemIntoLocal(nonVegCartItem.apply {
                quantity += 1
            })
        } else if (action == ACTION.DECREASE && (nonVegCartItem.quantity - 1) > 0) {
            nonVegAddToCartDao.insertNonVegItemIntoLocal(nonVegCartItem.apply {
                quantity -= 1
            })
        } else {
            nonVegAddToCartDao.deleteNonVegSingleCartItem(merchantId = 1, nonVegCartItem.productPriceId)
        }
    }

    @Transaction
    private suspend fun insertAllNewCartItemFromServerInLocal(data: NonVegCartData?) = viewModelScope.launch(Dispatchers.IO) {
        if (data == null) {
            return@launch
        }
        cartItemsForRequest.clear()
        val nonVegLocalCartItems = data.itemsInCart?.map {
            if (it.price > 0 && it.quantity > 0) {
                cartItemsForRequest.add(CreateCartItem(mrp = it.mrp, price = it.price, productPriceId = it.merchantProductId, quantity = it.quantity))
            }
            NonVegItemLocalModel(it.merchantProductId, it.merchantId, it.quantity, it.price, it.mrp, it.productId, -1)
        }
        if (nonVegLocalCartItems != null) {
            nonVegAddToCartDao.deleteAllNonVegCartItems()
            nonVegAddToCartDao.insertAllNonVegItemIntoLocal(nonVegLocalCartItems)
        }
    }

    internal fun saveLatestAddress(address: Address) = viewModelScope.launch {
        sharedPrefManager.saveAddress(address)
    }

    internal fun getLatestAddress() {
        val address = sharedPrefManager.getUserAddress()
        updateAddressItem(address)
    }

    internal fun updateAddressItem(address: Address?) {
        this.addressItemCache = address
        addressLineData.update {
            address?.getDisplayString() ?: ""
        }
    }

    internal fun lockUserCartFinalCall() = viewModelScope.launch {
        if (addressItemCache?.id == null) {
            return@launch
        }
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
        _createCartUiModel.value = NonVegCartUiModel.Initial(true)
        val createCartReqBody = CreateFinalCartReqBody(items = cartItemsForRequest,
            addressId = addressItemCache!!.id,
            cartId = currentCartIdFromServer!!, finalLock = true, merchantId = nonVegMerchantId)
        when (val response = apiRequestManager.finalLockNonVegCart(createCartReqBody)) {
            is ResultWrapper.Success -> {
                _createCartUiModel.value = NonVegCartUiModel.Success(response.value.data, false)
            }

            is ResultWrapper.NetworkError -> {
                _createCartUiModel.value = NonVegCartUiModel.Error("An error occurred", false)
            }

            is ResultWrapper.GenericError -> {
                _createCartUiModel.value = NonVegCartUiModel.Error("An error occurred", false)
            }

            is ResultWrapper.UserTokenNotFound -> {
                _createCartUiModel.value = NonVegCartUiModel.Error("An error occurred", false)
            }
        }
    }


    internal fun getCancellationTerms(): String {
        return "100% cancellation fee will be applicable if you decide to cancel th order anytime after order placement. Avoid cancellation as it leads to food wastage "
    }

}