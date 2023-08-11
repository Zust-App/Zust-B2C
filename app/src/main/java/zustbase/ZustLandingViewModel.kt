package zustbase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.address.model.getDisplayString
import `in`.opening.area.zustapp.locationManager.UserLocationDetails
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import zustbase.basepage.models.ZustServicePageResponse
import zustbase.basepage.models.ZustServicePageResponseReceiver
import zustbase.orderDetail.models.Address
import zustbase.services.models.ZustAvailableServiceResult
import zustbase.services.models.ZustServiceData
import zustbase.services.uiModel.ZustAvailServicesUiModel
import javax.inject.Inject

@HiltViewModel
class ZustLandingViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager) : ViewModel() {

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager
    internal val isAppUpdateAvail = MutableStateFlow(false)
    internal val userLocationFlow = MutableStateFlow(UserLocationDetails())

    private val _zustServicesUiModel = MutableStateFlow<ZustAvailServicesUiModel>(ZustAvailServicesUiModel.Empty(false))
    val zustServicesUiModel: StateFlow<ZustAvailServicesUiModel> get() = _zustServicesUiModel


    internal fun getListOfServiceableItem() = viewModelScope.launch(Dispatchers.IO) {
        val address = sharedPrefManager.getUserAddress()
        updateAddressItem(address)
        _zustServicesUiModel.update {
            ZustAvailServicesUiModel.Empty(true)
        }

        address?.takeIf { !it.pincode.isNullOrEmpty() && (it.latitude ?: 0.0) > 0.0 && (it.longitude ?: 0.0) > 0.0 }
            ?.let {
                val combinedResult = awaitAll(
                    async { apiRequestManager.getAllAvailableService(it.pincode!!, it.latitude!!, it.longitude!!) },
                    async { apiRequestManager.getServicePageData(it.pincode!!, it.latitude!!, it.longitude!!) }
                )

                var zustServiceData: ZustServiceData? = null
                var servicePageData: ZustServicePageResponse? = null

                when (val allAvailServiceResult = combinedResult[0]) {
                    is ResultWrapper.Success -> zustServiceData = (allAvailServiceResult.value as? ZustAvailableServiceResult)?.data
                    else -> _zustServicesUiModel.update { ZustAvailServicesUiModel.ErrorUi(false, "Something went wrong") }
                }

                when (val serviceDataResult = combinedResult[1]) {
                    is ResultWrapper.Success -> servicePageData = (serviceDataResult.value as? ZustServicePageResponseReceiver)?.data
                    else -> _zustServicesUiModel.update { ZustAvailServicesUiModel.ErrorUi(false, "Something went wrong") }
                }

                if (zustServiceData != null) {
                    _zustServicesUiModel.update { ZustAvailServicesUiModel.Success(zustServiceData, servicePageData, false) }
                }
            }
    }

    internal fun saveLatestAddress(address: Address) = viewModelScope.launch {
        sharedPrefManager.saveAddress(address)
    }

    private fun updateAddressItem(address: Address?) {
        userLocationFlow.update {
            UserLocationDetails(address?.latitude, address?.longitude, address?.getDisplayString())
        }
    }

    internal fun getAppMetaData() = viewModelScope.launch(Dispatchers.IO) {
        when (val response = apiRequestManager.getMetaData()) {
            is ResultWrapper.Success -> {
                response.value.data?.let {
                    if (it.isAppUpdateAvail == true) {
                        sharedPrefManager.saveFreeDeliveryBasePrice(it.freeDeliveryFee)
                        sharedPrefManager.saveDeliveryFee(it.deliveryCharge)
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