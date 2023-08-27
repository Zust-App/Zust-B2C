package non_veg.payment.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.payment.models.PaymentMethod
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import `in`.opening.area.zustapp.uiModels.PaymentMethodUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import non_veg.cart.models.NonVegCartDetailsForPayment
import non_veg.common.model.CartSummaryData
import non_veg.payment.models.NonVegCartPaymentReqBody
import non_veg.payment.uiModels.NonVegCreateOrderUiState
import non_veg.storage.dao.NonVegAddToCartDao
import javax.inject.Inject

@HiltViewModel
class NonVegPaymentViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager, private val nonVegAddToCartDao: NonVegAddToCartDao) : ViewModel() {

    @Inject
    internal lateinit var sharedPrefManager: SharedPrefManager

    internal var nonVegCartDetailsForPayment: NonVegCartDetailsForPayment? = null
    internal var cartSummaryData: CartSummaryData? = null

    private val _paymentMethodUiState = MutableStateFlow<PaymentMethodUi>(PaymentMethodUi.InitialUi(false))
    internal val paymentMethodUiState: StateFlow<PaymentMethodUi> by lazy { _paymentMethodUiState }

    internal var selectedPaymentKey: String = ""

    private val _nonVegCreateOrderUiState = MutableStateFlow<NonVegCreateOrderUiState>(NonVegCreateOrderUiState.Initial(false))
    val nonVegCreateOrderUiState: StateFlow<NonVegCreateOrderUiState> by lazy { _nonVegCreateOrderUiState }

    internal fun getPaymentMethodsFromServer() = viewModelScope.launch(Dispatchers.IO) {
        _paymentMethodUiState.update { PaymentMethodUi.InitialUi(true) }
        when (val response = apiRequestManager.getPaymentMethods()) {
            is ResultWrapper.Success -> {
                if (response.value.data?.paymentMethods == null) {
                    _paymentMethodUiState.update {
                        if (!response.value.message.isNullOrEmpty()) {
                            PaymentMethodUi.ErrorUi(false, errorMsg = response.value.message)
                        } else {
                            PaymentMethodUi.ErrorUi(false, errors = response.value.errors ?: emptyList())
                        }
                    }
                } else {
                    _paymentMethodUiState.update {
                        PaymentMethodUi.MethodSuccess(false, response.value.data.paymentMethods.map {
                            it.apply {
                                if (it.key.equals("cod", ignoreCase = true)) {
                                    it.isSelected = true
                                    selectedPaymentKey = it.key
                                }
                            }
                        })
                    }
                }
            }

            is ResultWrapper.NetworkError -> {
                _paymentMethodUiState.update { PaymentMethodUi.ErrorUi(false, errorMsg = "something went wrong") }
            }

            is ResultWrapper.GenericError -> {
                _paymentMethodUiState.update { PaymentMethodUi.ErrorUi(false, errorMsg = "something went wrong") }
            }

            is ResultWrapper.UserTokenNotFound -> {
                _paymentMethodUiState.update {
                    PaymentMethodUi.ErrorUi(false, errorMsg = "something went wrong")
                }
            }
        }
    }

    internal fun updateNonVegCartDetailsForPayment(nonVegCartDetailsForPayment: NonVegCartDetailsForPayment) {
        this.nonVegCartDetailsForPayment = nonVegCartDetailsForPayment
        nonVegCartDetailsForPayment.let {
            cartSummaryData = CartSummaryData(itemValueInCart = nonVegCartDetailsForPayment.itemPrice,
                itemCountInCart = nonVegCartDetailsForPayment.noOfItemsInCart,
                serviceFee = nonVegCartDetailsForPayment.serviceCharge,
                deliveryFee = nonVegCartDetailsForPayment.deliveryFee, packagingFee = nonVegCartDetailsForPayment.packagingFee)
        }
    }

    internal fun updatePaymentOptions(paymentMethod: PaymentMethod) {
        val postPaymentMethod = paymentMethodUiState.value
        if (postPaymentMethod is PaymentMethodUi.MethodSuccess) {
            val newPaymentList = postPaymentMethod.data.map {
                PaymentMethod(
                    it.key,
                    it.name,
                    isSelected = (it.key == paymentMethod.key)
                )
            }
            selectedPaymentKey = paymentMethod.key
            _paymentMethodUiState.update { PaymentMethodUi.MethodSuccess(false, newPaymentList) }
        }
    }

    //when payment method is COD
    internal fun createUserNonVegOrder() = viewModelScope.launch {
        nonVegCartDetailsForPayment?.let {
            val nonVegCartPaymentReqBody = NonVegCartPaymentReqBody(it.cartId!!, it.deliveryFee!!,
                it.itemPrice!!, it.merchantId!!, it.packagingFee!!, NonVegPaymentMethod.COD.name, it.serviceCharge!!)
            when (val response = apiRequestManager.createNonVegOrder(nonVegCartPaymentReqBody)) {
                is ResultWrapper.Success -> {
                    if (response.value.data != null) {
                        _nonVegCreateOrderUiState.update {
                            NonVegCreateOrderUiState.Success(false, orderId = response.value.data)
                        }
                    } else {
                        _nonVegCreateOrderUiState.update {
                            NonVegCreateOrderUiState.ErrorUi(false, "Something went wrong")
                        }
                    }
                }

                is ResultWrapper.GenericError -> {
                    _nonVegCreateOrderUiState.update {
                        NonVegCreateOrderUiState.ErrorUi(false, "Something went wrong")
                    }
                }

                is ResultWrapper.NetworkError -> {
                    _nonVegCreateOrderUiState.update {
                        NonVegCreateOrderUiState.ErrorUi(false, "Something went wrong")
                    }
                }

                is ResultWrapper.UserTokenNotFound -> {
                    _nonVegCreateOrderUiState.update {
                        NonVegCreateOrderUiState.ErrorUi(false, "Something went wrong")
                    }
                }
            }
        }
    }

    internal fun clearAllNonVegCartItems() = viewModelScope.launch(Dispatchers.IO) {
        nonVegAddToCartDao.deleteAllNonVegCartItems()
    }

    internal fun getLatestAddress() = sharedPrefManager.getUserAddress()

    internal fun getNonVegMinDeliveryAmount(): Int {
        return sharedPrefManager.getNonVegFreeDeliveryFee()
    }
    enum class NonVegPaymentMethod {
        COD, RAPID_WALLET
    }
}