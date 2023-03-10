package `in`.opening.area.zustapp.productDetails.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.product.ProductSelectionListener
import `in`.opening.area.zustapp.product.addIcon
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.product.removeIcon
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.uiModels.ProductDetailsUiState
import `in`.opening.area.zustapp.utility.ProductUtils
import `in`.opening.area.zustapp.viewmodels.ProductDetailsViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
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
    productSingleItem: ProductSingleItem?,
    callback: ProductSelectionListener,
) {
    if (productSingleItem == null) {
        return
    }
    val productDetailsUiState by viewModel.singleItemUiState.collectAsState(ProductDetailsUiState.InitialUi(false))
    when (val data = productDetailsUiState) {
        is ProductDetailsUiState.Success -> {
            Column(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight().verticalScroll(rememberScrollState())
                .background(color = colorResource(id = R.color.screen_surface_color))
                .padding(paddingValue)) {
                ConstraintLayout(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .background(color = colorResource(id = R.color.white),
                        shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 20.dp, vertical = 16.dp)) {
                    val (offerPercentage, imageIcon) = createRefs()
                    if (productSingleItem.discountPercentage > 0.0) {
                        Text(text = productSingleItem.discountPercentage.toInt().toString() + "% OFF", modifier = Modifier
                            .background(color = colorResource(id = R.color.new_material_primary), shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp,
                                vertical = 2.dp)
                            .constrainAs(offerPercentage) {
                                start.linkTo(parent.start, 5.dp)
                                top.linkTo(parent.top)
                            }, color = Color.White,
                            fontSize = 12.sp,
                            fontFamily = montserrat,
                            fontWeight = FontWeight.W600)
                    }

                    AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(productSingleItem.thumbnail)
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

                ConstraintLayout(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .background(color = colorResource(id = R.color.white), shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 20.dp, vertical = 16.dp)) {

                    val (title, quantityUnitText, priceText, mrpText, descTitle, descText, incDecContainer, addItemContainer) = createRefs()

                    Text(text = productSingleItem.productName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ENGLISH) else it.toString() }, color = colorResource(id = R.color.app_black), modifier = Modifier.constrainAs(title) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top, dp_8)
                        width = Dimension.fillToConstraints
                    }, fontFamily = montserrat,
                        fontWeight = FontWeight.W600,
                        fontSize = 14.sp)

                    Text(text = ProductUtils.getNumberDisplayValue(productSingleItem.quantity) + " " + productSingleItem.quantityUnit.lowercase(),
                        color = colorResource(id = R.color.new_hint_color),
                        modifier = Modifier.constrainAs(quantityUnitText) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(title.bottom, dp_12)
                            width = Dimension.fillToConstraints
                        }, fontFamily = montserrat,
                        fontWeight = FontWeight.W600,
                        fontSize = 12.sp)

                    Text(text = stringResource(id = R.string.ruppes) + " " + productSingleItem.price.toString(),
                        modifier = Modifier.constrainAs(priceText) {
                            start.linkTo(parent.start)
                            top.linkTo(quantityUnitText.bottom, dp_8)
                        }, fontFamily = montserrat,
                        fontWeight = FontWeight.W600,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.app_black))

                    Text(
                        text = stringResource(id = R.string.ruppes) + " " + productSingleItem.mrp.toString(),
                        modifier = Modifier.constrainAs(mrpText) {
                            start.linkTo(priceText.end, dp_8)
                            top.linkTo(priceText.top, dp_4)
                            width = Dimension.fillToConstraints
                        },
                        style = TextStyle(textDecoration = TextDecoration.LineThrough,
                            fontFamily = montserrat,
                            fontWeight = FontWeight.W400,
                            fontSize = 12.sp),
                        color = colorResource(id = R.color.new_hint_color),
                    )
                    if ((data.singleItem?.itemCountByUser ?: 0) > 0) {
                        Row(horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .wrapContentWidth()
                                .constrainAs(incDecContainer) {
                                    top.linkTo(parent.top, dp_8)
                                    end.linkTo(parent.end)
                                }
                                .background(color = colorResource(id = R.color.light_green),
                                    shape = RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                .wrapContentHeight()) {
                            Icon(painter = painterResource(removeIcon), contentDescription = "",
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable {
                                        callback.didTapOnDecrementCount(data.singleItem)
                                    }, tint = colorResource(id = R.color.white))

                            Text(text = (data.singleItem?.itemCountByUser ?: "").toString(),
                                modifier = Modifier.padding(start = dp_8, end = dp_8),
                                fontWeight = FontWeight.W600,
                                fontFamily = montserrat, fontSize = 12.sp, color = colorResource(id = R.color.white))

                            Icon(painter = painterResource(addIcon), contentDescription = "", modifier = Modifier
                                .size(20.dp)
                                .padding(0.dp)
                                .clickable {
                                    callback.didTapOnIncrementCount(data.singleItem)
                                }, tint = colorResource(id = R.color.white))

                        }
                    } else {
                        Button(modifier = Modifier
                            .constrainAs(addItemContainer) {
                                top.linkTo(parent.top, dp_8)
                                end.linkTo(parent.end)
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

                    Text(
                        text = "Important Information",
                        modifier = Modifier.constrainAs(descTitle) {
                            start.linkTo(parent.start)
                            top.linkTo(priceText.bottom, dp_24)
                            width = Dimension.fillToConstraints
                        },
                        style = Typography_Montserrat.body1,
                        color = colorResource(id = R.color.app_black),
                    )

                    Text(
                        text = productSingleItem.description,
                        modifier = Modifier.constrainAs(descText) {
                            start.linkTo(parent.start)
                            top.linkTo(descTitle.bottom, dp_8)
                            width = Dimension.fillToConstraints
                        },
                        style = Typography_Montserrat.body2,
                        color = colorResource(id = R.color.new_hint_color),
                    )
                }
            }
        }
        is ProductDetailsUiState.InitialUi -> {

        }
    }
}