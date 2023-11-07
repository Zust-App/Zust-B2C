package `in`.opening.area.zustapp.locationV2.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import zustbase.orderDetail.models.ZustAddress
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import `in`.opening.area.zustapp.uiModels.UserSavedAddressUi
import `in`.opening.area.zustapp.uiModels.locations.CheckDeliverableAddressUiState
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.UserCustomError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationPermissionViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager) : ViewModel() {
    @Inject
    lateinit var sharedPrefManager: SharedPrefManager
    internal val userAddressListUiState = MutableStateFlow<UserSavedAddressUi>(UserSavedAddressUi.InitialUi(isLoading = false))

    internal val deliverableAddressUiState = MutableStateFlow<CheckDeliverableAddressUiState>(CheckDeliverableAddressUiState.InitialUiState(false))
    internal val isAppUpdateAvail = MutableStateFlow(false)

    internal fun getAllAddress() = viewModelScope.launch {
        sharedPrefManager.removeSavedAddressPrevious()
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

    internal fun saveLatestAddress(zustAddress: ZustAddress) = viewModelScope.launch {
        sharedPrefManager.saveAddress(zustAddress)
    }

    internal fun saveUserCurrentLocation(parsedLocation: ZustAddress) {
        sharedPrefManager.saveUserCurrentLocation(parsedLocation)
    }

    internal fun verifyDeliverableAddress(savedAddress: AddressItem?) = viewModelScope.launch {
        when (val response = apiRequestManager.getAllAvailableService(savedAddress?.pinCode?:"000000", savedAddress?.latitude, savedAddress?.longitude, savedAddress?.is_high_priority)) {
            is ResultWrapper.Success -> {
                response.value.data?.serviceList?.let { data ->
                    val checkAnyServiceAvailable = data.any { it.enable }
                    if (checkAnyServiceAvailable) {
                        deliverableAddressUiState.update {
                            CheckDeliverableAddressUiState.SuccessUiState(false, savedAddress)
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

    }

    internal fun getAppMetaData() = viewModelScope.launch(Dispatchers.IO) {
        when (val response = apiRequestManager.getMetaData()) {
            is ResultWrapper.Success -> {
                response.value.data?.let {
                    if (it.isAppUpdateAvail == true) {
                        sharedPrefManager.saveFreeDeliveryBasePrice(it.freeDeliveryFee)
                        sharedPrefManager.saveDeliveryFee(it.deliveryCharge)
                        sharedPrefManager.saveNonVegFreeDeliveryFee(it.nonVegFreeDelivery)
                        isAppUpdateAvail.update { true }
                    } else {
                        isAppUpdateAvail.update { false }
                    }
                }
            }

            else -> {
                isAppUpdateAvail.update { false }
            }
        }
    }



}