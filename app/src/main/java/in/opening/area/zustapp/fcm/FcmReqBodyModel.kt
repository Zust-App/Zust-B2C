package `in`.opening.area.zustapp.fcm

import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class FcmReqBodyModel(
    @SerializedName("fcmToken")
    val fcmToken: String? = null)
