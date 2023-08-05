package non_veg.home.ui.commonUi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.product.addIcon
import `in`.opening.area.zustapp.product.removeIcon
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_8

@Composable
fun ZNonVegTopPicsItemUi() {
    ConstraintLayout(modifier = Modifier
        .padding(horizontal = 16.dp).width(180.dp).wrapContentHeight()) {
        val (productIcon, productTitle, productMinorTag, productPrice, productMrp, productDiscountPercentage, incrementDecContainer, addItemContainer) = createRefs()
        Image(painter = painterResource(id = R.drawable.delivery_boy), contentDescription = "", modifier = Modifier
            .size(100.dp)
            .constrainAs(productIcon) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
        Text(text = "Chicken drumstick - Pack of 2",
            style = ZustTypography.h1, modifier = Modifier.constrainAs(productTitle) {
                top.linkTo(productIcon.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        if (1 > 0) {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier
                .border(border = BorderStroke(width = 1.dp, color = colorResource(id = R.color.light_green)), shape = RoundedCornerShape(4.dp))
                .clip(RoundedCornerShape(4.dp))
                .wrapContentWidth()
                .background(color = colorResource(id = R.color.white))
                .height(22.dp)
                .constrainAs(incrementDecContainer) {
                    end.linkTo(parent.end, dp_8)
                    top.linkTo(productTitle.bottom, dp_8)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                Icon(painter = painterResource(removeIcon), contentDescription = "", modifier = Modifier
                    .background(color = colorResource(id = R.color.light_green), shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                    .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                    .size(22.dp)
                    .clickable {

                    }, tint = colorResource(id = R.color.white))

                Text(
                    text = "2",
                    modifier = Modifier
                        .defaultMinSize(22.dp)
                        .padding(horizontal = 2.dp)
                        .align(Alignment.CenterVertically),
                    style = ZustTypography.body1,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.app_black),
                    textAlign = TextAlign.Center,
                )

                Icon(painter = painterResource(addIcon), contentDescription = "", modifier = Modifier
                    .size(22.dp)
                    .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                    .background(color = colorResource(id = R.color.light_green), shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                    .clickable {

                    }, tint = colorResource(id = R.color.white))

            }
        } else {
            Icon(painter = painterResource(id = R.drawable.ic_baseline_add_24), contentDescription = "add", modifier = Modifier
                .constrainAs(addItemContainer) {
                    end.linkTo(parent.end, dp_8)
                    top.linkTo(productTitle.bottom, dp_8)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(22.dp)
                .clickable {

                }
                .background(color = colorResource(id = R.color.light_green), shape = RoundedCornerShape(4.dp)), tint = colorResource(id = R.color.white))
        }

    }
}
