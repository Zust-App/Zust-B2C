package `in`.opening.area.zustapp.uiModels

import `in`.opening.area.zustapp.address.model.CustomAddress
import `in`.opening.area.zustapp.address.model.DeliverableAddressResponse
import `in`.opening.area.zustapp.utility.UserCustomError


sealed interface SaveUserAddressUi {
    val isLoading: Boolean

    data class SaveAddressUi(
        override val isLoading: Boolean,
        val data: CustomAddress? = CustomAddress(),
        val timeStamp: Long = System.currentTimeMillis(),
    ) : SaveUserAddressUi

    data class InitialUi(override val isLoading: Boolean) : SaveUserAddressUi

    data class ErrorUi(
        override val isLoading: Boolean,
        val errors: List<UserCustomError> = arrayListOf(),
    ) : SaveUserAddressUi
}

sealed interface DeliverablePinCodeUi {
    val isLoading: Boolean

    data class SuccessUi(override val isLoading: Boolean, val data: DeliverableAddressResponse? = null) : DeliverablePinCodeUi

    data class InitialUi(override val isLoading: Boolean) : DeliverablePinCodeUi

    data class ErrorUi(
        override val isLoading: Boolean,
        val message: String? = null,
        val errors: List<UserCustomError>? = arrayListOf(),
    ) : DeliverablePinCodeUi
}