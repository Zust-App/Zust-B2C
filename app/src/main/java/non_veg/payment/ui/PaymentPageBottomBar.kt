package non_veg.payment.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.dp_8
import non_veg.payment.viewModels.NonVegPaymentViewModel


@Composable
fun PaymentPageBottomBar(nonVegPaymentViewModel: NonVegPaymentViewModel = viewModel(), onPlaceOrderClick: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .shadow(elevation = dp_16,
            clip = true,
            shape = RoundedCornerShape(topStart = dp_8, topEnd = dp_8))) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.white))
                .padding(horizontal = dp_16, vertical = dp_8)
        ) {
            val (tvPayTag, totalPayableAmountTv, placeOrderTvTag) = createRefs()
            Text(
                text = "Total",
                modifier = Modifier
                    .constrainAs(tvPayTag) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                style = ZustTypography.body2,
                fontSize = 12.sp,
                color = colorResource(id = R.color.light_black)
            )
            val totalPayableAmount = ((nonVegPaymentViewModel.cartSummaryData?.itemValueInCart ?: 0.0) +
                    (nonVegPaymentViewModel.cartSummaryData?.serviceFee ?: 0.0) +
                    (nonVegPaymentViewModel.cartSummaryData?.packagingFee ?: 0.0)
                    + (nonVegPaymentViewModel.cartSummaryData?.deliveryFee ?: 0.0))

            Text(
                text = stringResource(id = R.string.ruppes) + totalPayableAmount,
                modifier = Modifier
                    .constrainAs(totalPayableAmountTv) {
                        top.linkTo(tvPayTag.bottom)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    },
                style = ZustTypography.body1,
                fontSize = 16.sp,
                color = colorResource(id = R.color.black)
            )

            Button(onClick = {
                onPlaceOrderClick.invoke()
            },
                modifier = Modifier.constrainAs(placeOrderTvTag) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.new_material_primary))) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.pay_now),
                        style = ZustTypography.body1,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.white)
                    )
                    Spacer(modifier = Modifier.width(dp_12))
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_right_icon),
                        contentDescription = null,
                        tint = colorResource(id = R.color.white)
                    )
                }
            }

        }
    }
}

