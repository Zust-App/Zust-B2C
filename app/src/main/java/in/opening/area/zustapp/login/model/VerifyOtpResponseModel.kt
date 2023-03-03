package `in`.opening.area.zustapp.login.model

import `in`.opening.area.zustapp.utility.UserCustomError
import com.google.errorprone.annotations.Keep

@Keep
data class VerifyOtpResponseModel(
    val `data`: VerifyOtpResponseData? = null,
    val errors: List<UserCustomError>? = null,
    val message: String? = null,
)

@Keep
data class VerifyOtpResponseData(
    val isProfileCreated: Boolean=false,
    val phoneNo: String? = null,
    val token: String?=null
)