package `in`.opening.area.zustapp.orderSummary.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.orderSummary.OrderItemsClickListeners
import `in`.opening.area.zustapp.product.addIcon
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.product.removeIcon
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.utility.ProductUtils
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@Composable
fun SuggestItemContainer(product: ProductSingleItem,listeners: OrderItemsClickListeners) {
    ConstraintLayout(modifier = Modifier
        .width(150.dp)
        .padding(bottom = 6.dp, end = 8.dp)
        .background(color = colorResource(id = R.color.white),
            shape = RoundedCornerShape(12.dp))
        .padding(bottom = 8.dp)) {
        val (productImage, productName, productQuantity, productPrice, productMrp, incDecContainer, addItemContainer) = createRefs()
        AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(product.thumbnail).crossfade(true).build(),
            contentDescription = null, contentScale = ContentScale.FillBounds, modifier = Modifier
                .size(100.dp)
                .constrainAs(productImage) {
                    top.linkTo(parent.top, dp_8)
                    start.linkTo(parent.start, dp_12)
                    end.linkTo(parent.end, dp_12)
                })
        Text(text = product.productName, style = ZustTypography.bodyMedium,
            color = colorResource(id = R.color.app_black),
            fontSize = 14.sp, modifier = Modifier.constrainAs(productName) {
                top.linkTo(productImage.bottom, dp_4)
                start.linkTo(parent.start, dp_12)
                end.linkTo(parent.end, dp_4)
                width = Dimension.fillToConstraints
            }, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Text(text = buildString {
            append(ProductUtils.getNumberDisplayValue(product.quantity))
            append(" ")
            append(product.quantityUnit.lowercase())
        }, style = ZustTypography.bodyMedium,
            fontWeight = FontWeight.W600,
            color = colorResource(id = R.color.new_hint_color),
            fontSize = 12.sp,
            modifier = Modifier.constrainAs(productQuantity) {
                top.linkTo(productName.bottom, dp_12)
                start.linkTo(parent.start, dp_12)
            })
        Text(text = buildString {
            append(stringResource(id = R.string.ruppes))
            append(product.price.toInt())
        }, style = ZustTypography.bodyMedium,
            color = colorResource(id = R.color.app_black),
            fontSize = 14.sp,
            modifier = Modifier.constrainAs(productPrice) {
                top.linkTo(productQuantity.bottom, dp_8)
                start.linkTo(parent.start, dp_12)
            })

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
                start.linkTo(productPrice.end, dp_16)
                bottom.linkTo(productPrice.bottom)
            })

        if (product.itemCountByUser > 0) {
            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .border(border = BorderStroke(width = 1.dp,
                        color = colorResource(id = R.color.light_green)),
                        shape = RoundedCornerShape(4.dp))
                    .wrapContentWidth()
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
                            listeners.didTapOnDecreaseProductItemAmount(product)
                        },
                    tint = colorResource(id = R.color.white))

                Text(
                    text = product.itemCountByUser.toString(),
                    modifier = Modifier
                        .defaultMinSize(22.dp)
                        .padding(horizontal = 2.dp)
                        .align(Alignment.CenterVertically),
                    style = ZustTypography.bodyMedium,
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
                            listeners.didTapOnIncreaseProductItemAmount(product)
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
                        listeners.didTapOnIncreaseProductItemAmount(product)
                    }
                    .background(color = colorResource(id = R.color.light_green),
                        shape = RoundedCornerShape(4.dp)),
                tint = colorResource(id = R.color.white)
            )
        }
    }
}