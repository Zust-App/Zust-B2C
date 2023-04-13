package `in`.opening.area.zustapp.productDetails.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.CustomAnimatedProgressBar
import `in`.opening.area.zustapp.product.ProductSelectionListener
import `in`.opening.area.zustapp.product.addIcon
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.product.removeIcon
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.uiModels.ProductDetailsUiState
import `in`.opening.area.zustapp.utility.ProductUtils
import `in`.opening.area.zustapp.viewmodels.ProductDetailsViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import java.util.*

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
                            Text(text = data.productPriceSingleItems[0].productName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ENGLISH) else it.toString() }, color = colorResource(id = R.color.app_black),
                                fontFamily = montserrat,
                                fontWeight = FontWeight.W600,
                                fontSize = 16.sp, modifier = Modifier.padding(horizontal = 16.dp))
                            Spacer(modifier = Modifier.height(24.dp))
                        }

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
                                        .width(175.dp)
                                        .constrainAs(imageIcon) {
                                            top.linkTo(parent.top, dp_24)
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                            bottom.linkTo(parent.bottom, dp_24)
                                        }, alignment = Alignment.Center)
                            }
                        }

                        item {
                            Divider(modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(color = colorResource(id = com.google.android.material.R.color.material_divider_color)))
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "Pack Sizes",
                                fontFamily = openSansFontFamily,
                                fontWeight = FontWeight.W400,
                                fontSize = 16.sp,
                                color = colorResource(id = R.color.app_black),
                                modifier = Modifier.padding(horizontal = 16.dp)
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
                                text = "Important Information",
                                style = Typography_Okra.body1,
                                color = colorResource(id = R.color.app_black),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = data.productPriceSingleItems[0].description,
                                style = Typography_Montserrat.body2,
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
        }
    }
}

@Composable
private fun MultipleVariantItemUI(productSingleItem: ProductSingleItem, callback: ProductSelectionListener) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .border( // Set the background color
            shape = RoundedCornerShape(4.dp), // Set the corner shape
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
        }, fontFamily = montserrat,
            fontWeight = FontWeight.W500,
            fontSize = 14.sp,
            color = colorResource(id = R.color.new_hint_color))

        Text(text = stringResource(id = R.string.ruppes) + " " + productSingleItem.price.toString(),
            modifier = Modifier.constrainAs(priceText) {
                start.linkTo(quantityUnitText.start, dp_8)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(incDecContainer.start, dp_8)
            }, fontFamily = montserrat,
            fontWeight = FontWeight.W600,
            fontSize = 16.sp,
            color = colorResource(id = R.color.app_black))

        Text(
            text = stringResource(id = R.string.ruppes) + " " + productSingleItem.mrp.toString(),
            modifier = Modifier.constrainAs(mrpText) {
                start.linkTo(priceText.end, dp_8)
                top.linkTo(priceText.top)
                bottom.linkTo(priceText.bottom)
                end.linkTo(incDecContainer.start, dp_8)
                width = Dimension.fillToConstraints
            },
            style = TextStyle(textDecoration = TextDecoration.LineThrough,
                fontFamily = montserrat,
                fontWeight = FontWeight.W400,
                fontSize = 12.sp),
            color = colorResource(id = R.color.new_hint_color),
        )

        if (productSingleItem.itemCountByUser > 0) {
            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .wrapContentWidth()
                    .constrainAs(incDecContainer) {
                        top.linkTo(parent.top, dp_8)
                        end.linkTo(parent.end, dp_12)
                        bottom.linkTo(parent.bottom, dp_8)
                    }
                    .background(color = colorResource(id = R.color.light_green),
                        shape = RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .wrapContentHeight()) {
                Icon(painter = painterResource(removeIcon), contentDescription = "",
                    modifier = Modifier
                        .size(22.dp)
                        .clickable {
                            callback.didTapOnDecrementCount(productSingleItem)
                        }, tint = colorResource(id = R.color.white))

                Text(text = productSingleItem.itemCountByUser.toString(),
                    modifier = Modifier.padding(start = dp_8,
                        end = dp_8),
                    fontWeight = FontWeight.W600,
                    fontFamily = montserrat, fontSize = 14.sp,
                    color = colorResource(id = R.color.white),
                    textAlign = TextAlign.Center)

                Icon(painter = painterResource(addIcon), contentDescription = "", modifier = Modifier
                    .size(22.dp)
                    .padding(0.dp)
                    .clickable {
                        callback.didTapOnIncrementCount(productSingleItem)
                    }, tint = colorResource(id = R.color.white))
            }
        } else {
            Button(modifier = Modifier
                .constrainAs(incDecContainer) {
                    top.linkTo(parent.top, dp_8)
                    end.linkTo(parent.end, dp_12)
                    bottom.linkTo(parent.bottom, dp_8)
                }
                .background(color = colorResource(id = R.color.light_green), shape = RoundedCornerShape(8.dp))
                .height(26.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.light_green),
                    contentColor = colorResource(id = R.color.light_green)),
                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 20.dp),
                onClick = {
                    callback.didTapOnIncrementCount(productSingleItem)
                }) {
                Text(text = stringResource(R.string.add),
                    fontWeight = FontWeight.W600, color = colorResource(id = R.color.white),
                    fontFamily = montserrat, fontSize = 12.sp)
            }
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
    }
}