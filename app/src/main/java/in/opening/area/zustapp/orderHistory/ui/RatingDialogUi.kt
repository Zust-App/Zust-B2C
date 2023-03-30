package `in`.opening.area.zustapp.orderHistory.ui

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.extensions.collectAsStateLifecycleAware
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.orderDetail.ui.PREFIX_ORDER_ID
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.MyOrdersListViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.flow.update

@Composable
fun RatingDialog(canShowDialog: Boolean, orderId: Int, viewModel: MyOrdersListViewModel, callback: (Int) -> Unit) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(canShowDialog) }
    var rating by remember {
        mutableStateOf(0)
    }
    var isLoading by remember { mutableStateOf(canShowDialog) }
    val ratingApiData by viewModel.ratingOrderFlow.collectAsStateLifecycleAware(initial = RatingOrderUiState.InitialState(false))
    when (ratingApiData) {
        is RatingOrderUiState.SuccessState -> {
            isLoading = false
            viewModel.ratingOrderFlow.update {
                RatingOrderUiState.InitialState(false)
            }
            rating = 0
            showDialog = false
            callback.invoke(rating)
        }
        is RatingOrderUiState.ErrorState -> {
            isLoading = false
            AppUtility.showToast(context, "Something went wrong, please try later")
        }
        is RatingOrderUiState.InitialState -> {
            isLoading = ratingApiData.isLoading
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text("Rate your order $PREFIX_ORDER_ID$orderId",
                    style = Typography_Montserrat.body1,
                    color = colorResource(id = R.color.app_black))
            },
            text = {
                ConstraintLayout(modifier = Modifier) {
                    val (progressBar) = createRefs()
                    val (ratingBar) = createRefs()
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier
                            .size(30.dp)
                            .constrainAs(progressBar) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                            })
                    } else {
                        Row(modifier = Modifier.constrainAs(ratingBar) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                        }, verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center) {
                            Spacer(modifier = Modifier.width(12.dp))
                            repeat(5) {
                                if (it < rating) {
                                    Icon(painter = painterResource(id = R.drawable.rating_filled_icon),
                                        contentDescription = null, modifier = Modifier
                                            .size(28.dp)
                                            .clickable {
                                                rating = it + 1
                                            }, filledColor)
                                } else {
                                    Icon(painter = painterResource(id = R.drawable.rating_unfilled),
                                        contentDescription = null, modifier = Modifier
                                            .size(28.dp)
                                            .clickable {
                                                rating = it + 1
                                            })
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (!isLoading) {
                        viewModel.updateRating(orderId, rating)
                    }
                }) {
                    Text("Submit",
                        color = colorResource(id = R.color.new_material_primary), style = Typography_Montserrat.body2)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel", color = colorResource(id = R.color.red_primary), style = Typography_Montserrat.body2)
                }
            }
        )
    }
}
