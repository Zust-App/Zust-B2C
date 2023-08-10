package non_veg.home.model

import `in`.opening.area.zustapp.utility.UserCustomError

data class NonVegHomePageCombinedResponse(
    val `data`: NvHomePageData? = null,
    val errors: List<UserCustomError>?,
    val message: String?,
    val statusCode: Int,
)

data class NvHomePageData(
    val categories: List<NonVegCategory>?,
    val homeBanner: List<NonVegHomePageBannerData>?,
    val merchantDetails: NonVegMerchantData?,
    val homePageOffer: List<NvHomePageOfferData>?,
)