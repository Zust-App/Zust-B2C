package non_veg.payment.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.dp_8

@Composable
fun CommonPaymentMethodItemUi(
    modifier: Modifier,
    paymentKey: String,
    paymentMethodName: String,
    thumbnail: String,
    isSelected: Boolean,
    isEnabled: Boolean,
    onItemSelected: () -> Unit,
) {
    ConstraintLayout(
        modifier = modifier
            .clickable {
                onItemSelected.invoke()
            }) {
        val (paymentIcon, paymentText) = createRefs()
        AsyncImage(
            model = thumbnail,
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
                .border(
                    width = 2.dp,
                    color = if (isSelected) colorResource(id = R.color.language_default) else Color.Transparent,
                    shape = RoundedCornerShape(dp_4)
                ).padding(dp_4))

        Text(
            text = paymentMethodName,
            color = if (isSelected) {
                colorResource(id = R.color.black_3)
            } else {
                colorResource(id = R.color.language_default)
            },
            modifier = Modifier.constrainAs(paymentText) {
                top.linkTo(paymentIcon.bottom, dp_4)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            },
            style = ZustTypography.bodyMedium
        )
    }
}


@Composable
fun CommonPaymentMethodItemUiV2(
    modifier: Modifier,
    paymentKey: String,
    paymentMethodName: String,
    thumbnail: String,
    isSelected: Boolean,
    isEnabled: Boolean,
    onItemSelected: () -> Unit,
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onItemSelected.invoke()
            }) {
        val (paymentIcon, paymentText, radioButton) = createRefs()
        AsyncImage(
            model = thumbnail,
            placeholder = painterResource(id = R.drawable.baseline_image_24),
            error = painterResource(id = R.drawable.baseline_image_24),
            contentDescription = paymentKey,
            modifier = Modifier
                .constrainAs(paymentIcon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(paymentText.start)
                    bottom.linkTo(parent.bottom)
                }
                .size(32.dp)
        )

        Text(
            text = paymentMethodName,
            color = if (isSelected) {
                colorResource(id = R.color.black_3)
            } else {
                colorResource(id = R.color.language_default)
            },
            modifier = Modifier.constrainAs(paymentText) {
                top.linkTo(parent.top)
                start.linkTo(paymentIcon.end, dp_12)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
            },
            style = ZustTypography.bodyMedium
        )

        RadioButton(selected = isSelected, onClick = {
            onItemSelected.invoke()
        }, modifier = Modifier.constrainAs(radioButton) {
            top.linkTo(parent.top)
            end.linkTo(parent.end, dp_8)
            bottom.linkTo(parent.bottom)
        })
    }
}
