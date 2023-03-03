package `in`.opening.area.zustapp.orderSummary.model

import com.google.gson.annotations.SerializedName

data class LockOrderSummaryModel(
    val addressId: Int = 8,
    @SerializedName("items")
    val lockOrderSummaryItems: List<LockOrderSummaryItem> = arrayListOf(),
    val merchantId: Int = 0,
    val orderId: Int = 0,
    val deliveryPartnerTip: Int = 0,
)

data class LockOrderSummaryItem(
    val mrp: Double,
    val numberOfItem: Int,
    val payablePrice: Double,
    val productPriceId: Int,
)