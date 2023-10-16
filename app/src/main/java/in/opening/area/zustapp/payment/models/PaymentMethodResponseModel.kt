package `in`.opening.area.zustapp.payment.models

import `in`.opening.area.zustapp.utility.UserCustomError
import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PaymentMethodResponseModel(
    @SerializedName("data")
    val `data`: List<PaymentData>? = null,
    val errors: List<UserCustomError>? = null,
    val message: String? = null,
    val statusCode: Int? = null,
    val timestamp: String? = null,
)

@Keep
data class PaymentData(
    val paymentCategory: String,
    val paymentMethods: ArrayList<PaymentMethod>,
    val alignment: String,
)

@Keep
data class PaymentMethod(
    val key: String,
    val name: String,
    val packageName: String? = null,
    val thumbnail: String? = null,
    var isSelected: Boolean? = false,
    var enable: Boolean = false,
)