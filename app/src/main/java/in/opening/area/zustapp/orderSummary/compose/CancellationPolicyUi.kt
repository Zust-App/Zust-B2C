package `in`.opening.area.zustapp.orderSummary.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.extensions.collectAsStateLifecycleAware
import `in`.opening.area.zustapp.orderSummary.model.CancellationPolicyUiModel
import `in`.opening.area.zustapp.ui.theme.okraFontFamily
import `in`.opening.area.zustapp.viewmodels.OrderSummaryViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_8

@Composable
fun CancellationPolicyUi(orderSummaryViewModel: OrderSummaryViewModel) {
    val cancellationPolicyData by orderSummaryViewModel.cancellationPolicyCacheData.collectAsStateLifecycleAware(initial = CancellationPolicyUiModel.InitialUi(false))

    when (cancellationPolicyData) {
        is CancellationPolicyUiModel.CancellationPolicyUiSuccess -> {
            ConstraintLayout(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = colorResource(id = R.color.white), shape = RoundedCornerShape(dp_8))) {
                val (headers, valueText) = createRefs()

                Text(text = "Cancellation Policy", modifier = Modifier.constrainAs(headers) {
                    top.linkTo(parent.top, dp_12)
                    start.linkTo(parent.start, dp_16)
                    end.linkTo(parent.end, dp_16)
                    width = Dimension.fillToConstraints
                }, style = ZustTypography.titleMedium)

                Text(text = "Orders cannot be cancelled once packed for delivery. In case of unexpected delays, a refund will be provided", modifier = Modifier.constrainAs(valueText) {
                    top.linkTo(headers.bottom, dp_12)
                    start.linkTo(parent.start, dp_16)
                    end.linkTo(parent.end, dp_16)
                    bottom.linkTo(parent.bottom, dp_12)
                    width = Dimension.fillToConstraints
                }, style = ZustTypography.bodyMedium, color = colorResource(id = R.color.language_default))

            }
        }

        is CancellationPolicyUiModel.ErrorUi -> {

        }

        is CancellationPolicyUiModel.InitialUi -> {

        }
    }
}