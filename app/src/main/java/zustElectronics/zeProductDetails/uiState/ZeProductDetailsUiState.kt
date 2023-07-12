package zustElectronics.zeProductDetails.uiState

import `in`.opening.area.zustapp.uiModels.ProductDetailsUiState
import `in`.opening.area.zustapp.utility.UserCustomError
import zustElectronics.zeProductDetails.model.ZeProductDetailsData
import zustElectronics.zeProductDetails.model.ZeProductDetailsModel

sealed interface ZeProductDetailsUiState {
    val isLoading: Boolean

    data class ErrorState(override val isLoading: Boolean,val errors:List<UserCustomError>?=null,val errorMessage:String?=null) : ZeProductDetailsUiState

    data class SuccessState(override val isLoading: Boolean, val data: ZeProductDetailsData? = null) : ZeProductDetailsUiState

    data class LoadingState(override val isLoading: Boolean) : ZeProductDetailsUiState

}