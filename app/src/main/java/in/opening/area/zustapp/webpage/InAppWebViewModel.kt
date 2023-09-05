package `in`.opening.area.zustapp.webpage

import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InAppWebViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager) : ViewModel() {
    internal val orderUiResponse = MutableStateFlow<InvoiceOrderUiState>(InvoiceOrderUiState.Initial(false))

    internal fun getInvoiceBasedOnOrder(orderId: Int, intentSource: String) = viewModelScope.launch {
        orderUiResponse.update {
            InvoiceOrderUiState.Initial( true)
        }
        when (val response = apiRequestManager.getInvoice(orderId,intentSource)) {
            is ResultWrapper.Success -> {
                if (response.value.data != null) {
                    orderUiResponse.update {
                        InvoiceOrderUiState.Success(response.value.data, false)
                    }
                }else{
                    orderUiResponse.update {
                        InvoiceOrderUiState.ErrorUi("Something went wrong", false)
                    }
                }
            }
            else -> {
                orderUiResponse.update {
                    InvoiceOrderUiState.ErrorUi("Something went wrong" ,false)
                }
            }
        }
    }
}

sealed interface InvoiceOrderUiState {
    val isLoading: Boolean

    data class Initial(override val isLoading: Boolean) : InvoiceOrderUiState
    data class Success(val invoiceHtml: String?, override val isLoading: Boolean) : InvoiceOrderUiState
    data class ErrorUi(val errorMsg: String, override val isLoading: Boolean) : InvoiceOrderUiState
}