package `in`.opening.area.zustapp.orderDetail.models

import `in`.opening.area.zustapp.utility.UserCustomError
import com.google.errorprone.annotations.Keep

@Keep
data class OrderDetailModel(
    val `data`: OrderDetailData? = null,
    val errors: List<UserCustomError>? = null,
    val message: String? = null,
    val statusCode: Int? = -1,
    val timestamp: String? = null,
    val isLoading: Boolean = false,
)

@Keep
data class OrderDetailData(
    val address: Address?=null,
    val couponDiscountPrice: Double,
    val deliveryFee: Double,
    val itemTotalPrice: Double,
    val items: List<Item>? = arrayListOf(),
    val orderId: Int,
    val orderStatuses: List<OrderStatus>? = null,
    val packagingFee: Double,
    val paymentMethod: String?=null,
    val secretCode: String? = null,
    val verificationCode: String? = null,
    val expectedTimeToDelivery: String? = null,
    val deliveryPartnerTip: Double? = null,
    val statusSeq: String? = null,
    val reason: String? = null,
    val updatedTotalPrice: Double? = null,
    val riderDetails: RiderDetails? = null,
    var displayOrderStatus: ArrayList<OrderStatus>? = arrayListOf(),
)

@Keep
data class OrderStatus(
    val createdDateTime: String? = null,
    val orderStatusType: String? = null,
)

@Keep
data class Item(
    val mrp: Double,
    val productName: String,
    val numberOfItem: Int,
    val payablePrice: Double = 0.0,
    val productPriceId: Int,
    val thumbnail: String,
    val quantity: Double,
    val unit: String,
)

data class RiderDetails(val riderPhone: String? = null, val riderName: String? = null)

@Keep
data class Address(
    val addressLevel: String?,
    val addressType: String?,
    val description: String?,
    val houseNumberAndFloor: String?,
    val id: Int,
    val landmark: String?,
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0,
)

fun Address?.convertAsStringText(): String {
    val address = this
    return buildString {
        if (!address?.houseNumberAndFloor.isNullOrEmpty()) {
            append(address?.houseNumberAndFloor)
            append(",")
        }
        if (!address?.landmark.isNullOrEmpty()) {
            append(address?.landmark)
            append(",")
        }
        if (!address?.description.isNullOrEmpty()) {
            append(address?.description)
        }
    }
}

fun List<Item>.convertToDisplayText(): String {
    val list = this
    return buildString {
        append("Total ${list.size} Items Ordered")
    }
}