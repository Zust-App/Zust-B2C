package wallet.data

import androidx.annotation.Keep

@Keep
data class ZustPayAmountDetailModel(
    val data: ZustPayAmountDetail? = null,
    val errorMsg: String? = null,
)

@Keep
data class ZustPayAmountDetail(
    val referralAmount: Double? = 0.0,
    val referralUsedAmount: Double? = 0.0,
    val walletUsedAmount: Double? = 0.0,
    val walletAmount: Double? = 0.0,
    val walletMessage: String? = null,
    val walletInfo: String? = null
)
