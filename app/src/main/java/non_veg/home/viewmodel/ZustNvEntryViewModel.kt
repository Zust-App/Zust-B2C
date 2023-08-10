package non_veg.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.address.model.getDisplayString
import `in`.opening.area.zustapp.locationManager.UserLocationDetails
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import zustbase.orderDetail.models.Address
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import non_veg.home.uiModel.NonVegCategoryUiModel
import non_veg.home.uiModel.NvHomePageCombinedUiModel
import javax.inject.Inject

@HiltViewModel
class ZustNvEntryViewModel @Inject constructor(val apiRequestManager: ApiRequestManager, private val sharedPrefManager: SharedPrefManager) : ViewModel() {

    private val _nonVegHomePageUiModel = MutableStateFlow<NvHomePageCombinedUiModel>(NvHomePageCombinedUiModel.Initial(false))
    val nonVegHomePageUiModel: StateFlow<NvHomePageCombinedUiModel> get() = _nonVegHomePageUiModel

    internal val userLocationFlow = MutableStateFlow(UserLocationDetails())


    //this function handle all the location and home page data functionality
    internal fun getUserSavedAddress() {
        val address = sharedPrefManager.getUserAddress()
        updateAddressItem(address)
        address?.let {
            if (address.latitude != null && address.latitude != 0.0 && address.pincode != null) {
                getNonVegHomePageData(address.pincode, address.latitude, address.longitude)
            } else {
                _nonVegHomePageUiModel.value = NvHomePageCombinedUiModel.Error(true, "Something went wrong Invalid address")
            }
        }
    }

    private fun updateAddressItem(address: Address?) {
        userLocationFlow.update {
            UserLocationDetails(address?.latitude, address?.longitude, address?.getDisplayString())
        }
    }

    internal fun saveLatestAddress(address: Address) = viewModelScope.launch {
        sharedPrefManager.saveAddress(address)
    }

    private fun getNonVegHomePageData(pinCode: String, lat: Double?, lng: Double?) = viewModelScope.launch {
        _nonVegHomePageUiModel.update {
            NvHomePageCombinedUiModel.Initial(true)
        }
        val defaultPinCode="11001"
        when (val response = apiRequestManager.getNonVegHomePageData(defaultPinCode, lat = lat, lng = lng)) {
            is ResultWrapper.Success -> {
                if (response.value.data != null) {
                    response.value.data.merchantDetails?.let {
                        sharedPrefManager.saveNonVegMerchantId(it.id)
                    }
                    _nonVegHomePageUiModel.update {
                        NvHomePageCombinedUiModel.Success(response.value.data, false)
                    }
                } else {
                    _nonVegHomePageUiModel.update {
                        NvHomePageCombinedUiModel.Error(false, "We are currently not available")
                    }
                }
            }

            is ResultWrapper.NetworkError -> {
                sharedPrefManager.removeNonVegMerchantId()
                _nonVegHomePageUiModel.update {
                    NvHomePageCombinedUiModel.Error(false, "Something went wrong please try again")
                }
            }

            is ResultWrapper.GenericError -> {
                sharedPrefManager.removeNonVegMerchantId()
                _nonVegHomePageUiModel.update {
                    NvHomePageCombinedUiModel.Error(false, "Something went wrong Please try again")
                }
            }

            else -> {
                sharedPrefManager.removeNonVegMerchantId()
                _nonVegHomePageUiModel.update {
                    NvHomePageCombinedUiModel.Error(false, "Something went wrong Please try again")
                }
            }
        }
    }

}