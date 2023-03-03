package `in`.opening.area.zustapp.profile.models

import com.google.errorprone.annotations.Keep

@Keep
data class AppVersionData(
    val appVersion: String,
    val bundleVersion: String
)