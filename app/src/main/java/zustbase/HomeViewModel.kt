package zustbase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import zustbase.services.uiModel.ZustAvailServicesUiModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager) : ViewModel() {

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager
    internal val isAppUpdateAvail = MutableStateFlow(false)

    private val _zustServicesUiModel = MutableStateFlow<ZustAvailServicesUiModel>(ZustAvailServicesUiModel.Empty(false))
    val zustServicesUiModel: StateFlow<ZustAvailServicesUiModel> = _zustServicesUiModel

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

    internal fun getListOfServiceableItem() = viewModelScope.launch {
        val address = sharedPrefManager.getUserAddress()
        _zustServicesUiModel.update {
            ZustAvailServicesUiModel.Empty(true)
        }
        address?.let {
            if (!it.pincode.isNullOrEmpty() && it.latitude != null && it.longitude != null && it.latitude > 0.0 && it.longitude > 0.0) {
                when (val response = apiRequestManager.getAllAvailableService(it.pincode, it.latitude, it.longitude)) {
                    is ResultWrapper.Success -> {
                        response.value.data?.let { d ->
                            _zustServicesUiModel.update {
                                ZustAvailServicesUiModel.Success(d, false)
                            }
                        } ?: run {
//                            _zustServicesUiModel.update {
//                                ZustAvailServicesUiModel.Success(d, false)
//                            }
                        }
                    }

                    else -> {

                    }
                }
            }
        }
    }

}