package `in`.opening.area.zustapp.analytics

import `in`.opening.area.zustapp.MyApplication
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class FirebaseAnalytics @Inject constructor() {

    companion object {
        const val KEY_API_EVENTS = "api_event"
        const val LOGIN_PAGE_VIEW = "login_page_view"
        const val PRIVACY_POLICY_CLICK = "privacy_policy_view"
        const val TERM_VIEW = "term_condition_view"
        const val LOGIN_PROCEED = "login_proceed"
        const val LOGIN_GET_OTP_ERROR = "login_get_otp_error"
        const val LOGIN_GET_OTP_SUCCESS = "login_get_otp_success"
        const val RESEND_OTP_CLICK = "resend_otp_click"
        const val RESEND_OTP_SUCCESS = "resend_otp_success"
        const val RESEND_OTP_ERROR = "resend_otp_error"
        const val OTP_VERIFIED = "otp_verified"
        const val PROFILE_CREATED = "profile_created"
        const val OTP_VERIFICATION_ERROR = "otp_verification_error"
        const val OTP_VERIFICATION_CLICK = "otp_verification_click"

        const val PROFILE_SUCCESSFULLY_NEW_CREATED = "new_profile_created"
        const val NEW_PROFILE_CREATE_ERROR = "new_profile_create_error"
        const val PROFILE_CREATE_CLICK = "profile_create_click"

        const val ORDER_HISTORY_CLICK_BTM_NAV = "order_history_click_btm_nav"

        const val HOME_PAGE_AUTO_LOGOUT = "home_auto_logout"
        const val ORDER_HISTORY_VIEW = "order_history_view"
        const val ORDER_ITEM_CLICK = "order_item_click"
        const val RATE_ORDER_CLICK = "rate_order_click"
        const val ORDER_DETAIL_VIEW = "order_detail_view"
        const val ORDER_DETAIL_WA = "order_detail_wa"

        const val PROFILE_CLICK = "profile_click"
        const val PROFILE_MY_ORDERS = "profile_my_orders"
        const val PROFILE_MY_ADDRESS = "profile_my_address"
        const val PROFILE_SUGGEST_ITEMS = "profile_suggest_items"
        const val PROFILE_RATE_US = "profile_rate_us"
        const val PROFILE_FAQ = "profile_faq"
        const val PROFILE_ABOUT_US = "profile_about_us"
        const val PROFILE_OPEN_SOURCE = "profile_open_source"
        const val PROFILE_HELP_CLICK = "profile_help_click"
        const val PROFILE_TC_CLICK = "profile_term_condition_click"
        const val PROFILE_SHARE_APP = "profile_share_app"
        const val HOME_PROFILE_VIEW = "home_profile_view"
        const val PROFILE_PRIVACY_POLICY = "profile_privacy_policy"
        const val PROFILE_LOGOUT_CLICK = "profile_logout_click"
        const val SUGGEST_PRODUCT = "suggest_product"
        const val HOME_CATEGORY_CLICK = "home_category_click"
        const val PRODUCT_DETAIL_CLICK = "product_detail_click"
        const val API_RESPONSE = "api_response"
        const val API_ANDROID_EXCEPTION = "api_android_exception"
        const val NEW_ADDRESS_ADD_ERROR = "new_address_add_error"
        const val NEW_ADDRESS_ADD = "new_address_add"
        const val OPEN_PAYMENT_PAGE = "open_payment_page"
        const val CLICK_ON_VIEW_CART_SUCCESS_MOVE = "click_on_view_cart_success_move"
        const val CLICK_ON_VIEW_CART_SUCCESS = "click_on_view_cart_success"
        const val CLICK_ON_VIEW_CART = "click_on_view_cart"
        const val CLICK_ON_VIEW_CART_ERROR = "click_on_view_cart_error"
        const val PRODUCT_LISTING_CATEGORY_CLICK = "product_listing_category_click"
        const val START_SEARCH_ACTIVITY = "start_search_activity"
        const val OPEN_WA_ORDER = "open_whatsapp_order"
        const val OPEN_CALL = "open_call"
        const val REFER_AND_EARN_BTN = "refer_and_earn_button"
        const val REFERRAL_CODE_COPY = "referral_code_copy"
        fun logEvents(eventName: String?, bundle: Bundle? = null) {
            if (eventName != null) {
                if (bundle != null) {
                    FirebaseAnalytics.getInstance(MyApplication.appContext).logEvent(eventName, bundle)
                } else {
                    FirebaseAnalytics.getInstance(MyApplication.appContext).logEvent(eventName, null)
                }
            }
        }
    }

}