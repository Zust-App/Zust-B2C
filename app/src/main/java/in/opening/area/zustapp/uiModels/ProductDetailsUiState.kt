package `in`.opening.area.zustapp.uiModels

import `in`.opening.area.zustapp.product.model.ProductSingleItem

interface ProductDetailsUiState {
    val isLoading: Boolean

    data class Success(
        val singleItem: ProductSingleItem? = null,
        override val isLoading: Boolean, val time: Long = System.currentTimeMillis(),
    ) : ProductDetailsUiState

    data class InitialUi(override val isLoading: Boolean) : ProductDetailsUiState
}
