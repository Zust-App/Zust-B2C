package `in`.opening.area.zustapp.locationV2.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.orderDetail.models.Address
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import `in`.opening.area.zustapp.uiModels.UserSavedAddressUi
import `in`.opening.area.zustapp.uiModels.locations.CheckDeliverableAddressUiState
import `in`.opening.area.zustapp.uiModels.orderSummary.LockOrderCartUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.UserCustomError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LocationPermissionViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager) : ViewModel() {
    @Inject
    lateinit var sharedPrefManager: SharedPrefManager
    internal val userAddressListUiState = MutableStateFlow<UserSavedAddressUi>(UserSavedAddressUi.InitialUi(isLoading = false))

    internal val deliverableAddressUiState = MutableStateFlow<CheckDeliverableAddressUiState>(CheckDeliverableAddressUiState.InitialUiState(false))

    internal fun getAllAddress() = viewModelScope.launch {
        userAddressListUiState.update { UserSavedAddressUi.InitialUi(isLoading = true) }
        when (val response = apiRequestManager.getAllAddress()) {
            is ResultWrapper.Success -> {
                val successValue = response.value
                if (successValue.data != null) {
                    userAddressListUiState.update { UserSavedAddressUi.UserAddressResponse(isLoading = false, data = successValue.data) }
                } else {
                    if (!successValue.message.isNullOrEmpty()) {
                        userAddressListUiState.update { UserSavedAddressUi.ErrorState(isLoading = false, message = successValue.message) }
                    } else {
                        userAddressListUiState.update { UserSavedAddressUi.ErrorState(isLoading = false, errors = successValue.errors) }
                    }
                }
            }

            is ResultWrapper.NetworkError -> {
                userAddressListUiState.update { UserSavedAddressUi.ErrorState(isLoading = false, errors = arrayListOf(UserCustomError("No", "No value"))) }
            }

            is ResultWrapper.UserTokenNotFound -> {
                userAddressListUiState.update { UserSavedAddressUi.ErrorState(isLoading = false, errors = arrayListOf(AppUtility.getAuthError())) }
            }

            is ResultWrapper.GenericError -> {
                userAddressListUiState.update { UserSavedAddressUi.ErrorState(isLoading = false, message = "Something went wrong") }
            }
        }
    }

    internal fun saveLatestAddress(address: Address) = viewModelScope.launch {
        sharedPrefManager.saveAddress(address)
    }

    internal fun saveUserCurrentLocation(parsedLocation: android.location.Address) {
        sharedPrefManager.saveUserCurrentLocation(parsedLocation)
    }

    internal fun verifyDeliverableAddress(savedAddress: AddressItem?) = viewModelScope.launch {
        savedAddress?.let {
            when (val response = apiRequestManager.checkIsServiceAvail(savedAddress.latitude, savedAddress.longitude, savedAddress.pinCode)) {
                is ResultWrapper.Success -> {
                    val jsonObject = JSONObject(response.value)
                    if (jsonObject.has("data")) {
                        val dataJson = jsonObject.getJSONObject("data")
                        if (dataJson.has("isDeliverablePinCode") && dataJson.getBoolean("isDeliverablePinCode")) {
                            if (dataJson.has("merchantId")) {
                                val merchantId = dataJson.getInt("merchantId")
                                deliverableAddressUiState.update {
                                    CheckDeliverableAddressUiState.SuccessUiState(false, savedAddress)
                                }
                            } else {
                                deliverableAddressUiState.update {
                                    CheckDeliverableAddressUiState.ErrorUiState(false, null, "Please try another location")
                                }
                            }
                        } else {
                            deliverableAddressUiState.update {
                                CheckDeliverableAddressUiState.ErrorUiState(false, null, "Please try another location")
                            }
                        }
                    }
                }

                is ResultWrapper.GenericError -> {
                    deliverableAddressUiState.update {
                        CheckDeliverableAddressUiState.ErrorUiState(false, null, "Please try another location")
                    }
                }

                is ResultWrapper.UserTokenNotFound -> {
                    deliverableAddressUiState.update {
                        CheckDeliverableAddressUiState.ErrorUiState(false, null, "Token expired")
                    }
                }

                is ResultWrapper.NetworkError -> {
                    deliverableAddressUiState.update {
                        CheckDeliverableAddressUiState.ErrorUiState(false, null, "Something went wrong")
                    }
                }
            }
        } ?: run {
            deliverableAddressUiState.update {
                CheckDeliverableAddressUiState.ErrorUiState(false, null, "Please try another location")
            }
        }
    }

}