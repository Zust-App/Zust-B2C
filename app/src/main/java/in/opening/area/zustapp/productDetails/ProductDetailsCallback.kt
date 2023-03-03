package `in`.opening.area.zustapp.productDetails

import `in`.opening.area.zustapp.product.model.CreateCartData

interface ProductDetailsCallback {
    fun didTapOnViewCartBtn()
    fun startOrderSummary(cartData: CreateCartData)
}