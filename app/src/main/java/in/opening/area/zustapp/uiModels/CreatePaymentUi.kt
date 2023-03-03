package `in`.opening.area.zustapp.uiModels

import `in`.opening.area.zustapp.payment.models.CreatePaymentDataModel
import `in`.opening.area.zustapp.utility.UserCustomError

sealed interface CreatePaymentUi {
    val isLoading: Boolean

    data class CreateSuccess(
        override val isLoading: Boolean,
        val data: CreatePaymentDataModel = CreatePaymentDataModel(),
        val timeStamp: Long = System.currentTimeMillis()
    ) : CreatePaymentUi

    data class InitialUi(
        override val isLoading: Boolean,
        val timeStamp: Long = System.currentTimeMillis()
    ) : CreatePaymentUi

    data class ErrorUi(
        override val isLoading: Boolean,
        val errors: List<UserCustomError> = arrayListOf(),
        val errorMsg: String? = "",
        val timeStamp: Long = System.currentTimeMillis()
    ) : CreatePaymentUi
}
