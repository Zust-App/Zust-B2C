package `in`.opening.area.zustapp.product.model

import `in`.opening.area.zustapp.utility.UserCustomError
import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CreateCartResponseModel(
    @SerializedName("data")
    val createCartData: CreateCartData? = null,
    val errors: List<UserCustomError>? = null,
    val message: String? = null,
)

@Keep
data class CreateCartData(
    val address: Address? = null,
    val orderId: Int? = null,
    val couponCode: String? = null,
    val couponId: String? = null,
    val couponMaxDiscountPrice: Double? = 0.0,
    val deliveryFee: Double? = -1.0,
    val deliveryPartnerTip: Double? = -1.0,
    val itemTotalPrice: Double? = -1.0,
    val items: List<Item> = arrayListOf(),
    val packagingFee: Double? = -1.0,
    val expectedDelivery: String? = null,
    val isFreeDelivery: String? = null,
)

@Keep
data class Item(
    val isOutOfStock: Boolean,
    val mrp: Double,
    val numberOfItem: Int,
    val payablePrice: Double,
    val productPriceId: Int,
    val thumbnail: String,
)

data class Address(
    val addressLevel: Any,
    val addressType: String,
    val description: String,
    val houseNumberAndFloor: String,
    val id: Int,
    val landmark: String,
    val latitude: Double,
    val longitude: Double,
)