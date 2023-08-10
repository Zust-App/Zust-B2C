package zustbase.orderDetail.ui

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_8
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun ZustAppReason(reason: String) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .background(color = colorResource(id = R.color.red_secondary), shape = RoundedCornerShape(dp_8))) {
        val (title, body) = createRefs()
        Text(text = "What we did mistake!!", color = colorResource(id = R.color.white),
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, dp_12)
                start.linkTo(parent.start, dp_16)
                end.linkTo(parent.end, dp_16)
                width = Dimension.fillToConstraints
            }, style = ZustTypography.body1)
        Text(text = reason, color = colorResource(id = R.color.white),
            modifier = Modifier.constrainAs(body) {
                top.linkTo(title.bottom, dp_8)
                start.linkTo(parent.start, dp_16)
                end.linkTo(parent.end, dp_16)
                bottom.linkTo(parent.bottom, dp_12)
                width = Dimension.fillToConstraints
            }, style = ZustTypography.body2)
    }
}