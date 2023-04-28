package `in`.opening.area.zustapp.rapidwallet.model

import `in`.opening.area.zustapp.utility.UserCustomError

data class RapidWalletPaymentResponse(
    val `data`: RWPaymentResponseData? = null,
    val errors: List<UserCustomError>? = null,
    val message: String? = null,
    val statusCode: Int? = -1,
)

data class RWPaymentResponseData(
    val amount: Double? = null,
    val message: String? = null,
    val orderId: String? = null,
    val rapidUserid: String? = null,
    val status: String? = null,
)