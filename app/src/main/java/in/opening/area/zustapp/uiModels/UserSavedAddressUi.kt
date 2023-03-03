package `in`.opening.area.zustapp.uiModels

import `in`.opening.area.zustapp.address.model.AddressData
import `in`.opening.area.zustapp.locationManager.models.CustomLocationModel
import `in`.opening.area.zustapp.utility.UserCustomError

sealed interface UserSavedAddressUi {
    val isLoading: Boolean
    val timeStamp: Long

    data class InitialUi(override val isLoading: Boolean,
                         override val timeStamp: Long = System.currentTimeMillis()) : UserSavedAddressUi

    data class ErrorState(
        override val isLoading: Boolean,
        val errors: List<UserCustomError> = arrayListOf(),
        val message: String? = "",
        override val timeStamp: Long = System.currentTimeMillis()
    ) : UserSavedAddressUi

    data class UserAddressResponse(
        override val isLoading: Boolean,
        override val timeStamp: Long = System.currentTimeMillis(),
        val data: AddressData? = null
    ) : UserSavedAddressUi

}


sealed interface CurrentLocationUi {
    val isLoading: Boolean
    val timeStamp: Long

    data class InitialUi(override val isLoading: Boolean,
                         override val timeStamp: Long = System.currentTimeMillis()) : CurrentLocationUi

    data class ErrorState(
        override val isLoading: Boolean,
        val errors: List<UserCustomError> = arrayListOf(),
        val message: String? = "",
        override val timeStamp: Long = System.currentTimeMillis()
    ) : CurrentLocationUi

    data class ReceivedCurrentLocation(
        override val isLoading: Boolean,
        override val timeStamp: Long = System.currentTimeMillis(),
        val data: CustomLocationModel? = CustomLocationModel()
    ) : CurrentLocationUi

}

