package `in`.opening.area.zustapp.rapidwallet.model

import androidx.annotation.Keep

@Keep
data class RWCreatePaymentModel(
    val rapidUserId: String,
    val amount: Double? = null,
    val walletType: String,
    val orderId: String,
    val paymentType: String = PaymentTypeActivation.GROCERY.name,
)

enum class PaymentTypeActivation {
    GROCERY, NON_VEG, FOOD
}

enum class ZustServiceType {
    GROCERY, NON_VEG,ELECTRONICS, FOOD,SUBSCRIPTION,NEAR_BY_SHOP
}