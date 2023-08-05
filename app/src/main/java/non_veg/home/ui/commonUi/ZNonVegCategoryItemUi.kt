package non_veg.home.ui.commonUi

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.home.components.CATEGORY_ROW_KEY
import `in`.opening.area.zustapp.home.models.HomePageGenericData
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_6
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.navigateToNonVegProductListing
import `in`.opening.area.zustapp.utility.navigateToProductListing
import non_veg.home.model.NonVegCategory
import kotlin.math.ceil

private val categoryModifier = Modifier
    .wrapContentHeight()
    .padding(vertical = 6.dp,
        horizontal = 12.dp)
    .clip(RoundedCornerShape(16.dp))
    .fillMaxWidth()

@Composable
fun RowScope.NonVegSingleCategoryItem(categoryItem: NonVegCategory?) {
    if (categoryItem == null) {
        Spacer(modifier = Modifier.weight(1f))
        return
    }

    val context = LocalContext.current
    ConstraintLayout(modifier = categoryModifier
        .weight(1f)
        .clickable {
            context.navigateToNonVegProductListing(categoryItem.id, categoryItem.name)
        }) {
        val (categoryImage, categoryTitle) = createRefs()
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(categoryItem.thumbnail)
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

        Text(text = categoryItem.name,
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
            color = colorResource(id = R.color.app_black))
    }
}