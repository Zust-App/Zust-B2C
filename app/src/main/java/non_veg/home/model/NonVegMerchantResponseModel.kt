package non_veg.home.model

import `in`.opening.area.zustapp.utility.UserCustomError

data class NonVegMerchantResponseModel(
    val `data`: NonVegMerchantData? = null,
    val errors: List<UserCustomError>? = arrayListOf(),
    val message: String,
    val statusCode: Int,
)

data class NonVegMerchantData(
    val address: String,
    val email: String,
    val id: Int,
    val latitude: Int,
    val longitude: Int,
    val name: String,
    val phoneNumber: String,
    val pincode: String,
    val status: String,
)