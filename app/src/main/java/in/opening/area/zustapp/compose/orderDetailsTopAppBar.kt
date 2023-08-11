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
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun CustomGroceryTopBar(
    modifier: Modifier,
    viewModel:GroceryHomeViewModel= androidx.lifecycle.viewmodel.compose.viewModel(),
    callback: (ACTION) -> Unit,
) {
    val userAddress by viewModel.userLocationFlow.collectAsStateLifecycleAware(initial = UserLocationDetails())

    ConstraintLayout(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .fillMaxWidth()
        .background(color = colorResource(id = color.new_material_primary))
        .padding(vertical = 12.dp, horizontal = 20.dp)
    ) {
        val (
            locationTag, locationSubTitle,
            locationIcon, changeLanguageIcon, profileIcon, changeLocationIcon,
        ) = createRefs()

        Icon(painter = painterResource(id = drawable.simple_location_icon),
            contentDescription = "location", tint = colorResource(id = color.white),
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

        Text(text = "Address", color = colorResource(id = color.white),
            modifier = modifier
                .constrainAs(locationTag) {
                    top.linkTo(parent.top)
                    start.linkTo(locationIcon.end, dp_8)
                }
                .clickable {
                    callback.invoke(ACTION.OPEN_LOCATION)
                }, style = ZustTypography.body1)

        Icon(painter = painterResource(id = drawable.down_arrow),
            contentDescription = "location", tint = colorResource(id = color.white),
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
            text = userAddress.fullAddress?:"Delivery in Patna",
            style = ZustTypography.subtitle1,
            color = colorResource(id = color.white),
            modifier = modifier.constrainAs(locationSubTitle) {
                top.linkTo(locationTag.bottom, dp_4)
                start.linkTo(parent.start)
                end.linkTo(profileIcon.start, dp_8)
                width = Dimension.fillToConstraints
            },
            maxLines = 1,overflow= TextOverflow.Ellipsis
        )

        if (false) {
            Icon(painter = painterResource(id = drawable.language_icon),
                tint = colorResource(id =color.white),
                contentDescription = "language", modifier = modifier
                    .constrainAs(changeLanguageIcon) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(profileIcon.start, dp_12)
                    }
                    .clickable {
                        callback.invoke(ACTION.LANGUAGE_DIALOG)
                    }
                    .clip(shape = RoundedCornerShape(8.dp)))
        }
        Icon(painter = painterResource(id = drawable.new_profile_icon),
            tint = colorResource(id = color.white),
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



