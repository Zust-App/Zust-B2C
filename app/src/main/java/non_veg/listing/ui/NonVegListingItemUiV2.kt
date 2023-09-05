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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
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
import `in`.opening.area.zustapp.ui.theme.dp_6
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.ProductUtils
import `in`.opening.area.zustapp.utility.navigateToNonVegProductDetails
import `in`.opening.area.zustapp.viewmodels.ACTION
import non_veg.listing.models.NonVegListingSingleItem
import non_veg.listing.viewmodel.NonVegListingViewModel



@Composable
fun NonVegListingItemUiV2(singleItem: NonVegListingSingleItem, viewModel: NonVegListingViewModel) {
    val context = LocalContext.current
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .background(color = colorResource(id = R.color.white), shape = RoundedCornerShape(dp_8))
        .clickable {
            context.navigateToNonVegProductDetails(productId = singleItem.productId, productPriceId = singleItem.productPriceId)
        }) {
        val (
            productImage, productTitle, minorDescription, quantity,
            price, mrp, incrementDecContainer,
        ) = createRefs()
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(singleItem.productImageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(dp_8))
                .constrainAs(productImage) {
                    start.linkTo(parent.start, dp_12)
                    top.linkTo(parent.top, dp_12)
                    bottom.linkTo(parent.bottom, dp_12)
                }
        )

        Text(text = singleItem.variantName,
            modifier = Modifier.constrainAs(productTitle) {
                top.linkTo(parent.top, dp_12)
                start.linkTo(productImage.end, dp_12)
                end.linkTo(parent.end, dp_12)
                width = Dimension.fillToConstraints
            }, style = ZustTypography.bodyMedium,
            color = colorResource(id = R.color.app_black))

        Text(text =  buildString {
            append(ProductUtils.getNumberDisplayValue(singleItem.weightPack))
            append(singleItem.unit.lowercase())
        }, modifier = Modifier.constrainAs(quantity) {
            top.linkTo(productTitle.bottom, dp_8)
            start.linkTo(productImage.end, dp_12)
            end.linkTo(parent.end, dp_12)
            width = Dimension.fillToConstraints
        }, style = ZustTypography.bodySmall,
            color = Color(0xBF1E1E1E))

        Text(text = stringResource(id = R.string.ruppes) + ProductUtils.roundTo1DecimalPlaces(singleItem.price),
            modifier = Modifier.constrainAs(price) {
                top.linkTo(quantity.bottom, dp_8)
                start.linkTo(productImage.end, dp_12)
                bottom.linkTo(parent.bottom, dp_12)
                width = Dimension.fillToConstraints
            }, style = ZustTypography.bodyMedium,
            color = colorResource(id = R.color.app_black))

        if (singleItem.mrp != null && singleItem.mrp > 0.0 && singleItem.mrp.toInt() != singleItem.price.toInt()) {
            Text(
                text = stringResource(id = R.string.ruppes) + ProductUtils.roundTo1DecimalPlaces(singleItem.mrp),
                modifier = Modifier.constrainAs(mrp) {
                    top.linkTo(price.top)
                    start.linkTo(price.end, dp_8)
                    bottom.linkTo(price.bottom)
                    width = Dimension.fillToConstraints
                },
                style = ZustTypography.bodySmall.copy(textDecoration = TextDecoration.LineThrough),
                color = colorResource(id = R.color.language_default),
            )
        }

        val inCart = singleItem.quantityOfItemInCart != null && (singleItem.quantityOfItemInCart ?: 0) > 0

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .border(
                    border = BorderStroke(width = 1.dp, color = colorResource(id = R.color.light_green)),
                    shape = RoundedCornerShape(4.dp)
                )
                .clip(RoundedCornerShape(4.dp))
                .wrapContentWidth()
                .background(color = colorResource(id = R.color.white))
                .height(22.dp)
                .constrainAs(incrementDecContainer) {
                    end.linkTo(parent.end, dp_12)
                    bottom.linkTo(parent.bottom, dp_12)
                }
        ) {
            if (!inCart) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ADD",
                    modifier = Modifier
                        .defaultMinSize(44.dp)
                        .padding(horizontal = 2.dp)
                        .align(Alignment.CenterVertically)
                        .clickable {
                            viewModel.handleNonVegCartInsertOrUpdate(singleItem, ACTION.INCREASE)
                        },
                    style = ZustTypography.bodyMedium,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.app_black),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.width(12.dp))
            } else {
                Icon(
                    painter = painterResource(removeIcon),
                    contentDescription = "",
                    modifier = Modifier
                        .background(
                            color = colorResource(id = R.color.light_green),
                            shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp)
                        )
                        .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                        .size(22.dp)
                        .clickable {
                            viewModel.handleNonVegCartInsertOrUpdate(singleItem, ACTION.DECREASE)
                        },
                    tint = colorResource(id = R.color.white)
                )

                Text(
                    text = singleItem.quantityOfItemInCart.toString(),
                    modifier = Modifier
                        .defaultMinSize(22.dp)
                        .padding(horizontal = 2.dp)
                        .align(Alignment.CenterVertically),
                    style = ZustTypography.bodyMedium,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.app_black),
                    textAlign = TextAlign.Center,
                )

                Icon(
                    painter = painterResource(addIcon),
                    contentDescription = "",
                    modifier = Modifier
                        .size(22.dp)
                        .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                        .background(
                            color = colorResource(id = R.color.light_green),
                            shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                        )
                        .clickable {
                            viewModel.handleNonVegCartInsertOrUpdate(singleItem, ACTION.INCREASE)
                        },
                    tint = colorResource(id = R.color.white)
                )
            }
        }

    }
}
