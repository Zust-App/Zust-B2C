package `in`.opening.area.zustapp.product.model

interface OtherCategoriesUiModel {
    val isLoading: Boolean

    data class SuccessUi(
        val data: ArrayList<SingleCategoryData>? = arrayListOf(),
        val selectedCategoryId: Int? = -1,
        override val isLoading: Boolean,
    ) : OtherCategoriesUiModel

    data class ErrorUi(
        override val isLoading: Boolean,
        val errorMessage: String? = null,
    ) : OtherCategoriesUiModel

    data class InitialUi(override val isLoading: Boolean) : OtherCategoriesUiModel

}