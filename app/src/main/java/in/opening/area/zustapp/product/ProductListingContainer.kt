package `in`.opening.area.zustapp.product

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.R.*
import `in`.opening.area.zustapp.compose.CustomAnimatedProgressBar
import `in`.opening.area.zustapp.compose.NoProductFoundErrorPage
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.product.compose.ProductListingShimmerUi
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.uiModels.productList.ProductListUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.ProductUtils
import `in`.opening.area.zustapp.viewmodels.ProductListingViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.util.*

const val KEY_BANNER = "banner"

@Composable
fun ProductListingContainer(
    layoutScope: ConstraintLayoutScope,
    productList: ConstrainedLayoutReference,
    productListingViewModel: ProductListingViewModel,
    listener: ProductSelectionListener,
    otherCategoryUi: ConstrainedLayoutReference,
) {
    val context = LocalContext.current
    val productResponse by productListingViewModel.productListUiState.collectAsState(ProductListUi.InitialUi(false))
    val response = productResponse

    var showHidePgBar by remember {
        mutableStateOf(false)
    }

    showHidePgBar = response.isLoading

    layoutScope.apply {
        val (pgBar) = createRefs()
        when (response) {
            is ProductListUi.ErrorUi -> {
                showHidePgBar = response.isLoading
                if (!response.errorMsg.isNullOrEmpty()) {
                    AppUtility.showToast(context, response.errorMsg)
                } else {
                    AppUtility.showToast(context, response.errors.getTextMsg())
                }
            }
            is ProductListUi.InitialUi -> {
                showHidePgBar = response.isLoading
            }
            is ProductListUi.ProductListSuccess -> {
                if (!response.data.productItems.isNullOrEmpty()) {
                    LazyVerticalGrid(columns = GridCells.Fixed(2),
                        modifier = Modifier.constrainAs(productList) {
                            top.linkTo(otherCategoryUi.bottom, dp_8)
                            bottom.linkTo(parent.bottom, 8.dp)
                            start.linkTo(parent.start, dp_20)
                            end.linkTo(parent.end, dp_20)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }) {
                        item(span = { GridItemSpan(currentLineSpan = 2) }) {
                            Text(text = "Items", style = Typography_Montserrat.body1,
                                color = colorResource(id = R.color.app_black))
                        }
                        item(span = { GridItemSpan(currentLineSpan = 2) }) {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                        response.data.productItems?.forEach { productSingleItem: ProductSingleItem ->
                            item(productSingleItem.productPriceId) {
                                ProductSingleItem(productSingleItem, listener)
                            }
                        }
                    }
                } else {
                    NoProductFoundErrorPage(layoutScope, otherCategoryUi) {
                        listener.openSuggestProduct()
                    }
                }
            }
        }
        if (showHidePgBar) {
            ProductListingShimmerUi(modifier = Modifier
                .padding(20.dp)
                .constrainAs(productList) {
                    top.linkTo(otherCategoryUi.bottom, dp_40)
                    bottom.linkTo(parent.bottom, 8.dp)
                    start.linkTo(parent.start, dp_20)
                    end.linkTo(parent.end, dp_20)
                }
                .fillMaxWidth()
                .fillMaxHeight())
            CustomAnimatedProgressBar(modifier = Modifier.constrainAs(pgBar) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })
        }
    }
}

@Composable
private fun ProductSingleItem(productSingleItem: ProductSingleItem, callback: ProductSelectionListener) {
    ConstraintLayout(modifier = Modifier
        .padding(end = 6.dp,
            bottom = 10.dp)
        .clip(MaterialTheme.shapes.small)
        .background(color = colorResource(id = color.white), shape = RoundedCornerShape(8.dp))
        .padding(bottom = 10.dp, top = 10.dp, end = 10.dp)
        .clickable {
            callback.didTapOnContainerClick(productSingleItem)
        }
        .wrapContentHeight()) {
        val (imageIcon, titleText, priceText, mrpText, offerPercentage, quantityUnitText, incDecContainer, addItemContainer) = createRefs()
        AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(productSingleItem.thumbnail).allowHardware(true).build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(80.dp)
                .width(80.dp)
                .constrainAs(imageIcon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, alignment = Alignment.Center)

        Text(text = productSingleItem.productName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ENGLISH) else it.toString() }, color = colorResource(id = color.app_black), modifier = Modifier.constrainAs(titleText) {
            start.linkTo(parent.start, dp_8)
            end.linkTo(parent.end)
            top.linkTo(imageIcon.bottom, dp_8)
            width = Dimension.fillToConstraints
        }, fontFamily = montserrat,
            fontWeight = FontWeight.W600,
            fontSize = 14.sp)

        Text(text = ProductUtils.getNumberDisplayValue(productSingleItem.quantity) + " " + productSingleItem.quantityUnit.lowercase(),
            color = colorResource(id = color.new_hint_color),
            modifier = Modifier.constrainAs(quantityUnitText) {
                start.linkTo(parent.start, dp_10)
                end.linkTo(parent.end)
                top.linkTo(titleText.bottom, dp_12)
                width = Dimension.fillToConstraints
            }, fontFamily = montserrat,
            fontWeight = FontWeight.W600,
            fontSize = 12.sp)

        Text(text = stringResource(id = string.ruppes) + " " + ProductUtils.roundTo1DecimalPlaces(productSingleItem.price), modifier = Modifier.constrainAs(priceText) {
            start.linkTo(parent.start, dp_10)
            top.linkTo(quantityUnitText.bottom, dp_8)
        }, fontFamily = montserrat,
            fontWeight = FontWeight.W600,
            fontSize = 14.sp,
            color = colorResource(id = color.app_black))

        Text(
            text = stringResource(id = string.ruppes) + " " + ProductUtils.roundTo1DecimalPlaces(productSingleItem.mrp),
            modifier = Modifier.constrainAs(mrpText) {
                start.linkTo(priceText.end, dp_8)
                top.linkTo(priceText.top)
                bottom.linkTo(priceText.bottom)
                width = Dimension.fillToConstraints
            },
            style = TextStyle(textDecoration = TextDecoration.LineThrough,
                fontFamily = montserrat,
                fontWeight = FontWeight.W400,
                fontSize = 12.sp),
            color = colorResource(id = color.new_hint_color),
        )

        if (productSingleItem.itemCountByUser > 0) {

            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .border(border = BorderStroke(width = 1.dp,
                        color = colorResource(id = color.light_green)),
                        shape = RoundedCornerShape(4.dp))
                    .wrapContentWidth()
                    .height(24.dp)
                    .constrainAs(incDecContainer) {
                        top.linkTo(priceText.bottom, dp_12)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }) {
                Icon(painter = painterResource(removeIcon),
                    contentDescription = "",
                    modifier = Modifier
                        .background(color = colorResource(id = color.light_green),
                            shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                        .clip(RoundedCornerShape(topStart = 4.dp,
                            bottomStart = 4.dp))
                        .size(24.dp)
                        .clickable {
                            callback.didTapOnDecrementCount(productSingleItem)
                        },
                    tint = colorResource(id = color.white))

                Text(
                    text = productSingleItem.itemCountByUser.toString(),
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
                        .size(24.dp)
                        .clip(RoundedCornerShape(topEnd = 4.dp,
                            bottomEnd = 4.dp))
                        .background(color = colorResource(id = color.light_green),
                            shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                        .clickable {
                            callback.didTapOnIncrementCount(productSingleItem)
                        },
                    tint = colorResource(id = color.white))

            }
        } else {
            Button(modifier = Modifier
                .constrainAs(addItemContainer) {
                    top.linkTo(priceText.bottom, dp_12)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .background(color = colorResource(id = color.light_green), shape = RoundedCornerShape(4.dp))
                .height(24.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = color.light_green),
                    contentColor = colorResource(id = color.light_green)),
                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 16.dp),
                onClick = {
                    callback.didTapOnIncrementCount(productSingleItem)
                }) {
                Text(text = stringResource(string.add),
                    fontWeight = FontWeight.W600, color = colorResource(id = color.white),
                    fontFamily = montserrat, fontSize = 12.sp)
            }
        }

        if (productSingleItem.discountPercentage > 0.0) {
            Text(text = productSingleItem.discountPercentage.toInt().toString() + "% OFF", modifier = Modifier
                .background(color = colorResource(id = color.new_material_primary), shape = RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp,
                    vertical = 2.dp)
                .constrainAs(offerPercentage) {
                    start.linkTo(parent.start, 5.dp)
                    top.linkTo(parent.top)
                }, color = Color.White,
                fontSize = 10.sp,
                fontFamily = montserrat,
                fontWeight = FontWeight.W600)
        }
    }

}

const val addIcon = drawable.ic_baseline_add_24
const val removeIcon = drawable.ic_baseline_remove_24

interface ProductSelectionListener {
    fun didTapOnIncrementCount(productSingleItem: ProductSingleItem?)
    fun didTapOnDecrementCount(productSingleItem: ProductSingleItem?)
    fun didTapOnContainerClick(productSingleItem: ProductSingleItem?)
    fun openSuggestProduct() {

    }
}