package `in`.opening.area.zustapp.orderDetail.ui

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.orderDetail.models.OrderDetailData
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import `in`.opening.area.zustapp.utility.ProductUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BillContainer(data: OrderDetailData?) {
    if (data == null) {
        return
    }
    var isExpanded by remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier
        .background(color = Color.White,
            shape = RoundedCornerShape(8.dp))
        .padding(12.dp)) {

        Row {
            Text(text = "Billing Details", style = Typography_Montserrat.body1)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = ("Paid via " + data.paymentMethod),
                color = colorResource(id = R.color.light_green),
                style = Typography_Montserrat.body2,
                fontSize = 12.sp)
            Spacer(modifier = Modifier.weight(1f))
            Icon(painter = painterResource(id = R.drawable.down_arrow),
                contentDescription = "arrow", modifier = Modifier.clickable {
                    isExpanded = !isExpanded
                })
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (isExpanded) {
            Row {
                Text(text = "Item Fee",
                    style = Typography_Montserrat.body2,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.black_4))
                Spacer(Modifier.weight(1f))
                Text(text = stringResource(id = R.string.ruppes) + ProductUtils.roundTo1DecimalPlaces(data.itemTotalPrice),
                    style = Typography_Montserrat.body2,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.light_green))
            }
            Spacer(modifier = Modifier.height(4.dp))

            Row {
                Text(text = "Packaging fee",
                    style = Typography_Montserrat.body2,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.black_4))
                Spacer(Modifier.weight(1f))
                Text(text = stringResource(id = R.string.ruppes) + ProductUtils.roundTo1DecimalPlaces(data.packagingFee),
                    style = Typography_Montserrat.body2,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.light_green))
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(text = "Delivery charge",
                    style = Typography_Montserrat.body2,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.black_4))
                Spacer(Modifier.weight(1f))
                Text(text = stringResource(id = R.string.ruppes) + ProductUtils.roundTo1DecimalPlaces(data.deliveryFee),
                    style = Typography_Montserrat.body2,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.light_green))
            }
            Spacer(modifier = Modifier.height(4.dp))
            if (data.deliveryPartnerTip != null && data.deliveryPartnerTip > 0.0) {
                Row {
                    Text(text = "Delivery Partner Tip",
                        style = Typography_Montserrat.body2,
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.black_4))
                    Spacer(Modifier.weight(1f))
                    Text(text = stringResource(id = R.string.ruppes) + ProductUtils.roundTo1DecimalPlaces(data.deliveryPartnerTip),
                        style = Typography_Montserrat.body2,
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.light_green))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))

            Row {
                Text(text = "Discount",
                    style = Typography_Montserrat.body2,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.black_4))
                Spacer(Modifier.weight(1f))
                Text(text = "- " + stringResource(id = R.string.ruppes) + ProductUtils.roundTo1DecimalPlaces(data.couponDiscountPrice),
                    style = Typography_Montserrat.body2,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.light_green))

            }
            Spacer(modifier = Modifier.height(4.dp))
            Divider(
                color = Color.LightGray,
                modifier = Modifier
                    .fillMaxWidth()
                    .width(1.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        val totalPrice = data.itemTotalPrice +
                data.deliveryFee +
                data.packagingFee +
                (data.deliveryPartnerTip ?: 0.0) - data.couponDiscountPrice
        Row {
            Text(text = "Total bill",
                style = Typography_Montserrat.body2,
                fontSize = 12.sp,
                color = colorResource(id = R.color.new_material_primary))

            Spacer(Modifier.weight(1f))

            Text(text = stringResource(id = R.string.ruppes) + ProductUtils.roundTo1DecimalPlaces(totalPrice),
                style = Typography_Montserrat.body2,
                fontSize = 14.sp,
                color = colorResource(id = R.color.new_material_primary))
        }

    }
}