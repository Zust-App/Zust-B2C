package non_veg.cart.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.orderSummary.compose.eligibleFreeDeliveryText
import `in`.opening.area.zustapp.orderSummary.compose.gradientColors
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.zustFont
import `in`.opening.area.zustapp.utility.ProductUtils
import non_veg.cart.uiModel.NonVegCartItemSummaryUiModel
import non_veg.cart.viewmodel.NonVegCartViewModel
import non_veg.common.model.CartSummaryData

@Composable
fun NonVegBillingContainer(nonVegCartViewModel: NonVegCartViewModel = viewModel()) {
    val cartSummaryUiModel by nonVegCartViewModel.cartSummaryUiModel.collectAsState()//this will update the cart value

    when (cartSummaryUiModel) {
        is NonVegCartItemSummaryUiModel.Success -> {
            (cartSummaryUiModel as NonVegCartItemSummaryUiModel.Success).data?.let {
                NonVegBillingContainerDataHolder(it)
            }
        }

        else -> {

        }
    }
}

@Composable
fun NonVegBillingContainerDataHolder(data: CartSummaryData,modifier: Modifier?=null) {
    val rupees = stringResource(id = R.string.ruppes)

    Column(modifier = modifier?:Modifier
        .fillMaxWidth()
        .background(color = colorResource(id = R.color.white))
        .padding(horizontal = 16.dp, vertical = 16.dp)) {

        Row {
            Text(text = "Bill Summary",
                color = colorResource(id = R.color.app_black),
                style = ZustTypography.body1)
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Text(
                fontWeight = FontWeight.W500,
                color = colorResource(id = R.color.black_4),
                fontFamily = zustFont, fontSize = 12.sp,
                text = "Item price")
            Spacer(modifier = Modifier.weight(1f))

            Text(
                fontWeight = FontWeight.W500,
                color = colorResource(id = R.color.new_hint_color),
                fontFamily = zustFont, fontSize = 12.sp,
                text = rupees + ProductUtils.roundTo1DecimalPlaces(data.itemValueInCart))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Text(
                fontWeight = FontWeight.W500,
                fontFamily = zustFont, fontSize = 12.sp,
                color = colorResource(id = R.color.black_4),
                text = "Delivery Charges")
            Spacer(modifier = Modifier.weight(1f))
            Text(
                fontWeight = FontWeight.W500,
                color = colorResource(id = R.color.new_hint_color),
                fontFamily = zustFont, fontSize = 12.sp,
                text = rupees + if (data.deliveryFee == 0.0) {
                    "0"
                } else {
                    ProductUtils.roundTo1DecimalPlaces(data.deliveryFee)
                })
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Text(
                fontWeight = FontWeight.W500,
                fontFamily = zustFont, fontSize = 12.sp,
                color = colorResource(id = R.color.black_4),
                text = "Packaging fee")
            Spacer(modifier = Modifier.weight(1f))
            Text(
                fontWeight = FontWeight.W500,
                color = colorResource(id = R.color.new_hint_color),
                fontFamily = zustFont, fontSize = 12.sp,
                text = rupees + if (data.packagingFee == 0.0) {
                    "0"
                } else {
                    ProductUtils.roundTo1DecimalPlaces(data.packagingFee)
                })
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            fontWeight = FontWeight.W500,
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = gradientColors,
                    ),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp),
            color = colorResource(id = R.color.app_black),
            fontFamily = zustFont, fontSize = 12.sp,

            text = if (data.deliveryFee == 0.0) {
                eligibleFreeDeliveryText
            } else {
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append("Free Delivery ")
                    }
                    append("above ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append(stringResource(id = R.string.ruppes) + "99")
                    }
                }
            })

        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Text(
                fontWeight = FontWeight.W500,
                fontFamily = zustFont, fontSize = 14.sp,
                color = colorResource(id = R.color.new_material_primary),
                text = "Total bill")
            Spacer(modifier = Modifier.weight(1f))
            Text(
                fontWeight = FontWeight.W600,
                color = colorResource(id = R.color.new_material_primary),
                fontFamily = zustFont, fontSize = 14.sp,
                text = rupees + ProductUtils.roundTo1DecimalPlaces(((data.itemValueInCart ?: (0.0 + (data.deliveryFee ?: 0.0) + (data.serviceFee ?: 0.0) + (data.packagingFee ?: 0.0))))))
        }

    }
}