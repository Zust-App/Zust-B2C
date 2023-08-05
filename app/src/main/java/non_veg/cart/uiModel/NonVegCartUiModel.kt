package non_veg.cart.uiModel

import non_veg.cart.models.NonVegCartData

sealed interface NonVegCartUiModel {
    val isLoading: Boolean
    val isUpdateApiCall: Boolean

    data class Success(val data: NonVegCartData? = null, override val isLoading: Boolean, override val isUpdateApiCall: Boolean = false) : NonVegCartUiModel
    data class Initial(override val isLoading: Boolean, override val isUpdateApiCall: Boolean = false) : NonVegCartUiModel
    data class Error(val errorMessage: String, override val isLoading: Boolean, override val isUpdateApiCall: Boolean = false) : NonVegCartUiModel
}
