package `in`.opening.area.zustapp.home.components

import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.viewmodels.ACTION
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyListScope
import kotlin.math.ceil

private const val trendingProductsRowCount = 2
private const val trendingItemsKey = "trend_key"
fun LazyListScope.trendingProductsUi(
    productItems: List<ProductSingleItem>?,
    productItemClick: (ProductSingleItem?, ACTION) -> Unit,
) {
    if (productItems == null) {
        return
    }

    val numberOfRows = ceil(productItems.size.toFloat() / trendingProductsRowCount).toInt()
    for (rowIndex in 0 until numberOfRows) {
        item(key = trendingItemsKey + rowIndex) {
            Row {
                for (columnIndex in 0 until trendingProductsRowCount) {
                    val index = rowIndex * trendingProductsRowCount + columnIndex
                    if (index >= productItems.size) break
                    val item = productItems[index]

                    TrendingProductsSingleItem(item, productItemClick)
                }
            }
        }
    }
}
