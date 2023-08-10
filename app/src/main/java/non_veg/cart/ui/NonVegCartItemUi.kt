package non_veg.cart.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.product.addIcon
import `in`.opening.area.zustapp.product.removeIcon
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.viewmodels.ACTION
import non_veg.cart.models.ItemsInCart
import non_veg.listing.ui.dummyUrl

val topRound8dp = RoundedCornerShape(topStart = dp_8, topEnd = dp_8)
val bottomRound8dp = RoundedCornerShape(bottomEnd = dp_8, bottomStart = dp_8)


@Composable
fun NonVegCartItemUi(singleItem: ItemsInCart, modifier: Modifier, clickCallback: (singleItem: ItemsInCart, action: ACTION) -> Unit) {
    ConstraintLayout(modifier = modifier
        .background(color = colorResource(id = R.color.white))) {
        val (thumbnail, name, productQuantity, price, incrementDecContainer) = createRefs()
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(shape = RoundedCornerShape(12.dp))
                .constrainAs(thumbnail) {
                    top.linkTo(parent.top, dp_12)
                    start.linkTo(parent.start, dp_12)
                    bottom.linkTo(parent.bottom, dp_12)
                }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(dummyUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
            )
        }

        Text(text = singleItem.productName,
            style = ZustTypography.body1, modifier = Modifier.constrainAs(name) {
                top.linkTo(parent.top, dp_12)
                start.linkTo(thumbnail.end, dp_12)
                end.linkTo(parent.end, dp_12)
                width = Dimension.fillToConstraints
            }, maxLines = 1)

        Text(text = "500g", style = ZustTypography.body2,
            modifier = Modifier.constrainAs(productQuantity) {
                top.linkTo(name.bottom, dp_8)
                start.linkTo(thumbnail.end, dp_12)
                end.linkTo(parent.end, dp_12)
                width = Dimension.fillToConstraints
            })

        Text(text = stringResource(id = R.string.ruppes) + singleItem.price.toInt().toString(),
            style = ZustTypography.body1,
            modifier = Modifier.constrainAs(price) {
                start.linkTo(thumbnail.end, dp_12)
                bottom.linkTo(parent.bottom, dp_12)
                width = Dimension.fillToConstraints
            })

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier
            .border(border = BorderStroke(width = 1.dp, color = colorResource(id = R.color.light_green)), shape = RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp))
            .wrapContentWidth()
            .background(color = colorResource(id = R.color.white))
            .height(22.dp)
            .constrainAs(incrementDecContainer) {
                end.linkTo(parent.end, dp_12)
                bottom.linkTo(parent.bottom, dp_12)
            }) {
            Icon(painter = painterResource(removeIcon), contentDescription = "", modifier = Modifier
                .background(color = colorResource(id = R.color.light_green), shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                .size(22.dp)
                .clickable {
                    clickCallback.invoke(singleItem, ACTION.DECREASE)
                }, tint = colorResource(id = R.color.white))

            Text(
                text = singleItem.quantity.toString(),
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
                    clickCallback.invoke(singleItem, ACTION.INCREASE)
                }, tint = colorResource(id = R.color.white))

        }

    }
}