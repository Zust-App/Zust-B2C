package `in`.opening.area.zustapp.product

import `in`.opening.area.zustapp.analytics.FirebaseAnalytics
import `in`.opening.area.zustapp.product.model.SingleSubCategory
import `in`.opening.area.zustapp.product.model.SubCategoryDataMode
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.viewmodels.ProductListingViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun SubCategoryContainer(
    layoutScope: ConstraintLayoutScope, subCategoryList: ConstrainedLayoutReference, productList: ConstrainedLayoutReference,
    productListingViewModel: ProductListingViewModel,
) {
    val subCategory by productListingViewModel.subCategoryFlow.collectAsState(initial = SubCategoryDataMode())

    layoutScope.apply {
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier
                .width(70.dp)
                .background(color = Color.White)
                .constrainAs(subCategoryList) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(productList.start)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                },
        ) {
            subCategory.data?.forEach { itemValue ->
                item {
                    SingleSubCategoryItem(itemValue) {
                        productListingViewModel.updateSubCategorySelection(itemValue)
                    }
                }
            }
        }
    }
}

@Composable
private fun SingleSubCategoryItem(itemValue: SingleSubCategory?, callback: (SingleSubCategory?) -> Unit) {
    if (itemValue != null) {
        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
            .clickable {
                callback.invoke(itemValue)
            }
            .wrapContentHeight()) {
            val (image, title, selector) = createRefs()

            AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(itemValue.thumbnail).crossfade(true).build(), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .sizeIn(maxWidth = 50.dp, maxHeight = 50.dp)
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.medium))

            Text(text = itemValue.name ?: "Title", style = ZustTypography.body2, modifier = Modifier
                .width(60.dp)
                .constrainAs(title) {
                    top.linkTo(image.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, lineHeight = 12.sp, maxLines = 2)

            if (itemValue.isSelected) {
                Divider(modifier = Modifier
                    .width(3.dp)
                    .background(color = Color.Red, shape = RoundedCornerShape(24.dp))
                    .constrainAs(selector) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(title.end, 2.dp)
                        height = Dimension.fillToConstraints
                    })
            }
        }
    }
}