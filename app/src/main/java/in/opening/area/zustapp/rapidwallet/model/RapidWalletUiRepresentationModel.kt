package `in`.opening.area.zustapp.rapidwallet.model

sealed interface RapidWalletUiRepresentationModel {
    data class EnterUserIdUI(val x: Any? = null) : RapidWalletUiRepresentationModel
    data class WalletDetailsUI(val x: RwUserExistWalletData? = null) : RapidWalletUiRepresentationModel
    data class OtpUi(val x: Any? = null) : RapidWalletUiRepresentationModel
}