package `in`.opening.area.zustapp.uiModels.login

import `in`.opening.area.zustapp.login.model.UpdateUserProfileData
import `in`.opening.area.zustapp.utility.UserCustomError

sealed interface UpdateUserProfileUi {
    val isLoading: Boolean

    data class ProfileSuccess(
        override val isLoading: Boolean,
        val data: UpdateUserProfileData = UpdateUserProfileData(),
        val timeStamp: Long = System.currentTimeMillis()
    ) : UpdateUserProfileUi

    data class InitialUi(
        override val isLoading: Boolean,
        val timeStamp: Long = System.currentTimeMillis()
    ) : UpdateUserProfileUi

    data class ErrorUi(
        override val isLoading: Boolean,
        val errors: List<UserCustomError>? = arrayListOf(),
        val errorMsg: String? = "",
        val timeStamp: Long = System.currentTimeMillis()
    ) : UpdateUserProfileUi
}