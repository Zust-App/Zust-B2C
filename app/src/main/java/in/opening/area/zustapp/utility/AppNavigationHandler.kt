package `in`.opening.area.zustapp.utility

import `in`.opening.area.zustapp.HomeLandingActivity
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.login.LoginActivity
import `in`.opening.area.zustapp.orderDetail.OrderDetailActivity
import `in`.opening.area.zustapp.orderHistory.MyOrdersActivity
import `in`.opening.area.zustapp.product.ProductListingActivity
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.productDetails.presentation.ProductDetailsActivity
import `in`.opening.area.zustapp.profile.ProfileActivity
import `in`.opening.area.zustapp.profile.models.Refer
import `in`.opening.area.zustapp.refer.ReferAndEarnActivity
import `in`.opening.area.zustapp.search.SearchProductActivity
import `in`.opening.area.zustapp.webpage.InAppWebActivity
import android.content.Context
import android.content.Intent
import android.net.Uri


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

fun Context.startMyOrders() {
    val productDetailIntent = Intent(this, MyOrdersActivity::class.java)
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

fun Context.navigateToReferAndEarn(refer: Refer?) {
    if (refer == null) {
        return
    }
    val referAndEarnActivity = Intent(this, ReferAndEarnActivity::class.java)
    referAndEarnActivity.putExtra(ReferAndEarnActivity.REFER_DATA_KEY, refer)
    startActivity(referAndEarnActivity)
}

fun Context.openBrowser(url: String) {
    try {
        if (url.contains("https")) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } else {
            val httpsUrl = "https://$url"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(httpsUrl))
            startActivity(intent)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.proceedToOrderDetails(orderId: Int?) {
    if (orderId != null) {
        val orderDetailIntent = Intent(this, OrderDetailActivity::class.java)
        orderDetailIntent.putExtra(OrderDetailActivity.ORDER_ID, orderId)
        startActivity(orderDetailIntent)
    }
}

fun Context.startSearchActivity() {
    val searchIntent = Intent(this, SearchProductActivity::class.java)
    startActivity(searchIntent)
}

fun Context.startUserProfileActivity() {
    val profileIntent = Intent(this, ProfileActivity::class.java)
    startActivity(profileIntent)
}

fun Context.openWhatsAppOrderIntent() {
    val sendIntent = Intent(Intent.ACTION_VIEW)
    sendIntent.data = Uri.parse(AppUtility.getWhatsappHelpUrl())
    if (AppUtility.isAppInstalled(AppUtility.WA_PACKAGE_NAME)) {
        startActivity(sendIntent)
    } else if (AppUtility.isAppInstalled(AppUtility.BUSINESS_WA_PACKAGE_NAME)) {
        startActivity(sendIntent)
    }
}

fun Context?.openCallIntent( phoneNumber: String) {
    try {
        val phone = if (phoneNumber.contains("+91")) {
            phoneNumber
        } else {
            "+91$phoneNumber"
        }
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
        this?.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}