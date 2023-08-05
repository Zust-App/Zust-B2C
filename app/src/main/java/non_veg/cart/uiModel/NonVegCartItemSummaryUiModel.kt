package non_veg.cart.uiModel

import non_veg.common.model.CartSummaryData

sealed interface NonVegCartItemSummaryUiModel {
    data class Success(val data: CartSummaryData? = null) : NonVegCartItemSummaryUiModel
    object InitialUi : NonVegCartItemSummaryUiModel
}