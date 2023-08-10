package non_veg.payment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.opening.area.zustapp.R
import zustbase.orderDetail.models.convertAsStringText
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_24
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.ui.theme.zustTypographySecondary
import non_veg.payment.viewModels.NonVegPaymentViewModel

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
    ) {
        val (locationIcon, addressTitleTag, addressTextView) = createRefs()

        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            modifier = Modifier
                .size(dp_24)
                .constrainAs(locationIcon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )

        Text(
            text = "Delivery Address",
            style = zustTypographySecondary.h1,
            modifier = Modifier.constrainAs(addressTitleTag) {
                top.linkTo(locationIcon.top)
                start.linkTo(locationIcon.end, dp_8)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )

        Text(
            text = deliveryAddress,
            style = ZustTypography.body2,
            modifier = Modifier.constrainAs(addressTextView) {
                top.linkTo(addressTitleTag.bottom, dp_12)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
    }
}

