package `in`.opening.area.zustapp.product.model

import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CreateCartReqModel(
    var addressId: Int = -1,
    val totalItemCount: Int = 0,
    @SerializedName("items")
    val createCartReqItems: List<CreateCartReqItem> = ArrayList(),
    @SerializedName("itemTotal")
    val calculatedPrice: Double = 0.0,
    val merchantId: Int? = 1,
)

@Keep
data class CreateCartReqItem(
    val mrp: Double,
    var numberOfItem: Int,
    val payablePrice: Double = 0.0,
    @SerializedName("productPriceId")
    val productId: String,
)