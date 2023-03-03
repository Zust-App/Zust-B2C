package `in`.opening.area.zustapp.profile.models

import com.google.errorprone.annotations.Keep

@Keep
data class ProfileData(
    val aboutUsUrl: String?=null,
    val app: AppVersionData?=null,
    val customerSupport: CustomerSupport?=null,
    val faqurl: String?=null,
    val openSourceUrl: String?=null,
    val playStoreUrl: String?=null,
    val privacyPolicyUrl: String?=null,
    val refer: Refer?=null,
    val termAndConditionUrl: String?=null,
    val user: User?=null
)

fun ProfileData.convertProfileData() {

}