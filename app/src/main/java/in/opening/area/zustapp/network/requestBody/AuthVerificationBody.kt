package `in`.opening.area.zustapp.network.requestBody

import com.google.gson.annotations.SerializedName

data class AuthVerificationBody(
    val deviceId: String? = null,
    val otp: String,
    val phoneNo: String
)

data class UserProfileUpdateBody(
    @SerializedName("username")
    var name: String,
    @SerializedName("email")
    var userEmail: String? = null,
    var referCode:String?=null)