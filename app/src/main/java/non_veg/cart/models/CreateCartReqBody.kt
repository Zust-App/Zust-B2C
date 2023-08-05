package non_veg.cart.models

data class CreateCartReqBody(
    val items: List<CreateCartItem>,
    var merchantId: Int
)

data class CreateCartItem(
    val mrp: Double,
    val price: Double,
    val productPriceId: Int,
    val quantity: Int,
)

data class CreateFinalCartReqBody(
    val addressId: Int,
    val cartId: Int,
    val finalLock: Boolean = true,
    val items: List<CreateCartItem>,
    val merchantId: Int,
)