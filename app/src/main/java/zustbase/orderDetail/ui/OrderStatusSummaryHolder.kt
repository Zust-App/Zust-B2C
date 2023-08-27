package zustbase.orderDetail.ui

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.ComposeLottieWithoutScope
import zustbase.orderDetail.models.OrderDetailData
import `in`.opening.area.zustapp.ui.theme.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun OrderStatusSummaryHolder(data: OrderDetailData?) {
    if (data == null) {
        return
    }
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(color = colorResource(id = R.color.yellow),
            shape = RoundedCornerShape(8.dp))) {
        val (imageIcon, summaryText) = createRefs()
        ComposeLottieWithoutScope(rawId = R.raw.box,
            modifier = Modifier
                .width(80.dp)
                .height(66.dp)
                .constrainAs(imageIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start, dp_12)
                })

        Text(text = data.expectedTimeToDelivery ?: "",
            style = ZustTypography.bodyMedium,
            fontWeight = FontWeight.W700,
            fontSize = 14.sp,
            color = colorResource(id = R.color.new_material_primary),
            modifier = Modifier.constrainAs(summaryText) {
                top.linkTo(parent.top, dp_20)
                bottom.linkTo(parent.bottom, dp_20)
                start.linkTo(imageIcon.end)
                end.linkTo(parent.end, dp_8)
                width = Dimension.fillToConstraints
            })
    }
}
