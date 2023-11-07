package `in`.opening.area.zustapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phonepe.intent.sdk.api.UPIApplicationInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.coupon.model.ApplyCouponReqBody
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.payment.models.CreatePaymentReqBodyModel
import `in`.opening.area.zustapp.payment.models.Payment
import `in`.opening.area.zustapp.payment.models.PaymentActivityReqData
import `in`.opening.area.zustapp.payment.models.PaymentData
import `in`.opening.area.zustapp.payment.models.PaymentMethod
import `in`.opening.area.zustapp.repository.DbAddToCartRepository
import `in`.opening.area.zustapp.storage.datastore.DataStoreManager
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import `in`.opening.area.zustapp.uiModels.CreatePaymentUi
import `in`.opening.area.zustapp.uiModels.PaymentMethodUi
import `in`.opening.area.zustapp.uiModels.PaymentVerificationUi
import `in`.opening.area.zustapp.uiModels.ValidateCouponUi
import `in`.opening.area.zustapp.utility.AppUtility
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

    internal var paymentActivityReqData: PaymentActivityReqData? = null
    internal var paymentMethod: PaymentMethod? = null
    internal var cartItemCount: Int = 0

    fun getUserId(): String {
        return sharedPrefManager.getUserMobileNumber()
    }

    internal fun getPaymentMethodsFromServer(upiApps: List<UPIApplicationInfo>) = viewModelScope.launch(Dispatchers.IO) {
        paymentMethodUiState.update { PaymentMethodUi.InitialUi(true) }
        val address = sharedPrefManager.getUserAddress()
        when (val response = apiRequestManager.getPaymentMethods(address?.is_high_priority)) {
            is ResultWrapper.Success -> {
                if (response.value.data == null) {
                    paymentMethodUiState.update {
                        if (!response.value.message.isNullOrEmpty()) {
                            PaymentMethodUi.ErrorUi(false, errorMsg = response.value.message)
                        } else {
                            PaymentMethodUi.ErrorUi(false, errors = response.value.errors ?: emptyList())
                        }
                    }
                } else {
                    refactorPaymentMethods(upiApps, response.value.data)
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

    internal fun invokePaymentToGetId() = viewModelScope.launch {
        val totalPayableAmount = paymentActivityReqData?.run {
            ((itemPrice ?: 0.0) +
                    (deliveryFee ?: 0.0) +
                    (deliveryPartnerTip ?: 0.0)
                    + (packagingFee ?: 0.0))
        }
        paymentActivityReqData?.totalAmount = totalPayableAmount
        val createPayment = CreatePaymentReqBodyModel(totalPayableAmount, order_id = paymentActivityReqData?.orderId, paymentMethod = paymentMethod?.key!!)
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


    internal fun updatePaymentOptions(paymentMethod: PaymentMethod) {
        val postPaymentMethod = paymentMethodUiState.value
        if (postPaymentMethod is PaymentMethodUi.MethodSuccess) {
            this.paymentMethod = paymentMethod
            val updatedPaymentMethods = postPaymentMethod.data.map { paymentData ->
                paymentData.copy(paymentCategory = paymentData.paymentCategory, alignment = paymentData.alignment, paymentMethods = paymentData.paymentMethods.map { method ->
                    method.copy(key = method.key,
                        name = method.name,
                        packageName = method.packageName,
                        thumbnail = method.thumbnail,
                        isSelected = method.key == paymentMethod.key,
                        enable = method.enable
                    )
                } as ArrayList<PaymentMethod>)
            }
            paymentMethodUiState.update { PaymentMethodUi.MethodSuccess(false, updatedPaymentMethods) }
        }
    }

    private fun refactorPaymentMethods(upiApps: List<UPIApplicationInfo>, paymentDatas: List<PaymentData>) {
        val upiAppsAssociates = upiApps.associateBy { it.packageName }
        paymentDatas.forEach {
            if (it.paymentCategory.contains("upi", ignoreCase = true)) {
                val upiPaymentMethods = it.paymentMethods
                upiPaymentMethods.forEach { method ->
                    if (method.enable) {
                        method.enable = upiAppsAssociates.containsKey(method.packageName)
                    }
                }
            }
        }
        paymentMethodUiState.update {
            PaymentMethodUi.MethodSuccess(false, paymentDatas)
        }
    }
}

