@file:Suppress("DEPRECATION")

package `in`.opening.area.zustapp.utility

import `in`.opening.area.zustapp.MyApplication
import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings.Secure
import android.telephony.TelephonyManager
import java.util.*


object DeviceInfo {
    private var deviceID: String? = null
    fun getDeviceIdInfo(): String {
        if (deviceID != null) {
            return deviceID!!
        }
        try {
            if (!getDeviceId().isNullOrEmpty()) {
                deviceID = getDeviceId() ?: getUniqueId()
            } else if (!getAndroidId().isNullOrEmpty()) {
                deviceID = getAndroidId() ?: getUniqueId()
            }
        } catch (e: Exception) {
            deviceID = getUniqueId()
        }
        deviceID = getUniqueId()
        return deviceID!!
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceId(): String? {
        val telephonyManager = MyApplication.appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.deviceId
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId(): String? {
        return Secure.getString(MyApplication.appContext.contentResolver, Secure.ANDROID_ID)
    }

    private fun getUniqueId(): String {
        val uuid: UUID = UUID.randomUUID()
        return uuid.toString()
    }
}