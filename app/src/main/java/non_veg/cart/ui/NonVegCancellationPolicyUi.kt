package non_veg.cart.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import non_veg.cart.viewmodel.NonVegCartViewModel

@Composable
fun NonVegCancellationPolicyUi(nonVegCartViewModel: NonVegCartViewModel = viewModel()) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(color = colorResource(id = R.color.white))) {
        val (headers, valueText) = createRefs()

        Text(text = "Cancellation Policy", modifier = Modifier.constrainAs(headers) {
            top.linkTo(parent.top, dp_12)
            start.linkTo(parent.start, dp_16)
            end.linkTo(parent.end, dp_16)
            width = Dimension.fillToConstraints
        }, style = ZustTypography.h1)

        Text(text = nonVegCartViewModel.getCancellationTerms(), modifier = Modifier.constrainAs(valueText) {
            top.linkTo(headers.bottom, dp_12)
            start.linkTo(parent.start, dp_16)
            end.linkTo(parent.end, dp_16)
            bottom.linkTo(parent.bottom, dp_12)
            width = Dimension.fillToConstraints
        }, style = ZustTypography.body2, color = colorResource(id = R.color.light_black))

    }

}