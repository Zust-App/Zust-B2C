package zustbase.orderDetail.ui

import `in`.opening.area.zustapp.R
import zustbase.orderDetail.models.OrderDetailData
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.utility.ProductUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
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
            Text(text = "Billing Details", style = ZustTypography.bodyLarge)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = ("Paid via " + data.paymentMethod),
                color = colorResource(id = R.color.light_green),
                style = ZustTypography.bodyMedium,
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
                    style = ZustTypography.bodyMedium,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.black_4))
                Spacer(Modifier.weight(1f))
                Text(text = stringResource(id = R.string.ruppes) + ProductUtils.roundTo1DecimalPlaces(data.itemTotalPrice),
                    style = ZustTypography.bodyMedium,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.light_green))
            }
            Spacer(modifier = Modifier.height(4.dp))

            Row {
                Text(text = "Packaging fee",
                    style = ZustTypography.bodyMedium,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.black_4))
                Spacer(Modifier.weight(1f))
                Text(text = stringResource(id = R.string.ruppes) + ProductUtils.roundTo1DecimalPlaces(data.packagingFee),
                    style = ZustTypography.bodyMedium,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.light_green))
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(text = "Delivery charge",
                    style = ZustTypography.bodyMedium,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.black_4))
                Spacer(Modifier.weight(1f))
                Text(text = stringResource(id = R.string.ruppes) + ProductUtils.roundTo1DecimalPlaces(data.deliveryFee),
                    style = ZustTypography.bodyMedium,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.light_green))
            }
            Spacer(modifier = Modifier.height(4.dp))
            if (data.deliveryPartnerTip != null && data.deliveryPartnerTip > 0.0) {
                Row {
                    Text(text = "Delivery Partner Tip",
                        style = ZustTypography.bodyMedium,
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.black_4))
                    Spacer(Modifier.weight(1f))
                    Text(text = stringResource(id = R.string.ruppes) + ProductUtils.roundTo1DecimalPlaces(data.deliveryPartnerTip),
                        style = ZustTypography.bodyMedium,
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.light_green))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))

            Row {
                Text(text = "Discount",
                    style = ZustTypography.bodyMedium,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.black_4))
                Spacer(Modifier.weight(1f))
                Text(text = "- " + stringResource(id = R.string.ruppes) + ProductUtils.roundTo1DecimalPlaces(data.couponDiscountPrice),
                    style = ZustTypography.bodyMedium,
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
                style = ZustTypography.bodyMedium,
                fontSize = 12.sp,
                color = colorResource(id = R.color.new_material_primary))

            Spacer(Modifier.weight(1f))
            if (data.updatedTotalPrice != null && data.updatedTotalPrice != 0.0) {
                Text(text = stringResource(id = R.string.ruppes) + ProductUtils.roundTo1DecimalPlaces(totalPrice),
                    style = ZustTypography.bodyMedium.copy(textDecoration = TextDecoration.LineThrough),
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.new_material_primary))
            } else {
                Text(text = stringResource(id = R.string.ruppes) + ProductUtils.roundTo1DecimalPlaces(totalPrice),
                    style = ZustTypography.bodyMedium,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.new_material_primary))
            }
        }
        if (data.updatedTotalPrice != null && data.updatedTotalPrice != 0.0) {
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(text = "Updated Price",
                    style = ZustTypography.bodyMedium,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.red_secondary))

                Spacer(Modifier.weight(1f))

                Text(text = stringResource(id = R.string.ruppes) + ProductUtils.roundTo1DecimalPlaces(totalPrice),
                    style = ZustTypography.bodyMedium,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.red_secondary))
            }
        }
    }
}