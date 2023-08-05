package non_veg.home.model

import `in`.opening.area.zustapp.utility.UserCustomError

data class NonVegCategoryModel(
    val `data`: List<NonVegCategory>? = null,
    val errors: List<UserCustomError>? = null,
    val message: String,
    val statusCode: Int,
)

data class NonVegCategory(
    val description: Any,
    val id: Int,
    val name: String,
    val status: Any,
    val thumbnail: Any,
)