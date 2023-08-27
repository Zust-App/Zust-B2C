package zustbase.utility

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import dynamic.ZustGroceryEntryPointActivity
import dynamic.ZustNvEntryPointActivity
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.address.v2.AddressBottomSheetV2
import `in`.opening.area.zustapp.extensions.showBottomSheetIsNotPresent
import `in`.opening.area.zustapp.helper.SelectLanguageFragment
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.profile.SuggestProductBtmSheet
import `in`.opening.area.zustapp.rapidwallet.model.ZustServiceType
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.openCallIntent
import `in`.opening.area.zustapp.utility.openWhatsAppOrderIntent
import `in`.opening.area.zustapp.utility.startNonVegSearchActivity
import `in`.opening.area.zustapp.utility.startSearchActivity
import `in`.opening.area.zustapp.utility.startUserProfileActivity
import non_veg.cart.NonVegCartActivity
import non_veg.cart.models.NonVegCartData
import subscription.SubscriptionFormActivity
import zustbase.orderDetail.ui.INTENT_SOURCE
import zustbase.services.models.ZustService

fun Context.handleBasicCallbacks(zustService: ZustService) {
    if (!zustService.enable && !zustService.type.equals(ZustServiceType.SUBSCRIPTION.name,ignoreCase = true)) {
        AppUtility.showToast(this, "Zust ${zustService.title} coming soon in your area")
        return
    }
    when (zustService.type) {
        ZustServiceType.ELECTRONICS.name -> {
            val zustEntryIntent = Intent(this, ZustGroceryEntryPointActivity::class.java)
            zustEntryIntent.putExtra(INTENT_SOURCE, ZustServiceType.ELECTRONICS.name)
            startActivity(zustEntryIntent)
        }

        ZustServiceType.NON_VEG.name -> {
            val zustEntryIntent = Intent(this, ZustNvEntryPointActivity::class.java)
            zustEntryIntent.putExtra(INTENT_SOURCE, ZustServiceType.NON_VEG.name)
            startActivity(zustEntryIntent)
        }

        ZustServiceType.GROCERY.name -> {
            val zustEntryIntent = Intent(this, ZustGroceryEntryPointActivity::class.java)
            zustEntryIntent.putExtra(INTENT_SOURCE, ZustServiceType.GROCERY.name)
            startActivity(zustEntryIntent)
        }

        ZustServiceType.FOOD.name -> {
            val zustEntryIntent = Intent(this, ZustGroceryEntryPointActivity::class.java)
            zustEntryIntent.putExtra(INTENT_SOURCE, ZustServiceType.FOOD.name)
            startActivity(zustEntryIntent)
        }

        ZustServiceType.NEAR_BY_SHOP.name -> {
            val zustEntryIntent = Intent(this, ZustGroceryEntryPointActivity::class.java)
            zustEntryIntent.putExtra(INTENT_SOURCE, ZustServiceType.NEAR_BY_SHOP.name)
            startActivity(zustEntryIntent)
        }

        ZustServiceType.SUBSCRIPTION.name -> {
            if (zustService.deepLink==null || zustService.deepLink=="sForm") {
                val zustEntryIntent = Intent(this, SubscriptionFormActivity::class.java)
                zustEntryIntent.putExtra(INTENT_SOURCE, ZustServiceType.SUBSCRIPTION.name)
                startActivity(zustEntryIntent)
            }else{
                val zustEntryIntent = Intent(this, ZustGroceryEntryPointActivity::class.java)
                zustEntryIntent.putExtra(INTENT_SOURCE, ZustServiceType.SUBSCRIPTION.name)
                startActivity(zustEntryIntent)
            }
        }
    }
}

fun AppCompatActivity.handleActionIntent(action: ACTION, fragmentManager: FragmentManager? = null) {
    when (action) {
        ACTION.OPEN_USER_BOOKING -> {
            startUserProfileActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        ACTION.SEARCH_PRODUCT -> {
            startSearchActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        ACTION.SUGGEST_PRODUCT -> {
            showSuggestProductSheet()
        }

        ACTION.SEARCH_NON_VEG -> {
            startNonVegSearchActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        ACTION.OPEN_PROFILE -> {
            startUserProfileActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        ACTION.ORDER_WA -> {
            openWhatsAppOrderIntent()
        }

        ACTION.PHONE_CALL -> {
            openCallIntent("7564062907")
        }

        ACTION.OPEN_LOCATION -> {
            fragmentManager?.showAddressBtmSheet()
        }

        else -> {}
    }
}

fun FragmentManager?.showAddressBtmSheet() {
    this?.apply {
        val bottomSheetV2 = AddressBottomSheetV2.newInstance()
        showBottomSheetIsNotPresent(bottomSheetV2, AddressBottomSheetV2.SHEET_TAG)
    }
}

private fun FragmentManager.showLanguageSelectionDialog() {
    SelectLanguageFragment.showDialog(this, true)
}

fun AppCompatActivity.showSuggestProductSheet() {
    this.supportFragmentManager.showBottomSheetIsNotPresent(
        SuggestProductBtmSheet.newInstance(),
        SuggestProductBtmSheet.TAG)
}

fun Context.startSubscriptionFormActivity() {
    val intent = Intent(this, SubscriptionFormActivity::class.java)
    startActivity(intent)
}

fun Context.moveToCartDetailsActivity(nonVegCartData: NonVegCartData?) {
    if (nonVegCartData?.cartId == null) {
        return
    }
    val nonVegCartActivity = Intent(this, NonVegCartActivity::class.java)
    nonVegCartActivity.apply {
        putExtra(NonVegCartActivity.NON_VEG_CART_ID, nonVegCartData.cartId)
    }
    startActivity(nonVegCartActivity)
}