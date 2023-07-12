package zustElectronics.zeProductDetails.model

import `in`.opening.area.zustapp.utility.UserCustomError

data class ZeProductDetailsModel(
    val `data`: ZeProductDetailsData? = null,
    val errors: List<UserCustomError>? = null,
    val message: String,
    val statusCode: Int,
    val timestamp: String,
)