package `in`.opening.area.zustapp.login.model

import `in`.opening.area.zustapp.utility.UserCustomError
import com.google.errorprone.annotations.Keep

@Keep
data class GetOtpResponseModel(
    val `data`: GetOtpResponseData?=null,
    val errors: List<UserCustomError>?=null,
    val message: String?=null,
)

@Keep
data class GetOtpResponseData(
    val isNotificationSent: Boolean? = null,
    val phoneNo: String? = null
)