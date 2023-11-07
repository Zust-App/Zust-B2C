package `in`.opening.area.zustapp.refer.data

import androidx.annotation.Keep

@Keep
data class UserReferralDetailResponse(
    val `data`: UserReferralData? = null,
    val error: String?,
    val statusCode: Int,
    val status: String,
)

@Keep
data class UserReferralData(
    val totalReferralIncome: Double,
    val message: String,
    val userReferralDetails: List<UserReferralDetail>? = null,
)

@Keep
data class UserReferralDetail(
    val refereeId: Long,
    val referrerId: Long,
    val level: Int,
    val refereeName: String?,
    val phoneNum: String?,
    val joiningDate: Long?,
    val userOrderDetail: UserOrderSummary?,
)
data class UserOrderSummary(val userId: Long, val totalOrderSum: Double, val paymentMethod: Int)

