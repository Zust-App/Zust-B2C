package `in`.opening.area.zustapp.fcm

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmTokenManager @Inject constructor() {
    suspend fun getFirebaseToken(): String? {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Throwable) {
            null
        }
    }
}