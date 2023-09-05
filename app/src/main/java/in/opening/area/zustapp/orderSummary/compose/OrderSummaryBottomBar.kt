package `in`.opening.area.zustapp.orderSummary.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.extensions.collectAsStateLifecycleAware
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_24
import `in`.opening.area.zustapp.ui.theme.dp_6
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.uiModels.orderSummary.LockOrderCartUi
import `in`.opening.area.zustapp.uiModels.orderSummary.OrderSummaryUi
import `in`.opening.area.zustapp.utility.ProductUtils
import `in`.opening.area.zustapp.viewmodels.OrderSummaryViewModel
import ui.colorBlack

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
            Box(modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .shadow(elevation = dp_8)) {
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
                            Text(text = stringResource(R.string.delivery_to), style = ZustTypography.bodyMedium,
                                color = colorResource(id = R.color.app_black), modifier = Modifier.constrainAs(locationTag) {
                                    top.linkTo(parent.top)
                                    start.linkTo(locationIcon.end, dp_12)
                                    end.linkTo(changeLocationTag.start, dp_12)
                                    width = Dimension.fillToConstraints
                                })
                            Text(text = addressLine,
                                style = ZustTypography.bodySmall,
                                color = Color(0xCC1F1F1F),
                                modifier = Modifier.constrainAs(savedLocationText) {
                                    top.linkTo(locationTag.bottom, dp_6)
                                    end.linkTo(parent.end)
                                    start.linkTo(locationIcon.end, dp_12)
                                    width = Dimension.fillToConstraints
                                }, maxLines = 1, overflow = TextOverflow.Ellipsis)

                            Text(text = "Change", style = ZustTypography.bodySmall, fontWeight = FontWeight.W600,
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
                            .wrapContentHeight())
                        {
                            val (locationIcon, selectAddressTag) = createRefs()

                            Icon(painter = painterResource(id = R.drawable.custom_location_icon),
                                contentDescription = "address",
                                modifier = Modifier.constrainAs(locationIcon) {
                                    top.linkTo(parent.top)
                                    end.linkTo(selectAddressTag.start, dp_8)
                                }, tint = colorResource(id = R.color.new_material_primary))

                            Text(text = "Select Address",
                                style = ZustTypography.bodyMedium,
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
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topEnd = dp_8, topStart = dp_8))
                        .background(color = colorResource(id = R.color.white), shape = RoundedCornerShape(topEnd = dp_8, topStart = dp_8))) {
                        val (itemCount, price, viewCart, progressBar) = createRefs()
                        Text(text = buildString {
                            append(cartData.data.totalItemCount)
                            append(" Items")
                        }, modifier = Modifier.constrainAs(itemCount) {
                            start.linkTo(parent.start, dp_16)
                            top.linkTo(viewCart.top)
                            bottom.linkTo(price.top)
                        }, color = colorResource(id = R.color.language_default),
                            style = ZustTypography.bodyMedium)

                        Text(text = ProductUtils.roundTo1DecimalPlaces(
                            cartData.data.totalCurrentPrice +
                                    (cartData.data.deliveryPartnerTip) +
                                    (cartData.data.deliveryFee ?: 0.0)
                                    + (cartData.data.packagingFee ?: 0.0)), modifier = Modifier.constrainAs(price) {
                            start.linkTo(parent.start, dp_16)
                            top.linkTo(itemCount.bottom)
                            bottom.linkTo(viewCart.bottom)
                        }, color = colorBlack,
                            style = ZustTypography.titleLarge, fontSize = 14.sp)

                        Button(onClick = { updateOrderCallback.invoke(OrderSummaryAction.UpdateOrder) }, modifier = Modifier.constrainAs(viewCart) {
                            end.linkTo(parent.end, dp_8)
                            top.linkTo(parent.top, dp_8)
                            bottom.linkTo(parent.bottom, dp_8)
                        }, colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.new_material_primary))) {
                            Spacer(modifier = Modifier.width(dp_12))
                            Text(text = stringResource(R.string.review),
                                color = colorResource(id = R.color.white), style = ZustTypography.bodyMedium)
                            Spacer(modifier = Modifier.width(dp_12))
                            Icon(painter = painterResource(id = R.drawable.arrow_right_icon), contentDescription = "", tint = colorResource(id = R.color.white))
                            Spacer(modifier = Modifier.width(dp_12))
                        }

                        if (data.isLoading) {
                            CircularProgressIndicator(
                                color = colorResource(id = R.color.new_material_primary),
                                modifier = Modifier
                                    .width(dp_24)
                                    .height(dp_24)
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
        }

        is OrderSummaryUi.InitialUi -> {

        }
    }
}