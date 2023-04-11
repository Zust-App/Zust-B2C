package `in`.opening.area.zustapp.home.components

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.orderDetail.models.convertToDisplayText
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.uiModels.LatestOrderUi
import `in`.opening.area.zustapp.viewmodels.HomeViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.ViewModel

@Composable
fun LatestOrderUiContainer(viewModel: ViewModel, orderDetailCallback: (Int) -> Unit) {
    if (viewModel is HomeViewModel) {
        val latestOrderUiState by viewModel.latestOrderUiState.collectAsState(initial = LatestOrderUi.InitialUi(false))

        val response = latestOrderUiState
        if (response.isLoading) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                )
            }
        }

        when (response) {
            is LatestOrderUi.LOrderSuccess -> {
                ConstraintLayout(modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xffF9FAFF))
                ) {
                    val (viewDetails, cross, _, itemText, orderTiming) = createRefs()
                    if (!response.data?.items.isNullOrEmpty()) {
                        val orderItems = response.data?.items!!.convertToDisplayText()
                        Text(text = orderItems, modifier = Modifier.constrainAs(itemText) {
                            start.linkTo(parent.start, dp_12)
                            top.linkTo(parent.top, dp_8)
                            end.linkTo(viewDetails.start, dp_4)
                            width = Dimension.fillToConstraints
                        }, maxLines = 1, overflow = TextOverflow.Ellipsis,
                            style = Typography_Montserrat.body2,
                            color = colorResource(id = R.color.app_black), fontSize = 14.sp)
                    }

                    Text(text = response.data?.expectedTimeToDelivery ?: response.data?.orderStatuses?.last()?.orderStatusType!!,
                        color = colorResource(id = R.color.new_material_primary),
                        modifier = Modifier.constrainAs(orderTiming) {
                            top.linkTo(itemText.bottom, dp_4)
                            start.linkTo(parent.start, dp_12)
                            end.linkTo(viewDetails.start, dp_12)
                            bottom.linkTo(parent.bottom, dp_8)
                            width = Dimension.fillToConstraints
                        })

                    if (!response.data?.items.isNullOrEmpty()) {
                        Text(text = stringResource(R.string.view),
                            modifier = Modifier
                                .constrainAs(viewDetails) {
                                    top.linkTo(parent.top, dp_8)
                                    bottom.linkTo(parent.bottom, dp_8)
                                    end.linkTo(cross.start, dp_8)
                                }
                                .clickable {
                                    val orderId = response.data?.orderId
                                    if (orderId != null) {
                                        orderDetailCallback.invoke(orderId)
                                    }
                                }
                                .background(color = colorResource(id = R.color.red_primary), shape = RoundedCornerShape(4.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                            color = colorResource(id = R.color.white),
                            style = Typography_Montserrat.body2, fontSize = 12.sp)

                        Icon(painter = painterResource(id = R.drawable.cross_red_outline), contentDescription = stringResource(R.string.close),
                            modifier = Modifier
                                .constrainAs(cross) {
                                    end.linkTo(parent.end, dp_8)
                                    top.linkTo(parent.top, dp_8)
                                    bottom.linkTo(parent.bottom, dp_8)
                                }
                                .size(16.dp))
                    }
                }
            }
            is LatestOrderUi.ErrorUi -> {

            }
            is LatestOrderUi.InitialUi -> {

            }

        }
    }
}