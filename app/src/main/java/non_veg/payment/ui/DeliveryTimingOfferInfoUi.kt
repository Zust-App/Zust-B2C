package non_veg.payment.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DeliveryTimingOfferInfoUi() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
        Text(text = "Your order will be deliver in 2hrs")
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Free Delivery Eligible")
    }
}