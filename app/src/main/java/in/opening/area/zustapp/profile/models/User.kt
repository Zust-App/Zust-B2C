package `in`.opening.area.zustapp.profile.models

import com.google.errorprone.annotations.Keep

@Keep
data class User(
    val email: String? = null,
    val phoneNo: String? = null,
    val userName: String? = null
) {
    fun getDisplayIconText(): String {
        try {
            return buildString {
                userName?.trim()?.split(" ")?.apply {
                    if (this.isEmpty()) {
                        append("G")
                    }
                    if (this.size == 1) {
                        append(this[0][0].toString().uppercase())
                        if (this[0].length > 1) {
                            append(this[0][1].toString().uppercase())
                        }
                    }
                    if (this.size > 1) {
                        append(this[0][0].toString().uppercase())
                        append(this[1][0].toString().uppercase())
                    }
                } ?: run {
                    append("G")
                }
            }
        }catch (e:Exception){
            return userName?:"G"
        }
    }
}