package `in`.opening.area.zustapp.coupon

import `in`.opening.area.zustapp.R.color
import `in`.opening.area.zustapp.R.drawable
import `in`.opening.area.zustapp.coupon.model.Coupon
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_8
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension


@Composable
fun CouponItemView(item: Coupon, modifier: Modifier, couponItemClickListener: CouponItemClickListener) {
    ConstraintLayout(modifier = modifier
        .fillMaxWidth()
        .background(color = Color.White,
            shape = RoundedCornerShape(8.dp))
        .wrapContentHeight()) {
        val (couponIcon, couponName, descriptionText, couponCodeContainer, applyText) = createRefs()

        Icon(painter = painterResource(id = drawable.offer_icon),
            contentDescription = "coupon", tint = colorResource(id = color.new_material_primary),
            modifier = Modifier
                .size(20.dp)
                .constrainAs(couponIcon) {
                    top.linkTo(parent.top, dp_16)
                    start.linkTo(parent.start, dp_16)
                })

        Text(text = item.couponName,
            style = ZustTypography.body1,
            modifier = modifier.constrainAs(couponName) {
                top.linkTo(parent.top, dp_16)
                start.linkTo(couponIcon.end, dp_8)
                end.linkTo(applyText.start)
                width = Dimension.fillToConstraints
            }, color = Color(0xD91E1E1E))

        Text(text = item.description,
            style = ZustTypography.subtitle1,
            color = Color(0xBF1E1E1E),
            modifier = Modifier.constrainAs(descriptionText) {
                start.linkTo(couponName.start)
                end.linkTo(parent.end, dp_16)
                top.linkTo(couponName.bottom, dp_8)
                width = Dimension.fillToConstraints
            })

        Box(
            modifier = modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .border(width = 1.dp, color = Color(0xffD6C1F8),
                    shape = RoundedCornerShape(4.dp))
                .constrainAs(couponCodeContainer) {
                    start.linkTo(couponName.start)
                    top.linkTo(descriptionText.bottom, dp_8)
                    bottom.linkTo(parent.bottom, dp_16)
                }
                .clickable {
                    couponItemClickListener.didTapOnApply(item)
                },
            contentAlignment = Alignment.Center,
        ) {
            Text(text = item.couponCode,
                color = Color(0xff7B2AFF),
                style = ZustTypography.body2,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp))

        }

        Text(text = "APPLY",
            color = colorResource(id = color.new_material_primary),
            style = ZustTypography.body1,
            fontSize = 14.sp,
            modifier = modifier
                .constrainAs(applyText) {
                    end.linkTo(parent.end, dp_16)
                    top.linkTo(parent.top, dp_16)
                    start.linkTo(couponName.end, dp_16)
                }
                .clickable {
                    couponItemClickListener.didTapOnApply(item)
                })

    }
}