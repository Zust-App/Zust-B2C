package `in`.opening.area.zustapp.viewmodels

import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.uiModels.CouponListUi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CouponListingViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager) : ViewModel() {
    internal val couponListUiState = MutableStateFlow<CouponListUi>(CouponListUi.InitialUi(isLoading = false))
    internal var userInputCoupon by mutableStateOf("")

    internal fun getCouponListFromServer() = viewModelScope.launch {
        couponListUiState.update { CouponListUi.InitialUi(true) }
        when (val response = apiRequestManager.getCouponsFromServer()) {
            is ResultWrapper.Success -> {
                if (response.value.data?.coupons != null) {
                    couponListUiState.update { (CouponListUi.CouponSuccess(false, response.value.data.coupons)) }
                } else {
                    couponListUiState.update { CouponListUi.ErrorUi(false,response.value.errors) }
                }
            }
            is ResultWrapper.GenericError -> {
                couponListUiState.update { CouponListUi.ErrorUi(false,errorMsg= response.error?.error?:"Something went wrong") }

            }
            is ResultWrapper.UserTokenNotFound -> {
                couponListUiState.update { CouponListUi.ErrorUi(false, errorMsg = "User auth failure") }
            }
            is ResultWrapper.NetworkError -> {
                couponListUiState.update { CouponListUi.ErrorUi(false, errorMsg = "Something went wrong") }
            }
        }
    }

}