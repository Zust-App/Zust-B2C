package `in`.opening.area.zustapp.rapidwallet.model

import androidx.annotation.Keep

@Keep
data class RWCreatePaymentModel(
    val rapidUserId: String,
    val amount: Double? = null,
    val walletType: String,
    val orderId: String
)
