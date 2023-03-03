package `in`.opening.area.zustapp.uiModels.productList

import `in`.opening.area.zustapp.product.model.ProductItemResponse
import `in`.opening.area.zustapp.utility.UserCustomError
import com.google.errorprone.annotations.Keep

@Keep
sealed interface ProductListUi {
    val isLoading: Boolean

    data class ProductListSuccess(
        override val isLoading: Boolean,
        val data: ProductItemResponse = ProductItemResponse(),
        val timeStamp: Long = System.currentTimeMillis()
    ) : ProductListUi

    data class InitialUi(
        override val isLoading: Boolean,
        val timeStamp: Long = System.currentTimeMillis()
    ) : ProductListUi

    data class ErrorUi(
        override val isLoading: Boolean,
        val errors: List<UserCustomError> = arrayListOf(),
        val errorMsg: String? = "",
        val timeStamp: Long = System.currentTimeMillis()
    ) : ProductListUi
}