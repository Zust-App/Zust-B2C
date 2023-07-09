package zustElectronics.zeLanding.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.dp_6
import `in`.opening.area.zustapp.ui.theme.dp_8

@Composable
fun ZeCustomHomeTopBar(
    modifier: Modifier,
    callback: (ACTION) -> Unit,
) {

    ConstraintLayout(modifier = modifier
        .wrapContentHeight()
        .fillMaxWidth()
        .clip(RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp))
        .padding(vertical = 12.dp, horizontal = 20.dp)
    ) {
        val (
            locationTag, locationSubTitle,
            locationIcon, changeLanguageIcon, profileIcon, changeLocationIcon,
        ) = createRefs()

        Icon(painter = painterResource(id = R.drawable.simple_location_icon),
            contentDescription = "location", tint = colorResource(id = R.color.white),
            modifier = Modifier
                .height(17.dp)
                .width(12.dp)
                .constrainAs(locationIcon) {
                    top.linkTo(locationTag.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(locationTag.bottom)
                }
                .clickable {
                    callback.invoke(ACTION.OPEN_LOCATION)
                })

        Text(text = "Patna", color = colorResource(id = R.color.white),
            modifier = modifier
                .constrainAs(locationTag) {
                    top.linkTo(parent.top)
                    start.linkTo(locationIcon.end, dp_8)
                }
                .clickable {
                    callback.invoke(ACTION.OPEN_LOCATION)
                }, style = ZustTypography.body1)

        Icon(painter = painterResource(id = R.drawable.down_arrow),
            contentDescription = "location", tint = colorResource(id = R.color.white),
            modifier = Modifier
                .height(10.dp)
                .width(12.dp)
                .constrainAs(changeLocationIcon) {
                    top.linkTo(locationTag.top, dp_4)
                    start.linkTo(locationTag.end, dp_6)
                    bottom.linkTo(locationTag.bottom)
                }
                .clickable {
                    callback.invoke(ACTION.OPEN_LOCATION)
                })
        Text(
            text = "Delivery in Patna",
            style = ZustTypography.subtitle1,
            color = colorResource(id = R.color.white),
            modifier = modifier.constrainAs(locationSubTitle) {
                top.linkTo(locationTag.bottom, dp_4)
                start.linkTo(parent.start)
                end.linkTo(profileIcon.start, dp_8)
                width = Dimension.fillToConstraints
            },
            maxLines = 1, overflow = TextOverflow.Ellipsis
        )
    }
}
