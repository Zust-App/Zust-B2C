package `in`.opening.area.zustapp.orderSummary.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.extensions.collectAsStateLifecycleAware
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_6
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.uiModels.orderSummary.LockOrderCartUi
import `in`.opening.area.zustapp.uiModels.orderSummary.OrderSummaryUi
import `in`.opening.area.zustapp.utility.ProductUtils
import `in`.opening.area.zustapp.viewmodels.OrderSummaryViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

enum class OrderSummaryAction {
    UpdateOrder, ChangeAddress, AddNewLocation
}

@Composable
fun OrderSummaryBottomBarUi(orderSummaryViewModel: OrderSummaryViewModel, updateOrderCallback: (OrderSummaryAction) -> Unit) {
    val updateOrderWithServerData by orderSummaryViewModel.lockedCartUiState.collectAsState(initial = LockOrderCartUi.InitialUi(false))
    val cartItemData by orderSummaryViewModel.addedCartUiState.collectAsStateLifecycleAware(initial = OrderSummaryUi.InitialUi(false))
    val data = updateOrderWithServerData

    val addressLine by orderSummaryViewModel.addressLineData.collectAsState(initial = "")
    when (val cartData = cartItemData) {
        is OrderSummaryUi.SummarySuccess -> {
            Column(
                modifier = Modifier
                    .background(shape = RoundedCornerShape(12.dp),
                        color = colorResource(id = R.color.white))
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .wrapContentHeight()) {
                if (addressLine.isNotEmpty()) {
                    ConstraintLayout(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 20.dp, end = 20.dp)
                        .wrapContentHeight()) {
                        val (locationIcon, locationTag, savedLocationText, changeLocationTag) = createRefs()
                        Icon(painter = painterResource(id = R.drawable.custom_location_icon),
                            contentDescription = "address",
                            modifier = Modifier.constrainAs(locationIcon) {
                                top.linkTo(parent.top)

                            }, tint = colorResource(id = R.color.new_material_primary))
                        Text(text = stringResource(R.string.delivery_to), style = Typography_Montserrat.body1,
                            color = colorResource(id = R.color.app_black), modifier = Modifier.constrainAs(locationTag) {
                                top.linkTo(parent.top)
                                start.linkTo(locationIcon.end, dp_12)
                                end.linkTo(changeLocationTag.start, dp_12)
                                width = Dimension.fillToConstraints
                            })
                        Text(text = addressLine,
                            style = Typography_Montserrat.subtitle1,
                            color = Color(0xCC1F1F1F),
                            modifier = Modifier.constrainAs(savedLocationText) {
                                top.linkTo(locationTag.bottom, dp_6)
                                end.linkTo(parent.end)
                                start.linkTo(locationIcon.end, dp_12)
                                width = Dimension.fillToConstraints
                            }, maxLines = 1, overflow = TextOverflow.Ellipsis)

                        Text(text = "Change", style = Typography_Montserrat.subtitle1, fontWeight = FontWeight.W600,
                            color = colorResource(id = R.color.new_material_primary),
                            modifier = Modifier
                                .constrainAs(changeLocationTag) {
                                    top.linkTo(parent.top)
                                    end.linkTo(parent.end)
                                }
                                .clickable {
                                    updateOrderCallback.invoke(OrderSummaryAction.ChangeAddress)
                                })
                    }
                } else {
                    ConstraintLayout(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 20.dp, end = 20.dp)
                        .wrapContentHeight()) {
                        val (locationIcon, selectAddressTag) = createRefs()

                        Icon(painter = painterResource(id = R.drawable.custom_location_icon),
                            contentDescription = "address",
                            modifier = Modifier.constrainAs(locationIcon) {
                                top.linkTo(parent.top)
                                end.linkTo(selectAddressTag.start, dp_8)
                            }, tint = colorResource(id = R.color.new_material_primary))

                        Text(text = "Select Address",
                            style = Typography_Montserrat.body1,
                            color = colorResource(id = R.color.app_black),
                            modifier = Modifier
                                .constrainAs(selectAddressTag) {
                                    top.linkTo(parent.top)
                                    end.linkTo(parent.end)
                                    start.linkTo(parent.start)
                                }
                                .clickable {
                                    updateOrderCallback.invoke(OrderSummaryAction.ChangeAddress)
                                })
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Divider(modifier = Modifier.height(0.5.dp), color = Color(0x66000000))
                Spacer(modifier = Modifier.height(10.dp))

                ConstraintLayout(modifier = Modifier
                    .fillMaxWidth()) {
                    val (button, progressBar) = createRefs()
                    OutlinedButton(onClick = { updateOrderCallback.invoke(OrderSummaryAction.UpdateOrder) },
                        border = BorderStroke(1.dp, colorResource(id = R.color.new_material_primary)),
                        shape = RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp),
                        modifier = Modifier
                            .wrapContentHeight()
                            .constrainAs(button) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                width = Dimension.fillToConstraints
                            },
                        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.new_material_primary))) {
                        Column {
                            Text(text = "Pay", style = Typography_Montserrat.subtitle1,
                                color = colorResource(id = R.color.white))
                            Text(text = stringResource(id = R.string.ruppes) +
                                    ProductUtils.roundTo1DecimalPlaces(
                                        cartData.data.totalCurrentPrice +
                                                (cartData.data.deliveryPartnerTip) +
                                                (cartData.data.deliveryFee ?: 0.0)
                                                + (cartData.data.packagingFee ?: 0.0)),
                                style = Typography_Montserrat.body1,
                                color = colorResource(id = R.color.white))
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "Place order",
                            textAlign = TextAlign.Center,
                            color = colorResource(id = R.color.white),
                            style = Typography_Montserrat.body1)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(painter = painterResource(id = R.drawable.arrow_right_icon),
                            contentDescription = "",
                            tint = colorResource(id = R.color.white))
                    }
                    if (data.isLoading) {
                        CircularProgressIndicator(
                            color = colorResource(id = R.color.white),
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .constrainAs(progressBar) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                        )
                    }
                }

            }
        }
        is OrderSummaryUi.InitialUi -> {

        }
    }
}