package `in`.opening.area.zustapp.utility

import zustbase.ZustLandingActivity
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.HOME_CATEGORY_CLICK
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.OPEN_CALL
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.OPEN_WA_ORDER
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.PRODUCT_DETAIL_CLICK
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.START_SEARCH_ACTIVITY
import `in`.opening.area.zustapp.login.LoginActivity
import zustbase.orderDetail.OrderDetailActivity
import zustbase.orderHistory.MyOrdersActivity
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
import android.os.Bundle
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.FOOD_BTM_NAV_CLICK
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.HOME_NON_VEG_CATEGORY_CLICK
import zustbase.orderDetail.ui.ORDER_ID
import `in`.opening.area.zustapp.zustFood.ZustFoodEntryActivity
import non_veg.listing.NonVegItemListActivity
import non_veg.product_details.NonVegProductDetailsActivity
import non_veg.search.NonVegSearchActivity


fun Context.navigateToProductListing(categoryId: Int?, categoryName: String?) {
    if (categoryId != null) {
        val bundle = Bundle()
        bundle.putString("name", categoryName)
        bundle.putInt("id", categoryId)
        FirebaseAnalytics.logEvents(HOME_CATEGORY_CLICK, bundle)
        val intent = Intent(this, ProductListingActivity::class.java)
        intent.putExtra(ProductListingActivity.CATEGORY_ID, categoryId)
        if (categoryName != null) {
            intent.putExtra(ProductListingActivity.CATEGORY_NAME, categoryName)
        }
        startActivity(intent)
    }
}

fun Context.startProductDetailPage(productSingleItem: ProductSingleItem) {
    val bundle = Bundle()
    bundle.putString("name", productSingleItem.productName)
    bundle.putString("id", productSingleItem.productPriceId)
    FirebaseAnalytics.logEvents(PRODUCT_DETAIL_CLICK, bundle)
    val productDetailIntent = Intent(this, ProductDetailsActivity::class.java)
    productDetailIntent.putExtra(ProductDetailsActivity.PRODUCT_ID, productSingleItem.productGroupId)
    startActivity(productDetailIntent)
}

fun Context.startMyOrders() {
    val productDetailIntent = Intent(this, MyOrdersActivity::class.java)
    startActivity(productDetailIntent)
}

fun Context.proceedToHomePage() {
    val zustLandingActivity = Intent(this, ZustLandingActivity::class.java)
    startActivity(zustLandingActivity)
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
        orderDetailIntent.putExtra(ORDER_ID, orderId)
        startActivity(orderDetailIntent)
    }
}

fun Context.startSearchActivity() {
    FirebaseAnalytics.logEvents(START_SEARCH_ACTIVITY)
    val searchIntent = Intent(this, SearchProductActivity::class.java)
    startActivity(searchIntent)
}

fun Context.startUserProfileActivity() {
    val profileIntent = Intent(this, ProfileActivity::class.java)
    startActivity(profileIntent)
}

fun Context.openWhatsAppOrderIntent() {
    FirebaseAnalytics.logEvents(OPEN_WA_ORDER)
    val sendIntent = Intent(Intent.ACTION_VIEW)
    sendIntent.data = Uri.parse(AppUtility.getWhatsappHelpUrl())
    if (AppUtility.isAppInstalled(AppUtility.WA_PACKAGE_NAME)) {
        startActivity(sendIntent)
    } else if (AppUtility.isAppInstalled(AppUtility.BUSINESS_WA_PACKAGE_NAME)) {
        startActivity(sendIntent)
    }
}

fun Context?.openCallIntent(phoneNumber: String) {
    FirebaseAnalytics.logEvents(OPEN_CALL)
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

fun Context.startFoodEntryActivity() {
    FirebaseAnalytics.logEvents(FOOD_BTM_NAV_CLICK)
    val searchIntent = Intent(this, ZustFoodEntryActivity::class.java)
    startActivity(searchIntent)
}


fun Context.navigateToNonVegProductListing(categoryId: Int?, categoryName: String?) {
    if (categoryId != null) {
        val bundle = Bundle()
        bundle.putString("name", categoryName)
        bundle.putInt("id", categoryId)
        FirebaseAnalytics.logEvents(HOME_NON_VEG_CATEGORY_CLICK, bundle)
        val intent = Intent(this, NonVegItemListActivity::class.java)
        intent.putExtra(NonVegItemListActivity.KEY_NV_CATEGORY_ID, categoryId)
        if (categoryName != null) {
            intent.putExtra(NonVegItemListActivity.KEY_NV_CATEGORY_TITLE, categoryName)
        }
        startActivity(intent)
    }
}

fun Context.navigateToNonVegProductDetails(productId: Int?, productPriceId: Int?) {
    if (productId != null && productPriceId != null) {
        val intent = Intent(this, NonVegProductDetailsActivity::class.java)
        intent.putExtra(NonVegProductDetailsActivity.KEY_NV_PRODUCT_ID, productId)
        intent.putExtra(NonVegProductDetailsActivity.KEY_NV_PRODUCT_PRICE_ID, productPriceId)
        startActivity(intent)
    }
}

fun Context.startNonVegSearchActivity() {
    val searchIntent = Intent(this, NonVegSearchActivity::class.java)
    startActivity(searchIntent)
}