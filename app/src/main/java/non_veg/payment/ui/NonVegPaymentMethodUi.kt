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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_24
import `in`.opening.area.zustapp.ui.theme.dp_32
import `in`.opening.area.zustapp.ui.theme.dp_8

@Composable
fun NonVegPaymentMethodUi(
    paymentKey: String,
    paymentMethodName: String,
    isSelected: Boolean,
    onItemSelected: () -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.white))
            .clickable {
                onItemSelected.invoke()
            }) {
        val (paymentIcon, paymentText, radioIcon) = createRefs()
        Image(
            painter = painterResource(id = getPaymentMethodIcon(paymentKey)),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(paymentIcon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
                .size(30.dp)
        )

        Text(
            text = paymentMethodName,
            color = colorResource(id = R.color.language_default),
            modifier = Modifier.constrainAs(paymentText) {
                top.linkTo(parent.top)
                start.linkTo(paymentIcon.end, dp_12)
                bottom.linkTo(parent.bottom)
            },
            style = ZustTypography.bodyMedium
        )

        RadioButton(
            selected = isSelected,
            onClick = onItemSelected,
            colors = RadioButtonDefaults.colors(selectedColor = Color.Black), modifier = Modifier.constrainAs(radioIcon) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }
        )
    }
}

private fun getPaymentMethodIcon(paymentKey: String): Int {
    when (paymentKey) {
        "upi" -> {
            return R.drawable.upi_pay_mode
        }

        "debit", "credit" -> {
            return R.drawable.card_pay_mode
        }

        "netbanking" -> {
            return R.drawable.net_bank_pay_mode
        }

        "cod" -> {
            return R.drawable.card_pay_mode
        }

        "rapid" -> {
            return R.drawable.rapid_bazar_icon
        }

        else -> {
            return R.drawable.upi_pay_mode
        }
    }

}