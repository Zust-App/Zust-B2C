package `in`.opening.area.zustapp.utility

import `in`.opening.area.zustapp.fcm.CustomFcmService
import `in`.opening.area.zustapp.fcm.OrderDetailDeepLinkModel
import `in`.opening.area.zustapp.home.models.HomePageGenericData
import zustbase.orderDetail.OrderDetailActivity
import android.content.Context
import android.content.Intent
import android.net.Uri
import zustbase.orderDetail.ui.ORDER_ID
import zustbase.utility.startSubscriptionFormActivity
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("DEPRECATION")
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
                    val orderNumber = data?.orderId?.toInt()
                    if (orderNumber != null) {
                        redirectionIntent.putExtra(ORDER_ID, orderNumber)
                    }
                }
                if (redirectionIntent != null) {
                    context.startActivity(redirectionIntent)
                }
            }

        }

        fun handleOfferLink(
            context: Context?,
            data: Any?,
        ) {
            if (data == null) {
                return
            }
            if (context == null) {
                return
            }
            try {
                if (data is HomePageGenericData) {
                    if (data.deepLink?.contains("home") == true) {
                        context.navigateToProductListing(data.category?.toInt(), null)
                    }
                    if (data.deepLink?.contains("zForm")==true){
                        context.startSubscriptionFormActivity()
                    }
                }
                if (data is String) {
                    if (data == "sForm") {
                        context.startSubscriptionFormActivity()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * format --> https://www.zustapp.com/pageName
         */
        private const val ORDER_DETAIL_PAGE = "orderDetails"
        fun handleWebLink(uri: Uri? = null, context: Context?) {
            if (uri == null || context == null) {
                return
            }
            try {
                val pathSegments = uri.pathSegments
                if (pathSegments.isNotEmpty()) {
                    val pageName = pathSegments[0]
                    if (pageName.contains(ORDER_DETAIL_PAGE, true)) {
                        val orderId = uri.getQueryParameter("orderId")
                        if (orderId != null) {
                            context.proceedToOrderDetails(orderId.toInt())
                        }
                    }
                }
            } catch (e: Exception) {
                print(e.printStackTrace())
            }
        }
    }
}
