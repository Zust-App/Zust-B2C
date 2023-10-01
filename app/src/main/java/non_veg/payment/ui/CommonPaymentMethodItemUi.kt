package non_veg.payment.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography

@Composable
fun CommonPaymentMethodItemUi(
    modifier: Modifier,
    paymentKey: String,
    paymentMethodName: String,
    isSelected: Boolean,
    onItemSelected: () -> Unit,
) {
    ConstraintLayout(
        modifier = modifier
            .clickable {
                onItemSelected.invoke()
            }) {
        val (paymentIcon, paymentText) = createRefs()
        AsyncImage(
            model = "https://delasign.com/delasignBlack.png",
            placeholder = painterResource(id = R.drawable.baseline_image_24),
            error = painterResource(id = R.drawable.baseline_image_24),
            contentDescription = paymentKey,
            modifier = Modifier
                .constrainAs(paymentIcon) {
                    top.linkTo(parent.top)
                    start.linkTo(paymentText.start)
                    end.linkTo(paymentText.end)
                }
                .size(36.dp)
        )

        Text(
            text = paymentMethodName,
            color = colorResource(id = R.color.language_default),
            modifier = Modifier.constrainAs(paymentText) {
                top.linkTo(paymentIcon.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            },
            style = ZustTypography.bodyMedium
        )
    }
}
