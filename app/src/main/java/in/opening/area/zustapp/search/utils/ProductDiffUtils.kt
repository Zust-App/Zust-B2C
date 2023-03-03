package `in`.opening.area.zustapp.search.utils

import `in`.opening.area.zustapp.product.model.ProductSingleItem
import androidx.recyclerview.widget.DiffUtil

val productSingleItemDiffUtils = object : DiffUtil.ItemCallback<ProductSingleItem>() {
    override fun areItemsTheSame(oldItem: ProductSingleItem, newItem: ProductSingleItem): Boolean {
        return oldItem.productPriceId == newItem.productPriceId
    }

    override fun areContentsTheSame(oldItem: ProductSingleItem, newItem: ProductSingleItem): Boolean {
        return oldItem.itemCountByUser == newItem.itemCountByUser &&
                oldItem.isOutOfStock == newItem.isOutOfStock &&
                oldItem.productPriceId == newItem.productPriceId &&
                oldItem.categoryId == newItem.categoryId &&
                oldItem.wareHouseId == newItem.wareHouseId &&
                oldItem.subcategoryId == newItem.subcategoryId &&
                oldItem.itemInStock == newItem.itemInStock

    }

}