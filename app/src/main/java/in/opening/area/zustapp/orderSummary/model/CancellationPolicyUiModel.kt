package `in`.opening.area.zustapp.orderSummary.model

sealed interface CancellationPolicyUiModel {
    val isLoading: Boolean

    data class CancellationPolicyUiSuccess(override val isLoading: Boolean, val data:Any):CancellationPolicyUiModel
    data class ErrorUi(override val isLoading: Boolean, val errorMsg: String? = "") : CancellationPolicyUiModel

    data class InitialUi(override val isLoading: Boolean):CancellationPolicyUiModel

}