package `in`.opening.area.zustapp.orderSummary

import `in`.opening.area.zustapp.product.model.ProductSingleItem

interface OrderItemsClickListeners {
    fun didTapOnIncreaseProductItemAmount(v: ProductSingleItem)
    fun didTapOnDecreaseProductItemAmount(v: ProductSingleItem)
    fun deleteProductItem(v: ProductSingleItem)
    fun clickOnApplyCoupon()
    fun finishActivity() {

    }
}