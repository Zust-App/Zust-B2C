package `in`.opening.area.zustapp.profile

interface ProfileActionCallback {
    fun handleClick(code:Int,data:String?=null)

    companion object {
        const val MY_ORDER = 1
        const val MY_ADDRESS = 2
        const val SUGGEST_PRODUCT = 3
        const val HELPLINE = 4
        const val FAQ = 5
        const val SHARE_APP = 6
        const val RATE_US_PLAY_STORE = 7
        const val TC = 8
        const val PRIVACY_POLICY = 9
        const val ABOUT_US = 10
        const val OPEN_SOURCE = 11
        const val LOGOUT = 12
        const val UPDATE_APP=13
    }
}