package non_veg.cart.models

data class UpdateNonVegCartItemReqBody(
    val cartId: Int,
    val merchantId: Int,
    val mrp: Double,
    val price: Double,
    val productPriceId: Int,
    val quantity: Int,
    val updateType: String//DELETE, INCREASE,DECREASE
)