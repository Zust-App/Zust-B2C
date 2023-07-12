package zustElectronics.zeLanding.uiState

import zustElectronics.zeLanding.models.ZeLandingPageProductModel

sealed interface ZeLandingProductsUiState {
    val isLoading: Boolean

    data class ErrorState(val errorMessage: String? = "Something went wrong", override val isLoading: Boolean) : ZeLandingProductsUiState

    data class SuccessState(val data: ZeLandingPageProductModel? = null, override val isLoading: Boolean) : ZeLandingProductsUiState

    data class InitialState(override val isLoading: Boolean) : ZeLandingProductsUiState

}