package `in`.opening.area.zustapp.rapidwallet.model

import `in`.opening.area.zustapp.utility.UserCustomError

data class RwSendOTPResponse(
    val `data`: RapidWalletOTPData? = null,
    val errors: List<UserCustomError>? = null,
    val message: String? = null,
    val statusCode: Int,
)

data class RapidWalletOTPData(
    val opt: String? = null,
    val status: String? = null
)