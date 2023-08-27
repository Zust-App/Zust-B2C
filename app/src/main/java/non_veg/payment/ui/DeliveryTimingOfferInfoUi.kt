package non_veg.payment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.payment.models.PaymentActivityReqData
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_8

@Composable
fun DeliveryTimingOfferInfoUi(expectedDeliveryTime: String?,price:Int?=99) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(start = dp_16, end = dp_16, top = dp_12)
        .clip(shape = RoundedCornerShape(dp_8))
        .background(color = colorResource(id = R.color.light_green))
        .padding(horizontal = dp_16, vertical = dp_12)) {
        Text(text = expectedDeliveryTime ?: "You will receive your order within shortly",
            style = ZustTypography.titleMedium, color = colorResource(id = R.color.white))
        ViewSpacer8()
        Text(text = "Free delivery above ${(((stringResource(id = R.string.ruppes) + (price?:99)))) }",
            style = ZustTypography.bodyMedium, color = colorResource(id = R.color.light))
    }
}