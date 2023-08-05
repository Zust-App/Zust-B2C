package non_veg.listing.uiModel

import non_veg.listing.models.NonVegListingSingleItem

sealed interface NonVegProductListingUiModel {
    val isLoading:Boolean
    data class Success(val data: List<NonVegListingSingleItem>? = null, override val isLoading: Boolean) : NonVegProductListingUiModel
    data class Empty( override val isLoading: Boolean) : NonVegProductListingUiModel
    data class Error(val errorMessage: String, override val isLoading: Boolean) : NonVegProductListingUiModel
}