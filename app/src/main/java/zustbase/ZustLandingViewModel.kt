package zustbase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.address.model.getDisplayString
import `in`.opening.area.zustapp.locationManager.UserLocationDetails
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.rapidwallet.model.ZustServiceType
import `in`.opening.area.zustapp.repository.DbAddToCartRepository
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import zustbase.analysis.uimodels.UserReportAnalysisUiModel
import zustbase.basepage.models.ZustServicePageResponse
import zustbase.basepage.models.ZustServicePageResponseReceiver
import zustbase.orderDetail.models.ZustAddress
import zustbase.services.models.ZustAvailableServiceResult
import zustbase.services.models.ZustServiceData
import zustbase.services.uiModel.ZustAvailServicesUiModel
import javax.inject.Inject

@HiltViewModel
class ZustLandingViewModel @Inject constructor(
    private val apiRequestManager: ApiRequestManager,
    private val dbAddToCartRepository: DbAddToCartRepository,
) : ViewModel() {

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager
    internal val isAppUpdateAvail = MutableStateFlow(false)
    internal val userLocationFlow = MutableStateFlow(UserLocationDetails())

    private val _zustServicesUiModel = MutableStateFlow<ZustAvailServicesUiModel>(ZustAvailServicesUiModel.Empty(false))
    val zustServicesUiModel: StateFlow<ZustAvailServicesUiModel> get() = _zustServicesUiModel

    private val _userAnalysisReportUiModel = MutableStateFlow<UserReportAnalysisUiModel>(UserReportAnalysisUiModel.Empty(false))
    val userAnalysisReportUiModel: StateFlow<UserReportAnalysisUiModel> get() = _userAnalysisReportUiModel

    internal fun getListOfServiceableItem() = viewModelScope.launch(Dispatchers.IO) {
        val address = sharedPrefManager.getUserAddress()
        updateAddressItem(address)
        _zustServicesUiModel.update {
            ZustAvailServicesUiModel.Empty(true)
        }

        val localAddress = address?.takeIf { !it.pinCode.isNullOrEmpty() && (it.latitude ?: 0.0) > 0.0 && (it.longitude ?: 0.0) > 0.0 }
        localAddress?.let {
            val combinedResult = awaitAll(
                async { apiRequestManager.getAllAvailableService(it.pinCode!!, it.latitude!!, it.longitude!!) },
                async { apiRequestManager.getServicePageData(it.pinCode!!, it.latitude!!, it.longitude!!) }
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
                zustServiceData.serviceList?.forEach {
                    if (it.type.equals(ZustServiceType.GROCERY.name, ignoreCase = true)) {
                        sharedPrefManager.saveMerchantId(it.merchantId)
                    } else if (it.type.equals(ZustServiceType.NON_VEG.name, ignoreCase = true)) {
                        sharedPrefManager.saveNonVegMerchantId(it.merchantId)
                    }
                }
                _zustServicesUiModel.update { ZustAvailServicesUiModel.Success(zustServiceData, servicePageData, false) }
            }
        } ?: run {
            _zustServicesUiModel.update { ZustAvailServicesUiModel.ErrorUi(false, "Address is not found") }
        }
    }

    internal fun saveLatestAddress(zustAddress: ZustAddress) = viewModelScope.launch {
        sharedPrefManager.saveAddress(zustAddress)
    }

    private fun updateAddressItem(zustAddress: ZustAddress?) {
        userLocationFlow.update {
            UserLocationDetails(zustAddress?.latitude, zustAddress?.longitude, zustAddress?.getDisplayString())
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


    internal fun getUserAnalysisData() = viewModelScope.launch(Dispatchers.IO) {
        _userAnalysisReportUiModel.update {
            UserReportAnalysisUiModel.Empty(true)
        }
        when (val response = apiRequestManager.getUserAnalysisData()) {
            is ResultWrapper.Success -> {
                response.value.data?.let { data ->
                    _userAnalysisReportUiModel.update {
                        UserReportAnalysisUiModel.Success(false, data)
                    }
                }
            }

            else -> {
                _userAnalysisReportUiModel.update {
                    UserReportAnalysisUiModel.Error(false, "Something went wrong Please try again.")
                }
            }
        }
    }

    internal fun clearUserGroceryCart() = viewModelScope.launch(Dispatchers.IO) {
        if (!sharedPrefManager.getClearGroceryCart()) {
            dbAddToCartRepository.deleteAllCartItem()
            sharedPrefManager.saveClearGroceryCart(true)
        } else {
            return@launch
        }
    }

    internal fun resetStateOfUi(){
        _zustServicesUiModel.update {
            ZustAvailServicesUiModel.Empty(false)
        }
        _userAnalysisReportUiModel.update {
            UserReportAnalysisUiModel.Empty(false)
        }
    }

}