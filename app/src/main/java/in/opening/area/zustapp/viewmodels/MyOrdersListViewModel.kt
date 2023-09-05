package `in`.opening.area.zustapp.viewmodels

import android.app.Application
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import zustbase.orderDetail.models.OrderStatus
import zustbase.orderHistory.models.OrderHistoryItem
import zustbase.orderHistory.models.OrderRatingBody
import zustbase.orderHistory.ui.RatingOrderUiState
import `in`.opening.area.zustapp.pagination.UserBookingDataSource
import `in`.opening.area.zustapp.uiModels.OrderDetailUi
import `in`.opening.area.zustapp.utility.AppUtility
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.MyApplication
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.pagination.UserNonVegBookingDataSource
import kotlinx.coroutines.Dispatchers
import zustbase.orderDetail.ui.INTENT_SOURCE_NON_VEG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyOrdersListViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager) :
    ViewModel() {
    internal var userGroceryBookingFlow: Flow<PagingData<OrderHistoryItem>> =
        Pager(PagingConfig(pageSize = 10, enablePlaceholders = false)) {
            UserBookingDataSource(apiRequestManager)
        }.flow.cachedIn(viewModelScope)
    internal val orderDetailFlow = MutableStateFlow<OrderDetailUi>(OrderDetailUi.InitialUi(false))
    internal val ratingOrderFlow =
        MutableStateFlow<RatingOrderUiState>(RatingOrderUiState.InitialState(false))

    internal var orderIdCache: Int? = null
    internal var intentSource: String? = null

    internal val orderHistoryTab= MutableStateFlow(ACTION.GROCERY_TAB)

    internal var userNonVegBookingFlow: Flow<PagingData<OrderHistoryItem>> =
        Pager(PagingConfig(pageSize = 10, enablePlaceholders = false)) {
            UserNonVegBookingDataSource(apiRequestManager)
        }.flow.cachedIn(viewModelScope)

    internal fun getOrderDetails(orderId: Int, intentSource: String?) = viewModelScope.launch(Dispatchers.IO) {
        orderDetailFlow.update { OrderDetailUi.InitialUi(true) }
        when (val response = if (intentSource == INTENT_SOURCE_NON_VEG) {
            apiRequestManager.getOrderDetailsForNonVeg(orderId)
        } else {
            apiRequestManager.getOrderDetails(orderId)
        }) {
            is ResultWrapper.NetworkError -> {
                orderDetailFlow.update {
                    OrderDetailUi.ErrorUi(
                        false,
                        AppUtility.getAuthErrorArrayList()
                    )
                }
            }

            is ResultWrapper.UserTokenNotFound -> {

                orderDetailFlow.update {
                    OrderDetailUi.ErrorUi(
                        false,
                        AppUtility.getAuthErrorArrayList()
                    )
                }
            }

            is ResultWrapper.Success -> {
                if (response.value.data == null) {

                    orderDetailFlow.update {
                        OrderDetailUi.ErrorUi(
                            false,
                            response.value.errors ?: arrayListOf()
                        )
                    }
                } else {
                    val displayOrderStatus = ArrayList<OrderStatus>()
                    response.value.data.apply {
                        if (orderStatuses != null) {
                            val lastStatus = orderStatuses.last()

                            val allStatusSeq = this.statusSeq
                            val allStatusList: List<String>? = allStatusSeq?.split(",")
                            if (allStatusList != null) {
                                var index = -1

                                for (i in allStatusList.indices) {
                                    if (allStatusList[i] == lastStatus.orderStatusType) {
                                        index = i
                                    }
                                }
                                for (i in allStatusList.indices) {
                                    var isContains = false
                                    for (j in orderStatuses.indices) {
                                        if (allStatusList[i] == orderStatuses[j].orderStatusType) {
                                            isContains = true
                                            displayOrderStatus.add(
                                                OrderStatus(
                                                    orderStatuses[j].createdDateTime,
                                                    orderStatuses[j].orderStatusType?.replace(
                                                        "_",
                                                        " "
                                                    )
                                                )
                                            )
                                        }
                                    }
                                    if (!isContains) {
                                        displayOrderStatus.add(
                                            OrderStatus(
                                                orderStatusType = allStatusList[i].replace(
                                                    "_",
                                                    " "
                                                ), createdDateTime = if (i <= index) {
                                                    "-1"
                                                } else {
                                                    null
                                                }
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                    response.value.data.displayOrderStatus = displayOrderStatus
                    orderDetailFlow.update { OrderDetailUi.OrderDetail(false, response.value.data) }
                }
            }

            is ResultWrapper.GenericError -> {
                orderDetailFlow.update {
                    OrderDetailUi.ErrorUi(
                        false,
                        arrayListOf(),
                        response.error?.error ?: "Something went wrong"
                    )
                }
            }
        }
    }

    internal fun updateRating(orderId: Int, rating: Int) = viewModelScope.launch {
        ratingOrderFlow.update {
            RatingOrderUiState.InitialState(true)
        }
        when (val response = apiRequestManager.updateRating(OrderRatingBody(orderId, rating))) {
            is ResultWrapper.Success -> {
                if (response.value.data != null) {
                    ratingOrderFlow.update {
                        RatingOrderUiState.SuccessState(false)
                    }
                    return@launch
                }
                ratingOrderFlow.update {
                    RatingOrderUiState.ErrorState(false)
                }
            }

            else -> {
                ratingOrderFlow.update {
                    RatingOrderUiState.ErrorState(false)
                }
            }
        }

    }

}

