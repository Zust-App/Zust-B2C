package `in`.opening.area.zustapp.home.models

import `in`.opening.area.zustapp.utility.UserCustomError

data class HomePageApiResponse(
    val `data`: HomeData? = null,
    val errors: List<UserCustomError>? = arrayListOf(),
    val message: String,
    val statusCode: Int? = -1,
)

data class HomeData(
    val homeGrids: List<HomeGrid>? = arrayListOf(),
    val merchantId: Int,
)

data class HomeGrid(
    val `data`: List<HomePageGenericData>? = arrayListOf(),
    val title: String? = null,
    val type: String? = null,
)

data class HomePageGenericData(
    val bannerType: String? = null,
    val category: String? = null,
    val categoryName: String? = null,
    val deepLink: String? = null,
    val description: String? = null,
    val id: Int? = null,
    val imageUrl: String? = null,
    val name: String? = null,
    val subCategory: String? = null,
)