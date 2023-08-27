package zustbase.orderDetail.ui

import `in`.opening.area.zustapp.R
import zustbase.orderDetail.models.ZustAddress
import zustbase.orderDetail.models.OrderDetailData
import `in`.opening.area.zustapp.ui.theme.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun DeliveryAddressContainer(data: OrderDetailData?) {
    if (data?.address == null) {
        return
    }
    val zustAddress: ZustAddress = data.address
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.White, shape = RoundedCornerShape(8.dp))
        .wrapContentHeight()) {
        val (titleTag, addressTag, deliveryIcon) = createRefs()

        Text(text = "Delivery to", modifier = Modifier.constrainAs(titleTag) {
            start.linkTo(parent.start, dp_12)
            end.linkTo(parent.end, dp_12)
            top.linkTo(parent.top, dp_12)
            bottom.linkTo(addressTag.top)
            width = Dimension.fillToConstraints
        }, style = ZustTypography.bodyMedium, color = colorResource(id = R.color.app_black))

        Text(text = buildString {
            append(zustAddress.houseNumberAndFloor)
            append(" ")
            if (!zustAddress.landmark.equals("no",ignoreCase = true)) {
                append(zustAddress.landmark)
            }
            append(" ")
            append(zustAddress.description)
        }, modifier = Modifier.constrainAs(addressTag) {
            start.linkTo(parent.start, dp_12)
            end.linkTo(parent.end, dp_12)
            bottom.linkTo(parent.bottom, dp_8)
            top.linkTo(titleTag.bottom, dp_6)
            width = Dimension.fillToConstraints
        }, style = ZustTypography.bodySmall,
            fontSize = 14.sp,
            color = colorResource(id = R.color.new_hint_color))
    }
}