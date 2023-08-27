package `in`.opening.area.zustapp.productDetails.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.CustomAnimatedProgressBar
import `in`.opening.area.zustapp.home.components.FullScreenErrorUi
import `in`.opening.area.zustapp.product.ProductSelectionListener
import `in`.opening.area.zustapp.product.addIcon
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.product.removeIcon
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_24
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.ui.theme.okraFontFamily
import `in`.opening.area.zustapp.ui.theme.zustFont
import `in`.opening.area.zustapp.uiModels.ProductDetailsUiState
import `in`.opening.area.zustapp.utility.ProductUtils
import `in`.opening.area.zustapp.viewmodels.ProductDetailsViewModel
import ui.colorBlack
import ui.colorWhite
import java.util.Locale

@Composable
fun ProductDetailMainUi(
    viewModel: ProductDetailsViewModel,
    paddingValue: PaddingValues,
    callback: ProductSelectionListener,
) {
    val productDetailsUiState by viewModel.singleItemUiState.collectAsState(ProductDetailsUiState.InitialUi(false))

    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
        val (progressbar) = createRefs()
        when (val data = productDetailsUiState) {
            is ProductDetailsUiState.Success -> {
                if (data.isLoading) {
                    CustomAnimatedProgressBar(modifier = Modifier.constrainAs(progressbar) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    })
                }

                if (!data.productPriceSingleItems.isNullOrEmpty()) {
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(color = colorResource(id = R.color.white))
                        .padding(paddingValue)
                        .padding(vertical = 16.dp)) {
                        item {
                            ConstraintLayout(modifier = Modifier
                                .fillMaxWidth()) {
                                val (imageIcon) = createRefs()
                                AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(data.productPriceSingleItems[0].thumbnail)
                                    .allowHardware(true).build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .height(175.dp)
                                        .fillMaxWidth()
                                        .constrainAs(imageIcon) {
                                            top.linkTo(parent.top, dp_24)
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                            bottom.linkTo(parent.bottom, dp_24)
                                        }, alignment = Alignment.Center)
                            }
                        }
                        item {
                            Text(text = data.productPriceSingleItems[0].productName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ENGLISH) else it.toString() }, color = colorResource(id = R.color.app_black), modifier = Modifier.padding(horizontal = 16.dp),
                                style = ZustTypography.titleLarge)
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                        item {
                            Divider(modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(color = colorResource(id = com.google.android.material.R.color.material_divider_color)))
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "Pack Sizes",
                                color = colorResource(id = R.color.app_black),
                                modifier = Modifier.padding(horizontal = 16.dp),
                                style = ZustTypography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        data.productPriceSingleItems.forEach {
                            item {
                                MultipleVariantItemUI(it, callback)
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "Description",
                                style = ZustTypography.bodyLarge,
                                color = colorResource(id = R.color.app_black),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = data.productPriceSingleItems[0].description,
                                style = ZustTypography.bodyMedium,
                                color = colorResource(id = R.color.new_hint_color), modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }

            is ProductDetailsUiState.InitialUi -> {
                if (data.isLoading) {
                    CustomAnimatedProgressBar(modifier = Modifier.constrainAs(progressbar) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    })
                }
            }
            is ProductDetailsUiState.ErrorUi->{
                FullScreenErrorUi(errorCode = null, retryCallback = {
                    viewModel.getProductDetails()
                }) {

                }
            }
        }
    }
}

@Composable
private fun MultipleVariantItemUI(productSingleItem: ProductSingleItem, callback: ProductSelectionListener) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .border(
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(0.5.dp, colorResource(id = R.color.new_hint_color)) // Set the border
        )) {
        val (
            priceText, incDecContainer, quantityUnitText, mrpText, offerPercentage,
        ) = createRefs()

        Text(text = buildString {
            append(ProductUtils.getNumberDisplayValue(productSingleItem.quantity))
            append(" ")
            append(productSingleItem.quantityUnit.lowercase())
        }, modifier = Modifier.constrainAs(quantityUnitText) {
            start.linkTo(parent.start, dp_12)
            top.linkTo(parent.top, dp_16)
            bottom.linkTo(parent.bottom, dp_16)
        }, fontFamily = zustFont,
            fontWeight = FontWeight.W500,
            fontSize = 14.sp,
            color = colorResource(id = R.color.new_hint_color))

        Text(text = stringResource(id = R.string.ruppes) + " " + ProductUtils.roundTo1DecimalPlaces(productSingleItem.price),
            modifier = Modifier.constrainAs(priceText) {
                start.linkTo(quantityUnitText.start, dp_8)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(incDecContainer.start, dp_8)
            }, fontFamily = zustFont,
            fontWeight = FontWeight.W600,
            fontSize = 16.sp,
            color = colorResource(id = R.color.app_black))

        if (productSingleItem.discountPercentage > 0) {
            Text(
                text = stringResource(id = R.string.ruppes) + " " + ProductUtils.roundTo1DecimalPlaces(productSingleItem.mrp),
                modifier = Modifier.constrainAs(mrpText) {
                    start.linkTo(priceText.end, dp_8)
                    top.linkTo(priceText.top)
                    bottom.linkTo(priceText.bottom)
                    end.linkTo(incDecContainer.start, dp_8)
                    width = Dimension.fillToConstraints
                },
                style = TextStyle(textDecoration = TextDecoration.LineThrough,
                    fontFamily = zustFont,
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp),
                color = colorResource(id = R.color.new_hint_color),
            )
        }
        if (productSingleItem.discountPercentage > 0.0) {
            Text(text = productSingleItem.discountPercentage.toInt().toString() + "% OFF", modifier = Modifier
                .background(color = colorResource(id = R.color.light_offer_color),
                    shape = RoundedCornerShape(topStart = 4.dp))
                .padding(horizontal = 4.dp)
                .constrainAs(offerPercentage) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }, color = Color.White,
                fontSize = 12.sp,
                fontFamily = okraFontFamily,
                fontWeight = FontWeight.W600)
        }

        val inCart = productSingleItem.itemCountByUser > 0
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier
            .border(border = BorderStroke(width = 1.dp, color = colorResource(id = R.color.light_green)), shape = RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp))
            .wrapContentWidth()
            .background(color = colorResource(id = R.color.white))
            .wrapContentHeight()
            .constrainAs(incDecContainer) {
                top.linkTo(parent.top, dp_8)
                end.linkTo(parent.end, dp_12)
                bottom.linkTo(parent.bottom, dp_8)
            }) {
            if (!inCart) {
                Text(
                    text = "ADD",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .background(color = colorResource(id = R.color.light_green))
                        .clickable {
                            callback.didTapOnIncrementCount(productSingleItem)
                        }
                        .width(65.dp)
                        .size(dp_24)
                        .wrapContentHeight(),
                    style = ZustTypography.titleMedium,
                    fontSize = 12.sp,
                    color = colorWhite,
                    textAlign = TextAlign.Center,
                )
            } else {
                Icon(painter = painterResource(removeIcon), contentDescription = "", modifier = Modifier
                    .background(color = colorResource(id = R.color.light_green), shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                    .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                    .size(22.dp)
                    .clickable {
                        callback.didTapOnDecrementCount(productSingleItem)
                    }, tint = colorWhite)

                Text(
                    text = productSingleItem.itemCountByUser.toString(),
                    modifier = Modifier
                        .defaultMinSize(22.dp)
                        .padding(horizontal = 2.dp)
                        .align(Alignment.CenterVertically),
                    style = ZustTypography.bodyMedium,
                    fontSize = 12.sp,
                    color = colorBlack,
                    textAlign = TextAlign.Center,
                )

                Icon(painter = painterResource(addIcon), contentDescription = "", modifier = Modifier
                    .size(22.dp)
                    .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                    .background(color = colorResource(id = R.color.light_green), shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                    .clickable {
                        callback.didTapOnIncrementCount(productSingleItem)
                    }, tint = colorWhite)
            }
        }
    }
}