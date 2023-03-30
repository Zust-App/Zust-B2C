package `in`.opening.area.zustapp.utility

import `in`.opening.area.zustapp.HomeLandingActivity
import `in`.opening.area.zustapp.login.LoginActivity
import `in`.opening.area.zustapp.product.ProductListingActivity
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.productDetails.presentation.ProductDetailsActivity
import `in`.opening.area.zustapp.webpage.InAppWebActivity
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


 fun Context.proceedToHomePage() {
    val homeLandingActivity = Intent(this, HomeLandingActivity::class.java)
    startActivity(homeLandingActivity)
}

fun Context.proceedToLoginActivity() {
    val loginActivity = Intent(this, LoginActivity::class.java)
    startActivity(loginActivity)
}

fun Context.moveToInAppWebPage(url: String, title: String) {
    val inAppWebActivity = Intent(this, InAppWebActivity::class.java)
    inAppWebActivity.putExtra(InAppWebActivity.WEB_URL, url)
    inAppWebActivity.putExtra(InAppWebActivity.TITLE_TEXT, title)
    startActivity(inAppWebActivity)
}