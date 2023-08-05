package non_veg.listing.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
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
import `in`.opening.area.zustapp.ui.theme.dp_6
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.viewmodels.ACTION
import non_veg.listing.models.NonVegListingSingleItem
import non_veg.listing.viewmodel.NonVegListingViewModel


var dummyUrl = "https://static.freshtohome.com/media/catalog/product/cache/3/image/400x267/18ae109e34f485bd0b0c075abec96b2e/c/h/chicken_breast_fillet_1_2.jpg"

@Composable
fun NonVegListingItemUiV2(singleItem: NonVegListingSingleItem, viewModel: NonVegListingViewModel) {
    Card(modifier = Modifier,
        shape = RoundedCornerShape(topEnd = dp_8, topStart = dp_8),
        backgroundColor = colorResource(id = R.color.white), border = BorderStroke(width = 0.5.dp,
            color = colorResource(id = R.color.indicator_color))) {
        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()) {
            val (
                productImage, productTitle, minorDescription, quantity,
                price, mrp, incrementDecContainer,
            ) = createRefs()
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(dummyUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(120.dp)
                    .clip(RoundedCornerShape(topEnd = dp_8, topStart = dp_8))
                    .constrainAs(productImage) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
            )
            Text(text = singleItem.variantName,
                modifier = Modifier.constrainAs(productTitle) {
                    top.linkTo(productImage.bottom, dp_16)
                    start.linkTo(parent.start, dp_12)
                    end.linkTo(parent.end, dp_12)
                    width = Dimension.fillToConstraints
                }, style = ZustTypography.body1,
                color = colorResource(id = R.color.app_black))

            Text(text = singleItem.productDescription,
                modifier = Modifier.constrainAs(minorDescription) {
                    top.linkTo(productTitle.bottom, dp_8)
                    start.linkTo(parent.start, dp_12)
                    end.linkTo(parent.end, dp_12)
                    width = Dimension.fillToConstraints
                }, style = ZustTypography.subtitle1,
                color = colorResource(id = R.color.app_black))

            Text(text = singleItem.productQuantity ?: "500g",
                modifier = Modifier.constrainAs(quantity) {
                    top.linkTo(minorDescription.bottom, dp_8)
                    start.linkTo(parent.start, dp_12)
                    end.linkTo(parent.end, dp_12)
                    width = Dimension.fillToConstraints
                }, style = ZustTypography.body2,
                color = colorResource(id = R.color.app_black))

            Text(text = stringResource(id = R.string.ruppes) + singleItem.price.toInt(),
                modifier = Modifier.constrainAs(price) {
                    top.linkTo(quantity.bottom, dp_8)
                    start.linkTo(parent.start, dp_12)
                    bottom.linkTo(parent.bottom, dp_12)
                    width = Dimension.fillToConstraints
                }, style = ZustTypography.body1,
                color = colorResource(id = R.color.app_black))

            if (singleItem.mrp != null && singleItem.mrp > 0.0 && singleItem.mrp.toInt() != singleItem.price.toInt()) {
                Text(
                    text = stringResource(id = R.string.ruppes) + singleItem.mrp.toInt(),
                    modifier = Modifier.constrainAs(mrp) {
                        top.linkTo(price.top)
                        start.linkTo(price.end, dp_8)
                        bottom.linkTo(price.bottom)
                        width = Dimension.fillToConstraints
                    },
                    style = ZustTypography.body2.copy(textDecoration = TextDecoration.LineThrough),
                    color = colorResource(id = R.color.app_black),
                )
            }

            if (singleItem.quantityOfItemInCart == null || singleItem.quantityOfItemInCart == 0) {
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier
                    .border(border = BorderStroke(width = 1.dp, color = colorResource(id = R.color.light_green)), shape = RoundedCornerShape(4.dp))
                    .clip(RoundedCornerShape(4.dp))
                    .wrapContentWidth()
                    .background(color = colorResource(id = R.color.white))
                    .height(22.dp)
                    .clickable {
                        viewModel.handleNonVegCartInsertOrUpdate(singleItem, ACTION.INCREASE)
                    }
                    .constrainAs(incrementDecContainer) {
                        end.linkTo(parent.end, dp_12)
                        bottom.linkTo(parent.bottom, dp_12)
                    })
                {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ADD",
                        modifier = Modifier
                            .defaultMinSize(22.dp)
                            .padding(horizontal = 2.dp)
                            .align(Alignment.CenterVertically),
                        style = ZustTypography.body1,
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.app_black),
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Icon(painter = painterResource(addIcon), contentDescription = "", modifier = Modifier
                        .size(22.dp)
                        .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                        .background(color = colorResource(id = R.color.light_green),
                            shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)), tint = colorResource(id = R.color.white))
                }
            } else {
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier
                    .border(border = BorderStroke(width = 1.dp, color = colorResource(id = R.color.light_green)), shape = RoundedCornerShape(4.dp))
                    .clip(RoundedCornerShape(4.dp))
                    .wrapContentWidth()
                    .background(color = colorResource(id = R.color.white))
                    .height(22.dp)
                    .constrainAs(incrementDecContainer) {
                        end.linkTo(parent.end, dp_12)
                        bottom.linkTo(parent.bottom, dp_12)
                    })
                {
                    Icon(painter = painterResource(removeIcon), contentDescription = "", modifier = Modifier
                        .background(color = colorResource(id = R.color.light_green), shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                        .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                        .size(22.dp)
                        .clickable {
                            viewModel.handleNonVegCartInsertOrUpdate(singleItem, ACTION.DECREASE)
                        }, tint = colorResource(id = R.color.white))

                    Text(
                        text = singleItem.quantityOfItemInCart.toString(),
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
                            viewModel.handleNonVegCartInsertOrUpdate(singleItem, ACTION.INCREASE)
                        }, tint = colorResource(id = R.color.white))
                }
            }
        }
    }
}