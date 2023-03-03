package `in`.opening.area.zustapp.uiModels

import `in`.opening.area.zustapp.utility.UserCustomError
import org.json.JSONObject

sealed interface PaymentVerificationUi {
    val isLoading: Boolean

    data class PaymentSuccess(
        override val isLoading: Boolean,
        val data: JSONObject = JSONObject(),
        val timeStamp: Long = System.currentTimeMillis()
    ) : PaymentVerificationUi

    data class InitialUi(
        override val isLoading: Boolean,
        val timeStamp: Long = System.currentTimeMillis()
    ) : PaymentVerificationUi

    data class ErrorUi(
        override val isLoading: Boolean,
        val errors: List<UserCustomError> = arrayListOf(),
        val errorMsg: String? = "",
        val timeStamp: Long = System.currentTimeMillis()
    ) : PaymentVerificationUi
}