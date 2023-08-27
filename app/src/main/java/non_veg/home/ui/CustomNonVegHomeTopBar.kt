package non_veg.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.extensions.collectAsStateLifecycleAware
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.locationManager.UserLocationDetails
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.dp_6
import `in`.opening.area.zustapp.ui.theme.dp_8
import non_veg.home.viewmodel.ZustNvEntryViewModel
import ui.linearGradientNonVegBrush

@Composable
fun CustomNonVegHomeTopBar(
    zNVEntryViewModel: ZustNvEntryViewModel = viewModel(),
    modifier: Modifier,
    callback: (ACTION) -> Unit,
) {
    val userAddress by zNVEntryViewModel.userLocationFlow.collectAsStateLifecycleAware(initial = UserLocationDetails())

    ConstraintLayout(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .fillMaxWidth()
        .background(brush = linearGradientNonVegBrush)
        .padding(vertical = 12.dp, horizontal = 20.dp)
    ) {
        val (
            locationTag, locationSubTitle,
            locationIcon, profileIcon, changeLocationIcon,
        ) = createRefs()

        Icon(painter = painterResource(id = R.drawable.home_location_pin_icon),
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

        Text(text = "Address", color = colorResource(id = R.color.white),
            modifier = modifier
                .constrainAs(locationTag) {
                    top.linkTo(parent.top)
                    start.linkTo(locationIcon.end, dp_8)
                }
                .clickable {
                    callback.invoke(ACTION.OPEN_LOCATION)
                }, style = ZustTypography.bodyMedium)

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
            text = userAddress.fullAddress ?: "Delivery in Patna",
            style = ZustTypography.bodySmall,
            color = colorResource(id = R.color.white),
            modifier = modifier.constrainAs(locationSubTitle) {
                top.linkTo(locationTag.bottom, dp_4)
                start.linkTo(parent.start)
                end.linkTo(profileIcon.start, dp_8)
                width = Dimension.fillToConstraints
            },
            maxLines = 1, overflow = TextOverflow.Ellipsis
        )

        Icon(painter = painterResource(id = R.drawable.new_profile_icon),
            tint = colorResource(id = R.color.white),
            contentDescription = "profile", modifier = modifier
                .constrainAs(profileIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
                .clickable {
                    callback.invoke(ACTION.OPEN_PROFILE)
                }
                .clip(shape = RoundedCornerShape(8.dp)))
    }
}

@Composable
fun OvalIconBackground(modifier: Modifier, icon: Int) {
    BoxWithConstraints(
        modifier = modifier
            .background(Color.LightGray, shape = CircleShape),
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp), tint = colorResource(id = R.color.white)
        )
    }
}