package `in`.opening.area.zustapp.payment.models

import `in`.opening.area.zustapp.utility.UserCustomError
import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CreatePaymentResponseModel(
    val message: String? = null,
    val statusCode: Int? = null,
    @SerializedName("data")
    val data: CreatePaymentDataModel? = null,
    val errors: List<UserCustomError> = arrayListOf(),
)

data class CreatePaymentDataModel(
    val success: Boolean? = null,
    @SerializedName("order")
    val order: PaymentOrder? = null,
)

@Keep
data class PaymentOrder(
    val amount: Int,
    val amount_due: Int,
    val amount_paid: Int,
    val created_at: Int,
    val currency: String,
    val entity: String,
    @SerializedName("id")
    val rzrPayOrderId: String,
    val notes: List<UserCustomError>,
    val status: String,
)