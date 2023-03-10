package `in`.opening.area.zustapp.home.components

import `in`.opening.area.zustapp.home.models.HomePageGenericData
import `in`.opening.area.zustapp.product.ProductListingActivity
import `in`.opening.area.zustapp.product.ProductListingActivity.Companion.CATEGORY_ID
import `in`.opening.area.zustapp.product.ProductListingActivity.Companion.CATEGORY_NAME
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import `in`.opening.area.zustapp.ui.theme.dp_6
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.navigateToProductListing
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlin.math.ceil

const val CATEGORY_ROW_COUNT = 3
const val CATEGORY_ROW_KEY = "cat_key"
fun LazyListScope.categoryHolder(dataList: List<HomePageGenericData>?) {
    if (dataList == null) {
        return
    }
    val numberOfRows = ceil(dataList.size.toFloat() / 3).toInt()

    for (rowIndex in 0 until numberOfRows) {
        item(key = CATEGORY_ROW_KEY + rowIndex) {
            Row {
                for (columnIndex in 0 until 3) {
                    val index = rowIndex * 3 + columnIndex
                    if (index >= dataList.size) break
                    val item = dataList[index]
                    SingleCategoryItem(item)
                }
            }
        }
    }
}


@Composable
private fun RowScope.SingleCategoryItem(categoryItem: HomePageGenericData) {
    val context = LocalContext.current
    ConstraintLayout(modifier = Modifier
        .height(120.dp)
        .weight(1f)
        .padding(vertical = 6.dp,
            horizontal = 8.dp)
        .clip(RoundedCornerShape(12.dp))
        .clickable {
            context.navigateToProductListing(categoryItem.id, categoryItem.name)
        }
        .fillMaxWidth()) {
        val (categoryImage, categoryTitle) = createRefs()
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(categoryItem.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .constrainAs(categoryImage) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, dp_6)
                    bottom.linkTo(categoryTitle.top)
                    height = Dimension.fillToConstraints
                }
        )

        Text(text = categoryItem.name ?: "",
            modifier = Modifier.constrainAs(categoryTitle) {
                start.linkTo(parent.start, dp_8)
                end.linkTo(parent.end, dp_8)
                top.linkTo(categoryImage.bottom, dp_8)
                bottom.linkTo(parent.bottom, dp_8)
                width = Dimension.fillToConstraints
            }, style = Typography_Montserrat.body2,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            color = colorResource(id = `in`.opening.area.zustapp.R.color.app_black))
    }
}
