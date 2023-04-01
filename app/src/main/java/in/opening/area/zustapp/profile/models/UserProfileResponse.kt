package `in`.opening.area.zustapp.profile.models

import `in`.opening.area.zustapp.utility.UserCustomError
import com.google.errorprone.annotations.Keep

@Keep
data class UserProfileResponse(
    val `data`: ProfileData? = null,
    val errors: List<UserCustomError>? = arrayListOf(),
    val message: String? = null,
    val statusCode: Int? = -1,
)