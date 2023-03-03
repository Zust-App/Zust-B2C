package `in`.opening.area.zustapp.uiModels

import `in`.opening.area.zustapp.product.model.CreateCartData
import `in`.opening.area.zustapp.utility.UserCustomError

enum class VALUE {
    A,S
}

sealed interface CreateCartResponseUi {
    val isLoading: Boolean

    data class CartSuccess(
        override val isLoading: Boolean,
        val data: CreateCartData = CreateCartData(),
        val value: VALUE,
        val timeStamp: Long = System.currentTimeMillis()
    ) : CreateCartResponseUi

    data class InitialUi(
        override val isLoading: Boolean,
        val timeStamp: Long = System.currentTimeMillis()
    ) : CreateCartResponseUi

    data class ErrorUi(
        override val isLoading: Boolean,
        val errors: List<UserCustomError>? = arrayListOf(),
        val errorMsg: String? = "",
        val timeStamp: Long = System.currentTimeMillis()
    ) : CreateCartResponseUi
}