package `in`.opening.area.zustapp.productDetails.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun ProductDetailTopBarUi(
    modifier: Modifier,
    callback: (ACTION) -> Unit,
) {
    ConstraintLayout(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .fillMaxWidth()
        .clip(RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp))
        .background(color = colorResource(id = R.color.new_material_primary))
        .padding(vertical = 16.dp)
    ) {
        val (title, profileIcon) = createRefs()
        Text(text = stringResource(R.string.product_details),
            color = colorResource(id = R.color.white),
            modifier = modifier.constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(profileIcon.end, dp_12)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }, style = ZustTypography.body1)

        Icon(painter = painterResource(id = R.drawable.app_nav_arrow),
            tint = colorResource(id = R.color.white),
            contentDescription = stringResource(R.string.arrow), modifier = Modifier.wrapContentHeight().wrapContentWidth()
                .constrainAs(profileIcon) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start, dp_16)
                    top.linkTo(parent.top)
                }
                .clickable {
                    callback.invoke(ACTION.NAV_BACK)
                })
    }
}