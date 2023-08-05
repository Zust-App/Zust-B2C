package non_veg.home.uiModel

import non_veg.home.model.NonVegCategory

sealed interface NonVegCategoryUiModel {
    val isLoading: Boolean
    data class Success(val data: List<NonVegCategory>?, override val isLoading: Boolean) : NonVegCategoryUiModel
    data class Initial(override val isLoading: Boolean) : NonVegCategoryUiModel
    data class Error(override val isLoading: Boolean, val errorMessage: String) : NonVegCategoryUiModel
}