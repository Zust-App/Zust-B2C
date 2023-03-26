package `in`.opening.area.zustapp.orderHistory.ui

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.orderDetail.OrderDetailActivity
import `in`.opening.area.zustapp.orderDetail.OrderDetailActivity.Companion.ORDER_ID
import `in`.opening.area.zustapp.orderDetail.ui.PREFIX_ORDER_ID
import `in`.opening.area.zustapp.orderHistory.models.OrderHistoryItem
import `in`.opening.area.zustapp.pagination.NoPageFoundException
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.viewmodels.MyOrdersListViewModel
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items

@Composable
fun BookingHistoryList(viewModel: MyOrdersListViewModel, paddingValues: PaddingValues) {
    val context: Context = LocalContext.current
    val userBookingItems: LazyPagingItems<OrderHistoryItem> = viewModel.userBookingFlow.collectAsLazyPagingItems()

    userBookingItems.apply {
        if (userBookingItems.loadState.refresh is LoadState.Error) {
            val error = (userBookingItems.loadState.refresh as LoadState.Error).error
            if (error is NoPageFoundException) {
                NoOrderFoundContainer()
            }
        }
    }

    LazyColumn(modifier = Modifier.fillMaxWidth(),
        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(6.dp)) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
        items(userBookingItems) { item ->
            UserBookingItem(item)
        }

        userBookingItems.apply {
            when (loadState.append) {
                is LoadState.Loading -> {
                    item {
                        ShowLoadMore()
                    }
                }
                is LoadState.NotLoading -> Unit

                is LoadState.Error -> {

                }
            }
            if (loadState.refresh == LoadState.Loading) {
                item {
                    ShowLoadMore()
                }
            }

        }
    }
}

@Composable
private fun UserBookingItem(userBooking: OrderHistoryItem? = null) {
    if (userBooking == null) {
        return
    }
    val context = LocalContext.current
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .background(color = Color.White,
            shape = RoundedCornerShape(8.dp))
        .wrapContentHeight()
        .clickable {
            val intent = Intent(context, OrderDetailActivity::class.java)
            intent.putExtra(ORDER_ID, userBooking.orderId)
            context.startActivity(intent)
        }) {
        val (orderItemName, orderId, orderTime, orderStatus, orderAmount) = createRefs()

        Text(text = userBooking.itemText, style = Typography_Montserrat.body2,
            color = colorResource(id = R.color.app_black),
            modifier = Modifier.constrainAs(orderItemName) {
                top.linkTo(parent.top, dp_12)
                start.linkTo(parent.start, dp_12)
                end.linkTo(orderAmount.start, dp_16)
                width = Dimension.fillToConstraints
            })

        Text(text = stringResource(id = R.string.ruppes) + userBooking.payablePrice, modifier = Modifier.constrainAs(orderAmount) {
            end.linkTo(parent.end, dp_12)
            top.linkTo(parent.top, dp_12)
        }, color = colorResource(id = R.color.grey_color_2), style = Typography_Montserrat.subtitle1)

        Text(text = buildString {
            append("OrderId:#$PREFIX_ORDER_ID")
            append(userBooking.orderId.toString())
        }, modifier = Modifier.constrainAs(orderId) {
            top.linkTo(orderItemName.bottom, dp_4)
            start.linkTo(parent.start, dp_12)
        }, color = colorResource(id = R.color.grey_color_3),
            style = Typography_Montserrat.body2)

        Text(text = userBooking.orderedDateAndTime, modifier = Modifier.constrainAs(orderTime) {
            top.linkTo(orderId.bottom, dp_4)
            start.linkTo(parent.start, dp_12)
            bottom.linkTo(parent.bottom, dp_12)
        }, color = colorResource(id = R.color.grey_color_3), style = Typography_Montserrat.body2)

        Text(
            text = userBooking.orderStatusType.lowercase(),
            modifier = Modifier
                .background(color = Color.LightGray)
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .constrainAs(orderStatus) {
                    top.linkTo(orderId.top)
                    end.linkTo(parent.end, dp_12)
                },
            style = Typography_Montserrat.body2,
        )
    }
}

@Composable
private fun ShowLoadMore() {
    ConstraintLayout(modifier = Modifier
        .height(30.dp)
        .fillMaxWidth()) {
        val (pgBar, loadingText) = createRefs()
        androidx.compose.material.CircularProgressIndicator(modifier = Modifier
            .size(width = 24.dp, height = 24.dp)
            .constrainAs(pgBar) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, color = colorResource(id = R.color.light_blue))

    }
}