package `in`.opening.area.zustapp.storage.datastore

import `in`.opening.area.zustapp.helper.LanguageManager
import `in`.opening.area.zustapp.orderDetail.models.Address
import android.content.SharedPreferences
import androidx.core.content.edit
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
        const val ADDRESS_KEY = "saved_address"
        const val MERCHANT_ID = "merchant_id"
        const val FCM_TOKEN = "fcm_token"
        const val PROFILE_CREATED = "profile_created"
        const val ONBOARDING_SHOWN = "onboarding_shown"
        const val SELECTED_LANG = "select_lang"
        const val USER_MOBILE_NUM = "user_mobile_num"
        const val FREE_DELIVERY_BASE_PRICE = "free_delivery_base_price"
        const val DELIVERY_FEE_PRICE = "delivery_fee_price"

        const val SUPPORT_WA_NUM = "support_wa_num"
        const val SUPPORT_EMAIL = "support_email"
        const val SUPPORT_CALL = "support_call"
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

    fun saveAddress(address: Address) {
        try {
            val string = gson.toJson(address)
            sharedPreferences.edit().putString(ADDRESS_KEY, string).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getUserAddress(): Address? {
        return try {
            val addressString = sharedPreferences.getString(ADDRESS_KEY, "")
            gson.fromJson(addressString, Address::class.java)
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

    fun saveUserCurrentLocation(address: android.location.Address) {
        try {
            val string = gson.toJson(address)
            sharedPreferences.edit().putString(ADDRESS_KEY, string).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getUserCurrentLocation(): android.location.Address? {
        return try {
            val addressString = sharedPreferences.getString(ADDRESS_KEY, "")
            gson.fromJson(addressString, android.location.Address::class.java)
        } catch (e: Exception) {
            null
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


}