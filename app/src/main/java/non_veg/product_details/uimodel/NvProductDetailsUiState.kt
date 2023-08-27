package non_veg.product_details.uimodel

import non_veg.listing.models.NonVegListingSingleItem

sealed interface NvProductDetailsUiState {
    val isLoading: Boolean
    data class Success(override val isLoading: Boolean,val data: List<NonVegListingSingleItem>?) : NvProductDetailsUiState
    data class Error(override val isLoading: Boolean, val errorMessage: String? = null) : NvProductDetailsUiState
    data class Initial(override val isLoading: Boolean) : NvProductDetailsUiState
}