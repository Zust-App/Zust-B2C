package `in`.opening.area.zustapp.product

import `in`.opening.area.zustapp.product.model.ProductSingleItem

class Utils {
    companion object{

        fun calculatePriceAndItemCount(products: ArrayList<ProductSingleItem>): Pair<Int, Double> {
            var price = 0.0
            var totalItemCount = 0
             products.forEach {
                price += (it.price) * it.itemCountByUser
                totalItemCount += it.itemCountByUser
            }
            return Pair(totalItemCount, price)
        }
    }
}