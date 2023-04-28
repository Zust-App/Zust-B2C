package `in`.opening.area.zustapp.uiModels

import `in`.opening.area.zustapp.coupon.model.AppliedCouponData
import `in`.opening.area.zustapp.rapidwallet.model.RWPaymentResponseData
import `in`.opening.area.zustapp.rapidwallet.model.RWUserExistWalletResponse
import `in`.opening.area.zustapp.rapidwallet.model.RapidWalletOTPData
import `in`.opening.area.zustapp.rapidwallet.model.RwUserExistWalletData
import `in`.opening.area.zustapp.utility.UserCustomError

sealed interface RWUserWalletUiState {
    val isLoading: Boolean

    data class Success(
        override val isLoading: Boolean, val `data`: RwUserExistWalletData? = null,
        val timeStamp: Long = System.currentTimeMillis(),
    ) : RWUserWalletUiState

    data class InitialUi(override val isLoading: Boolean) : RWUserWalletUiState

    data class GetRapidWalletOtp(
        override val isLoading: Boolean,
        val data: RapidWalletOTPData? = null
    ) : RWUserWalletUiState

    data class CreatePaymentSuccess(override val isLoading: Boolean,val `data`: RWPaymentResponseData?=null) : RWUserWalletUiState

    data class ErrorUi(
        override val isLoading: Boolean, val message: String? = "",
        val errors: List<UserCustomError>? = arrayListOf(),
    ) : RWUserWalletUiState

    data class ShowUserIdInput(override val isLoading: Boolean) : RWUserWalletUiState

}