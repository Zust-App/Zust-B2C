package `in`.opening.area.zustapp.home.components

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.product.addIcon
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.product.removeIcon
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.utility.ProductUtils
import `in`.opening.area.zustapp.utility.startProductDetailPage
import `in`.opening.area.zustapp.viewmodels.ACTION
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest

val trendingItemsIMageModifier = Modifier
    .width(80.dp)
    .height(80.dp)
val trendingItemsModifier = Modifier
    .padding(bottom = 6.dp, end = 8.dp)
    .background(color = Color.White, shape = RoundedCornerShape(12.dp))
    .padding(bottom = 8.dp)

@Composable
fun RowScope.TrendingProductsSingleItem(product: ProductSingleItem, productItemClick: (ProductSingleItem?, ACTION) -> Unit) {
    val context = LocalContext.current
    ConstraintLayout(modifier = trendingItemsModifier
        .weight(1f)
        .clickable {
            context.startProductDetailPage(product)
        }) {
        val (
            productImage, productName, productQuantity, productPrice, productMrp,
            incDecContainer, addItemContainer, offerPercentage,
        ) = createRefs()
        AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(product.thumbnail)
            .crossfade(true).build(), contentDescription = null, contentScale = ContentScale.FillBounds, modifier
        = trendingItemsIMageModifier
            .constrainAs(productImage) {
                top.linkTo(parent.top, dp_8)
                start.linkTo(parent.start, dp_12)
                end.linkTo(parent.end, dp_12)
            })
        Text(text = product.productName,
            style = ZustTypography.body2,
            color = colorResource(id = R.color.app_black),
            fontSize = 14.sp, modifier = Modifier
                .height(38.dp)
                .constrainAs(productName) {
                    top.linkTo(productImage.bottom, dp_4)
                    start.linkTo(parent.start, dp_12)
                    end.linkTo(parent.end, dp_4)
                    width = Dimension.fillToConstraints
                }, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Text(text = buildString {
            append(ProductUtils.getNumberDisplayValue(product.quantity))
            append(" ")
            append(product.quantityUnit.lowercase())
        }, style = ZustTypography.body2,
            fontWeight = FontWeight.W600,
            color = colorResource(id = R.color.new_hint_color),
            fontSize = 12.sp,
            modifier = Modifier.constrainAs(productQuantity) {
                top.linkTo(productName.bottom, dp_8)
                start.linkTo(parent.start, dp_12)
            })

        Text(text = buildString {
            append(stringResource(id = R.string.ruppes))
            append(product.price.toInt())
        }, style = ZustTypography.body1,
            color = colorResource(id = R.color.app_black),
            fontSize = 14.sp,
            fontWeight = FontWeight.W600,
            modifier = Modifier.constrainAs(productPrice) {
                top.linkTo(productQuantity.bottom, dp_8)
                start.linkTo(parent.start, dp_12)
            })

        if (product.discountPercentage > 0) {
            Text(text = buildString {
                append(stringResource(id = R.string.ruppes))
                append(product.mrp.toInt())
            }, style = TextStyle(textDecoration = TextDecoration.LineThrough,
                fontSize = 12.sp,
                fontWeight = FontWeight.W500,
                fontFamily = zustFont),
                color = colorResource(id = R.color.new_hint_color),
                modifier = Modifier.constrainAs(productMrp) {
                    top.linkTo(productPrice.top)
                    start.linkTo(productPrice.end, dp_8)
                    bottom.linkTo(productPrice.bottom)
                })
        }

        if (product.itemCountByUser > 0) {
            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .border(border = BorderStroke(width = 1.dp,
                        color = colorResource(id = R.color.light_green)),
                        shape = RoundedCornerShape(4.dp))
                    .clip(RoundedCornerShape(4.dp))
                    .wrapContentWidth()
                    .background(color = colorResource(id = R.color.white))
                    .height(22.dp)
                    .constrainAs(incDecContainer) {
                        end.linkTo(parent.end, dp_8)
                        bottom.linkTo(productImage.bottom)
                    }) {
                Icon(painter = painterResource(removeIcon),
                    contentDescription = "",
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.light_green),
                            shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                        .clip(RoundedCornerShape(topStart = 4.dp,
                            bottomStart = 4.dp))
                        .size(22.dp)
                        .clickable {
                            productItemClick.invoke(product, ACTION.DECREASE)
                        },
                    tint = colorResource(id = R.color.white))

                Text(
                    text = product.itemCountByUser.toString(),
                    modifier = Modifier
                        .defaultMinSize(22.dp)
                        .padding(horizontal = 2.dp)
                        .align(Alignment.CenterVertically),
                    style = ZustTypography.body1,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.app_black),
                    textAlign = TextAlign.Center,
                )

                Icon(painter = painterResource(addIcon),
                    contentDescription = "",
                    modifier = Modifier
                        .size(22.dp)
                        .clip(RoundedCornerShape(topEnd = 4.dp,
                            bottomEnd = 4.dp))
                        .background(color = colorResource(id = R.color.light_green),
                            shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                        .clickable {
                            productItemClick.invoke(product, ACTION.INCREASE)
                        },
                    tint = colorResource(id = R.color.white))

            }
        } else {
            Icon(painter = painterResource(id = R.drawable.ic_baseline_add_24),
                contentDescription = "add", modifier = Modifier
                    .constrainAs(addItemContainer) {
                        end.linkTo(parent.end, dp_8)
                        bottom.linkTo(productImage.bottom)
                    }
                    .size(22.dp)
                    .clickable {
                        productItemClick.invoke(product, ACTION.INCREASE)
                    }
                    .background(color = colorResource(id = R.color.light_green),
                        shape = RoundedCornerShape(4.dp)),
                tint = colorResource(id = R.color.white)
            )
        }
        if (product.discountPercentage > 0.0) {
            Text(text = product.discountPercentage.toInt().toString() + "% OFF", modifier = Modifier
                .background(color = colorResource(id = R.color.light_offer_color), shape = RoundedCornerShape(4.dp))
                .padding(horizontal = 4.dp,
                    vertical = 2.dp)
                .constrainAs(offerPercentage) {
                    start.linkTo(parent.start, dp_4)
                    top.linkTo(parent.top, dp_4)
                }, color = Color.White,
                fontSize = 10.sp,
                fontFamily = zustFont,
                fontWeight = FontWeight.W600)
        }
    }
}
