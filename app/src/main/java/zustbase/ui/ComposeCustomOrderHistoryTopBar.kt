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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.android.material.tabs.TabItem
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
    color: Color,
    @DrawableRes endImageId: Int? = null,
    callback: (ACTION) -> Unit,
) {
    var currentTab by remember {
        mutableIntStateOf(ACTION.GROCERY_TAB.ordinal)
    }
    ConstraintLayout(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(vertical = 12.dp, horizontal = 20.dp)
    ) {
        val (backArrowImage, tabSection) = createRefs()

        Icon(painter = painterResource(id = R.drawable.app_nav_arrow),
            tint = color,
            contentDescription = "profile", modifier = modifier
                .constrainAs(backArrowImage) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .clickable {
                    callback.invoke(ACTION.NAV_BACK)
                })

        Row(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(tabSection) {
                top.linkTo(parent.top)
                start.linkTo(backArrowImage.end, dp_8)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
            }
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
            style = if (isSelected) {
                ZustTypography.bodyMedium.copy(fontWeight = FontWeight.W600)
            } else {
                ZustTypography.bodyMedium
            }
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

