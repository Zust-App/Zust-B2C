package `in`.opening.area.zustapp.rapidwallet

import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.payment.models.PaymentActivityReqData
import `in`.opening.area.zustapp.rapidwallet.model.RapidWalletUiRepresentationModel
import `in`.opening.area.zustapp.uiModels.RWUserWalletUiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RapidWalletViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager) :
    ViewModel() {
    internal var paymentActivityReqData: PaymentActivityReqData? = null
    internal var itemCartCount: Int = -1
    internal val rapidUserExistUiState =
        MutableStateFlow<RWUserWalletUiState>(RWUserWalletUiState.InitialUi(false))

    internal val rapidWalletUiView = MutableStateFlow<RapidWalletUiRepresentationModel>(
        RapidWalletUiRepresentationModel.EnterUserIdUI(null))

    internal var rapidUserIdCache: String? = null
    internal var rapidServerOTP: String? = null
    private var walletTypeCache: String = "1"

    internal fun verifyRapidWalletAndBalance() = viewModelScope.launch {
        if (rapidUserIdCache == null) {
            return@launch
        }
        rapidUserExistUiState.update {
            RWUserWalletUiState.InitialUi(true)
        }
        when (val response = apiRequestManager.checkRapidWalletAndBalance(rapidUserIdCache!!)) {
            is ResultWrapper.Success -> {
                response.value.data?.let {
                    rapidUserExistUiState.update {
                        RWUserWalletUiState.Success(false, response.value.data)
                    }
                } ?: run {
                    rapidUserExistUiState.update {
                        RWUserWalletUiState.ErrorUi(
                            false,
                            response.value.message,
                            response.value.errors
                        )
                    }
                }
            }
            is ResultWrapper.NetworkError -> {
                rapidUserExistUiState.update {
                    RWUserWalletUiState.ErrorUi(false, "something went wrong")
                }
            }
            else -> {
                rapidUserExistUiState.update {
                    RWUserWalletUiState.ErrorUi(false, "something went wrong")
                }
            }
        }
    }

    internal fun sendOtpToRapidUser(rapidUserId: String?) = viewModelScope.launch {
        if (rapidUserId.isNullOrEmpty()) {
            return@launch
        }
        rapidUserExistUiState.update {
            RWUserWalletUiState.InitialUi(true)
        }
        when (val response = apiRequestManager.sendRapidWalletOTP(rapidUserId)) {
            is ResultWrapper.Success -> {
                response.value.data?.let { data ->
                    rapidServerOTP = data.opt
                    rapidUserExistUiState.update {
                        RWUserWalletUiState.GetRapidWalletOtp(false, data)
                    }
                } ?: run {
                    rapidUserExistUiState.update {
                        RWUserWalletUiState.ErrorUi(false, response.value.message)
                    }
                }
            }
            is ResultWrapper.NetworkError -> {
                rapidUserExistUiState.update {
                    RWUserWalletUiState.ErrorUi(false, "Something went wrong please try again")
                }
            }
            is ResultWrapper.UserTokenNotFound -> {
                rapidUserExistUiState.update {
                    RWUserWalletUiState.ErrorUi(false, "Something went wrong please try again")
                }
            }
            is ResultWrapper.GenericError -> {
                rapidUserExistUiState.update {
                    RWUserWalletUiState.ErrorUi(
                        false,
                        response.error?.error ?: "Something went wrong"
                    )
                }
            }
        }
    }

    internal fun getPayablePrice(): Double {
        return (paymentActivityReqData?.itemPrice ?: 0.0) +
                (paymentActivityReqData?.deliveryFee ?: 0.0) -
                (paymentActivityReqData?.couponDiscount ?: 0.0) +
                (paymentActivityReqData?.packagingFee
                    ?: 0.0) + (paymentActivityReqData?.deliveryPartnerTip ?: 0.0)
    }

    internal fun verifyUserInputOTP(userInputOTP: String): Boolean {
        //  val decodedOTP = AesUtil.decrypt(rapidServerOTP)
        return rapidServerOTP?.trim() == userInputOTP.trim()
    }


    internal fun createPaymentWithRapidWallet() = viewModelScope.launch {
        if (rapidUserIdCache.isNullOrEmpty()) {
            return@launch
        }
        if (walletTypeCache.isNullOrEmpty()) {
            return@launch
        }
        paymentActivityReqData?.let {
            rapidUserExistUiState.update {
                RWUserWalletUiState.InitialUi(true)
            }
            when (val response = apiRequestManager.createPaymentWithRapidWallet(
                rapidUserIdCache!!, getPayablePrice(), walletTypeCache, it.orderId.toString()
            )) {
                is ResultWrapper.Success -> {
                    response.value.data?.let { success ->
                        rapidUserExistUiState.update {
                            RWUserWalletUiState.CreatePaymentSuccess(false, success)
                        }
                    } ?: run {
                        rapidUserExistUiState.update {
                            RWUserWalletUiState.ErrorUi(
                                false,
                                response.value.errors?.getTextMsg() ?: response.value.message
                            )
                        }
                    }
                }
                is ResultWrapper.NetworkError -> {
                    rapidUserExistUiState.update {
                        RWUserWalletUiState.ErrorUi(false, "Something went wrong")
                    }
                }
                is ResultWrapper.GenericError -> {
                    rapidUserExistUiState.update {
                        RWUserWalletUiState.ErrorUi(
                            false,
                            response.error?.error ?: "Something went wrong"
                        )
                    }
                }
                is ResultWrapper.UserTokenNotFound -> {
                    rapidUserExistUiState.update {
                        RWUserWalletUiState.ErrorUi(false, "Something went wrong")
                    }
                }
            }

        }
    }

    internal fun getOrderId(): Int? {
        return paymentActivityReqData?.orderId
    }


}