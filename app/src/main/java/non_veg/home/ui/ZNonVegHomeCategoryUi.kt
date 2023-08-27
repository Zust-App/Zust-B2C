package non_veg.home.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.navigateToNonVegProductListing
import non_veg.home.model.NonVegCategory
import non_veg.home.ui.commonUi.NonVegSingleCategoryItem
import zustbase.custom.pressClickEffect


private const val chunkSize = 3
fun LazyListScope.zNonVegHomeCategoryUi(dataList: List<NonVegCategory>?) {
    if (dataList == null) {
        return
    }
    if (dataList.isEmpty()) {
        return
    }
    val chunkedServices = dataList.chunked(chunkSize)

    items(chunkedServices.size) { index ->
        val context = LocalContext.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dp_8, vertical = dp_8)
        ) {
            val chunk = chunkedServices[index]
            chunk.forEachIndexed { _, category ->
                NonVegSingleCategoryItem(
                    category,
                    modifier = Modifier
                        .pressClickEffect {
                            context.navigateToNonVegProductListing(category.id, category.name)
                        }
                        .weight(1f)
                        .padding(horizontal = dp_8)
                )
            }
            if (chunk.size < (chunkSize)) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}