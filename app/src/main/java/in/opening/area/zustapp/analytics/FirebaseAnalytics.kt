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

        const val HOME_PAGE_AUTO_LOGOUT = "home_auto_logout"
        const val ORDER_HISTORY_VIEW = "order_history_view"
        const val ORDER_ITEM_CLICK = "order_item_click"
        const val RATE_ORDER_CLICK = "rate_order_click"
        const val ORDER_DETAIL_VIEW = "order_detail_view"
        const val ORDER_DETAIL_WA = "order_detail_wa"

        const val PROFILE_VIEW="profile_view"
        const val HOME_PROFILE_VIEW="home_profile_view"

        fun logEvents(eventName: String?, bundle: Bundle? = null) {
            if (eventName != null) {
                if (bundle != null) {
                    FirebaseAnalytics.getInstance(MyApplication.appContext).logEvent(eventName, null)
                } else {
                    FirebaseAnalytics.getInstance(MyApplication.appContext).logEvent(eventName, null)
                }
            }
        }
    }

    fun logFirebaseApiEvents(url: String?, statusCode: String?) {
        if (url != null && statusCode != null) {
            val bundle = Bundle()
            bundle.putString(url, statusCode)
            FirebaseAnalytics.getInstance(MyApplication.appContext).logEvent(KEY_API_EVENTS, bundle)
        }
    }

}