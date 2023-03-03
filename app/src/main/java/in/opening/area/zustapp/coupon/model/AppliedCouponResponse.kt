package `in`.opening.area.zustapp.coupon.model

import `in`.opening.area.zustapp.utility.UserCustomError
import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AppliedCouponResponse(
    @SerializedName("data")
    val `data`: AppliedCouponData? = null,
    val errors: List<UserCustomError>? = null,
    val message: String? = null,
    val statusCode: Int? = -1,
)

@Keep
data class AppliedCouponData(
    val couponCode: String,
    val couponId: Int,
    val discountAmount: Double,
    val orderId: Int
)

fun List<UserCustomError>.getTextMsg(): String {
    return if (this.isEmpty()) {
        "Something went wrong"
    } else {
        this[0].errorMessage
    }
}