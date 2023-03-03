package `in`.opening.area.zustapp.utility

import `in`.opening.area.zustapp.fcm.CustomFcmService
import `in`.opening.area.zustapp.fcm.OrderDetailDeepLinkModel
import `in`.opening.area.zustapp.orderDetail.OrderDetailActivity
import android.content.Context
import android.content.Intent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDeepLinkHandler @Inject constructor() {
    companion object {
        fun handleDeepLink(
            intent: Intent? = null,
            context: Context?,
        ) {
            if (intent == null) {
                return
            }
            if (context == null) {
                return
            }
            var redirectionIntent: Intent? = null
            if (intent.hasExtra(CustomFcmService.DEEP_LINK_DATA)) {
                val data = intent.getParcelableExtra<OrderDetailDeepLinkModel>(CustomFcmService.DEEP_LINK_DATA)
                if (!data?.orderId.isNullOrEmpty()) {
                    redirectionIntent = Intent(context, OrderDetailActivity::class.java)
                    redirectionIntent.putExtra(OrderDetailActivity.ORDER_ID, data?.orderId)
                }
            }
            if (redirectionIntent != null) {
                context.startActivity(redirectionIntent)
            }
        }
    }
}