package `in`.opening.area.zustapp.orderHistory.ui

sealed interface RatingOrderUiState {
    val isLoading: Boolean

    data class InitialState(override val isLoading: Boolean) : RatingOrderUiState
    data class SuccessState(override val isLoading: Boolean) : RatingOrderUiState
    data class ErrorState(override val isLoading: Boolean) : RatingOrderUiState
}