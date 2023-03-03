package `in`.opening.area.zustapp.orderSummary.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.extensions.collectAsStateLifecycleAware
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import `in`.opening.area.zustapp.ui.theme.montserrat
import `in`.opening.area.zustapp.uiModels.orderSummary.OrderSummaryUi
import `in`.opening.area.zustapp.utility.ProductUtils
import `in`.opening.area.zustapp.viewmodels.OrderSummaryViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CartBillingDetails(orderSummaryViewModel: OrderSummaryViewModel) {
    val cartItemData by orderSummaryViewModel.addedCartUiState.collectAsStateLifecycleAware(initial = OrderSummaryUi.InitialUi(false))
    val value = cartItemData

    var doesBillingCollapse by remember {
        mutableStateOf(false)
    }

    val rupees = stringResource(id = R.string.ruppes)
    when (value) {
        is OrderSummaryUi.SummarySuccess -> {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color = colorResource(id = R.color.white), shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 24.dp)) {

                Row() {
                    Text(text = "Bill Summary",
                        color = colorResource(id = R.color.app_black),
                        style = Typography_Montserrat.body1)
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(painter = painterResource(id = R.drawable.down_arrow), contentDescription = "down_arrow", modifier = Modifier.clickable {
                        doesBillingCollapse = !doesBillingCollapse
                    })
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (doesBillingCollapse) {
                    Row() {
                        Text(
                            fontWeight = FontWeight.W500,
                            color = colorResource(id = R.color.black_4),
                            fontFamily = montserrat, fontSize = 12.sp,
                            text = "Item price")
                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            fontWeight = FontWeight.W500,
                            color = colorResource(id = R.color.new_hint_color),
                            fontFamily = montserrat, fontSize = 12.sp,
                            text = rupees + ProductUtils.roundTo1DecimalPlaces(value.data.totalCurrentPrice))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row() {
                        Text(
                            fontWeight = FontWeight.W500,
                            fontFamily = montserrat, fontSize = 12.sp,
                            color = colorResource(id = R.color.black_4),
                            text = "Delivery Charges")
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            fontWeight = FontWeight.W500,
                            color = colorResource(id = R.color.new_hint_color),
                            fontFamily = montserrat, fontSize = 12.sp,
                            text = rupees + "0")
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    if (value.data.deliveryPartnerTip != 0) {
                        Row() {
                            Text(
                                fontWeight = FontWeight.W500,
                                fontFamily = montserrat, fontSize = 12.sp,
                                color = colorResource(id = R.color.black_4),
                                text = "Delivery Tip")
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                fontWeight = FontWeight.W500,
                                color = colorResource(id = R.color.new_hint_color),
                                fontFamily = montserrat, fontSize = 12.sp,
                                text = rupees + value.data.deliveryPartnerTip)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        fontWeight = FontWeight.W500,
                        modifier = Modifier
                            .background(color = colorResource(id = R.color.new_material_primary),
                                shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        color = colorResource(id = R.color.white),
                        fontFamily = montserrat, fontSize = 12.sp,
                        text = "Free delivery above " + stringResource(id = R.string.ruppes) + "149")

                } else {
                    AnimatedVisibility(visible = true) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row() {
                            Text(
                                fontWeight = FontWeight.W500,
                                fontFamily = montserrat, fontSize = 12.sp,
                                color = colorResource(id = R.color.new_material_primary),
                                text = "Total bill")
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                fontWeight = FontWeight.W600,
                                color = colorResource(id = R.color.new_material_primary),
                                fontFamily = montserrat, fontSize = 12.sp,
                                text = rupees + ProductUtils.roundTo1DecimalPlaces(value.data.totalCurrentPrice+value.data.deliveryPartnerTip))
                        }
                    }

                }
            }
        }
    }
}