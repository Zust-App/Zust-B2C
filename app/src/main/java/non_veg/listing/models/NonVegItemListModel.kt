package non_veg.listing.models

import `in`.opening.area.zustapp.utility.UserCustomError
import non_veg.storage.NonVegItemLocalModel

data class NonVegItemListModel(
    val `data`: List<NonVegListingSingleItem>? = null,
    val errors: List<UserCustomError>? = null,
    val message: String,
    val statusCode: Int,
)

data class NonVegListingSingleItem(
    val categoryId: Int,
    val merchantId: Int,
    val merchantImageUrl: String,
    val mrp: Double? = null,
    val price: Double,
    val productDescription: String,
    val productId: Int,
    val productImageUrl: String,
    val productIsAvailable: Boolean,
    val productName: String,
    val productPriceId: Int,
    val productStatus: String,
    val unit: String? = null,
    val productQuantity: Double? = null,
    val variantName: String,
    var quantityOfItemInCart: Int? = 0,
)

fun NonVegListingSingleItem.convertToNonVegCartItem(merchantId: Int): NonVegItemLocalModel {
    return NonVegItemLocalModel(
        this.productPriceId, merchantId, this.quantityOfItemInCart ?: 0,
        price = this.price, mrp = this.mrp ?: -1.0, this.productId,
    )
}