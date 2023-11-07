package `in`.opening.area.zustapp.locationV2.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.locationV2.uistate.ApartmentListingUiState
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApartmentListingViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager) : ViewModel() {
    private val _apartmentListingUiState = MutableStateFlow<ApartmentListingUiState>(ApartmentListingUiState.Initial(false))
    internal val apartmentListingUiState: StateFlow<ApartmentListingUiState> get() = _apartmentListingUiState
    internal fun getApartmentListing() = viewModelScope.launch {
        when (val response = apiRequestManager.getAppApartmentListing()) {
            is ResultWrapper.Success -> {
                response.value.data?.let {data->
                    _apartmentListingUiState.update {ApartmentListingUiState.Success(data,false)  }
                }?:run {
                    _apartmentListingUiState.update {ApartmentListingUiState.Error("Something went wrong",false)  }
                }
            }
            else -> {
                _apartmentListingUiState.update {ApartmentListingUiState.Error("Something went wrong",false)  }
            }
        }
    }
}