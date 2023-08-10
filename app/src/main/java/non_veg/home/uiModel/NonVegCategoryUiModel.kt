package non_veg.home.uiModel

import non_veg.home.model.NonVegCategory
import non_veg.home.model.NonVegHomePageCombinedResponse
import non_veg.home.model.NvHomePageData

sealed interface NonVegCategoryUiModel {
    val isLoading: Boolean
    data class Success(val data: List<NonVegCategory>?, override val isLoading: Boolean) : NonVegCategoryUiModel
    data class Initial(override val isLoading: Boolean) : NonVegCategoryUiModel
    data class Error(override val isLoading: Boolean, val errorMessage: String) : NonVegCategoryUiModel
}

sealed interface NvHomePageCombinedUiModel {
    val isLoading: Boolean
    data class Success(val data: NvHomePageData?, override val isLoading: Boolean) : NvHomePageCombinedUiModel
    data class Initial(override val isLoading: Boolean) : NvHomePageCombinedUiModel
    data class Error(override val isLoading: Boolean, val errorMessage: String) : NvHomePageCombinedUiModel
}