package zustbase.orderHistory.models

import `in`.opening.area.zustapp.utility.UserCustomError

data class RatingResponseModel(
    val `data`: String? = null,
    val errors: List<UserCustomError>? = null,
    val message: String? = null,
    val statusCode: Int? = -1,
)