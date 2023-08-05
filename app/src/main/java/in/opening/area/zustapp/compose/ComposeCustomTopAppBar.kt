package `in`.opening.area.zustapp.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.viewmodels.ProductListingViewModel

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun ComposeCustomTopAppBar(
    modifier: Modifier, titleText: String,
    subTitleText: String?=null,
    @DrawableRes endImageId: Int?=null,
    callback: (ACTION) -> Unit
) {
    ConstraintLayout(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .fillMaxWidth()
        .clip(RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp))
        .background(color = colorResource(id = (R.color.new_material_primary)))
        .padding(vertical = 12.dp, horizontal = 20.dp)
    ) {
        val (titleTag, subTitleTag, endImage, backArrowImage) = createRefs()
        Text(text = titleText, color = colorResource(id = R.color.white),
            modifier = modifier.constrainAs(titleTag) {
                if (subTitleText.isNullOrEmpty()) {
                    top.linkTo(parent.top, dp_8)
                    bottom.linkTo(parent.bottom, dp_8)
                } else {
                    top.linkTo(parent.top)
                }
                start.linkTo(backArrowImage.end, dp_16)
            }, style = ZustTypography.body1,
            maxLines = 1, overflow = TextOverflow.Ellipsis)
        if (!subTitleText.isNullOrEmpty()) {
            Text(
                text = subTitleText,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
                style = ZustTypography.subtitle1,
                color = colorResource(id = R.color.white),
                modifier = modifier.constrainAs(subTitleTag) {
                    top.linkTo(titleTag.bottom, dp_4)
                    start.linkTo(backArrowImage.end, dp_16)
                },
            )
        }
        if (endImageId != null) {
            Icon(painter = painterResource(id = endImageId),
                tint = colorResource(id = R.color.white),
                contentDescription = "profile", modifier = modifier
                    .constrainAs(endImage) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .clickable {
                        callback.invoke(ACTION.OPEN_PROFILE)
                    }
                    .clip(shape = RoundedCornerShape(8.dp)))
        }

        Icon(painter = painterResource(id = R.drawable.app_nav_arrow),
            tint = colorResource(id = R.color.white),
            contentDescription = "profile", modifier = modifier
                .constrainAs(backArrowImage) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .clickable {
                    callback.invoke(ACTION.NAV_BACK)
                })
    }
}


@Composable
fun ComposeTopAppBarProductList(
    modifier: Modifier,
    productListingViewModel: ProductListingViewModel,
    subTitleText: String?,
    @DrawableRes endImageId: Int?,
    callback: (ACTION) -> Unit,
) {
    val data by productListingViewModel.headingData.collectAsState(initial = "")

    ConstraintLayout(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .fillMaxWidth()
        .clip(RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp))
        .background(color = colorResource(id = R.color.new_material_primary))
        .padding(vertical = 12.dp, horizontal = 20.dp)
    ) {
        val (titleTag, subTitleTag, endImage, backArrowImage) = createRefs()
        Text(text = "$data Category", color = colorResource(id = R.color.white),
            modifier = modifier.constrainAs(titleTag) {
                if (subTitleText.isNullOrEmpty()) {
                    top.linkTo(parent.top, dp_8)
                    bottom.linkTo(parent.bottom, dp_8)
                } else {
                    top.linkTo(parent.top)
                }
                start.linkTo(backArrowImage.end, dp_16)
            }, style = ZustTypography.body1,
            maxLines = 1, overflow = TextOverflow.Ellipsis)
        if (!subTitleText.isNullOrEmpty()) {
            Text(
                text = subTitleText,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
                style = ZustTypography.subtitle1,
                color = colorResource(id = R.color.white),
                modifier = modifier.constrainAs(subTitleTag) {
                    top.linkTo(titleTag.bottom, dp_4)
                    start.linkTo(backArrowImage.end, dp_16)
                },
            )
        }
        if (endImageId != null) {
            Icon(painter = painterResource(id = endImageId),
                tint = colorResource(id = R.color.white),
                contentDescription = "profile", modifier = modifier
                    .constrainAs(endImage) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .clickable {
                        callback.invoke(ACTION.SEARCH_PRODUCT)
                    }
                    .clip(shape = RoundedCornerShape(8.dp)))
        }

        Icon(painter = painterResource(id = R.drawable.app_nav_arrow),
            tint = colorResource(id = R.color.white),
            contentDescription = "profile", modifier = modifier
                .constrainAs(backArrowImage) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .clickable {
                    callback.invoke(ACTION.NAV_BACK)
                })
    }
}