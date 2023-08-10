package zustbase.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.dp_8

@Composable
fun ComposeCustomOrderHistoryTopBar(
    modifier: Modifier, titleText: String,
    subTitleText: String? = null,
    @DrawableRes endImageId: Int? = null,
    callback: (ACTION) -> Unit,
) {
    var currentTab by rememberSaveable {
        mutableStateOf(ACTION.GROCERY_TAB.ordinal)
    }
    Column(modifier = Modifier.fillMaxWidth()) {
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
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.white))) {
            TabItem(
                text = "Grocery",
                isSelected = currentTab == ACTION.GROCERY_TAB.ordinal,
                onItemClick = {
                    callback.invoke(ACTION.GROCERY_TAB)
                    currentTab = ACTION.GROCERY_TAB.ordinal
                }
            )
            TabItem(
                text = "Non-veg",
                isSelected = currentTab == ACTION.NON_VEG_TAB.ordinal,
                onItemClick = {
                    callback.invoke(ACTION.NON_VEG_TAB)
                    currentTab = ACTION.NON_VEG_TAB.ordinal
                }
            )
        }
    }
}

@Composable
fun TabItem(
    text: String,
    isSelected: Boolean,
    onItemClick: () -> Unit,
) {
    ConstraintLayout(modifier = Modifier
        .wrapContentHeight()
        .wrapContentWidth()) {
        val (tabText, tabIndicator) = createRefs()
        Text(
            text = text,
            modifier = Modifier
                .constrainAs(tabText) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(horizontal = dp_16, vertical = dp_8)
                .clickable { onItemClick.invoke() },
            style = ZustTypography.h1.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        )
        if (isSelected) {
            Divider(modifier = Modifier
                .constrainAs(tabIndicator) {
                    top.linkTo(tabText.bottom)
                    start.linkTo(tabText.start)
                    end.linkTo(tabText.end)
                    width = Dimension.fillToConstraints
                }, color = colorResource(id = R.color.new_material_primary), thickness = dp_4)
        }
    }
}

