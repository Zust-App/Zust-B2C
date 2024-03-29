package `in`.opening.area.zustapp.product.others

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics
import `in`.opening.area.zustapp.product.compose.OtherCategoryShimmerUi
import `in`.opening.area.zustapp.product.model.OtherCategoriesUiModel
import `in`.opening.area.zustapp.product.model.SingleCategoryData
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.viewmodels.ProductListingViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ConstraintLayoutScope.OtherCategoryUi(
    otherCategoryUi: ConstrainedLayoutReference,
    productListingViewModel: ProductListingViewModel,
) {

    val allCategoryData by productListingViewModel.categoryListUiState.collectAsState(initial = OtherCategoriesUiModel.InitialUi(false))

    apply {
        when (val data = allCategoryData) {
            is OtherCategoriesUiModel.SuccessUi -> {
                LazyRow(modifier = Modifier
                    .constrainAs(otherCategoryUi) {
                        top.linkTo(parent.top, dp_8)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(horizontal = dp_8),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    items(data.data ?: arrayListOf()) { categoryItems ->
                        OptionalCategoryItem(categoryItems, data.selectedCategoryId) { clickedCategory ->
                            productListingViewModel.updateHeaderData(clickedCategory.name)
                            productListingViewModel.updateCategoryIdBasedOnSelection(clickedCategory.id)
                        }
                    }
                }
            }

            is OtherCategoriesUiModel.InitialUi -> {
                if (data.isLoading) {
                    OtherCategoryShimmerUi(modifier = Modifier
                        .constrainAs(otherCategoryUi) {
                            top.linkTo(parent.top, dp_12)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(horizontal = 20.dp))
                }
            }

        }
    }

}

@Composable
fun OptionalCategoryItem(categoryItem: SingleCategoryData, selectedCategoryId: Int?, clickCallback: (SingleCategoryData) -> Unit) {
    if (categoryItem.name.isEmpty()) {
        return
    }
    val isSelectedItem = categoryItem.id == selectedCategoryId
    ConstraintLayout(modifier = Modifier.clickable {
        FirebaseAnalytics.logEvents(FirebaseAnalytics.PRODUCT_LISTING_CATEGORY_CLICK)
        clickCallback.invoke(categoryItem)
    }) {
        val (image, title) = createRefs()
        val combinedModifier = imageModifier
            .background(color = colorResource(id = R.color.white), shape = CircleShape)
            .then(if (isSelectedItem) {
                Modifier.border(
                    width = 3.dp,
                    color = colorResource(id = R.color.new_material_primary),
                    shape = CircleShape
                )
            } else {
                Modifier
            })

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(categoryItem.thumbnail)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = combinedModifier
                .size(40.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        Text(
            fontSize = 14.sp, fontFamily = zustFont,
            fontWeight = if (isSelectedItem) {
                FontWeight.W600
            } else {
                FontWeight.W500
            },
            text = categoryItem.name,
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.app_black),
            modifier = Modifier.constrainAs(title) {
                top.linkTo(image.bottom, dp_8)
                bottom.linkTo(parent.bottom, dp_8)
                start.linkTo(image.start)
                end.linkTo(image.end)
            }
        )
    }
}

private val imageModifier = Modifier
    .size(60.dp)
    .clip(CircleShape)