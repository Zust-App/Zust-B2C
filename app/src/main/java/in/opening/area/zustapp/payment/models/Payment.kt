package `in`.opening.area.zustapp.payment.models

import com.google.gson.annotations.SerializedName

data class Payment(
    @SerializedName("razorpay_order_id")
    val razorPayOrderId: String,
    @SerializedName("razorpay_signature")
    val signatureId: String,
    @SerializedName("razorpay_payment_id")
    val paymentId: String)