package `in`.opening.area.zustapp.rapidwallet.model

import `in`.opening.area.zustapp.utility.UserCustomError

data class RWUserExistWalletResponse(
    val `data`: RwUserExistWalletData?=null,
    val errors: List<UserCustomError>? = arrayListOf(),
    val message: String? = null,
    val statusCode: Int,
)

data class RwUserExistWalletData(
    val rapidBank: RapidBank? = null,
    val rapidValid: RapidValid? = null,
    val rapidWallet: RapidWallet? = null,
)


data class RapidValid(
    val payload: String? = null,
    val status: String? = null,
)

data class RapidBank(
    val bankBalance: Double? = null,
    val userId: String? = null,
)

data class RapidWallet(
    val userId: String? = null,
    val walletBalance: Double? = null,
)