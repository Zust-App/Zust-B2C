package `in`.opening.area.zustapp.uiModels

import org.json.JSONObject

sealed interface AddressValidationUi {
    val isLoading: Boolean
    val errorMessage: String

    data class AddressValidation(override val isLoading: Boolean,
                                 override val errorMessage: String,
                                 val isAnyServiceAvailable: Boolean,
                                 val infoMessage: String, val timeStamp: Long = System.currentTimeMillis()
    ) : AddressValidationUi

    data class ErrorUi(
        override val isLoading: Boolean,
        override val errorMessage: String,
        val timeStamp: Long = System.currentTimeMillis()
    ) : AddressValidationUi

    data class InitialUi(override val isLoading: Boolean,
                         override val errorMessage: String, val timeStamp: Long = System.currentTimeMillis()) : AddressValidationUi
}