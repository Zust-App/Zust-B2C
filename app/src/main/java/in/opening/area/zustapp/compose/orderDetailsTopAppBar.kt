package `in`.opening.area.zustapp.compose

import `in`.opening.area.zustapp.R.*
import `in`.opening.area.zustapp.extensions.collectAsStateLifecycleAware
import `in`.opening.area.zustapp.home.ACTION

import `in`.opening.area.zustapp.locationManager.UserLocationDetails
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.viewmodels.GroceryHomeViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ui.colorBlack
import ui.linearGradientGroceryBrush

@Composable
fun CustomGroceryTopBar(
    modifier: Modifier,
    viewModel: GroceryHomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    callback: (ACTION) -> Unit,
) {
    val userAddress by viewModel.userLocationFlow.collectAsStateLifecycleAware(initial = UserLocationDetails())

    ConstraintLayout(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight().shadow(elevation = dp_8, clip = true)
        .fillMaxWidth()
        .background(brush = linearGradientGroceryBrush)
        .padding(vertical = dp_12, horizontal = dp_20)
    ) {
        val (
            locationTag, locationSubTitle,
            locationIcon, changeLanguageIcon, profileIcon, changeLocationIcon,
        ) = createRefs()

        Icon(painter = painterResource(id = drawable.simple_location_icon),
            contentDescription = "location", tint = colorBlack,
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

        Text(text = "Address", color = colorBlack,
            modifier = modifier
                .constrainAs(locationTag) {
                    top.linkTo(parent.top)
                    start.linkTo(locationIcon.end, dp_8)
                }
                .clickable {
                    callback.invoke(ACTION.OPEN_LOCATION)
                }, style = ZustTypography.titleMedium)

        Icon(painter = painterResource(id = drawable.down_arrow),
            contentDescription = "location", tint = colorBlack,
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
            color = colorBlack,
            modifier = modifier.constrainAs(locationSubTitle) {
                top.linkTo(locationTag.bottom, dp_4)
                start.linkTo(parent.start)
                end.linkTo(profileIcon.start, dp_8)
                width = Dimension.fillToConstraints
            },
            maxLines = 1, overflow = TextOverflow.Ellipsis
        )

        Icon(painter = painterResource(id = drawable.new_profile_icon),
            tint = colorBlack,
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



