package non_veg.payment.uiModels

sealed interface NonVegCreateOrderUiState {
    val isLoading:Boolean
    data class Initial(override val isLoading: Boolean):NonVegCreateOrderUiState
    data class Success(override val isLoading: Boolean,val orderId:Int):NonVegCreateOrderUiState
    data class ErrorUi(override val isLoading: Boolean,val message:String?=null):NonVegCreateOrderUiState
}