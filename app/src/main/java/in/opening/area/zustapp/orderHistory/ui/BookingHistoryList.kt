package `in`.opening.area.zustapp.orderHistory.ui

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.orderDetail.OrderDetailActivity
import `in`.opening.area.zustapp.orderDetail.OrderDetailActivity.Companion.ORDER_ID
import `in`.opening.area.zustapp.orderDetail.ui.PREFIX_ORDER_ID
import `in`.opening.area.zustapp.orderHistory.models.OrderHistoryItem
import `in`.opening.area.zustapp.pagination.NoPageFoundException
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.ProductUtils
import `in`.opening.area.zustapp.viewmodels.MyOrdersListViewModel
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun BookingHistoryList(viewModel: MyOrdersListViewModel, paddingValues: PaddingValues) {
    val userBookingItems: LazyPagingItems<OrderHistoryItem> = viewModel.userBookingFlow.collectAsLazyPagingItems()
    val state = rememberSwipeRefreshState(
        isRefreshing = userBookingItems.loadState.refresh is LoadState.Loading,
    )
    userBookingItems.apply {
        if (userBookingItems.loadState.refresh is LoadState.Error) {
            val error = (userBookingItems.loadState.refresh as LoadState.Error).error
            if (error is NoPageFoundException) {
                NoOrderFoundContainer()
            }
        }
    }
    SwipeRefresh(
        state = state,
        onRefresh = {
            userBookingItems.refresh()
        },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            contentPadding = paddingValues, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
            items(userBookingItems) { item ->
                if (item != null) {
                    UserBookingItem(item, viewModel)
                }
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

}

val orderListItemModifier = Modifier
    .fillMaxWidth()
    .padding(horizontal = 16.dp)
    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
    .wrapContentHeight()
val filledColor = Color(0xffFF8585)

@Composable
private fun UserBookingItem(userBooking: OrderHistoryItem? = null, viewModel: MyOrdersListViewModel) {
    if (userBooking == null) {
        return
    }

    val context = LocalContext.current
    var canShowRatingDialog by remember {
        mutableStateOf(false)
    }

    if (canShowRatingDialog) {
        RatingDialog(canShowDialog = true, orderId = userBooking.orderId, viewModel) {
            AppUtility.showToast(context, "Thanks for your rating It will updated soon")
            userBooking.rating = it
            canShowRatingDialog = false
        }
    }
    val colorAndStatusPair = getColorBasedOnStatus(userBooking.orderStatusType)
    ConstraintLayout(modifier = orderListItemModifier.clickable {
        val intent = Intent(context, OrderDetailActivity::class.java)
        intent.putExtra(ORDER_ID, userBooking.orderId)
        context.startActivity(intent)
    }) {
        val (bagIcon, orderItemCount, orderId, orderStatus, orderTime, orderAmount) = createRefs()

        Image(painter = painterResource(id = R.drawable.tabler_paper_bag), contentDescription = "bag", modifier = Modifier
            .size(height = 18.dp, width = 16.dp)
            .constrainAs(bagIcon) {
                top.linkTo(parent.top, dp_16)
                start.linkTo(parent.start, dp_16)
            })

        Text(text = buildString {
            append(PREFIX_ORDER_ID)
            append(userBooking.orderId.toString())
        }, style = Typography_Montserrat.body2,
            fontWeight = FontWeight.W600,
            color = colorResource(id = R.color.app_black),
            modifier = Modifier.constrainAs(orderId) {
                top.linkTo(bagIcon.top)
                start.linkTo(bagIcon.end, dp_12)
                end.linkTo(orderStatus.start, dp_16)
                bottom.linkTo(bagIcon.bottom)
                width = Dimension.fillToConstraints
            })

        Text(text = colorAndStatusPair.second.uppercase(),
            modifier = Modifier.constrainAs(orderStatus) {
                end.linkTo(parent.end, dp_16)
                top.linkTo(bagIcon.top)
                bottom.linkTo(bagIcon.bottom)
            }, style = Typography_Montserrat.body2,
            color = colorAndStatusPair.first,
            fontWeight = FontWeight.W600)

        Text(text = buildString {
            append("items:- ")
            append(userBooking.totalItems)
        }, modifier = Modifier.constrainAs(orderItemCount) {
            top.linkTo(bagIcon.bottom, dp_4)
            start.linkTo(orderId.start)
        }, color = Color(0xBF1E1E1E), fontWeight = FontWeight.W500, style = Typography_Montserrat.subtitle1)
        val (divider) = createRefs()

        Divider(modifier = Modifier
            .width(0.2.dp)
            .background(color = Color(0xffA0A0A0))
            .constrainAs(divider) {
                top.linkTo(orderItemCount.bottom, dp_12)
                start.linkTo(parent.start, dp_16)
                end.linkTo(parent.end, dp_16)
                width = Dimension.fillToConstraints
            })

        Text(text = stringResource(id = R.string.ruppes) + ProductUtils.getNumberDisplayValue(userBooking.payablePrice), modifier = Modifier.constrainAs(orderAmount) {
            end.linkTo(parent.end, dp_16)
            top.linkTo(divider.bottom, dp_12)
        }, color = colorResource(id = R.color.app_black), style = Typography_Montserrat.body2, fontWeight = FontWeight.W600)

        Text(text = userBooking.orderedDateAndTime, modifier = Modifier.constrainAs(orderTime) {
            top.linkTo(divider.bottom, dp_12)
            start.linkTo(parent.start, dp_16)
        }, color = Color(0xBF1E1E1E), style = Typography_Montserrat.subtitle1, fontWeight = FontWeight.W500)

        val (detailsBtn) = createRefs()
        Text(style = Typography_Montserrat.body2,
            textAlign = TextAlign.Center, text = "Details",
            color = colorResource(id = R.color.white), modifier = Modifier
                .width(80.dp)
                .clickable {
                    val intent = Intent(context, OrderDetailActivity::class.java)
                    intent.putExtra(ORDER_ID, userBooking.orderId)
                    context.startActivity(intent)
                }
                .constrainAs(detailsBtn) {
                    top.linkTo(orderAmount.bottom, dp_8)
                    bottom.linkTo(parent.bottom, dp_16)
                    end.linkTo(parent.end, dp_16)
                }
                .background(color = colorResource(id = R.color.new_material_primary),
                    shape = RoundedCornerShape(4.dp))
                .padding(vertical = 6.dp)
                .clip(RoundedCornerShape(4.dp)))

        val (ratingSection) = createRefs()

        Row(modifier = Modifier
            .constrainAs(ratingSection) {
                start.linkTo(parent.start, dp_16)
                top.linkTo(orderAmount.bottom, dp_8)
                bottom.linkTo(parent.bottom, dp_16)
                end.linkTo(detailsBtn.start, dp_8)
                width = Dimension.fillToConstraints
            }
            .clickable {
                if (userBooking.rating == 0) {
                    AppUtility.showToast(context, "You have already rated")
                }
            }, verticalAlignment = Alignment.CenterVertically) {
            if (userBooking.rating == 0) {
                Text(text = "Rate order", style = Typography_Montserrat.body2,
                    color = colorResource(id = R.color.new_material_primary), modifier = Modifier.clickable {
                        canShowRatingDialog = true
                    })
            } else {
                Text(text = "Rating", style = Typography_Montserrat.body2,
                    color = colorResource(id = R.color.black_2))
                Spacer(modifier = Modifier.width(12.dp))
                repeat(5) {
                    if (it < (userBooking.rating ?: 0)) {
                        Icon(painter = painterResource(id = R.drawable.rating_filled_icon),
                            contentDescription = null, modifier = Modifier
                                .size(20.dp), filledColor)
                        Spacer(modifier = Modifier.width(4.dp))
                    } else {
                        Icon(painter = painterResource(id = R.drawable.rating_unfilled),
                            contentDescription = null, modifier = Modifier
                                .size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowLoadMore() {
    ConstraintLayout(modifier = Modifier
        .height(30.dp)
        .fillMaxWidth()) {
        val (pgBar, _) = createRefs()
        CircularProgressIndicator(modifier = Modifier
            .size(width = 24.dp, height = 24.dp)
            .constrainAs(pgBar) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, color = colorResource(id = R.color.light_blue))

    }
}

@Composable
private fun getColorBasedOnStatus(status: String): Pair<Color, String> {
    if (status.contains("confirm", ignoreCase = true)) {
        return Pair(colorResource(id = R.color.light_green), "confirmed")
    } else if (status.contains("shipped", ignoreCase = true)) {
        return Pair(colorResource(id = R.color.light_green), "shipped")
    } else if (status.contains("out", ignoreCase = true)) {
        return Pair(colorResource(id = R.color.light_green), "out for delivery")
    } else if (status.contains("delivered", ignoreCase = true)) {
        return Pair(colorResource(id = R.color.light_green), "delivered")
    } else if (status.contains("cancel", ignoreCase = true)) {
        return Pair(colorResource(id = R.color.red_primary), "cancelled")
    }
    return Pair(colorResource(id = R.color.light_black), status)
}