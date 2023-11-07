package `in`.opening.area.zustapp.refer.uistate

import `in`.opening.area.zustapp.refer.data.UserReferralDetail

sealed interface UserReferralDetailUiState {
    val isLoading: Boolean

    data class Success(val data: Map<Int, List<UserReferralDetail>>, val totalReferralIncome: Double, val message: String?, override val isLoading: Boolean) : UserReferralDetailUiState
    data class Error(override val isLoading: Boolean, val errorMsg: String? = null) : UserReferralDetailUiState
    data class Initial(override val isLoading: Boolean) : UserReferralDetailUiState

}