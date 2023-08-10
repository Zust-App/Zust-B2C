package non_veg.home.model

import `in`.opening.area.zustapp.utility.UserCustomError

data class NonVegHomePageBannerModel(
    val `data`: List<NonVegHomePageBannerData>? = null,
    val errors: List<UserCustomError>? = null,
    val message: String,
    val statusCode: Int,
)

data class NonVegHomePageBannerData(
    val bannerType: String?,
    val deepLink: String?,
    val id: Int,
    val imageUrl: String?,
)