package `in`.opening.area.zustapp.login.model

import com.google.errorprone.annotations.Keep

@Keep
class UserLoginModel(var mobileNum: String = "",
                     var otp: String = "",
                     var userName: String = "",
                     var userEmail: String = "") {
    fun copy(): UserLoginModel {
        val user = UserLoginModel()
        user.userEmail = this.userEmail
        user.userName = this.userName
        user.otp = this.otp
        user.mobileNum = this.mobileNum
        return user
    }
}