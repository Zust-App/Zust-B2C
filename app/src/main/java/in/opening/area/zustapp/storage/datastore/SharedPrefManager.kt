package `in`.opening.area.zustapp.storage.datastore

import `in`.opening.area.zustapp.helper.LanguageManager
import zustbase.orderDetail.models.ZustAddress
import android.content.SharedPreferences
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class SharedPrefManager @Inject constructor(private val sharedPreferences: SharedPreferences) {
    @Inject
    lateinit var gson: Gson

    companion object {
        const val SHARED_PREF_NAME = "shared_pref_name"
        private const val AUTH_TOKEN = "auth_token"
        const val ADDRESS_KEY_1 = "saved_address_1"
        const val ADDRESS_KEY = "saved_address"
        const val MERCHANT_ID = "merchant_id"
        const val FCM_TOKEN = "fcm_token"
        const val PROFILE_CREATED = "profile_created"
        const val ONBOARDING_SHOWN = "onboarding_shown"
        const val SELECTED_LANG = "select_lang"
        const val USER_MOBILE_NUM = "user_mobile_num"
        const val FREE_DELIVERY_BASE_PRICE = "free_delivery_base_price"
        const val DELIVERY_FEE_PRICE = "delivery_fee_price"
        const val NON_VEG_FREE_DELIVERY_MIN_PRICE = "non_veg_delivery_min_fee"
        const val SUPPORT_WA_NUM = "support_wa_num"
        const val SUPPORT_EMAIL = "support_email"
        const val SUPPORT_CALL = "support_call"

        const val SAVE_NON_VEG_MERCHANT_ID = "nv_merchant_id"
        const val CLEAR_CART_GROCERY = "clear_cart_grocery"
        const val CLEAR_CART_GROCERY1 = "clear_cart_grocery1"
        const val CLEAR_CART_GROCERY2 = "clear_cart_grocery1"
        const val CLEAR_CART_GROCERY3 = "clear_cart_grocery3"


        const val AFFILIATE_PARTNER_LINK = "afp_link"

    }

    open fun getUserAuthToken(): String? {
        return sharedPreferences.getString(AUTH_TOKEN, "")
    }

    open fun saveFcmToken(fcmToken: String) {
        sharedPreferences.edit().putString(FCM_TOKEN, fcmToken).apply()
    }

    open fun getFcmToken(): String? {
        return sharedPreferences.getString(FCM_TOKEN, "")
    }

    open fun removeAuthToken() {
        sharedPreferences.edit().remove(AUTH_TOKEN).apply()
    }

    open fun removeMerchantId() {
        sharedPreferences.edit().remove(MERCHANT_ID).apply()
    }

    open fun removeSavedAddress() {
        sharedPreferences.edit().remove(ADDRESS_KEY_1).apply()
    }

    open fun removeSavedAddressPrevious() {
        sharedPreferences.edit().remove(ADDRESS_KEY).apply()
    }

    open fun removeIsProfileCreated() {
        sharedPreferences.edit().remove(PROFILE_CREATED).apply()
    }

    open fun removePhoneNumber() {
        sharedPreferences.edit().remove(USER_MOBILE_NUM).apply()
    }

    fun saveAuthToken(authToken: String?) {
        if (authToken.isNullOrEmpty()) {
            return
        }
        sharedPreferences.edit().putString(AUTH_TOKEN, authToken).apply()
    }

    fun saveAddress(zustAddress: ZustAddress) {
        try {
            val string = gson.toJson(zustAddress)
            sharedPreferences.edit().putString(ADDRESS_KEY_1, string).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getUserAddress(): ZustAddress? {
        return try {
            val addressString = sharedPreferences.getString(ADDRESS_KEY_1, "")
            gson.fromJson(addressString, ZustAddress::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun saveKeyForProfileCreate(isProfileCreated: Boolean) {
        sharedPreferences.edit().putBoolean(PROFILE_CREATED, isProfileCreated).apply()
    }

    fun checkIsProfileCreate(): Boolean {
        return sharedPreferences.getBoolean(PROFILE_CREATED, false)
    }


    fun saveUserSelectedLanguage(language: String) {
        sharedPreferences.edit().putString(SELECTED_LANG, language).apply()
    }

    fun getSavedUserLanguage(): String {
        val value = sharedPreferences.getString(SELECTED_LANG, LanguageManager.ENG)
        return value ?: LanguageManager.ENG
    }


    fun saveUserPhoneNumber(mobileNum: String) {
        sharedPreferences.edit().putString(USER_MOBILE_NUM, mobileNum).apply()
    }

    fun getUserMobileNumber(): String {
        return sharedPreferences.getString(USER_MOBILE_NUM, "8000012121") ?: "8000012121"
    }

    fun saveFreeDeliveryBasePrice(freeDeliveryFee: Double?) {
        sharedPreferences.edit().putFloat(FREE_DELIVERY_BASE_PRICE, (freeDeliveryFee?.toFloat()) ?: 99f).apply()
    }

    fun getFreeDeliveryBasePrice(): Int {
        return sharedPreferences.getFloat(FREE_DELIVERY_BASE_PRICE, 99f).toInt()
    }


    fun saveDeliveryFee(deliveryFee: Double?) {
        sharedPreferences.edit().putFloat(DELIVERY_FEE_PRICE, (deliveryFee?.toFloat()) ?: 10f).apply()
    }

    fun getDeliveryFee(): Int {
        return sharedPreferences.getFloat(DELIVERY_FEE_PRICE, 10f).toInt()
    }

    fun saveNonVegFreeDeliveryFee(deliveryFee: Double?) {
        sharedPreferences.edit().putFloat(NON_VEG_FREE_DELIVERY_MIN_PRICE, (deliveryFee?.toFloat()) ?: 200f).apply()
    }

    fun getNonVegFreeDeliveryFee(): Int {
        return sharedPreferences.getFloat(NON_VEG_FREE_DELIVERY_MIN_PRICE, 200f).toInt()
    }

    fun getSupportPhoneNumber(): String {
        return sharedPreferences.getString(SUPPORT_CALL, "7858906229") ?: "7858906229"
    }

    fun getSupportEmail(): String {
        return sharedPreferences.getString(SUPPORT_EMAIL, "7858906229") ?: "7858906229"
    }

    fun getSupportWhatsapp(): String {
        return sharedPreferences.getString(SUPPORT_WA_NUM, "7858906229") ?: "7858906229"
    }

    fun saveSupportPhoneNumber(supportPhNum: String?) {
        if (supportPhNum == null) {
            return
        }
        sharedPreferences.edit().putString(SUPPORT_CALL, supportPhNum).apply()
    }

    fun saveSupportEmail(supportEmail: String?) {
        if (supportEmail == null) {
            return
        }
        sharedPreferences.edit().putString(SUPPORT_EMAIL, supportEmail).apply()
    }

    fun saveSupportWhatsapp(supportWhatsapp: String?) {
        if (supportWhatsapp == null) {
            return
        }
        sharedPreferences.edit().putString(SUPPORT_WA_NUM, supportWhatsapp).apply()
    }

    fun saveUserCurrentLocation(zustAddress: ZustAddress) {
        try {
            val string = gson.toJson(zustAddress)
            sharedPreferences.edit().putString(ADDRESS_KEY_1, string).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun getMerchantId(): Int {
        return sharedPreferences.getInt(MERCHANT_ID, -1)
    }

    fun saveMerchantId(merchantId: Int?) {
        if (merchantId != null) {
            sharedPreferences.edit().putInt(MERCHANT_ID, merchantId).apply()
        } else {
            sharedPreferences.edit().putInt(MERCHANT_ID, -1).apply()
        }
    }

    fun getNonVegMerchantId(): Int {
        return sharedPreferences.getInt(SAVE_NON_VEG_MERCHANT_ID, -1)
    }

    fun saveNonVegMerchantId(merchantId: Int?) {
        if (merchantId != null) {
            sharedPreferences.edit().putInt(SAVE_NON_VEG_MERCHANT_ID, merchantId).apply()
        } else {
            sharedPreferences.edit().putInt(SAVE_NON_VEG_MERCHANT_ID, -1).apply()
        }
    }

    fun removeNonVegMerchantId() {
        sharedPreferences.edit().remove(SAVE_NON_VEG_MERCHANT_ID).apply()
    }

    open fun getClearGroceryCart(): Boolean {
        return sharedPreferences.getBoolean(CLEAR_CART_GROCERY3, false)
    }

    open fun saveClearGroceryCart(value: Boolean) {
        removeClearCart()
        sharedPreferences.edit().putBoolean(CLEAR_CART_GROCERY3, value).apply()
    }

    open fun removeClearCart() {
        sharedPreferences.edit().remove(CLEAR_CART_GROCERY).apply()
        sharedPreferences.edit().remove(CLEAR_CART_GROCERY1).apply()
        sharedPreferences.edit().remove(CLEAR_CART_GROCERY2).apply()
    }

    open fun getAffiliatePartnerLink(): String {
        return sharedPreferences.getString(AFFILIATE_PARTNER_LINK, "https://docs.google.com/forms/d/e/1FAIpQLSe7-e7MwGjMUpOGWAeoxAWo4s7bKcmt23BBtkLhElZbvh2iDw/viewform?vc=0&c=0&w=1&flr=0")
            ?: "https://docs.google.com/forms/d/e/1FAIpQLSe7-e7MwGjMUpOGWAeoxAWo4s7bKcmt23BBtkLhElZbvh2iDw/viewform?vc=0&c=0&w=1&flr=0"
    }

    open fun saveAffiliatePartnerLink(link: String?) {
        sharedPreferences.edit().putString(AFFILIATE_PARTNER_LINK, link ?: "https://docs.google.com/forms/d/e/1FAIpQLSe7-e7MwGjMUpOGWAeoxAWo4s7bKcmt23BBtkLhElZbvh2iDw/viewform?vc=0&c=0&w=1&flr=0").apply()
    }

    open fun ktorBaseUrl(){

    }
    open fun getKtorBaseUrl(){
       // return sharedPreferences.getBoolean(CLEAR_CART_GROCERY3, false)
    }
}
