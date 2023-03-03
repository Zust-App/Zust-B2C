package `in`.opening.area.zustapp.uiModels.productList

import `in`.opening.area.zustapp.product.model.ProductSingleItem

data class SingleProductUi(
    val productSingleItem: ProductSingleItem? = null,
    val id: String? = null, val time: Long = System.currentTimeMillis())