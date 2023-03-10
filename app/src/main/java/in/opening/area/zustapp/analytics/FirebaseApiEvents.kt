package `in`.opening.area.zustapp.analytics

import `in`.opening.area.zustapp.MyApplication
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class FirebaseApiEvents @Inject constructor() {

    companion object {
        const val KEY_API_EVENTS = "api_event"
    }

    fun logFirebaseApiEvents(url: String?, statusCode: String?) {
        if (url != null && statusCode != null) {
            val bundle = Bundle()
            bundle.putString(url, statusCode)
            FirebaseAnalytics.getInstance(MyApplication.appContext).logEvent(KEY_API_EVENTS, bundle)
        }
    }
}