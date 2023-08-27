package zustbase.orderDetail.ui

import `in`.opening.area.zustapp.R
import zustbase.orderDetail.models.Item
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.utility.ProductUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun OrderedItemSingleUnit(item: Item) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.White)
        .wrapContentHeight()) {
        val (
            productImage, productName, productPrice,
            productQuantity, productNumItems,
        ) = createRefs()
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.thumbnail)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(60.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .constrainAs(productImage) {
                    start.linkTo(parent.start, dp_12)
                    top.linkTo(parent.top, dp_8)
                    bottom.linkTo(parent.bottom, dp_8)
                }
        )
        Text(text = item.productName,
            modifier = Modifier.constrainAs(productName) {
                start.linkTo(productImage.end, dp_12)
                end.linkTo(productNumItems.start, dp_12)
                top.linkTo(parent.top, dp_12)
                width = Dimension.fillToConstraints
            }, style = ZustTypography.bodyMedium,
            color = colorResource(id = R.color.app_black),
            fontSize = 14.sp)


        Text(text = buildString {
            append(ProductUtils.getNumberDisplayValue(item.quantity))
            append(" ")
            append(item.unit.lowercase())
        }, modifier = Modifier.constrainAs(productQuantity) {
            top.linkTo(productName.bottom, dp_8)
            start.linkTo(productImage.end, dp_12)
            end.linkTo(parent.end, dp_12)
            width = Dimension.fillToConstraints
        }, style = ZustTypography.bodyMedium,
            fontSize = 12.sp,
            fontWeight = FontWeight.W600,
            color = colorResource(id = R.color.new_hint_color))

        Text(text = buildString {
            append(stringResource(id = R.string.ruppes))
            append(ProductUtils.roundTo1DecimalPlaces(item.payablePrice))
        }, modifier = Modifier.constrainAs(productPrice) {
            top.linkTo(productQuantity.bottom, dp_10)
            start.linkTo(productImage.end, dp_12)
            end.linkTo(parent.end, dp_12)
            bottom.linkTo(parent.bottom, dp_12)
            width = Dimension.fillToConstraints
        }, style = ZustTypography.bodyMedium,
            fontSize = 14.sp)

        Text(text = buildString {
            append("Qty-")
            append(item.numberOfItem)
        }, modifier = Modifier.constrainAs(productNumItems) {
            end.linkTo(parent.end, dp_12)
            top.linkTo(parent.top, dp_12)
        }, style = ZustTypography.bodyMedium,
            color = colorResource(id = R.color.new_hint_color),
            fontSize = 12.sp)
    }
}