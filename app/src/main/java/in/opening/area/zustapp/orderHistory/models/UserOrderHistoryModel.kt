package `in`.opening.area.zustapp.orderHistory.models

import `in`.opening.area.zustapp.utility.PagingMetadata
import `in`.opening.area.zustapp.utility.UserCustomError
import com.google.errorprone.annotations.Keep

@Keep
data class UserOrderHistoryModel(
    val `data`: OrderHistoryData? = null,
    val errors: List<UserCustomError>? = null,
    val message: String? = null,
    val statusCode: Int? = -1,
)
@Keep
data class OrderHistoryData(
    val _metadata: PagingMetadata,
    val orderHistories: List<OrderHistoryItem>
)

@Keep
data class OrderHistoryItem(
    val orderedDateAndTime: String,
    val itemText: String,
    val orderId: Int,
    val orderStatusType: String,
    val payablePrice: Double? = -1.0
)
