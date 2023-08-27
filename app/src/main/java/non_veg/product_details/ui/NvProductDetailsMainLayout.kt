package non_veg.product_details.ui

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.product.addIcon
import `in`.opening.area.zustapp.product.removeIcon
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_10
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_20
import `in`.opening.area.zustapp.ui.theme.dp_24
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.dp_6
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.ACTION
import non_veg.listing.models.NonVegListingSingleItem
import non_veg.listing.ui.dummyUrl
import non_veg.product_details.uimodel.NvProductDetailsUiState
import non_veg.product_details.viewmodel.NvProductDetailsViewModel

@Composable
fun NvProductDetailsMainLayout(paddingValues: PaddingValues, nonVegProductDetailsViewModel: NvProductDetailsViewModel) {
    val nonVegProductUiModel by nonVegProductDetailsViewModel.nvProductDetailUiModel.collectAsState(NvProductDetailsUiState.Initial(false))
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(paddingValues)) {
        val (loader) = createRefs()
        if (nonVegProductUiModel.isLoading) {
            CircularProgressIndicator(modifier = Modifier
                .size(dp_24)
                .constrainAs(loader) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }, color = colorResource(id = R.color.new_material_primary))
        }
        when (val response = nonVegProductUiModel) {
            is NvProductDetailsUiState.Success -> {
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()) {
                    if (!response.data.isNullOrEmpty()) {
                        item {
                            NvProductDetailsImageUi()
                        }
                        item {
                            ProductDetailsTitlePriceSectionUi(response.data[0], nonVegProductDetailsViewModel)
                        }
                        item {
                            NvProductDetailsMinorInfoUi(response.data[0])
                        }

                        item {
                            NvProductDetailsDescSection(response.data[0].productDescription)
                        }

                    }
                }

            }

            is NvProductDetailsUiState.Error -> {
                AppUtility.showToast(LocalContext.current, "Something went wrong")
            }

            else -> {

            }

        }
    }
}


@Composable
private fun NvProductDetailsImageUi() {
    LazyRow(modifier = Modifier
        .fillMaxWidth()
        .height(240.dp)) {
        item {
            AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(dummyUrl).crossfade(true).build(), contentDescription = null, contentScale = ContentScale.FillBounds, modifier = Modifier
                .height(240.dp)
                .fillParentMaxWidth())
        }
    }
}

@Composable
private fun NvProductDetailsDescSection(description: String) {
    Spacer(modifier = Modifier.height(dp_20))
    Text(text = "Description", style = ZustTypography.bodyMedium, modifier = Modifier.padding(horizontal = dp_16), color = colorResource(id = R.color.app_black))
    Spacer(modifier = Modifier.height(dp_6))
    Text(text = description, style = ZustTypography.bodyMedium, modifier = Modifier.padding(horizontal = dp_16), color = colorResource(id = R.color.language_default))

}

@Composable
private fun ProductDetailsTitlePriceSectionUi(singleItem: NonVegListingSingleItem, nonVegProductDetailsViewModel: NvProductDetailsViewModel) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = dp_16, vertical = dp_20)) {
        val (title, subTitle, incDescContainer) = createRefs()
        Text(text = singleItem.productName, style = ZustTypography.titleLarge, modifier = Modifier.constrainAs(title) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            end.linkTo(incDescContainer.start, dp_16)
            width = Dimension.fillToConstraints
        }, color = colorResource(id = R.color.app_black))
        Text(text = singleItem.minorTag ?: "", modifier = Modifier.constrainAs(subTitle) {
            top.linkTo(title.bottom, dp_6)
            start.linkTo(parent.start)
            end.linkTo(incDescContainer.start, dp_16)
            width = Dimension.fillToConstraints
        }, color = colorResource(id = R.color.black_3), style = ZustTypography.bodySmall)
        NvAddIncreaseDecContainer(Modifier.constrainAs(incDescContainer) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            end.linkTo(parent.end)
        }, singleItem, nonVegProductDetailsViewModel)
    }
}

@Composable
private fun NvProductDetailsMinorInfoUi(nonVegListingSingleItem: NonVegListingSingleItem) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = dp_16)
        .background(color = colorResource(id = R.color.screen_surface_color), shape = RoundedCornerShape(dp_4))
        .padding(vertical = dp_12, horizontal = dp_12)) {
        val (info1, divider1, info2) = createRefs()

        Row(modifier = Modifier
            .constrainAs(info1) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
                end.linkTo(divider1.start)
            }
            .wrapContentWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = R.drawable.home_icon), contentDescription = "pieces", modifier = Modifier.size(dp_16))
            Text(text = nonVegListingSingleItem.piecesDescription ?: "", color = colorResource(id = R.color.black_2), style = ZustTypography.bodySmall, modifier = Modifier.padding(start = dp_4) // Adjust padding as needed
            )
        }

        Divider(modifier = Modifier
            .constrainAs(divider1) {
                start.linkTo(info1.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                centerHorizontallyTo(parent)
                height = Dimension.fillToConstraints
            }
            .width(1.dp))

        Row(modifier = Modifier
            .constrainAs(info2) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                start.linkTo(divider1.end)
            }
            .wrapContentWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = R.drawable.home_icon), contentDescription = "pieces", modifier = Modifier.size(dp_16))
            Text(text = nonVegListingSingleItem.servesDescription ?: "", color = colorResource(id = R.color.black_2), style = ZustTypography.bodySmall, modifier = Modifier.padding(start = dp_4) // Adjust padding as needed
            )
        }
    }
}

@Composable
private fun NvAddIncreaseDecContainer(modifier: Modifier, singleItem: NonVegListingSingleItem, viewModel: NvProductDetailsViewModel) {
    val inCart = singleItem.quantityOfItemInCart != null && (singleItem.quantityOfItemInCart ?: 0) > 0
    Row(horizontalArrangement = Arrangement.Center, modifier = modifier
        .border(border = BorderStroke(width = 1.dp, color = colorResource(id = R.color.light_green)), shape = RoundedCornerShape(4.dp))
        .clip(RoundedCornerShape(4.dp))
        .wrapContentWidth()
        .background(color = colorResource(id = R.color.white))
        .height(22.dp)) {
        if (!inCart) {
            Text(
                text = "ADD",
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        viewModel.handleNonVegCartInsertionOrUpdate(singleItem, ACTION.INCREASE)
                    }
                    .padding(horizontal = dp_10),
                style = ZustTypography.bodyMedium,
                fontSize = 12.sp,
                color = colorResource(id = R.color.app_black),
                textAlign = TextAlign.Center,
            )
        } else {
            Icon(painter = painterResource(removeIcon), contentDescription = "", modifier = Modifier
                .background(color = colorResource(id = R.color.light_green), shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                .size(22.dp)
                .clickable {
                    viewModel.handleNonVegCartInsertionOrUpdate(singleItem, ACTION.DECREASE)
                }, tint = colorResource(id = R.color.white))

            Text(
                text = singleItem.quantityOfItemInCart.toString(),
                modifier = Modifier
                    .defaultMinSize(22.dp)
                    .padding(horizontal = 2.dp)
                    .align(Alignment.CenterVertically),
                style = ZustTypography.bodyMedium,
                fontSize = 12.sp,
                color = colorResource(id = R.color.app_black),
                textAlign = TextAlign.Center,
            )

            Icon(painter = painterResource(addIcon), contentDescription = "", modifier = Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                .background(color = colorResource(id = R.color.light_green), shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                .clickable {
                    viewModel.handleNonVegCartInsertionOrUpdate(singleItem, ACTION.INCREASE)
                }, tint = colorResource(id = R.color.white))
        }
    }
}
