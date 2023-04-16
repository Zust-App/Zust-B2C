package `in`.opening.area.zustapp.home.components

import `in`.opening.area.zustapp.home.models.HomePageGenericData
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_6
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.navigateToProductListing
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
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

private val categoryModifier = Modifier
    .wrapContentHeight()
    .padding(vertical = 6.dp,
        horizontal = 12.dp)
    .clip(RoundedCornerShape(16.dp))
    .fillMaxWidth()

@Composable
private fun RowScope.SingleCategoryItem(categoryItem: HomePageGenericData) {
    val context = LocalContext.current
    ConstraintLayout(modifier = categoryModifier
        .weight(1f)
        .clickable {
            if (categoryItem.categoryStatus == "ENABLE" || categoryItem.categoryStatus == null) {
                context.navigateToProductListing(categoryItem.id, categoryItem.name)
            } else {
                AppUtility.showToast(context, "Available soon")
            }
        }
    ) {
        val (categoryImage, categoryTitle, currentStatus) = createRefs()
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(categoryItem.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(90.dp)
                .clip(RoundedCornerShape(8.dp))
                .constrainAs(categoryImage) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, dp_6)
                    bottom.linkTo(categoryTitle.top)
                }
        )

        Text(text = categoryItem.name ?: "",
            modifier = Modifier
                .height(32.dp)
                .constrainAs(categoryTitle) {
                    start.linkTo(parent.start, dp_8)
                    end.linkTo(parent.end, dp_8)
                    top.linkTo(categoryImage.bottom, dp_8)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }, style = ZustTypography.body2,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            color = colorResource(id = `in`.opening.area.zustapp.R.color.app_black))
        categoryItem.categoryStatus?.let {
            if (it != "ENABLE") {
                Box(
                    modifier = Modifier
                        .constrainAs(currentStatus) {
                            top.linkTo(categoryImage.top)
                            start.linkTo(categoryImage.start)
                            end.linkTo(categoryImage.end)
                            bottom.linkTo(categoryImage.bottom)
                            width = Dimension.fillToConstraints
                        }
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFD6C28F),
                                    Color(0xFFFEECBC)
                                )
                            ))
                        .padding(vertical = 4.dp), contentAlignment = Alignment.Center
                ) {
                    Text(text = it, style = ZustTypography.subtitle1,
                        fontSize = 10.sp, fontWeight = FontWeight.W600,
                        color = colorResource(id = `in`.opening.area.zustapp.R.color.app_black),
                        textAlign = TextAlign.Center)
                }
            }
        }
    }
}

