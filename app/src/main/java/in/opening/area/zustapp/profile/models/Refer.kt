package `in`.opening.area.zustapp.profile.models

import com.google.errorprone.annotations.Keep

@Keep
data class Refer(
    val code: String?=null,
    val description: String?=null,
    val header: String?=null,
    val imageUrl: String?=null
)