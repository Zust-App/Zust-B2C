package `in`.opening.area.zustapp.uiModels

import zustbase.orderDetail.models.OrderDetailData
import `in`.opening.area.zustapp.utility.UserCustomError

sealed interface OrderDetailUi {
    val isLoading: Boolean

    data class OrderDetail(override val isLoading: Boolean, val data: OrderDetailData, val timeStamp: Long = System.currentTimeMillis()) : OrderDetailUi

    data class InitialUi(override val isLoading: Boolean, val timeStamp: Long = System.currentTimeMillis()) : OrderDetailUi

    data class ErrorUi(override val isLoading: Boolean,
                       val errors: List<UserCustomError> = arrayListOf(),val errorMessage:String="") : OrderDetailUi
}