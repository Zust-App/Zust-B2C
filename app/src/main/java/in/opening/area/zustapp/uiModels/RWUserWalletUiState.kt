package `in`.opening.area.zustapp.uiModels

import `in`.opening.area.zustapp.coupon.model.AppliedCouponData
import `in`.opening.area.zustapp.rapidwallet.model.RWUserExistWalletResponse
import `in`.opening.area.zustapp.rapidwallet.model.RwUserExistWalletData
import `in`.opening.area.zustapp.utility.UserCustomError

sealed interface RWUserWalletUiState {
    val isLoading: Boolean

    data class Success(
        override val isLoading: Boolean, val `data`: RwUserExistWalletData?=null,
        val timeStamp: Long = System.currentTimeMillis(),
    ) : RWUserWalletUiState

    data class InitialUi(override val isLoading: Boolean) : RWUserWalletUiState

    data class ErrorUi(
        override val isLoading: Boolean, val message: String? = "",
        val errors: List<UserCustomError>? = arrayListOf(),
    ) : RWUserWalletUiState
}