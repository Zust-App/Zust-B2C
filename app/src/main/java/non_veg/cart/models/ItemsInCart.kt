package non_veg.cart.models

data class ItemsInCart(
    val imageUrl: String,
    val merchantId: Int,
    val merchantProductId: Int,
    val mrp: Double,
    val price: Double,
    val productDescription: String,
    val productId: Int,
    val productName: String,
    val productStatus: String,
    val quantity: Int,
    val unit: String,
    val productQuantity: Double? = null,
)