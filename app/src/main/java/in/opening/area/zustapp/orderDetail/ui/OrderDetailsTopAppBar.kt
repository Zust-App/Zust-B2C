package `in`.opening.area.zustapp.orderDetail.ui

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import `in`.opening.area.zustapp.ui.theme.dp_12
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun OrderDetailsTopAppBar(
    modifier: Modifier,
    callback: (ACTION) -> Unit,
) {
    ConstraintLayout(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .fillMaxWidth()
        .clip(RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp))
        .background(color = colorResource(id = R.color.new_material_primary))
        .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        val (locationTag, navIcon) = createRefs()
        Text(text = "Order status", color = colorResource(id = R.color.white), modifier = modifier.constrainAs(locationTag) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(navIcon.end, dp_12)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }, style = Typography_Montserrat.body1)

        Icon(painter = painterResource(id = R.drawable.app_nav_arrow),
            tint = colorResource(id = R.color.white),
            contentDescription = "back arrow", modifier = modifier
                .constrainAs(navIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .clickable {
                    callback.invoke(ACTION.NAV_BACK)
                }
                .clip(shape = RoundedCornerShape(8.dp)))
    }
}