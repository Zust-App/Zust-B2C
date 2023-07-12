package zustElectronics.network

import `in`.opening.area.zustapp.BuildConfig

class ZeApiMetaData {
    companion object {
        private const val isProdEnv = true
        private val zeBaseUrl = getCompleteBaseUrl()
        private fun getCompleteBaseUrl(): String {
            return if (!BuildConfig.DEBUG) {
                BuildConfig.PROD_BASE_URL
            } else {
                if (isProdEnv) {
                    BuildConfig.PROD_BASE_URL
                } else {
                    BuildConfig.DEV_BASE_URL
                }
            }
        }

        val PRODUCT_LIST_URL = "$zeBaseUrl/electronic_product"
        val PRODUCT_BY_MERCHANT_ID_URL = "$zeBaseUrl/electronic_product/by-merchantId"
        val PRODUCT_BY_ID_URL = "$zeBaseUrl/electronic_product/1"

    }

}