package `in`.opening.area.zustapp.utility

import com.google.errorprone.annotations.Keep

@Keep
data class UserCustomError(
    val errorField: String,
    val errorMessage: String
)
