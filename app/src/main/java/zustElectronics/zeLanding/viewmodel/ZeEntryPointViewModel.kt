package zustElectronics.zeLanding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.network.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import zustElectronics.network.ZeApiReqManager
import zustElectronics.zeLanding.uiState.ZeLandingProductsUiState
import javax.inject.Inject

@HiltViewModel
class ZeEntryPointViewModel @Inject constructor(private val zeApiReqManager: ZeApiReqManager) : ViewModel() {
    internal val zeProductsUiState = MutableStateFlow<ZeLandingProductsUiState>(ZeLandingProductsUiState.InitialState(false))

    internal fun getAllZeProducts() = viewModelScope.launch(Dispatchers.IO) {
        zeProductsUiState.update {
            ZeLandingProductsUiState.InitialState(true)
        }
        when (val response = zeApiReqManager.getZeProducts()) {
            is ResultWrapper.Success -> {
                if (!response.value.data.isNullOrEmpty()) {
                    zeProductsUiState.update {
                        ZeLandingProductsUiState.SuccessState(response.value, false)
                    }
                }
                else{
                    zeProductsUiState.update {
                        ZeLandingProductsUiState.ErrorState("Something went wrong Please try again later", false)
                    }
                }

            }

            is ResultWrapper.GenericError -> {
                zeProductsUiState.update {
                    ZeLandingProductsUiState.ErrorState(response.error?.error, false)
                }
            }

            is ResultWrapper.NetworkError -> {
                zeProductsUiState.update {
                    ZeLandingProductsUiState.ErrorState("Something went wrong", false)
                }
            }

            is ResultWrapper.UserTokenNotFound -> {
                zeProductsUiState.update {
                    ZeLandingProductsUiState.ErrorState("Token Expired", false)
                }
            }
        }
    }
}