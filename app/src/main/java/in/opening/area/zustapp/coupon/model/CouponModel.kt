package `in`.opening.area.zustapp.coupon.model

import `in`.opening.area.zustapp.utility.UserCustomError

data class CouponModel(
    val `data`: Data? = null,
    val message: String? = null,
    val statusCode: Int? = 0,
    val errors: List<UserCustomError> = arrayListOf()
)

data class Coupon(
    val couponCode: String,
    val couponName: String,
    val couponStatus: String,
    val couponType: String,
    val description: String,
    val discountAmount: Int,
    val discountPercentage: Int,
    val endDate: String,
    val id: Int,
    val maximumDiscountAmount: Int,
    val maximumTotalUsePerUser: Int,
    val minimumAmountInCart: Int,
    val startDate: String
)

data class Data(
    val coupons: List<Coupon>? = null
)
