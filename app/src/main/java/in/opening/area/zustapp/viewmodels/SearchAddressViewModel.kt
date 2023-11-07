package `in`.opening.area.zustapp.viewmodels

import `in`.opening.area.zustapp.address.model.SearchAddressModel
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.uiModels.AddressValidationUi
import `in`.opening.area.zustapp.uiModels.LocationAddressUi
import `in`.opening.area.zustapp.uiModels.SearchPlacesUi
import `in`.opening.area.zustapp.utility.AddressUtils.Companion.parseSearchResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class SearchAddressViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager) : ViewModel() {
    internal val searchPlacesUiState = MutableStateFlow<SearchPlacesUi>(SearchPlacesUi.InitialUi("", false))

    internal val validLatLngUiState = MutableStateFlow<AddressValidationUi>(AddressValidationUi.InitialUi(false, ""))
    private var job: Job? = null

    //address field
    internal var searchedAddress = MutableStateFlow<LocationAddressUi>(LocationAddressUi.InitialUi(false))

    //map activity uses
    internal var searchAddressModel: SearchAddressModel? = null

    internal fun getSearchPlacesResult(inputString: String) {
        job?.cancel()
        job = viewModelScope.launch {
            if (inputString.length < 3) {
                return@launch
            }
            searchPlacesUiState.update { SearchPlacesUi.InitialUi("", true) }
            when (val response = apiRequestManager.searchPlaces(inputString)) {
                is ResultWrapper.Success -> {
                    val result = parseSearchResult(response.value)
                    searchPlacesUiState.update { SearchPlacesUi.SearchPlaceResult(false, "", result) }
                }

                is ResultWrapper.GenericError -> {
                    searchPlacesUiState.update { SearchPlacesUi.ErrorUi(false, response.error?.error ?: "Something went wrong") }
                }

                is ResultWrapper.UserTokenNotFound -> {
                    searchPlacesUiState.update { SearchPlacesUi.SearchPlaceResult(false, "User token expired") }
                }

                is ResultWrapper.NetworkError -> {
                    searchPlacesUiState.update { SearchPlacesUi.SearchPlaceResult(false, "Something went wrong") }
                }
            }

        }
    }

    internal fun checkServiceAvailBasedOnLatLng(lat: Double?, lng: Double?, postalCode: String?) =
        viewModelScope.launch {
            validLatLngUiState.update { AddressValidationUi.InitialUi(true, "") }
            when (val response = apiRequestManager.getAllAvailableService(postalCode ?: "000000", lat, lng, false)) {
                is ResultWrapper.Success -> {
                    response.value.data?.serviceList?.let { zustServices ->
                        val isAnyServiceAvailable = zustServices.any { it.enable }
                        validLatLngUiState.update { AddressValidationUi.AddressValidation(isLoading = false, errorMessage = "Service details fetched", isAnyServiceAvailable, "okk") }
                    } ?: run {
                        validLatLngUiState.update { AddressValidationUi.ErrorUi(false, "Something went wrong") }
                    }
                }

                is ResultWrapper.NetworkError -> {
                    validLatLngUiState.update { AddressValidationUi.ErrorUi(false, "Something went wrong") }
                }

                is ResultWrapper.UserTokenNotFound -> {
                    validLatLngUiState.update { AddressValidationUi.ErrorUi(false, "Auth Error") }
                }

                is ResultWrapper.GenericError -> {
                    validLatLngUiState.update { AddressValidationUi.ErrorUi(false, response.error?.error ?: "Something went wrong") }
                }
            }
        }

}