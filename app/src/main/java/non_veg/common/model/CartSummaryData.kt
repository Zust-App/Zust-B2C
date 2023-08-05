package non_veg.common.model

data class CartSummaryData(
    val itemCountInCart: Int?,
    val itemValueInCart: Double?,
    val serviceFee: Double? = 0.0,
    val deliveryFee: Double? = 0.0,
    val packagingFee: Double? = 0.0,
)