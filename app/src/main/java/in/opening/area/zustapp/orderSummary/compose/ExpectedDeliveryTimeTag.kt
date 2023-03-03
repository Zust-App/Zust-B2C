package `in`.opening.area.zustapp.orderSummary.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import `in`.opening.area.zustapp.viewmodels.OrderSummaryViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun ExpectedDeliveryTimeTag(orderSummaryViewModel: OrderSummaryViewModel) {
    val expectedDeliveryTimeData by orderSummaryViewModel.expectedDeliveryTimeUiState.collectAsState(initial = "")
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 24.dp)) {
        val (timeText, icon) = createRefs()
        Text(text = expectedDeliveryTimeData ?: "Get your order in 45 mins",
            fontSize = 14.sp,
            color = colorResource(id = R.color.new_material_primary),
            style = Typography_Montserrat.body1, modifier = Modifier
                .constrainAs(timeText) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    width = Dimension.fillToConstraints
                }
                .background(color = colorResource(id = R.color.yellow),
                    shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 12.dp))

        Image(painter = painterResource(id = R.drawable.delivery_boy),
            contentDescription = "delivery_boy", modifier = Modifier
                .constrainAs(icon) {
                    end.linkTo(parent.end)
                    top.linkTo(timeText.top)
                    bottom.linkTo(timeText.bottom)
                }
                .size(74.dp))
    }
}