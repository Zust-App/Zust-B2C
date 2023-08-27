package subscription.uiModel

sealed interface SubscriptionFormUiModel {
    val isLoading: Boolean

    data class Success(override val isLoading: Boolean,val data:Boolean) : SubscriptionFormUiModel
    data class Error(override val isLoading: Boolean,val error:String?=null) : SubscriptionFormUiModel
    data class Empty(override val isLoading: Boolean) : SubscriptionFormUiModel

}