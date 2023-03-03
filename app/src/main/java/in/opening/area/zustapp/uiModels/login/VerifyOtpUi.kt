package `in`.opening.area.zustapp.uiModels.login

import `in`.opening.area.zustapp.login.model.VerifyOtpResponseData
import `in`.opening.area.zustapp.utility.UserCustomError

sealed interface VerifyOtpUi {
    val isLoading: Boolean

    data class VerificationSuccess(
        override val isLoading: Boolean,
        val data: VerifyOtpResponseData = VerifyOtpResponseData(),
        val timeStamp: Long = System.currentTimeMillis()
    ) : VerifyOtpUi

    data class InitialUi(
        override val isLoading: Boolean,
        val timeStamp: Long = System.currentTimeMillis()
    ) : VerifyOtpUi

    data class ErrorUi(
        override val isLoading: Boolean,
        val errors: List<UserCustomError>? = arrayListOf(),
        val errorMsg: String? = "",
        val timeStamp: Long = System.currentTimeMillis()
    ) : VerifyOtpUi
}