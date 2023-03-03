package `in`.opening.area.zustapp.uiModels

import `in`.opening.area.zustapp.profile.models.ProfileData
import `in`.opening.area.zustapp.utility.UserCustomError

sealed interface UserProfileUi {
    val isLoading: Boolean

    data class ProfileSuccess(
        override val isLoading: Boolean,
        val data: ProfileData? = ProfileData(),
        val timeStamp: Long = System.currentTimeMillis()
    ) : UserProfileUi

    data class InitialUi(
        override val isLoading: Boolean,
        val timeStamp: Long = System.currentTimeMillis()
    ) : UserProfileUi

    data class ErrorUi(
        override val isLoading: Boolean,
        val errors: List<UserCustomError> = arrayListOf(),
        val errorMsg: String? = "",
        val timeStamp: Long = System.currentTimeMillis()
    ) : UserProfileUi
}