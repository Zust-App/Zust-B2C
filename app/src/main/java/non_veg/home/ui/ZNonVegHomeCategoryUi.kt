package non_veg.home.ui

import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceEvenly
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import `in`.opening.area.zustapp.home.components.CATEGORY_ROW_KEY
import non_veg.home.model.NonVegCategory
import non_veg.home.ui.commonUi.NonVegSingleCategoryItem
import non_veg.home.uiModel.NonVegCategoryUiModel
import non_veg.home.viewmodel.ZustNvEntryViewModel
import kotlin.math.ceil


fun LazyListScope.zNonVegHomeCategoryUi(dataList: List<NonVegCategory>?) {
    if (dataList == null) {
        return
    }
    val numberOfRows = ceil(dataList.size.toFloat() / 3).toInt()

    for (rowIndex in 0 until numberOfRows) {
        item(key = CATEGORY_ROW_KEY + rowIndex) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (columnIndex in 0 until 3) {
                    val index = rowIndex * 3 + columnIndex
                    val categoryItem = if (index >= dataList.size) null else dataList[index]
                    NonVegSingleCategoryItem(categoryItem)
                }
            }
        }
    }
}
