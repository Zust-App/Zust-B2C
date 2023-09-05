package `in`.opening.area.zustapp.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.product.addIcon
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.dp_6
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.viewmodels.ProductListingViewModel
import ui.colorBlack
import ui.colorWhite

@Composable
fun ComposeCustomTopAppBar(
    modifier: Modifier,
    titleText: String,
    subTitleText: String? = null,
    color: Color? = colorWhite,
    @DrawableRes endImageId: Int? = null,
    endText: String? = null,
    callback: (ACTION) -> Unit,
) {
    ConstraintLayout(modifier = modifier
        .wrapContentHeight()
        .fillMaxWidth()
        .background(color = colorWhite)
        .padding(vertical = dp_12, horizontal = 20.dp)
    ) {
        val (titleTag, subTitleTag, endImage, backArrowImage) = createRefs()
        Text(text = titleText, color = color ?: colorWhite,
            modifier = modifier.constrainAs(titleTag) {
                if (subTitleText.isNullOrEmpty()) {
                    top.linkTo(parent.top, dp_8)
                    bottom.linkTo(parent.bottom, dp_8)
                } else {
                    top.linkTo(parent.top)
                }
                start.linkTo(backArrowImage.end, dp_16)
            }, style = ZustTypography.titleLarge,
            maxLines = 1, overflow = TextOverflow.Ellipsis)
        if (!subTitleText.isNullOrEmpty()) {
            Text(
                text = subTitleText,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
                style = ZustTypography.bodySmall,
                color = color ?: colorWhite,
                modifier = modifier.constrainAs(subTitleTag) {
                    top.linkTo(titleTag.bottom, dp_4)
                    start.linkTo(backArrowImage.end, dp_16)
                },
            )
        }
        if (endImageId != null) {
            Icon(painter = painterResource(id = endImageId),
                tint = color ?: colorWhite,
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
        if (endText != null) {
            Row(modifier = Modifier
                .constrainAs(endImage) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
                .border(border = BorderStroke(1.dp, color = colorResource(id = R.color.new_material_primary)), shape = RoundedCornerShape(dp_4))
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .clickable {
                    callback.invoke(ACTION.ADD_MORE)
                }) {
                Icon(painter = painterResource(id = addIcon),
                    tint = color ?: colorWhite,
                    contentDescription = "profile", modifier = modifier.size(dp_16))
                Spacer(modifier = Modifier.width(dp_6))
                Text(text = endText, style = ZustTypography.bodyMedium,
                    color = colorResource(id = R.color.light_green))
            }
        }

        Icon(painter = painterResource(id = R.drawable.app_nav_arrow),
            tint = color ?: colorWhite,
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
    titleText: String?,
    subTitleText: String?,
    color: Color = colorBlack,
    @DrawableRes endImageId: Int?,
    callback: (ACTION) -> Unit,
) {
    ConstraintLayout(modifier = modifier
        .wrapContentHeight()
        .fillMaxWidth()
        .padding(vertical = dp_12, horizontal = 20.dp)
    ) {
        val (titleTag, subTitleTag, endImage, backArrowImage) = createRefs()
        Text(text = titleText ?: "", color = color,
            modifier = modifier.constrainAs(titleTag) {
                if (subTitleText.isNullOrEmpty()) {
                    top.linkTo(parent.top, dp_8)
                    bottom.linkTo(parent.bottom, dp_8)
                } else {
                    top.linkTo(parent.top)
                }
                start.linkTo(backArrowImage.end, dp_16)
            }, style = ZustTypography.titleMedium,
            maxLines = 1, overflow = TextOverflow.Ellipsis)
        if (!subTitleText.isNullOrEmpty()) {
            Text(
                text = subTitleText,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
                style = ZustTypography.bodySmall,
                color = color,
                modifier = modifier.constrainAs(subTitleTag) {
                    top.linkTo(titleTag.bottom, dp_4)
                    start.linkTo(backArrowImage.end, dp_16)
                },
            )
        }
        if (endImageId != null) {
            Icon(painter = painterResource(id = endImageId),
                tint = color,
                contentDescription = "profile", modifier = modifier
                    .constrainAs(endImage) {
                        top.linkTo(titleTag.top)
                        bottom.linkTo(titleTag.bottom)
                        end.linkTo(parent.end)
                    }
                    .clickable {
                        callback.invoke(ACTION.SEARCH_PRODUCT)
                    }
                    .clip(shape = RoundedCornerShape(8.dp)))
        }

        Icon(painter = painterResource(id = R.drawable.app_nav_arrow),
            tint = color,
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

