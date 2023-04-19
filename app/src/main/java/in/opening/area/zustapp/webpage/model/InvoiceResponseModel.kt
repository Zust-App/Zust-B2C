package `in`.opening.area.zustapp.webpage.model

import `in`.opening.area.zustapp.utility.UserCustomError

data class InvoiceResponseModel(
    val `data`: String? = null,
    val errors: List<UserCustomError>? = arrayListOf(),
    val statusCode: Int? = -1,
)