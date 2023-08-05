package non_veg.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.address.model.getDisplayString
import `in`.opening.area.zustapp.locationManager.UserLocationDetails
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.orderDetail.models.Address
import `in`.opening.area.zustapp.storage.datastore.DataStoreManager
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import `in`.opening.area.zustapp.uiModels.HomePageResUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import non_veg.home.uiModel.NonVegCategoryUiModel
import javax.inject.Inject

@HiltViewModel
class ZustNvEntryViewModel @Inject constructor(val apiRequestManager: ApiRequestManager,private val dataStoreManager: DataStoreManager,private val sharedPrefManager: SharedPrefManager) : ViewModel() {

    private val _nonVegCategoryUiModel = MutableStateFlow<NonVegCategoryUiModel>(NonVegCategoryUiModel.Initial(false))
    val nonVegCategoryUiModel: StateFlow<NonVegCategoryUiModel> get() = _nonVegCategoryUiModel

    internal val userLocationFlow = MutableStateFlow(UserLocationDetails())

    init {
        getUserSavedAddress()
    }

    internal fun getNonVegMerchantDetails(pinCode: String) = viewModelScope.launch {
        when (val response = apiRequestManager.getNonVegMerchantDetails(pinCode)) {

        }
        getNonVegHomePageBanner()
    }

    internal fun getNonVegHomePageBanner() = viewModelScope.launch {
        when (val response = apiRequestManager.getNonVegHomePageBanner("home")) {
            is ResultWrapper.Success -> {

            }

            is ResultWrapper.NetworkError -> {

            }

            is ResultWrapper.GenericError -> {

            }

            is ResultWrapper.UserTokenNotFound -> {

            }
        }
        getNonVegCategory()
    }


    internal fun getNonVegCategory() = viewModelScope.launch {
        when (val response = apiRequestManager.getNonVegCategory()) {
            is ResultWrapper.Success -> {
                _nonVegCategoryUiModel.value = NonVegCategoryUiModel.Success(response.value.data, false)
            }

            is ResultWrapper.NetworkError -> {
                _nonVegCategoryUiModel.value = NonVegCategoryUiModel.Error(true, "Network Error")
            }

            is ResultWrapper.GenericError -> {
                _nonVegCategoryUiModel.value = NonVegCategoryUiModel.Error(true, "Generic Error")
            }

            is ResultWrapper.UserTokenNotFound -> {
                _nonVegCategoryUiModel.value = NonVegCategoryUiModel.Error(true, "User Token Not Found")
            }
        }

    }

    private fun getUserSavedAddress() {
        val address = sharedPrefManager.getUserAddress()
        updateAddressItem(address)
        address?.let {
            if (address.latitude != null && address.latitude != 0.0) {

            } else {

            }
        }
    }

    private fun updateAddressItem(address: Address?) {
        userLocationFlow.update {
            UserLocationDetails(address?.latitude, address?.longitude, address?.getDisplayString())
        }
    }
}