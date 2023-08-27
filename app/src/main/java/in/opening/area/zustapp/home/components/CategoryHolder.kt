package `in`.opening.area.zustapp.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import `in`.opening.area.zustapp.home.models.HomePageGenericData
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.dp_6
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.navigateToProductListing
import ui.colorBlack
import zustbase.custom.pressClickEffect

const val CATEGORY_ROW_COUNT = 4
const val CATEGORY_ROW_KEY = "cat_key"

fun LazyListScope.categoryHolder(dataList: List<HomePageGenericData>?) {
    if (dataList.isNullOrEmpty()) {
        return
    }
    val chunkedServices = dataList.chunked(CATEGORY_ROW_COUNT)
    items(chunkedServices.size) { index ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dp_16, vertical = dp_4)
        ) {
            val chunk = chunkedServices[index]
            for (i in (0 until CATEGORY_ROW_COUNT)) {
                if (i < chunk.size) {
                    SingleCategoryItem(chunk[i])
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun RowScope.SingleCategoryItem(categoryItem: HomePageGenericData) {
    val context = LocalContext.current
    ConstraintLayout(modifier = Modifier
        .weight(1f)
        .padding(horizontal = dp_4)
        .pressClickEffect {
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
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(CircleShape)
                .constrainAs(categoryImage) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, dp_6)
                    bottom.linkTo(categoryTitle.top)
                }
        )

        Text(text = categoryItem.name ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .constrainAs(categoryTitle) {
                    start.linkTo(parent.start, dp_8)
                    end.linkTo(parent.end, dp_8)
                    top.linkTo(categoryImage.bottom, dp_8)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }, style = ZustTypography.bodySmall,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            color = colorBlack)

//        categoryItem.categoryStatus?.let {
//            if (it != "ENABLE") {
//                Box(
//                    modifier = Modifier
//                        .constrainAs(currentStatus) {
//                            top.linkTo(categoryImage.top)
//                            start.linkTo(categoryImage.start)
//                            end.linkTo(categoryImage.end)
//                            bottom.linkTo(categoryImage.bottom)
//                            width = Dimension.fillToConstraints
//                        }
//                        .background(
//                            brush = Brush.linearGradient(
//                                colors = listOf(
//                                    Color(0xFFD6C28F),
//                                    Color(0xFFFEECBC)
//                                )
//                            ))
//                        .padding(vertical = 4.dp), contentAlignment = Alignment.Center
//                ) {
//                    Text(text = it, style = ZustTypography.bodySmall,
//                        fontSize = 10.sp, fontWeight = FontWeight.W600,
//                        color = colorResource(id = `in`.opening.area.zustapp.R.color.app_black),
//                        textAlign = TextAlign.Center)
//                }
//            }
//        }
    }
}

