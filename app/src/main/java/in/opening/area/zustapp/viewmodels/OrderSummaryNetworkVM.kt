package `in`.opening.area.zustapp.viewmodels

import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.product.model.CreateCartReqModel
import `in`.opening.area.zustapp.repository.ProductRepo
import `in`.opening.area.zustapp.uiModels.CreateCartResponseUi
import `in`.opening.area.zustapp.uiModels.VALUE
import `in`.opening.area.zustapp.utility.AppUtility
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
open class OrderSummaryNetworkVM @Inject constructor(private val productRepo: ProductRepo) : ViewModel() {

    internal val addToCartFlow = MutableStateFlow(CreateCartReqModel())

    internal val createCartUiState = MutableStateFlow<CreateCartResponseUi>(CreateCartResponseUi.InitialUi(false))

    internal fun createCartOrderWithServer(value: VALUE) = viewModelScope.launch(Dispatchers.IO) {
        createCartUiState.update { CreateCartResponseUi.InitialUi(true) }

        if (addToCartFlow.value.createCartReqItems.isNotEmpty()) {
            when (val response = productRepo.apiRequestManager.createCartWithServer(addToCartFlow.value)) {
                is ResultWrapper.Success -> {
                    if (response.value.createCartData != null) {
                        val updateCartCall = async { productRepo.dbRepo.updateCartPrice(response.value.createCartData) }
                        if (updateCartCall.await()) {
                            createCartUiState.update { CreateCartResponseUi.CartSuccess(false, response.value.createCartData, value) }
                        } else {
                            createCartUiState.update { CreateCartResponseUi.CartSuccess(false, response.value.createCartData, value) }
                        }
                    } else {
                        createCartUiState.update { CreateCartResponseUi.ErrorUi(false, errorMsg = response.value.message, errors = response.value.errors) }
                    }
                }
                is ResultWrapper.NetworkError -> {
                    createCartUiState.update { CreateCartResponseUi.ErrorUi(false, errorMsg = "Something went wrong") }
                }
                is ResultWrapper.GenericError -> {
                    createCartUiState.update { CreateCartResponseUi.ErrorUi(false, errorMsg = response.error?.error ?: "Something went wrong") }
                }
                is ResultWrapper.UserTokenNotFound -> {
                    createCartUiState.update { CreateCartResponseUi.ErrorUi(false, errors = AppUtility.getAuthErrorArrayList()) }
                }
            }
        } else {
            createCartUiState.update { CreateCartResponseUi.ErrorUi(false, errorMsg = "Cart item is empty") }
        }
    }


    internal fun isCreateCartOnGoing(): Boolean {
        return createCartUiState.value.isLoading
    }
}
