package `in`.opening.area.zustapp.uiModels

import `in`.opening.area.zustapp.payment.models.PaymentMethod
import `in`.opening.area.zustapp.utility.UserCustomError

sealed interface PaymentMethodUi {
    val isLoading: Boolean

    data class MethodSuccess(
        override val isLoading: Boolean,
        val data: List<PaymentMethod> = arrayListOf(),
        val timeStamp: Long = System.currentTimeMillis()
    ) : PaymentMethodUi

    data class InitialUi(
        override val isLoading: Boolean,
        val timeStamp: Long = System.currentTimeMillis()
    ) : PaymentMethodUi

    data class ErrorUi(
        override val isLoading: Boolean,
        val errors: List<UserCustomError> = arrayListOf(),
        val errorMsg: String? = "",
        val timeStamp: Long = System.currentTimeMillis()
    ) : PaymentMethodUi
}