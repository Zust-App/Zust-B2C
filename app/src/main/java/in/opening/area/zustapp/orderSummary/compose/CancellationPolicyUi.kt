package `in`.opening.area.zustapp.orderSummary.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.extensions.collectAsStateLifecycleAware
import `in`.opening.area.zustapp.orderSummary.model.CancellationPolicyUiModel
import `in`.opening.area.zustapp.ui.theme.montserrat
import `in`.opening.area.zustapp.ui.theme.okraFontFamily
import `in`.opening.area.zustapp.viewmodels.OrderSummaryViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CancellationPolicyUi(orderSummaryViewModel: OrderSummaryViewModel) {
    val cancellationPolicyData by orderSummaryViewModel.cancellationPolicyCacheData.collectAsStateLifecycleAware(initial = CancellationPolicyUiModel.InitialUi(false))

    when (cancellationPolicyData) {
        is CancellationPolicyUiModel.CancellationPolicyUiSuccess -> {
            Column(modifier = Modifier
                .padding(vertical = 10.dp)
                .background(color = colorResource(id = R.color.white), shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 16.dp)) {
                Text(
                    fontWeight = FontWeight.W500,
                    fontFamily = okraFontFamily,
                    color = colorResource(id = R.color.new_hint_color),
                    fontSize = 12.sp,
                    text = "Orders cannot be cancelled once packed for delivery. In case of unexpected delays, a refund will be provided")
            }
        }
        is CancellationPolicyUiModel.ErrorUi -> {

        }
        is CancellationPolicyUiModel.InitialUi -> {

        }
    }
}