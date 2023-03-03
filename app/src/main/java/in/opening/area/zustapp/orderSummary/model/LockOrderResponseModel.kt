package `in`.opening.area.zustapp.orderSummary.model

import `in`.opening.area.zustapp.orderDetail.models.Address
import `in`.opening.area.zustapp.product.model.Item
import `in`.opening.area.zustapp.utility.UserCustomError
import com.google.gson.annotations.SerializedName

data class LockOrderResponseModel(
    @SerializedName("data")
    val lockOrderResponseData: LockOrderResponseData? = null,
    val errors: List<UserCustomError> = arrayListOf(),
    val message: String = "",
)

data class LockOrderResponseData(
    val deliveryFee: Double,
    val packagingFee: Double,
    val itemTotalPrice: Double,
    val deliveryPartnerTip: Double,
    val couponCode: String? = null,
    val items: List<Item>,
    val orderId: Int,
    val address: Address,
    val expectedDelivery: String? = null,
    val isFreeDelivery: String? = null,
)