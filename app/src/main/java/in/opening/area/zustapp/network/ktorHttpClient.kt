package `in`.opening.area.zustapp.network

import `in`.opening.area.zustapp.BuildConfig
import `in`.opening.area.zustapp.utility.DeviceInfo
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

private const val APP_VERSION_CODE = "version_code"
private const val APP_VERSION_NAME = "version_name"
private const val DEVICE_ID = "device_id"
private const val SOURCE = "ZUST_B2C"
private const val OS_MODEL = "os_model"
private const val DEVICE_MODEL = "device_model"
private const val BRAND = "brand"

val ktorHttpClient = HttpClient(CIO) {
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
        install(ResponseObserver) {
            onResponse { response ->
                if (BuildConfig.DEBUG) {
                    Log.e("Zust", "${response.status.value}")
                }
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
                append(OS_MODEL, Build.MODEL)
                append(DEVICE_MODEL, Build.DEVICE)
                append(BRAND, Build.BRAND)
            }
        }
    }
}


