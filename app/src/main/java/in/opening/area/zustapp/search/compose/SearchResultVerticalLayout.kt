package `in`.opening.area.zustapp.search.compose

import `in`.opening.area.zustapp.R
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
fun SearchResultVerticalLayout(
    cartItem: ProductSingleItem,
    modifier: Modifier = Modifier,
    incrementCallback: (ProductSingleItem) -> Unit,
    decrementCallback: (ProductSingleItem) -> Unit,
    detailsCallback: (ProductSingleItem) -> Unit,
) {
    ConstraintLayout(modifier = modifier.clickable {
        detailsCallback.invoke(cartItem)
    })
    {
        val (image, title, quantity, priceText, mrpText, incDecContainer, addItemContainer) = createRefs()
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(cartItem.thumbnail)
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
            text = cartItem.productName,
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
            append(ProductUtils.getNumberDisplayValue(cartItem.quantity))
            append(" ")
            append(cartItem.quantityUnit.lowercase())
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
                append(ProductUtils.roundTo1DecimalPlaces(cartItem.price))
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
                append(ProductUtils.roundTo1DecimalPlaces(cartItem.mrp))
            },
            color = colorResource(id = R.color.new_hint_color),
            fontFamily = zustFont,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            style = LocalTextStyle.current.copy(textDecoration = TextDecoration.LineThrough),
        )

        if (cartItem.itemCountByUser > 0) {
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
                            decrementCallback.invoke(cartItem)
                        },
                    tint = colorResource(id = R.color.white))

                Text(
                    text = cartItem.itemCountByUser.toString(),
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
                            incrementCallback.invoke(cartItem)
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
                    incrementCallback.invoke(cartItem)
                }) {
                Text(text = stringResource(R.string.add),
                    fontWeight = FontWeight.W600, color = colorResource(id = R.color.white),
                    fontFamily = zustFont, fontSize = 12.sp)
            }
        }
    }
}