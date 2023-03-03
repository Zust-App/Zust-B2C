package `in`.opening.area.zustapp.storage.datastore

import `in`.opening.area.zustapp.helper.LanguageManager
import `in`.opening.area.zustapp.orderDetail.models.Address
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
        const val ADDRESS_KEY = "address"
        const val MERCHANT_ID = "merchant_id"
        const val FCM_TOKEN = "fcm_token"
        const val PROFILE_CREATED = "profile_created"
        const val ONBOARDING_SHOWN = "onboarding_shown"
        const val SELECTED_LANG = "select_lang"
        const val MOBILE_NUM = "mobile_num"

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
    open fun removeSavedAddress() {
        sharedPreferences.edit().remove(ADDRESS_KEY).apply()
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

    open fun saveOnBoardingShown(isShown: Boolean) {
        sharedPreferences.edit().putBoolean(ONBOARDING_SHOWN, isShown).apply()
    }

    open fun doesOnBoardingShown(): Boolean {
        return sharedPreferences.getBoolean(ONBOARDING_SHOWN, false)
    }

    fun saveUserSelectedLanguage(language: String) {
        sharedPreferences.edit().putString(SELECTED_LANG, language).apply()
    }

    fun getSavedUserLanguage(): String {
        val value = sharedPreferences.getString(SELECTED_LANG, LanguageManager.ENG)
        return value ?: LanguageManager.ENG
    }

    fun saveUserPhoneNumber(mobileNum: String) {
        sharedPreferences.edit().putString(MOBILE_NUM, mobileNum).apply()
    }

    fun getUserMobileNumber(): String {
        return sharedPreferences.getString(MOBILE_NUM, "8000012121")?:"8000012121"
    }

}