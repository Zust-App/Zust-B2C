package zustElectronics.zeLanding.models

import `in`.opening.area.zustapp.utility.UserCustomError

data class ZeLandingPageProductModel(
    val `data`: List<ZeLandingProductsData>?=null,
    val errors: List<UserCustomError>?=null,
    val message: String?=null,
    val statusCode: Int?=null,
    val timestamp: String?=null
)