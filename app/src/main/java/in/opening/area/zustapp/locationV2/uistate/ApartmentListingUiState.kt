package `in`.opening.area.zustapp.locationV2.uistate

import `in`.opening.area.zustapp.locationV2.models.ApartmentData

sealed interface ApartmentListingUiState {
    val isLoading:Boolean
    data class Success(val data:List<ApartmentData>?, override val isLoading: Boolean):ApartmentListingUiState
    data class Error(val errorMsg:String?,override val isLoading: Boolean):ApartmentListingUiState
    data class Initial(override val isLoading: Boolean):ApartmentListingUiState
}