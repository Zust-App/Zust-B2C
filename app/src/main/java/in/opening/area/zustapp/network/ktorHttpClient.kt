package `in`.opening.area.zustapp.network

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.http.*


val ktorHttpClient = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = GsonSerializer()
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
    }
}