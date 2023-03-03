package `in`.opening.area.zustapp.orderSummary.compose

import `in`.opening.area.zustapp.R.color
import `in`.opening.area.zustapp.R.string
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
import androidx.compose.material.*
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

val modifierType1 = Modifier
    .fillMaxWidth()
    .background(shape = RoundedCornerShape(topStart = 12.dp,
        topEnd = 12.dp), color = Color(0xffffffff))
    .height(12.dp)

val modifierType2 = Modifier
    .fillMaxWidth()
    .background(shape = RoundedCornerShape(bottomStart = 12.dp,
        bottomEnd = 12.dp), color = Color(0xffffffff))
    .height(12.dp)


val modifierType4 = Modifier
    .fillMaxWidth()
    .wrapContentHeight()
    .background(color = Color(0xffffffff))
    .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 6.dp)

@Composable
fun SelectedCartVerticalItemUi(
    cartItem: ProductSingleItem, modifier: Modifier = Modifier,
    canShowDivider: Boolean,
    incrementCallback: (ProductSingleItem) -> Unit,
    decrementCallback: (ProductSingleItem) -> Unit,
    detailsCallback: (ProductSingleItem) -> Unit,
) {
    ConstraintLayout(modifier = modifier.clickable {
        detailsCallback.invoke(cartItem)
    })
    {
        val (image, title, quantity, priceText, mrpText, incDecContainer,addItemContainer) = createRefs()
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(cartItem.thumbnail)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(60.dp)
                .clip(MaterialTheme.shapes.medium)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
        )

        Text(
            overflow = TextOverflow.Ellipsis, maxLines = 1,
            text = cartItem.productName,
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(image.end, dp_10)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            color = colorResource(id = color.app_black),
            style = Typography_Montserrat.body1,
            fontSize = 14.sp,
        )
        Text(text = buildString {
            append(ProductUtils.getNumberDisplayValue(cartItem.quantity))
            append(" ")
            append(cartItem.quantityUnit.lowercase())
        }, modifier = Modifier.constrainAs(quantity) {
            top.linkTo(title.bottom, dp_8)
            start.linkTo(image.end, dp_10)
        }, color = colorResource(id = color.new_hint_color),
            style = Typography_Montserrat.body2,
            fontSize = 12.sp)

        Text(
            overflow = TextOverflow.Ellipsis, maxLines = 1,
            modifier = Modifier
                .constrainAs(priceText) {
                    top.linkTo(quantity.bottom, dp_8)
                    start.linkTo(image.end, dp_10)
                    bottom.linkTo(parent.bottom, dp_8)
                },
            text = buildString {
                append(stringResource(id = string.ruppes))
                append(ProductUtils.roundTo1DecimalPlaces(cartItem.price))
            },
            color = colorResource(id = color.app_black),
            style = Typography_Montserrat.body1,
            fontSize = 14.sp,
        )

        Text(
            overflow = TextOverflow.Ellipsis, maxLines = 1,
            modifier = Modifier
                .constrainAs(mrpText) {
                    top.linkTo(priceText.top)
                    bottom.linkTo(priceText.bottom)
                    start.linkTo(priceText.end, dp_16)
                    end.linkTo(incDecContainer.start)
                    width = Dimension.fillToConstraints
                },
            text = buildString {
                append(stringResource(id = string.ruppes))
                append(ProductUtils.roundTo1DecimalPlaces(cartItem.mrp))
            },
            color = colorResource(id = color.new_hint_color),
            fontFamily = montserrat,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            style = LocalTextStyle.current.copy(textDecoration = TextDecoration.LineThrough),
        )

        if (cartItem.itemCountByUser > 0) {
            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .border(border = BorderStroke(width = 1.dp,
                        color = colorResource(id = color.light_green)),
                        shape = RoundedCornerShape(4.dp))
                    .wrapContentWidth()
                    .height(22.dp)
                    .constrainAs(incDecContainer) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        top.linkTo(parent.top)
                    }) {
                Icon(painter = painterResource(removeIcon),
                    contentDescription = "",
                    modifier = Modifier
                        .background(color = colorResource(id = color.light_green),
                            shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                        .clip(RoundedCornerShape(topStart = 4.dp,
                            bottomStart = 4.dp))
                        .size(22.dp)
                        .clickable {
                            decrementCallback.invoke(cartItem)
                        },
                    tint = colorResource(id = color.white))

                Text(
                    text = cartItem.itemCountByUser.toString(),
                    modifier = Modifier
                        .defaultMinSize(22.dp)
                        .padding(horizontal = 2.dp)
                        .align(Alignment.CenterVertically),
                    style = Typography_Montserrat.body1,
                    fontSize = 12.sp,
                    color = colorResource(id = color.app_black),
                    textAlign = TextAlign.Center,
                )

                Icon(painter = painterResource(addIcon),
                    contentDescription = "",
                    modifier = Modifier
                        .size(22.dp)
                        .clip(RoundedCornerShape(topEnd = 4.dp,
                            bottomEnd = 4.dp))
                        .background(color = colorResource(id = color.light_green),
                            shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                        .clickable {
                            incrementCallback.invoke(cartItem)
                        },
                    tint = colorResource(id = color.white))

            }
        }else{
            Button(modifier = Modifier
                .constrainAs(addItemContainer) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    top.linkTo(parent.top)
                }
                .background(color = colorResource(id = color.light_green), shape = RoundedCornerShape(4.dp))
                .height(24.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = color.light_green),
                    contentColor = colorResource(id = color.light_green)),
                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 16.dp),
                onClick = {
                    incrementCallback.invoke(cartItem)
                }) {
                Text(text = stringResource(string.add),
                    fontWeight = FontWeight.W600, color = colorResource(id = color.white),
                    fontFamily = montserrat, fontSize = 12.sp)
            }
        }

    }
    if (canShowDivider) {
        Divider(modifier = Modifier
            .height(1.dp)
            .fillMaxWidth())
    }
}