package `in`.opening.area.zustapp.network

class NetworkUtility {
    companion object {
        private val baseUrl = getCompleteBaseUrl()

        private val paymentBaseUrl = getPaymentUrl()
        val END_POINT_REGISTER = "$baseUrl/auth/register-send-otp"
        val END_POINT_AUTH_VERIFICATION = "$baseUrl/auth/register-verify-otp"
        val END_POINT_UPDATE_PROFILE = "$baseUrl/users/update-profile"
        val END_POINT_SED_FCM = "$baseUrl/users/save-fcm-token"
        val TRENDING_PRODUCTS = "$baseUrl/products/trend-product"
        val SUB_CATEGORY = "$baseUrl/products/sub-category"
        val PRODUCT_LIST_BY_CATEGORY = "$baseUrl/products/category-by-product"
        val ORDERS_CART = "$baseUrl/orders/cart"
        val COUPONS = "$baseUrl/coupons/user-coupons"
        val PRODUCT_SEARCH = "$baseUrl/products/search"
        val PAYMENT_METHOD = "$baseUrl/orders/payment-methods"
        val CREATE_PAYMENT = "$baseUrl/orders/create-payment"
        val VERIFY_PAYMENT = "$paymentBaseUrl/payment/verify"
        val TIME_SLOT = "$baseUrl/orders/time-slot"
        val APPLY_COUPON = "$baseUrl/coupons/apply-coupon"
        val UPSELLING_PRODUCTS = "$baseUrl/products/suggestions/upselling-products"
        val META_DATA = "$baseUrl/metadata"
        val CHECK_DELIVERABLE_ADDRESS = "$baseUrl/addresses/checkdeliverable"
        val ALL_CATEGORY = "$baseUrl/dashboard/categories/allcategory"
        val USER_CART = "$baseUrl/orders/cart"
        val ORDER_HISTORY = "$baseUrl/orders/delivery"
        val SUGGEST_PRODUCT = "$baseUrl/products/suggest-product"
        val USER_PROFILE_PAGE = "$baseUrl/users/profile-page"
        val VERIFY_DELIVERABLE_ADDRESS = "$baseUrl/dashboard/warehouses/verify-address"
        val ADDRESS = "$baseUrl/addresses"
        val HOME_PAGE_V1 = "$baseUrl/products/home-page1"
        val USER_ORDERS = "$baseUrl/orders"


        private const val isProd = true

        private fun getCompleteBaseUrl(): String {
            return if (isProd) {
                "https://zcapi.zustapp.com/" + "greenboys-api/api/v1"
            } else {
                "https://gcapi.grinzy.in/" + "greenboys-api/api/v1"
            }
        }

        private fun getPaymentUrl(): String {
            return if (isProd) {
                "https://paymentapi.zustapp.com/api/v1"
            } else {
                "https://paymentapi.grinzy.in/api/v1"
            }
        }
    }
}