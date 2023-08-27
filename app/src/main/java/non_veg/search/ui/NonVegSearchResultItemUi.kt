package non_veg.search.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.product.addIcon
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.product.removeIcon
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_10
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.ui.theme.zustFont
import `in`.opening.area.zustapp.utility.ProductUtils
import `in`.opening.area.zustapp.utility.navigateToNonVegProductDetails
import `in`.opening.area.zustapp.viewmodels.ACTION
import non_veg.listing.models.NonVegListingSingleItem

@Composable
fun NonVegSearchResultItemUi(
    item: NonVegListingSingleItem,
    modifier: Modifier = Modifier, callback: (ACTION) -> Unit,
) {
    val context = LocalContext.current
    ConstraintLayout(modifier = modifier.clickable {
        context.navigateToNonVegProductDetails(productId = item.productId, productPriceId = item.productPriceId)
    }) {
        val (image, title, quantity, priceText, mrpText, incDecContainer, addItemContainer) = createRefs()
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.productImageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(80.dp)
                .clip(MaterialTheme.shapes.medium)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
        )

        Text(
            overflow = TextOverflow.Ellipsis, maxLines = 2,
            text = item.productName,
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(image.end, dp_10)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            color = colorResource(id = R.color.app_black),
            style = ZustTypography.bodyMedium,
        )

        Text(text = buildString {
            append(ProductUtils.getNumberDisplayValue(item.weightPack))
            append(item.unit.lowercase())
        }, modifier = Modifier.constrainAs(quantity) {
            top.linkTo(title.bottom, dp_8)
            start.linkTo(image.end, dp_10)
        }, color = colorResource(id = R.color.new_hint_color),
            style = ZustTypography.bodyMedium,
            fontSize = 12.sp)

        Text(
            modifier = Modifier
                .constrainAs(priceText) {
                    top.linkTo(quantity.bottom, dp_8)
                    start.linkTo(image.end, dp_10)
                },
            text = buildString {
                append(stringResource(id = R.string.ruppes))
                append(ProductUtils.roundTo1DecimalPlaces(item.mrp))
            },
            color = colorResource(id = R.color.app_black),
            style = ZustTypography.bodyMedium,
            fontSize = 14.sp,
        )

        Text(
            modifier = Modifier
                .constrainAs(mrpText) {
                    top.linkTo(priceText.bottom)
                    start.linkTo(image.end, dp_10)
                },
            text = buildString {
                append(stringResource(id = R.string.ruppes))
                append(ProductUtils.roundTo1DecimalPlaces(item.price))
            },
            color = colorResource(id = R.color.new_hint_color),
            fontFamily = zustFont,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            style = LocalTextStyle.current.copy(textDecoration = TextDecoration.LineThrough),
        )

        if ((item.quantityOfItemInCart ?: 0) > 0) {
            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .border(border = BorderStroke(width = 1.dp,
                        color = colorResource(id = R.color.light_green)),
                        shape = RoundedCornerShape(4.dp))
                    .wrapContentWidth()
                    .height(22.dp)
                    .constrainAs(incDecContainer) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        top.linkTo(priceText.top)
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
                            callback.invoke(ACTION.DECREASE)
                        },
                    tint = colorResource(id = R.color.white))

                Text(
                    text = item.quantityOfItemInCart.toString(),
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
                            callback.invoke(ACTION.INCREASE)
                        },
                    tint = colorResource(id = R.color.white))

            }
        } else {
            Button(modifier = Modifier
                .constrainAs(addItemContainer) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    top.linkTo(priceText.top)
                }
                .background(color = colorResource(id = R.color.light_green), shape = RoundedCornerShape(4.dp))
                .height(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.light_green),
                    contentColor = colorResource(id = R.color.light_green)),
                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 16.dp),
                onClick = {
                    callback.invoke(ACTION.INCREASE)
                }) {
                Text(text = stringResource(R.string.add),
                    fontWeight = FontWeight.W600, color = colorResource(id = R.color.white),
                    fontFamily = zustFont, fontSize = 12.sp)
            }
        }
    }
}