package `in`.opening.area.zustapp.rapidwallet

import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.payment.models.PaymentActivityReqData
import `in`.opening.area.zustapp.uiModels.RWUserWalletUiState
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RapidWalletViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager) : ViewModel() {
    internal var paymentActivityReqData: PaymentActivityReqData? = null
    internal var itemCartCount: Int = -1
    internal val rapidUserExistUiState = MutableStateFlow<RWUserWalletUiState>(RWUserWalletUiState.InitialUi(false))

    internal fun verifyRapidWalletAndBalance(rapidUserId: String) = viewModelScope.launch {
        rapidUserExistUiState.update {
            RWUserWalletUiState.InitialUi(true)
        }
        when (val response = apiRequestManager.checkRapidWalletAndBalance(rapidUserId)) {
            is ResultWrapper.Success -> {
                response.value.data?.let {
                    rapidUserExistUiState.update {
                        RWUserWalletUiState.Success(false, response.value.data)
                    }
                } ?: run {
                    rapidUserExistUiState.update {
                        RWUserWalletUiState.ErrorUi(false, response.value.message, response.value.errors)
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

    internal fun getPayablePrice(): Double {
       return (paymentActivityReqData?.itemPrice ?: 0.0) +
                (paymentActivityReqData?.deliveryFee ?: 0.0) -
                (paymentActivityReqData?.couponDiscount ?: 0.0) +
                (paymentActivityReqData?.packagingFee ?: 0.0) + (paymentActivityReqData?.deliveryPartnerTip ?: 0.0)
    }

}