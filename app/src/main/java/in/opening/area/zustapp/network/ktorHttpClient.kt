package `in`.opening.area.zustapp.network

import `in`.opening.area.zustapp.BuildConfig
import `in`.opening.area.zustapp.analytics.FirebaseApiEvents
import `in`.opening.area.zustapp.utility.DeviceInfo
import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

private const val APP_VERSION_CODE = "version_code"
private const val APP_VERSION_NAME = "version_name"
private const val DEVICE_ID = "device_id"
private const val SOURCE = "ZUST_B2C"
private const val OS_MODEL = "os_model"
private const val DEVICE_MODEL = "device_model"
private const val BRAND = "brand"

val ktorHttpClient = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = GsonSerializer()

    }
    install(DefaultRequest) {
        // Increase the timeout to 30 seconds
        timeout {
            requestTimeoutMillis = 30_000
        }
    }

    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Log.e("Zust", message)
            }
        }
        level = LogLevel.ALL

        install(ResponseObserver) {
            onResponse { response ->
                Log.e("Zust", "${response.status.value}")
            }
        }
    }

    install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        headers {
            append(APP_VERSION_CODE, BuildConfig.VERSION_CODE.toString())
            append("source", SOURCE)
            append(DEVICE_ID, DeviceInfo.getDeviceIdInfo())
            if (url.build().toString().contains("home")) {
                append(APP_VERSION_NAME, BuildConfig.VERSION_NAME)
                append(OS_MODEL, android.os.Build.MODEL)
                append(DEVICE_MODEL, android.os.Build.DEVICE)
                append(BRAND, android.os.Build.BRAND)
            }
        }
    }
}


