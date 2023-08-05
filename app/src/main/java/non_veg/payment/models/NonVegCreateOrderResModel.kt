package non_veg.payment.models

import `in`.opening.area.zustapp.utility.UserCustomError

data class NonVegCreateOrderResModel(
    val `data`: Int?=null,
    val errors: List<UserCustomError>? = null,
    val message: String? = null,
    val statusCode: Int,
)