package zustElectronics.zeProductDetails.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.dp_6
import `in`.opening.area.zustapp.ui.theme.montserratFontFamily
import zustElectronics.zeProductDetails.model.ZeProductDetailsData
import zustElectronics.zeProductDetails.uiState.ZeProductDetailsUiState
import zustElectronics.zeProductDetails.viewModel.ZeProductDetailViewModel

@Composable
fun ZeProductDetailMainUi(zeProductDetailViewModel: ZeProductDetailViewModel) {
    val zeProductDetailsUiState by zeProductDetailViewModel.productDetailsUiState.collectAsState()
    when (zeProductDetailsUiState) {
        is ZeProductDetailsUiState.LoadingState -> {

        }

        is ZeProductDetailsUiState.SuccessState -> {
            (zeProductDetailsUiState as ZeProductDetailsUiState.SuccessState).data?.let { data ->
                ConstraintLayout(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()) {
                    val (bottomStrip, scrollableSection) = createRefs()
                    LazyColumn(modifier = Modifier.constrainAs(scrollableSection) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(bottomStrip.top)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }) {
                        item {
                            ProductTitleImageSection(data)
                        }
                        item {
                            ZeProductSpecifications(data)
                        }
                        item {
                            ProductDescriptionSection(data)
                        }
                    }
                    ConstraintLayout(modifier = Modifier
                        .constrainAs(bottomStrip) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .fillMaxWidth()) {
                        val (pricing, buyNowBtn) = createRefs()
                        Text(text = "123", modifier = Modifier.constrainAs(pricing) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        })

                        Button(onClick = {

                        }, modifier = Modifier.constrainAs(buyNowBtn) {
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        }) {
                            Text(
                                text = "Buy now",
                                fontFamily = montserratFontFamily
                            )
                        }
                    }
                }
            }
        }

        is ZeProductDetailsUiState.ErrorState -> {

        }
    }
}

@Composable
private fun ProductTitleImageSection(data: ZeProductDetailsData) {
    ConstraintLayout(modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth()) {
        Text(
            text = data.modelNumber,
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.W600
        )
        ZeProductImages(data.images)
    }
}

@Composable
private fun ZeProductImages(images: String) {
    val productImages = images.split(",")
    if (productImages.isNotEmpty()) {
        LazyRow {
            items(productImages) { img ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(img)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .height(90.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

@Composable
private fun ProductDescriptionSection(data: ZeProductDetailsData) {
    ConstraintLayout(modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth()) {
        Text(
            text = data.name,
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.W400
        )
    }
}

@Composable
private fun ZeProductSpecifications(data: ZeProductDetailsData) {
    Text(text = data.description)
}