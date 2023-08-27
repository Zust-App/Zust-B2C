package zustbase.orderDetail.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.CustomAnimatedProgressBar
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import zustbase.orderDetail.models.OrderStatus
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_20
import `in`.opening.area.zustapp.uiModels.OrderDetailUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.MyOrdersListViewModel

@Composable
fun OrderedDetailsContainer(
    viewModel: MyOrdersListViewModel,
    constraintLayoutScope: ConstraintLayoutScope,
    callback: () -> Unit,
) {
    val orderDetail =
        viewModel.orderDetailFlow.collectAsState(initial = OrderDetailUi.InitialUi(false))
    val context: Context = LocalContext.current
    constraintLayoutScope.apply {
        val (list) = createRefs()
        val response = orderDetail.value
        if (response.isLoading) {
            ShowLoadMore(this)
        }
        when (response) {
            is OrderDetailUi.OrderDetail -> {
                LazyColumn(modifier = Modifier
                    .fillMaxHeight()
                    .constrainAs(list) {
                        top.linkTo(parent.top, dp_20)
                        start.linkTo(parent.start, dp_20)
                        end.linkTo(parent.end, dp_20)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }) {
                    if (!response.data.expectedTimeToDelivery.isNullOrEmpty()) {
                        item {
                            OrderStatusSummaryHolder(response.data)
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(dp_20))
                        ItemTopInfoContainer(response.data)
                        Spacer(modifier = Modifier.height(dp_20))
                    }

                    if (!response.data.items.isNullOrEmpty()) {
                        items(response.data.items) { item ->
                            OrderedItemSingleUnit(item)
                            Divider(
                                modifier = Modifier
                                    .width(1.dp)
                                    .fillMaxWidth()
                                    .background(color = Color(0xFFA0A0A0))
                            )
                        }
                    }

                    if (!response.data.displayOrderStatus.isNullOrEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(dp_20))
                            OrderStatusTitleContainer()
                        }
                        itemsIndexed(response.data.displayOrderStatus!!) { index, item: OrderStatus ->
                            OrderStatusSingleItem(
                                item,
                                index,
                                response.data.displayOrderStatus!!.size
                            )
                        }
                        item {
                            OrderStatusBottomContainer()
                        }
                    } else {
                        item {
                            Spacer(modifier = Modifier.height(dp_20))
                            OrderStatusTitleContainer()
                        }
                        if (!response.data.orderStatuses.isNullOrEmpty()) {
                            itemsIndexed(response.data.orderStatuses) { index, item: OrderStatus ->
                                OrderStatusSingleItem(item, index, response.data.orderStatuses.size)
                            }
                        }
                        item {
                            OrderStatusBottomContainer()
                        }
                    }
                    if (!response.data.secretCode.isNullOrEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(dp_20))
                            OrderSecretCodeContainer(data = response.data)
                        }
                    }
                    item {
                        if (response.data.riderDetails != null) {
                            Spacer(modifier = Modifier.height(dp_20))
                            DeliveryPartnerInfoUi(response.data.riderDetails)
                        }
                    }
                    if (!response.data.reason.isNullOrEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(dp_20))
                            ZustAppReason(response.data.reason)
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(dp_20))
                        HelpAndSupportUi()
                    }
                    item {
                        Spacer(modifier = Modifier.height(dp_20))
                        DeliveryAddressContainer(response.data)
                    }
                    item {
                        Spacer(modifier = Modifier.height(dp_20))
                        BillContainer(response.data)
                        Spacer(modifier = Modifier.height(dp_20))
                        ExploreMoreUi {
                            callback.invoke()
                        }
                        Spacer(modifier = Modifier.height(dp_20))
                    }
                }
            }

            is OrderDetailUi.ErrorUi -> {
                if (response.errorMessage.isNotEmpty()) {
                    AppUtility.showToast(context, response.errorMessage)
                } else {
                    AppUtility.showToast(context, response.errors.getTextMsg())
                }
                RetryOrderDetails {
                    viewModel.orderIdCache?.let { orderId ->
                        viewModel.getOrderDetails(orderId, viewModel.intentSource)
                    }
                }
            }

            is OrderDetailUi.InitialUi -> {

            }
        }
    }
}

@Composable
fun ShowLoadMore(constraintLayoutScope: ConstraintLayoutScope) {
    constraintLayoutScope.apply {
        val (pgBar) = createRefs()
        CustomAnimatedProgressBar(modifier = Modifier
            .size(100.dp)
            .constrainAs(pgBar) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
    }
}

@Composable
private fun RetryOrderDetails(retryCallback: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = "Something went wrong please try again",
            color = colorResource(id = R.color.red_primary),
            style = ZustTypography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(onClick = {
            retryCallback.invoke()
        }) {
            Text(
                text = "Retry",
                color = colorResource(id = R.color.app_black),
                style = ZustTypography.bodyMedium
            )
        }

    }
}