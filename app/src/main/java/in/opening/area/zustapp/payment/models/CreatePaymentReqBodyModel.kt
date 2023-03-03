package `in`.opening.area.zustapp.payment.models

data class CreatePaymentReqBodyModel(
    val amount: Double? = 0.0,
    val currency: String? = "INR",
    val order_id: Int? = -1,
    val paymentMethod: String
)