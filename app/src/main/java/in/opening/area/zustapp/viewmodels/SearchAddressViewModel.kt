package `in`.opening.area.zustapp.viewmodels

import `in`.opening.area.zustapp.address.model.SearchAddressModel
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.uiModels.AddressValidationUi
import `in`.opening.area.zustapp.uiModels.SearchPlacesUi
import `in`.opening.area.zustapp.utility.AddressUtils.Companion.parseSearchResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
open class SearchAddressViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager) : ViewModel() {
    internal val searchPlacesUiState = MutableStateFlow<SearchPlacesUi>(SearchPlacesUi.InitialUi("", false))

    internal var searchAddressModel: SearchAddressModel? = null

    internal val validLatLngUiState = MutableStateFlow<AddressValidationUi>(AddressValidationUi.InitialUi(false, ""))
    private var job: Job? = null

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
                    searchPlacesUiState.update { SearchPlacesUi.ErrorUi(false, response.error?.error ?: "User token expired") }
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

    fun checkServiceAvailBasedOnLatLng(lat: Double, lng: Double) = viewModelScope.launch {
        validLatLngUiState.update { AddressValidationUi.InitialUi(true, "") }
        when (val response = apiRequestManager.checkIsServiceAvail(lat, lng)) {
            is ResultWrapper.Success -> {
                val jsonObject = JSONObject(response.value)
                validLatLngUiState.update { AddressValidationUi.AddressValidation(false, "", jsonObject, "") }
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