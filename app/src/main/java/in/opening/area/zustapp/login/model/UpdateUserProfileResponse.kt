package `in`.opening.area.zustapp.login.model

import `in`.opening.area.zustapp.utility.UserCustomError
import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UpdateUserProfileResponse(
    @SerializedName("data")
    val updateUserProfileData: UpdateUserProfileData?=null,
    val errors: List<UserCustomError>?=null,
    val message: String?=null,
)

@Keep
data class UpdateUserProfileData(
    val email:String?=null,
    val id:String?=null,
    val phoneNo:String?=null,
    val username:String?=null
)

