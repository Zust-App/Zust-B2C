package non_veg.payment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.zustTypographySecondary

@Composable
fun DeliveryTimingOfferInfoUi() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(color = colorResource(id = R.color.white))
        .padding(horizontal = dp_16, vertical = dp_12)) {
        Text(text = "Your order will be deliver in 2hrs",
            style = zustTypographySecondary.h1)
        ViewSpacer8()
        Text(text = "Free Delivery Eligible",
            style = ZustTypography.body2)
    }
}