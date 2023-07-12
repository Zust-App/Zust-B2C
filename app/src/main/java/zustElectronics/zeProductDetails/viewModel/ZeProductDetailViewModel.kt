package zustElectronics.zeProductDetails.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.network.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import zustElectronics.network.ZeApiReqManager
import zustElectronics.zeProductDetails.uiState.ZeProductDetailsUiState
import javax.inject.Inject

@HiltViewModel
class ZeProductDetailViewModel @Inject constructor(private val zeApiReqManager: ZeApiReqManager) : ViewModel() {
    internal val productDetailsUiState = MutableStateFlow<ZeProductDetailsUiState>(ZeProductDetailsUiState.LoadingState(false))

    internal fun getZeProductDetails(productId: Int) = viewModelScope.launch {
        productDetailsUiState.update { ZeProductDetailsUiState.LoadingState(true) }
        when (val response = zeApiReqManager.getZeProductDetails(productId)) {
            is ResultWrapper.Success -> {
                response.value.data?.let { data ->
                    productDetailsUiState.update { ZeProductDetailsUiState.SuccessState(false, data) }
                } ?: run {
                    productDetailsUiState.update { ZeProductDetailsUiState.ErrorState(false, response.value.errors, response.value.message) }
                }
            }

            is ResultWrapper.GenericError -> {
                productDetailsUiState.update { ZeProductDetailsUiState.ErrorState(false, null, response.error?.error) }
            }

            is ResultWrapper.NetworkError -> {
                productDetailsUiState.update { ZeProductDetailsUiState.ErrorState(false, null, "Something went wrong") }
            }

            is ResultWrapper.UserTokenNotFound -> {
                productDetailsUiState.update { ZeProductDetailsUiState.ErrorState(false, null, "Token expired Please login") }
            }
        }
    }
}