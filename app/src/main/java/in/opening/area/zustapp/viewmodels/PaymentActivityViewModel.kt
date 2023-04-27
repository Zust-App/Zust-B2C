package `in`.opening.area.zustapp.viewmodels

import `in`.opening.area.zustapp.coupon.model.ApplyCouponReqBody
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.payment.models.*
import `in`.opening.area.zustapp.repository.DbAddToCartRepository
import `in`.opening.area.zustapp.storage.datastore.DataStoreManager
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import `in`.opening.area.zustapp.uiModels.*
import `in`.opening.area.zustapp.utility.AppUtility
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class PaymentActivityViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager, private val dbAddToCartRepository: DbAddToCartRepository) : ViewModel() {
    internal val paymentMethodUiState = MutableStateFlow<PaymentMethodUi>(PaymentMethodUi.InitialUi(false))
    internal val createPaymentUiState = MutableStateFlow<CreatePaymentUi>(CreatePaymentUi.InitialUi(false))
    internal val paymentValidationUiState = MutableStateFlow<PaymentVerificationUi>(PaymentVerificationUi.InitialUi(false))
    internal val validateCouponUiState = MutableStateFlow<ValidateCouponUi>(ValidateCouponUi.InitialUi(false))

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    internal lateinit var sharedPrefManager: SharedPrefManager

    internal var appliedCoupon: String = ""
    fun getPaymentMethodsFromServer() = viewModelScope.launch(Dispatchers.IO) {
        paymentMethodUiState.update { PaymentMethodUi.InitialUi(true) }
        when (val response = apiRequestManager.getPaymentMethods()) {
            is ResultWrapper.Success -> {
                if (response.value.data?.paymentMethods == null) {
                    paymentMethodUiState.update {
                        if (!response.value.message.isNullOrEmpty()) {
                            PaymentMethodUi.ErrorUi(false, errorMsg = response.value.message)
                        } else {
                            PaymentMethodUi.ErrorUi(false, errors = response.value.errors ?: emptyList())
                        }
                    }
                } else {
                    paymentMethodUiState.update { PaymentMethodUi.MethodSuccess(false, response.value.data.paymentMethods) }
                }
            }
            is ResultWrapper.NetworkError -> {
                paymentMethodUiState.update { PaymentMethodUi.ErrorUi(false, errorMsg = "something went wrong") }
            }

            is ResultWrapper.GenericError -> {
                paymentMethodUiState.update { PaymentMethodUi.ErrorUi(false, errorMsg = "something went wrong") }
            }
            is ResultWrapper.UserTokenNotFound -> {
                paymentMethodUiState.update {
                    PaymentMethodUi.ErrorUi(false, errorMsg = "something went wrong")
                }
            }
        }
    }

    internal fun invokePaymentToGetId(createPayment: CreatePaymentReqBodyModel) = viewModelScope.launch {
        createPaymentUiState.update { CreatePaymentUi.InitialUi(true) }
        when (val response = apiRequestManager.invokePaymentToGetId(createPayment)) {
            is ResultWrapper.Success -> {
                if (response.value.data != null) {
                    createPaymentUiState.update {
                        CreatePaymentUi.CreateSuccess(false, response.value.data)
                    }
                } else {
                    createPaymentUiState.update {
                        if (!response.value.message.isNullOrEmpty()) {
                            CreatePaymentUi.ErrorUi(false, errorMsg = response.value.message)
                        } else {
                            CreatePaymentUi.ErrorUi(false, errors = response.value.data ?: emptyList())
                        }
                    }
                }
            }
            is ResultWrapper.NetworkError -> {
                createPaymentUiState.update {
                    CreatePaymentUi.ErrorUi(false, errorMsg = "something went wrong")
                }
            }
            is ResultWrapper.UserTokenNotFound -> {
                createPaymentUiState.update {
                    CreatePaymentUi.ErrorUi(false, errors = AppUtility.getAuthErrorArrayList())
                }
            }
            is ResultWrapper.GenericError -> {
                createPaymentUiState.update {
                    CreatePaymentUi.ErrorUi(false, errorMsg = response.error?.error ?: "something went wrong")
                }
            }

        }
    }

    internal fun verifyPaymentWithServer(payment: Payment) = viewModelScope.launch {
        paymentValidationUiState.update { PaymentVerificationUi.InitialUi(true) }
        when (val response = apiRequestManager.verifyPaymentWithServer(payment)) {
            is ResultWrapper.Success -> {
                paymentValidationUiState.update {
                    PaymentVerificationUi.PaymentSuccess(false, JSONObject(response.value))
                }
            }
            is ResultWrapper.NetworkError -> {
                paymentValidationUiState.update {
                    PaymentVerificationUi.ErrorUi(false, errorMsg = "something went wrong")
                }
            }
            is ResultWrapper.UserTokenNotFound -> {
                paymentValidationUiState.update {
                    PaymentVerificationUi.ErrorUi(false, errors = AppUtility.getAuthErrorArrayList())
                }
            }
            is ResultWrapper.GenericError -> {
                paymentValidationUiState.update {
                    PaymentVerificationUi.ErrorUi(false, errorMsg = response.error?.error ?: "something went wrong")
                }
            }
        }

    }

    internal fun validateCoupon(couponBody: ApplyCouponReqBody) = viewModelScope.launch {
        validateCouponUiState.update { ValidateCouponUi.InitialUi(true) }
        when (val response = apiRequestManager.validateCoupon(couponBody)) {
            is ResultWrapper.Success -> {
                if (response.value.data != null) {
                    validateCouponUiState.update {
                        ValidateCouponUi.AppliedSuccessfully(false, response.value.data, isCouponRemoved = couponBody.couponRemoved)
                    }
                } else {
                    validateCouponUiState.update {
                        if (!response.value.errors.isNullOrEmpty()) {
                            ValidateCouponUi.ErrorUi(false, errors = response.value.errors)
                        } else {
                            ValidateCouponUi.ErrorUi(false, message = response.value.message)
                        }
                    }
                }
            }
            is ResultWrapper.NetworkError -> {
                validateCouponUiState.update {
                    ValidateCouponUi.ErrorUi(false, "something went wrong")
                }
            }
            is ResultWrapper.UserTokenNotFound -> {
                validateCouponUiState.update {
                    ValidateCouponUi.ErrorUi(false, errors = AppUtility.getAuthErrorArrayList())
                }
            }
            is ResultWrapper.GenericError -> {
                validateCouponUiState.update {
                    ValidateCouponUi.ErrorUi(false, response.error?.error ?: "something went wrong")
                }
            }

        }
    }

    internal fun getLatestAddress() = sharedPrefManager.getUserAddress()

    internal fun clearCartItems() = viewModelScope.launch {
        dbAddToCartRepository.deleteAllCartItem()
    }

    internal fun getUserMobileNumber(): String = sharedPrefManager.getUserMobileNumber()

    internal fun isCreatePaymentOnGoing(): Boolean {
        return createPaymentUiState.value.isLoading
    }


}