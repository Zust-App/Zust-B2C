package `in`.opening.area.zustapp.network

import `in`.opening.area.zustapp.BuildConfig
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics
import `in`.opening.area.zustapp.utility.DeviceInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.http.*


private const val APP_VERSION_CODE = "versionCode"
private const val APP_VERSION_NAME = "versionName"
private const val DEVICE_ID = "deviceId"
private const val SOURCE = "ZUST_B2C"
private const val OS_MODEL = "osModel"
private const val DEVICE_MODEL = "deviceModel"
private const val BRAND = "brand"

val ktorHttpClient = HttpClient(CIO) {

    install(JsonFeature) {
        serializer = GsonSerializer()
    }
    install(DefaultRequest) {
        timeout {
            requestTimeoutMillis = 30_000
        }
    }

    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                if (BuildConfig.DEBUG) {
                    Log.e("Zust", message)
                }
            }
        }
        level = LogLevel.ALL
    }

    install(ResponseObserver) {
        onResponse { response ->
            try {
                val bundle = Bundle()
                bundle.putString("url", response.call.request.url.toString())
                bundle.putString("response_time", response.responseTime.toString())
                bundle.putInt("response_status_code", response.status.value)
                FirebaseAnalytics.logEvents(FirebaseAnalytics.API_RESPONSE, bundle)
            } catch (e: Exception) {
                FirebaseAnalytics.logEvents(FirebaseAnalytics.API_ANDROID_EXCEPTION)
            }
            if (BuildConfig.DEBUG) {
                Log.e("Zust code-->", "${response.status.value}")
            }
        }
    }
    install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        headers {
            append(APP_VERSION_CODE, BuildConfig.VERSION_CODE.toString())
            append("source", SOURCE)
            append(DEVICE_ID, DeviceInfo.getDeviceIdInfo())
            url.build().toString().let {
                if (it.contains("home") || it.contains("metadata")) {
                    append(APP_VERSION_NAME, BuildConfig.VERSION_NAME)
                    append(OS_MODEL, Build.MODEL)
                    append(DEVICE_MODEL, Build.DEVICE)
                    append(BRAND, Build.BRAND)
                }
            }
        }
    }
}

//    engine {
//        https {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                val customTrustManager = CustomX509TrustManager()
//                val sslContext = SSLContext.getInstance("TLS")
//                sslContext.init(null, arrayOf<TrustManager>(customTrustManager), SecureRandom())
//                this.trustManager = customTrustManager
//            }
//        }
//    }
