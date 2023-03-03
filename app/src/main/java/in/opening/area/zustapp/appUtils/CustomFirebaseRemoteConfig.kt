package `in`.opening.area.zustapp.appUtils

import `in`.opening.area.zustapp.BuildConfig
import `in`.opening.area.zustapp.R
import android.app.Activity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

object CustomFirebaseRemoteConfig {

    private const val show_bill="show_bill";//boolean

    fun init(){
        val duration = if (BuildConfig.DEBUG) 20L else 3600L
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(duration)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

    }

    fun fetchAndActivate(activity: Activity){
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(activity) {

            }
    }

    fun isDownloadBillAvail(): Boolean {
        return FirebaseRemoteConfig.getInstance().getBoolean(show_bill)
    }
}