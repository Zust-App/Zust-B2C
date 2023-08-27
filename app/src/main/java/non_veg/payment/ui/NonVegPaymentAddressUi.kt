package non_veg.payment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_20
import `in`.opening.area.zustapp.ui.theme.dp_24
import `in`.opening.area.zustapp.ui.theme.dp_8
import non_veg.payment.viewModels.NonVegPaymentViewModel
import zustbase.orderDetail.models.convertAsStringText

@Composable
fun NonVegPaymentAddressUi(nonVegPaymentViewModel: NonVegPaymentViewModel = viewModel()) {
    val deliveryAddress = remember {
        nonVegPaymentViewModel.getLatestAddress()?.convertAsStringText() ?: ""
    }

    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(color = colorResource(id = R.color.white), shape = RoundedCornerShape(dp_8))
            .padding(horizontal = dp_16, vertical = dp_16)
            .fillMaxWidth()
    ) {
        val (locationIcon, addressTitleTag, addressTextView) = createRefs()

        Icon(
            painter = painterResource(id = R.drawable.home_location_pin_icon),
            contentDescription = null,
            modifier = Modifier
                .size(dp_20)
                .constrainAs(locationIcon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )

        Text(
            text = "Delivery Address",
            style = ZustTypography.titleMedium, color =
            colorResource(id = R.color.app_black),
            modifier = Modifier.constrainAs(addressTitleTag) {
                top.linkTo(locationIcon.top)
                start.linkTo(locationIcon.end, dp_8)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )

        Text(
            text = deliveryAddress,
            style = ZustTypography.bodyMedium, color =
            colorResource(id = R.color.language_default),
            modifier = Modifier.constrainAs(addressTextView) {
                top.linkTo(addressTitleTag.bottom, dp_12)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )
    }
}

