package `in`.opening.area.zustapp.viewmodels

import `in`.opening.area.zustapp.address.model.SaveAddressPostModel
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.orderDetail.models.Address
import `in`.opening.area.zustapp.storage.datastore.DataStoreManager
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import `in`.opening.area.zustapp.uiModels.CurrentLocationUi
import `in`.opening.area.zustapp.uiModels.SaveUserAddressUi
import `in`.opening.area.zustapp.uiModels.UserSavedAddressUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.AppUtility.Companion.getAuthErrorArrayList
import `in`.opening.area.zustapp.utility.AppUtility.Companion.getNotDeliverableErrorArrayList
import `in`.opening.area.zustapp.utility.UserCustomError
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager) : SearchAddressViewModel(apiRequestManager) {
    internal val userAddressListUiState = MutableStateFlow<UserSavedAddressUi>(UserSavedAddressUi.InitialUi(isLoading = false))
    internal val saveUserAddressUiState = MutableStateFlow<SaveUserAddressUi>(SaveUserAddressUi.InitialUi(isLoading = false))

    internal val currentLocationUiState = MutableStateFlow<CurrentLocationUi>(CurrentLocationUi.InitialUi(false))

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    internal val userAddressInputCache = SaveAddressPostModel()

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

    //first check deliverable area and then save address
    internal fun checkAndSaveAddressWithServer() {
        if (!userAddressInputCache.pinCode.isNullOrEmpty()) {
            checkIsDeliverablePinCodeOrNot(userAddressInputCache.pinCode!!,userAddressInputCache.latitude,userAddressInputCache.longitude)
        } else {
            //
        }
    }

    internal fun checkIsSaveAddressAlreadyGoing(): Boolean {
        return saveUserAddressUiState.value.isLoading
    }

    private fun checkIsDeliverablePinCodeOrNot(inputPinCode: String, latitude: Double?, longitude: Double?) = viewModelScope.launch {
        saveUserAddressUiState.update { SaveUserAddressUi.InitialUi(true) }
        when (val response = apiRequestManager.checkPinCodeIsDeliverableOrNot(inputPinCode,latitude,longitude)) {
            is ResultWrapper.Success -> {
                if (response.value.data?.isDeliverablePinCode == true) {
                    saveAddressUserAddress(saveAddressPostModel = userAddressInputCache)
                } else {
                    saveUserAddressUiState.update { SaveUserAddressUi.ErrorUi(false, getNotDeliverableErrorArrayList()) }
                }
            }
            is ResultWrapper.NetworkError -> {
                saveUserAddressUiState.update { SaveUserAddressUi.ErrorUi(false, getAuthErrorArrayList()) }
            }
            is ResultWrapper.GenericError -> {
                saveUserAddressUiState.update { SaveUserAddressUi.ErrorUi(false, getAuthErrorArrayList()) }
            }
            else -> {
                saveUserAddressUiState.update { SaveUserAddressUi.ErrorUi(false, getAuthErrorArrayList()) }
            }

        }

    }

    private fun saveAddressUserAddress(saveAddressPostModel: SaveAddressPostModel) {
        saveUserAddressUiState.update { SaveUserAddressUi.InitialUi(true) }
        viewModelScope.launch {
            when (val response = apiRequestManager.saveUserAddress(saveAddressPostModel)) {
                is ResultWrapper.Success -> {
                    saveUserAddressUiState.update { SaveUserAddressUi.SaveAddressUi(false, response.value.data) }
                }
                is ResultWrapper.GenericError -> {
                    saveUserAddressUiState.update { SaveUserAddressUi.ErrorUi(false, getAuthErrorArrayList()) }
                }
                is ResultWrapper.UserTokenNotFound -> {
                    saveUserAddressUiState.update { SaveUserAddressUi.ErrorUi(false, getAuthErrorArrayList()) }
                }
                is ResultWrapper.NetworkError -> {
                    saveUserAddressUiState.update { SaveUserAddressUi.ErrorUi(false, getAuthErrorArrayList()) }
                }
            }
        }
    }


    internal fun saveLatestAddress(address: Address) = viewModelScope.launch {
        sharedPrefManager.saveAddress(address)
    }

    internal fun getLatestAddress() = sharedPrefManager.getUserAddress()


}