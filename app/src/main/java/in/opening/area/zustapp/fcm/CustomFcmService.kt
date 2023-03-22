package `in`.opening.area.zustapp.fcm

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.HomeLandingActivity
import `in`.opening.area.zustapp.network.ApiRequestManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@ServiceScoped
@AndroidEntryPoint
class CustomFcmService : FirebaseMessagingService() {
    @Inject
    lateinit var fcmManager: FCMManager

    companion object {
        const val FCM_KEY_HEADER = "header"
        const val FCM_KEY_BODY_TEXT = "bodyText"
        const val FCM_KEY_DEEP_LINK = "deep_link"
        const val FCM_IMAGE_URL_KEY = "image_url"
        const val ORDER_ID = "orderId"
        const val KEY_ORDER_DETAIL = "order_detail"
        const val KEY = "key"
        const val DEEP_LINK_DATA = "fcm_data"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("FCM TOKEN", token)

        fcmManager.sendFirebaseToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        try {
            parseFcmRemoteMessage(message)
        } catch (e: Exception) {
            e.localizedMessage
        }
    }

    private fun parseFcmRemoteMessage(message: RemoteMessage) {
        val data = message.data
        var title: String? = null
        var orderId: String? = null
        if (data.containsKey(FCM_KEY_HEADER)) {
            title = data[FCM_KEY_HEADER]
        }
        var body: String? = null
        if (data.containsKey(FCM_KEY_BODY_TEXT)) {
            body = data[FCM_KEY_BODY_TEXT]
        }
        var imageUrl: String? = null
        if (data.containsKey(FCM_IMAGE_URL_KEY)) {
            imageUrl = data[FCM_IMAGE_URL_KEY]
        }
        if (data.containsKey(KEY)) {
            val key = data[KEY]
            if (key?.equals(KEY_ORDER_DETAIL, ignoreCase = true) == true) {
                if (data.containsKey(ORDER_ID)) {
                    orderId = data[ORDER_ID]
                }
                createFcmNotification(title, body, imageUrl, orderId, key)
            } else {
                createFcmNotification(title, body, imageUrl, null, key)
            }
        } else {
            createFcmNotification(title, body, imageUrl, null, null)
        }
    }

    private fun createFcmNotification(
        title: String?, body: String?,
        imageUrl: String?,
        orderId: String?,
        key: String?,
    ) {
        if (title != null && body != null) {
            val notificationId = System.currentTimeMillis().toInt()
            val intent = Intent(this, HomeLandingActivity::class.java)
            if (orderId != null && key.equals(KEY_ORDER_DETAIL, true)) {
                intent.putExtra(DEEP_LINK_DATA, OrderDetailDeepLinkModel(orderId, KEY_ORDER_DETAIL))
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val flag = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            val pendingIntent = PendingIntent.getActivity(this, notificationId, intent, flag)
            var imageBitmap: Bitmap? = null
            if (imageUrl != null) {
                imageBitmap = getBitmapFromUrl(imageUrl)
            }
            val channelName = getString(R.string.notif_channel)
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(this, notificationId.toString())
                    .setSmallIcon(R.mipmap.zust_app_logo)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
            if (imageBitmap != null) {
                notificationBuilder.setLargeIcon(imageBitmap)
            }
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(notificationId.toString(),
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(notificationId, notificationBuilder.build())
        }
    }

    private fun getBitmapFromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }

}

@Singleton
class FCMManager @Inject constructor() {

    @Inject
    lateinit var fcmServerConnection: FCMServerConnection

    fun sendFirebaseToken(fcmToken: String) {
        fcmServerConnection.sendFcmTokenToServer(fcmToken)
    }

}

@Singleton
class FCMServerConnection @Inject constructor(private val apiRequestManager: ApiRequestManager) {

    fun sendFcmTokenToServer(fcmToken: String) {
        CoroutineScope(Dispatchers.IO).launch {
            apiRequestManager.sendFcmTokenToServer(fcmToken = fcmToken)
        }
    }
}

data class OrderDetailDeepLinkModel(
    val orderId: String? = null,
    val key: String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(orderId)
        parcel.writeString(key)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetailDeepLinkModel> {
        override fun createFromParcel(parcel: Parcel): OrderDetailDeepLinkModel {
            return OrderDetailDeepLinkModel(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetailDeepLinkModel?> {
            return arrayOfNulls(size)
        }
    }
}