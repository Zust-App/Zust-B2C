package `in`.opening.area.zustapp.uiModels

import zustbase.orderDetail.models.OrderDetailData

sealed interface LatestOrderUi {
    val isLoading: Boolean

    data class ErrorUi(override val isLoading: Boolean, val canRemove: Boolean = false) : LatestOrderUi

    data class InitialUi(override val isLoading: Boolean) : LatestOrderUi

    data class LOrderSuccess(override val isLoading: Boolean,
                             val data: OrderDetailData? = null) : LatestOrderUi

}