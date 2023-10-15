package wallet.uistate

import wallet.data.ZustPayAmountDetail
import wallet.data.ZustPayAmountDetailModel

sealed interface ZustPayUiState {
    val isLoading: Boolean

    data class Success(val data: ZustPayAmountDetail? = null, override val isLoading: Boolean) : ZustPayUiState
    data class Error(val errorMessage: String? = null, override val isLoading: Boolean) : ZustPayUiState
    data class Initial(override val isLoading: Boolean) : ZustPayUiState
}