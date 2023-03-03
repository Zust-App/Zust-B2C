package `in`.opening.area.zustapp.payment.models

import `in`.opening.area.zustapp.utility.UserCustomError
import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PaymentMethodResponseModel(
    @SerializedName("data")
    val `data`: PaymentData? = null,
    val errors: List<UserCustomError>? = null,
    val message: String? = null,
    val statusCode: Int? = null,
    val timestamp: String? = null
)

@Keep
data class PaymentData(
    val paymentMethods: List<PaymentMethod>
)

@Keep
data class PaymentMethod(
    val key: String,
    val name: String,
    var isSelected: Boolean? = false
)