package non_veg.payment.models

data class NonVegCartPaymentReqBody(
    val cartId: Int,
    val deliveryFee: Double,
    val itemTotalAmount: Double,
    val merchantId: Int,
    val packagingFee: Double,
    val paymentMethod: String,
    val serviceCharge: Double
)