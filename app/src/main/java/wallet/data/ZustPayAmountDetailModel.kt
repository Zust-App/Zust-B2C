package wallet.data

data class ZustPayAmountDetailModel(
    val data: ZustPayAmountDetail? = null,
    val errorMsg: String? = null,
)

data class ZustPayAmountDetail(
    val referralAmount: Double? = 0.0,
    val referralUsedAmount: Double? = 0.0,
    val walletUsedAmount: Double? = 0.0,
    val walletAmount: Double? = 0.0,
)