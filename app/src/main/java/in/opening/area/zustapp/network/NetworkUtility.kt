package `in`.opening.area.zustapp.network

import `in`.opening.area.zustapp.BuildConfig

class NetworkUtility {

    companion object {

        private const val isProd = true

        private fun getCompleteBaseUrl(): String {
            return if (!BuildConfig.DEBUG) {
                BuildConfig.PROD_BASE_URL
            } else {
                if (isProd) {
                    BuildConfig.PROD_BASE_URL
                } else {
                    BuildConfig.DEV_BASE_URL
                }
            }
        }

        private fun getPaymentUrl(): String {
            return if (isProd) {
                "https://paymentapi.zustapp.com/api/v1"
            } else {
                "https://paymentapi.grinzy.in/api/v1"
            }
        }

        private val baseUrl = getCompleteBaseUrl()

        private val zustPayBaseUrl = "https://zustpay.zustapp.com/api/v1"
        private val paymentBaseUrl = getPaymentUrl()
        val END_POINT_REGISTER = "$baseUrl/auth/register-send-otp"
        val END_POINT_AUTH_VERIFICATION = "$baseUrl/auth/register-verify-otp"
        val END_POINT_UPDATE_PROFILE = "$baseUrl/users/update-profile"
        val END_POINT_SED_FCM = "$baseUrl/users/save-fcm-token"
        val TRENDING_PRODUCTS = "$baseUrl/products/trend-product"
        val SUB_CATEGORY = "$baseUrl/products/sub-category"
        val PRODUCT_LIST_BY_CATEGORY = "$baseUrl/products/category-by-product"
        val ORDERS_CART = "$baseUrl/orders/cart1"
        val COUPONS = "$baseUrl/coupons/user-coupons"
        val PRODUCT_SEARCH = "$baseUrl/products/search"
        val PAYMENT_METHOD = "$baseUrl/orders/payment-methods-v1"
        val CREATE_PAYMENT = "$baseUrl/orders/create-payment"
        val VERIFY_PAYMENT = "$paymentBaseUrl/payment/verify"
        val TIME_SLOT = "$baseUrl/orders/time-slot"
        val APPLY_COUPON = "$baseUrl/coupons/apply-coupon"
        val UPSELLING_PRODUCTS = "$baseUrl/products/suggestions/upselling-products"
        val META_DATA = "$baseUrl/metadata"
        val CHECK_DELIVERABLE_ADDRESS = "$baseUrl/addresses/checkdeliverable"
        val ALL_CATEGORY = "$baseUrl/dashboard/categories/allcategory"
        val USER_CART = "$baseUrl/orders/cart1"
        val ORDER_HISTORY = "$baseUrl/orders/delivery"
        val SUGGEST_PRODUCT = "$baseUrl/products/suggest-product"
        val USER_PROFILE_PAGE = "$baseUrl/users/profile-page"
        val VERIFY_DELIVERABLE_ADDRESS = "$baseUrl/dashboard/warehouses/verify-address"
        val ADDRESS = "$baseUrl/addresses"
        val HOME_PAGE = "$baseUrl/products/home-page"
        val USER_ORDERS = "$baseUrl/orders"
        val UPDATE_RATING = "$baseUrl/orders/update-rating"
        val PRODUCT_DETAILS = "$baseUrl/products/products-by-productid"
        val ORDER_INVOICE = "$baseUrl/orders/invoice"
        val PLACES_SEARCH_NAME = "$baseUrl/places/autocomplete"
        val RAPID_WALLET_VERIFY_USER = "$baseUrl/auth/rapid-verify"
        val RAPID_WALLET_SEND_OTP = "$baseUrl/rapid/send-otp"
        val RAPID_DO_PAYMENT = "$baseUrl/rapid/rapid-transaction"

        //non-veg
        val NON_VEG_MERCHANT_DETAILS = "$baseUrl/nv_home/merchant-details"
        val NON_VEG_HOME_PAGE_BANNER = "$baseUrl/nv_home/home-page-banner"
        val NON_VEG_CATEGORY = "$baseUrl/nv_category/get-category-list"
        val NON_VEG_PRODUCT_BY_CATEGORY_MERCHANT = "$baseUrl/nv_merchant_product/get-merchant-product-by-category"
        val NON_VEG_CART_DETAILS = "$baseUrl/nv_cart/get-cart-details"
        val NON_VEG_CREATE_CART = "$baseUrl/nv_cart/create-cart"
        val NON_VEG_UPDATE_CART = "$baseUrl/nv_cart/update-cart"
        val NON_VEG_COD_CREATE_PAYMENT = "$baseUrl/nv_payment/make-payment"
        val NON_VEG_ORDER_DETAILS = "$baseUrl/nv_order/order-details"
        val NON_VEG_RAPID_PAYMENT = "$baseUrl/nv_rapid_payment/rapid-transaction"
        val NON_VEG_SEARCH_ITEM = "$baseUrl/nv_search/product-search"
        val NON_VEG_HOME_PAGE_DATA = "$baseUrl/nv_home/data"
        val NON_VEG_USER_BOOKING_HISTORY = "$baseUrl/nv_order/all-order-of-user"
        val NON_VEG_PRODUCT_DETAILS = "$baseUrl/nv_merchant_product/get-merchant-product-details"
        val GET_USER_LATEST_LOCAL_NV_CART_DETAILS = "$baseUrl/nv_merchant_product/local-cart-product-details"


        //common
        val GET_SERVICE_LIST = "$baseUrl/zust_service/service-list"
        val GET_SERVICE_PAGE_DATA = "$baseUrl/zust_service/service-page-data"
        val GET_USER_ANALYSIS = "$baseUrl/report/user-analysis"
        val SEND_SUBSCRIPTION_DETAILS = "$baseUrl/zust_subscription/form"

        //zust pay
        val ZUST_PAY_AMOUNT = "$zustPayBaseUrl/zust_pay/wallet-amount"
        val ZUST_PAY_CREDIT_DETAILS = "$zustPayBaseUrl/zust_pay/credit_details"
        val ZUST_PAY_TRANSACTION_DETAIL = "$zustPayBaseUrl/zust_pay/credit_details"

    }
}