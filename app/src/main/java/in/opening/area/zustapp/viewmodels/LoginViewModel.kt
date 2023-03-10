package `in`.opening.area.zustapp.viewmodels

import `in`.opening.area.zustapp.fcm.FcmTokenManager
import `in`.opening.area.zustapp.login.model.UserLoginModel
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import `in`.opening.area.zustapp.uiModels.login.GetOtpLoginUi
import `in`.opening.area.zustapp.uiModels.login.UpdateUserProfileUi
import `in`.opening.area.zustapp.uiModels.login.VerifyOtpUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.TmCountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

const val halfMinutes = (10 * 1000L)

@HiltViewModel
class LoginViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager) : ViewModel() {

    @Inject
    internal lateinit var sharedPrefManager: SharedPrefManager

    internal val userLoginModelFlow = MutableStateFlow(UserLoginModel())//locally needed only

    internal var getOtpUiState = MutableStateFlow<GetOtpLoginUi>(GetOtpLoginUi.InitialUi(false))
    internal var resendOtpUiState = MutableStateFlow<GetOtpLoginUi>(GetOtpLoginUi.InitialUi(false))

    internal var verifyOtpUiState = MutableStateFlow<VerifyOtpUi>(VerifyOtpUi.InitialUi(false))
    internal var userProfileUiState = MutableStateFlow<UpdateUserProfileUi>(UpdateUserProfileUi.InitialUi(false))

    private val tmCountDownTimer: TmCountDownTimer? by lazy { TmCountDownTimer(halfMinutes, callback, stopped) }
    internal val timerTextFlow by lazy { MutableSharedFlow<String?>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST) }


    internal infix fun makeLoginRequestForGetOtp(inputMobileNumber: String) = viewModelScope.launch {
        getOtpUiState.update { GetOtpLoginUi.InitialUi(true) }
        when (val response = apiRequestManager.makeLoginRequestForGetOtp(inputMobileNumber)) {
            is ResultWrapper.Success -> {
                if (response.value.data != null) {
                    getOtpUiState.update { GetOtpLoginUi.OtpGetSuccess(false, response.value.data) }
                } else {
                    getOtpUiState.update { GetOtpLoginUi.ErrorUi(false, errors = response.value.errors, errorMsg = response.value.message) }
                }
            }
            is ResultWrapper.UserTokenNotFound -> {
                getOtpUiState.update { GetOtpLoginUi.ErrorUi(false, errors = AppUtility.getAuthErrorArrayList()) }
            }
            is ResultWrapper.GenericError -> {
                getOtpUiState.update { GetOtpLoginUi.ErrorUi(false, errorMsg = response.error?.error ?: "Something went wrong") }
            }
            is ResultWrapper.NetworkError -> {
                getOtpUiState.update { GetOtpLoginUi.ErrorUi(false, errorMsg = "Something went wrong") }
            }
        }
    }

    internal infix fun makeResendOtp(inputMobileNumber: String) = viewModelScope.launch {
        resendOtpUiState.update { GetOtpLoginUi.InitialUi(true) }
        when (val response = apiRequestManager.makeLoginRequestForGetOtp(inputMobileNumber)) {
            is ResultWrapper.Success -> {
                if (response.value.data != null) {
                    resendOtpUiState.update { GetOtpLoginUi.OtpGetSuccess(false, response.value.data) }
                } else {
                    resendOtpUiState.update { GetOtpLoginUi.ErrorUi(false, errors = response.value.errors, errorMsg = response.value.message) }
                }
            }
            is ResultWrapper.UserTokenNotFound -> {
                resendOtpUiState.update { GetOtpLoginUi.ErrorUi(false, errors = AppUtility.getAuthErrorArrayList()) }
            }
            is ResultWrapper.GenericError -> {
                resendOtpUiState.update { GetOtpLoginUi.ErrorUi(false, errorMsg = response.error?.error ?: "Something went wrong") }
            }
            is ResultWrapper.NetworkError -> {
                resendOtpUiState.update { GetOtpLoginUi.ErrorUi(false, errorMsg = "Something went wrong") }
            }
        }
    }
    internal fun loginVerifyUserOTP(mobileNum: String, otp: String) = viewModelScope.launch {
        verifyOtpUiState.update { VerifyOtpUi.InitialUi(true) }
        when (val response = apiRequestManager.postAuthVerification(mobileNumber = mobileNum, otp = otp)) {
            is ResultWrapper.NetworkError -> {
                verifyOtpUiState.update { VerifyOtpUi.ErrorUi(false, errorMsg = "Something went wrong") }
            }
            is ResultWrapper.UserTokenNotFound -> {
                verifyOtpUiState.update { VerifyOtpUi.ErrorUi(false, errors = AppUtility.getAuthErrorArrayList()) }
            }
            is ResultWrapper.GenericError -> {
                verifyOtpUiState.update { VerifyOtpUi.ErrorUi(false, errorMsg = response.error?.error ?: "Something went wrong") }
            }
            is ResultWrapper.Success -> {
                if (response.value.data != null) {
                    verifyOtpUiState.update { VerifyOtpUi.VerificationSuccess(false, response.value.data) }
                } else {
                    verifyOtpUiState.update { VerifyOtpUi.ErrorUi(false, errors = response.value.errors, errorMsg = response.value.message) }
                }
            }
        }
    }

    internal fun updateUserProfile(userName: String, userEmail: String? = null, referralCode: String? = null) = viewModelScope.launch {
        userProfileUiState.update { UpdateUserProfileUi.InitialUi(true) }
        when (val response = apiRequestManager.postUpdateUserProfile(userName = userName, userEmail = userEmail, referralCode)) {
            is ResultWrapper.NetworkError -> {
                userProfileUiState.update { UpdateUserProfileUi.ErrorUi(false, errorMsg = "Something went wrong") }
            }
            is ResultWrapper.UserTokenNotFound -> {
                userProfileUiState.update { UpdateUserProfileUi.ErrorUi(false, errors = AppUtility.getAuthErrorArrayList()) }
            }
            is ResultWrapper.GenericError -> {
                userProfileUiState.update { UpdateUserProfileUi.ErrorUi(false, errorMsg = response.error?.error ?: "Something went wrong") }
            }
            is ResultWrapper.Success -> {
                if (response.value.updateUserProfileData != null) {
                    userProfileUiState.update { UpdateUserProfileUi.ProfileSuccess(false, response.value.updateUserProfileData) }
                } else {
                    userProfileUiState.update { UpdateUserProfileUi.ErrorUi(false, errors = response.value.errors, errorMsg = response.value.message) }
                }
            }
        }
    }

    internal infix fun setUserMobNum(it: String) {
        val user = userLoginModelFlow.value.copy()
        user.mobileNum = it
        userLoginModelFlow.value = user
    }

    internal infix fun setUserOtp(it: String) {
        val user = userLoginModelFlow.value.copy()
        user.otp = it
        userLoginModelFlow.value = user
    }

    @Inject
    lateinit var fcmTokenManager: FcmTokenManager

    fun sendFcmToken() {
        CoroutineScope(Dispatchers.IO).launch {
            val localSavedToken = sharedPrefManager.getFcmToken()
            if (localSavedToken.isNullOrEmpty()) {
                val fcmToken = withContext(Dispatchers.Default) { fcmTokenManager.getFirebaseToken() }
                if (fcmToken != null) {
                    apiRequestManager.sendFcmTokenToServer(fcmToken)
                }
            } else {
                apiRequestManager.sendFcmTokenToServer(localSavedToken)
            }
        }
    }

    fun saveKeyForProfileCreated(isProfileCreated: Boolean) {
        sharedPrefManager.saveKeyForProfileCreate(isProfileCreated)
    }

    internal fun startResendOtpTimer() {
        tmCountDownTimer?.startCountDownTimer()
    }

    internal fun stopResendOtpTimer() {
        tmCountDownTimer?.stopCountDownTimer()
    }

    fun isProfileCreated(): Boolean {
        return sharedPrefManager.checkIsProfileCreate()
    }

    private var callback = fun(timeLeftString: String) {
        timerTextFlow.tryEmit(timeLeftString)
    }

    private var stopped = fun() {
        timerTextFlow.tryEmit("")
    }
}