package `in`.opening.area.zustapp.orderSummary.utils

import `in`.opening.area.zustapp.product.model.ProductSingleItem
import androidx.recyclerview.widget.DiffUtil

val productDiffUtils = object : DiffUtil.ItemCallback<ProductSingleItem>() {
    override fun areItemsTheSame(oldItem: ProductSingleItem, newItem: ProductSingleItem): Boolean {
        return oldItem.productPriceId == newItem.productPriceId
    }

    override fun areContentsTheSame(oldItem: ProductSingleItem, newItem: ProductSingleItem): Boolean {
        return  oldItem==newItem
    }

}