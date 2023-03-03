package `in`.opening.area.zustapp.orderSummary.compose


import `in`.opening.area.zustapp.orderSummary.OrderItemsClickListeners
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable

@Composable
fun UpSellingUi(products: List<ProductSingleItem>,listeners: OrderItemsClickListeners) {
    LazyRow() {
        products.forEach {
            item(key = it.productPriceId) {
                SuggestItemContainer(it,listeners)
            }
        }
    }
}