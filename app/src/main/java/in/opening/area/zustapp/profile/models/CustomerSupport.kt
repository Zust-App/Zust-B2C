package `in`.opening.area.zustapp.profile.models

import com.google.errorprone.annotations.Keep

@Keep
data class CustomerSupport(
    val email: List<String>,
    val phoneNos: List<String>
)