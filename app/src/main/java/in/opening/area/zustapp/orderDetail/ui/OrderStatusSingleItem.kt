package `in`.opening.area.zustapp.orderDetail.ui

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.orderDetail.models.OrderStatus
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_8
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import java.util.*

@Composable
fun OrderStatusTitleContainer() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            )
    ) {
        val (title) = createRefs()
        Text(text = "Order status",
            style = ZustTypography.body1,
            color = colorResource(id = R.color.app_black),
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, dp_16)
                bottom.linkTo(parent.bottom, dp_12)
                start.linkTo(parent.start, dp_16)
                end.linkTo(parent.end, dp_16)
                width = Dimension.fillToConstraints
            })

    }
}


@Composable
fun OrderStatusBottomContainer() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp)
            )
    ) {
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun OrderStatusSingleItem(orderStatus: OrderStatus, index: Int, size: Int) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
    ) {
        val (orderStatusTag, orderTime, divider, statusIcon) = createRefs()

        if (!orderStatus.createdDateTime.isNullOrEmpty()) {
            Icon(
                painter = painterResource(id = R.drawable.ic_outline_check_circle_outline_24),
                tint = colorResource(id = R.color.light_green), contentDescription = "completed",
                modifier = Modifier
                    .size(16.dp)
                    .constrainAs(statusIcon) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start, dp_12)
                        bottom.linkTo(divider.top)
                    },
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_outline_circle_24),
                contentDescription = "completed",
                modifier = Modifier
                    .size(16.dp)
                    .constrainAs(statusIcon) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start, dp_12)
                        bottom.linkTo(divider.top)
                    },
            )
        }

        Text(text = orderStatus.orderStatusType?.firstLetterCapitalOtherSmall() ?: "",
            style = ZustTypography.body2,
            color = if (!orderStatus.createdDateTime.isNullOrEmpty()) {
                colorResource(id = R.color.app_black)
            } else {
                Color(0x801F1F1F)
            },
            modifier = Modifier.constrainAs(orderStatusTag) {
                top.linkTo(statusIcon.top)
                start.linkTo(statusIcon.end, dp_8)
                bottom.linkTo(parent.bottom, dp_16)
            })

        Text(text = if (orderStatus.createdDateTime != "-1") {
            orderStatus.createdDateTime
        } else {
            ""
        } ?: "",
            style = ZustTypography.subtitle1,
            color = colorResource(id = R.color.new_hint_color),
            modifier = Modifier.constrainAs(orderTime) {
                start.linkTo(orderStatusTag.end, dp_8)
                end.linkTo(parent.end, dp_12)
                top.linkTo(orderStatusTag.top)
                bottom.linkTo(orderStatusTag.bottom)
                width = Dimension.fillToConstraints
            }, fontSize = 12.sp, maxLines = 1
        )

        if (index != size - 1) {
            Divider(modifier = Modifier
                .width(1.dp)
                .background(color = Color.LightGray)
                .constrainAs(divider) {
                    top.linkTo(statusIcon.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(statusIcon.start)
                    end.linkTo(statusIcon.end)
                    height = Dimension.fillToConstraints
                })
        }

    }
}

@Composable
fun CircleInCircle(modifier: Modifier) {
    val outerCircleColor = Color(0xff8DA450)
    val innerCircleColor = outerCircleColor.copy(0.77f)
    val strokeWidth = 8.dp.value

    Canvas(
        modifier = modifier
    ) {
        val radius = size.minDimension / 2 - strokeWidth / 2
        val center = Offset(size.width / 2, size.height / 2)

        drawCircle(
            brush = Brush.linearGradient(
                colors = listOf(outerCircleColor, outerCircleColor),
                start = Offset(0f, 0f),
                end = Offset(0f, size.height)
            ),
            radius = radius,
            center = center,
            style = Stroke(strokeWidth)
        )

        drawCircle(
            brush = Brush.linearGradient(
                colors = listOf(innerCircleColor, innerCircleColor),
                start = Offset(0f, 0f),
                end = Offset(0f, size.height)
            ),
            radius = radius / 2,
            center = center,
            style = Stroke(strokeWidth)
        )
    }
}

val materialColor = Color(0xff6750A4)

@Composable
fun OutlineCircle(modifier: Modifier) {
    Canvas(
        modifier = modifier
    ) {
        val radius = size.minDimension / 4
        val center = Offset(size.width / 2, size.height / 2)
        drawCircle(
            color = materialColor,
            radius = radius,
            center = center,
            style = Fill
        )
    }
}

fun String.firstLetterCapitalOtherSmall(): String {
    return this.lowercase().capitalize(Locale.ROOT)
}
