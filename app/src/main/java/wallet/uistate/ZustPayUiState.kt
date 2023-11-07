package wallet.uistate

import `in`.opening.area.zustapp.refer.data.UserReferralDetail
import wallet.data.ZustPayAmountDetail
import wallet.data.ZustPayAmountDetailModel

sealed interface ZustPayUiState {
    val isLoading: Boolean

    data class Success(val data: ZustPayAmountDetail? = null,
                       override val isLoading: Boolean, val referralDetails: Map<Int, List<UserReferralDetail>>, val totalReferralIncome: Double, val message: String?) : ZustPayUiState
    data class Error(val errorMessage: String? = null, override val isLoading: Boolean) : ZustPayUiState
    data class Initial(override val isLoading: Boolean) : ZustPayUiState
}