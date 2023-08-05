package non_veg.payment.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NonVegPaymentBillContainer(text: String, amount: Double) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
        Text(text = text)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = amount.toInt().toString())
    }
}