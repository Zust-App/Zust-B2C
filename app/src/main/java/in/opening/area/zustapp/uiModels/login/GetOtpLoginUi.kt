package `in`.opening.area.zustapp.uiModels.login

import `in`.opening.area.zustapp.login.model.GetOtpResponseData
import `in`.opening.area.zustapp.utility.UserCustomError
import com.google.errorprone.annotations.Keep

@Keep
sealed interface GetOtpLoginUi {
    val isLoading: Boolean

    @Keep
    data class OtpGetSuccess(
        override val isLoading: Boolean,
        val data: GetOtpResponseData = GetOtpResponseData(),
        val timeStamp: Long = System.currentTimeMillis()
    ) : GetOtpLoginUi

    @Keep
    data class InitialUi(
        override val isLoading: Boolean,
        val timeStamp: Long = System.currentTimeMillis()
    ) : GetOtpLoginUi

    data class ErrorUi(
        override val isLoading: Boolean,
        val errors: List<UserCustomError>? = arrayListOf(),
        val errorMsg: String? = "",
        val timeStamp: Long = System.currentTimeMillis()
    ) : GetOtpLoginUi
}