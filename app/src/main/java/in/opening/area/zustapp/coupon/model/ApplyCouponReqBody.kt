package `in`.opening.area.zustapp.coupon.model

data class ApplyCouponReqBody(
    val couponCode: String? = null,
    val orderId: Int? = null,
    val couponRemoved: Boolean = false
)