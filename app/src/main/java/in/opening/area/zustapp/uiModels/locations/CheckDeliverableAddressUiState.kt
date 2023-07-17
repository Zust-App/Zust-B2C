package `in`.opening.area.zustapp.uiModels.locations

import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.utility.UserCustomError

sealed interface CheckDeliverableAddressUiState {
    val isLoading: Boolean

    data class SuccessUiState(override val isLoading: Boolean, val data: AddressItem? = null,val timeStamp:Long=System.currentTimeMillis()) : CheckDeliverableAddressUiState

    data class ErrorUiState(
        override val isLoading: Boolean,
        val errors: List<UserCustomError>? = null,
        val message: String? = null,
        val timeStamp:Long=System.currentTimeMillis()
    ) : CheckDeliverableAddressUiState

    data class InitialUiState(override val isLoading: Boolean) : CheckDeliverableAddressUiState
}