package non_veg.payment.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
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
import non_veg.payment.viewModels.NonVegPaymentViewModel


@Composable
fun PaymentPageBottomBar(nonVegPaymentViewModel: NonVegPaymentViewModel = viewModel(), onPlaceOrderClick: (String?) -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                color = colorResource(id = R.color.new_material_primary))
            .clipToBounds()
            .zIndex(4f)
            .clickable {
                onPlaceOrderClick.invoke(nonVegPaymentViewModel.selectedPaymentKey)
            }
    ) {
        val (orderPlaceContainer, tvPayTag, totalPayableAmountTv, placeOrderTvTag) = createRefs()
        Text(
            text = "Total",
            modifier = Modifier
                .padding(start = 20.dp, top = 6.dp, end = 8.dp)
                .constrainAs(tvPayTag) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
            style = ZustTypography.body2,
            fontSize = 12.sp,
            color = Color.White
        )
        val totalPayableAmount = ((nonVegPaymentViewModel.cartSummaryData?.itemValueInCart ?: 0.0) +
                (nonVegPaymentViewModel.cartSummaryData?.serviceFee ?: 0.0) +
                (nonVegPaymentViewModel.cartSummaryData?.packagingFee ?: 0.0)
                + (nonVegPaymentViewModel.cartSummaryData?.deliveryFee ?: 0.0))

        Text(
            text = stringResource(id = R.string.ruppes) + totalPayableAmount,
            modifier = Modifier
                .padding(start = 20.dp, top = 2.dp, end = 8.dp, bottom = 6.dp)
                .constrainAs(totalPayableAmountTv) {
                    top.linkTo(tvPayTag.bottom)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                },
            style = ZustTypography.body1,
            fontSize = 16.sp,
            color = Color.White
        )

        Row(
            modifier = Modifier
                .padding(end = 16.dp)
                .constrainAs(placeOrderTvTag) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            Text(
                text = stringResource(R.string.pay_now),
                style = ZustTypography.body1,
                fontSize = 16.sp,
                color = Color.White
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

