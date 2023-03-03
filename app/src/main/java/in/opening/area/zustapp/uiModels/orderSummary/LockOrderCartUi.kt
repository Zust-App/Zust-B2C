package `in`.opening.area.zustapp.uiModels.orderSummary

import `in`.opening.area.zustapp.orderSummary.model.LockOrderResponseData
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.utility.UserCustomError

sealed interface LockOrderCartUi {
    val isLoading: Boolean

    data class InitialUi(override val isLoading: Boolean, val timeStamp: Long = System.currentTimeMillis()) : LockOrderCartUi

    data class SuccessLocked(
        override val isLoading: Boolean,
        val data: LockOrderResponseData? = null,
        val timeStamp: Long = System.currentTimeMillis(),
    ) : LockOrderCartUi

    data class ErrorUi(
        override val isLoading: Boolean,
        val errors: List<UserCustomError> = arrayListOf(),
        val errorMessage: String? = "", val timeStamp: Long = System.currentTimeMillis(),
    ) : LockOrderCartUi
}

sealed interface UpsellingProductsUiState {
    val isLoading: Boolean

    data class InitialUi(override val isLoading: Boolean, val timeStamp: Long = System.currentTimeMillis()) : UpsellingProductsUiState

    data class SuccessState(
        override val isLoading: Boolean,
        val data: List<ProductSingleItem>? = arrayListOf(),
        val timeStamp: Long = System.currentTimeMillis(),
    ) : UpsellingProductsUiState

    data class ErrorUi(
        override val isLoading: Boolean,
        val errors: List<UserCustomError> = arrayListOf(),
        val errorMessage: String? = "", val timeStamp: Long = System.currentTimeMillis(),
    ) : UpsellingProductsUiState
}