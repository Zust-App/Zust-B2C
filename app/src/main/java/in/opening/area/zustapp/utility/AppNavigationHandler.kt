package `in`.opening.area.zustapp.utility

import `in`.opening.area.zustapp.product.ProductListingActivity
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.productDetails.presentation.ProductDetailsActivity
import android.content.Context
import android.content.Intent


fun Context.navigateToProductListing(categoryId: Int?, categoryName: String?) {
    if (categoryId != null) {
        val intent = Intent(this, ProductListingActivity::class.java)
        intent.putExtra(ProductListingActivity.CATEGORY_ID, categoryId)
        if (categoryName != null) {
            intent.putExtra(ProductListingActivity.CATEGORY_NAME, categoryName)
        }
        startActivity(intent)
    }
}
 fun Context.startProductDetailPage(productSingleItem: ProductSingleItem) {
    val productDetailIntent = Intent(this, ProductDetailsActivity::class.java)
    productDetailIntent.putExtra(ProductDetailsActivity.PRODUCT_KEY, productSingleItem)
    startActivity(productDetailIntent)
}
