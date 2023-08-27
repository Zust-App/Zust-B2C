package zustbase.orderDetail.ui

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_4
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ui.colorWhite
import zustbase.orderDetail.OrderDetailActivity


@Composable
fun OrderDetailsTopAppBar(
    modifier: Modifier,
    orderId: Int?,
    color: Color,
    intentSource: String?,
    callback: (ACTION) -> Unit,
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 16.dp, horizontal = 16.dp),
    ) {
        val (locationTag, navIcon, subTitle) = createRefs()

        Text(text = "Order Details", color = color,
            modifier = modifier.constrainAs(locationTag) {
                top.linkTo(parent.top)
                start.linkTo(navIcon.end, dp_12)
                end.linkTo(parent.end)
                bottom.linkTo(subTitle.top)
                width = Dimension.fillToConstraints
            }, style = ZustTypography.titleMedium,
            fontSize = 16.sp)

        Text(text = "Order number - " + if (intentSource == INTENT_SOURCE_NON_VEG) {
            PREFIX_ORDER_ID_NON_VEG + orderId
        } else {
            PREFIX_ORDER_ID_GROCERY + orderId
        },
            color = color,
            modifier = modifier.constrainAs(subTitle) {
                top.linkTo(locationTag.bottom, dp_4)
                bottom.linkTo(parent.bottom)
                start.linkTo(navIcon.end, dp_12)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }, style = ZustTypography.bodySmall,
            fontWeight = FontWeight.W500)

        Icon(painter = painterResource(id = R.drawable.app_nav_arrow),
            tint = color,
            contentDescription = "back arrow", modifier = modifier
                .constrainAs(navIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .clickable {
                    callback.invoke(ACTION.NAV_BACK)
                }
                .clip(shape = RoundedCornerShape(8.dp)))
    }
}