package `in`.opening.area.zustapp.login

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.login.model.UserLoginModel
import `in`.opening.area.zustapp.ui.generic.CustomUpBtn
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.primaryColor
import `in`.opening.area.zustapp.uiModels.login.GetOtpLoginUi
import `in`.opening.area.zustapp.uiModels.login.VerifyOtpUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.LoginViewModel
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.flow.update


@Composable
fun OtpVerification(loginViewModel: LoginViewModel, navigationAction: (String) -> Unit) {
    val context: Context = LocalContext.current
    val user by loginViewModel.userLoginModelFlow.collectAsState(UserLoginModel())
    val verifyOtpUiState by loginViewModel.verifyOtpUiState.collectAsState(VerifyOtpUi.InitialUi(false))
    val resendOtpTimeLeft by loginViewModel.timerTextFlow.collectAsState(initial = "")
    val resendOtpUiState by loginViewModel.resendOtpUiState.collectAsState(initial = GetOtpLoginUi.InitialUi(false))

    val autoReadOTP by loginViewModel.autoFetchOTP.collectAsState()

    if (autoReadOTP.isNotEmpty()) {
        user.otp = autoReadOTP
    }

    var canShowProgressbar by rememberSaveable {
        mutableStateOf(false)
    }

    when (val resendOtpData = resendOtpUiState) {
        is GetOtpLoginUi.InitialUi -> {
            canShowProgressbar = resendOtpData.isLoading
        }
        is GetOtpLoginUi.OtpGetSuccess -> {
            if (resendOtpData.data.isNotificationSent == true) {
                FirebaseAnalytics.logEvents(FirebaseAnalytics.RESEND_OTP_SUCCESS)
                AppUtility.showToast(context, "Otp send to your mobile number")
                loginViewModel.startResendOtpTimer()
            } else {
                FirebaseAnalytics.logEvents(FirebaseAnalytics.RESEND_OTP_ERROR)
                loginViewModel.resendOtpUiState.update { GetOtpLoginUi.InitialUi(false) }
                AppUtility.showToast(context, "Something went wrong")
            }
        }
        is GetOtpLoginUi.ErrorUi -> {
            FirebaseAnalytics.logEvents(FirebaseAnalytics.RESEND_OTP_ERROR)
            if (!resendOtpData.errors.isNullOrEmpty()) {
                AppUtility.showToast(context, resendOtpData.errors.getTextMsg())
            } else {
                AppUtility.showToast(context, resendOtpData.errorMsg)
            }
            loginViewModel.verifyOtpUiState.update { VerifyOtpUi.InitialUi(false) }
        }
    }

    val canShowErrorMsg by remember {
        mutableStateOf(false)
    }

    val response = verifyOtpUiState
    canShowProgressbar = response.isLoading

    when (response) {
        is VerifyOtpUi.VerificationSuccess -> {
            if (response.data.token != null) {
                loginViewModel.sharedPrefManager.saveAuthToken(response.data.token)
                loginViewModel.sharedPrefManager.saveUserPhoneNumber(user.mobileNum)
                loginViewModel.sendFcmToken()
                FirebaseAnalytics.logEvents(FirebaseAnalytics.OTP_VERIFIED)
                if (response.data.isProfileCreated) {
                    FirebaseAnalytics.logEvents(FirebaseAnalytics.PROFILE_CREATED)
                    loginViewModel.saveKeyForProfileCreated(true)
                    loginViewModel.verifyOtpUiState.update { VerifyOtpUi.InitialUi(false) }
                    navigationAction.invoke(LoginNav.MOVE_TO_NEXT)
                } else {
                    loginViewModel.verifyOtpUiState.update { VerifyOtpUi.InitialUi(false) }
                    loginViewModel.saveKeyForProfileCreated(false)
                    navigationAction.invoke(LoginNav.MOVE_TO_PROFILE)
                }
            } else {
                FirebaseAnalytics.logEvents(FirebaseAnalytics.OTP_VERIFICATION_ERROR)
                AppUtility.showToast(context, "Something went wrong")
            }
        }
        is VerifyOtpUi.ErrorUi -> {
            FirebaseAnalytics.logEvents(FirebaseAnalytics.OTP_VERIFICATION_ERROR)
            if (!response.errorMsg.isNullOrEmpty()) {
                AppUtility.showToast(context, response.errorMsg)
            } else {
                AppUtility.showToast(context, response.errors?.getTextMsg())
            }
            loginViewModel.verifyOtpUiState.update { VerifyOtpUi.InitialUi(false) }
        }
        is VerifyOtpUi.InitialUi -> {
            canShowProgressbar = response.isLoading
        }
    }

    Column(modifier = Modifier
        .fillMaxHeight()
        .verticalScroll(rememberScrollState())
        .background(color = colorResource(id = R.color.white))
        .padding(horizontal = 20.dp)
        .fillMaxWidth()) {

        Spacer(modifier = Modifier.height(24.dp))
        CustomUpBtn {
            navigationAction.invoke(LoginNav.MOVE_TO_PHONE)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = buildString {
            append("Enter OTP")
        }, color = colorResource(id = R.color.app_black), style = Typography_Montserrat.body1, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = buildString {
            append("Please enter the 4-digit OTP sent to you at ${obscureMobileNumber(user.mobileNum)}")
            //append(user.mobileNum)
        }, style = Typography_Montserrat.subtitle1, fontSize = 14.sp, color = Color(0xff1E1E1E), modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(24.dp))

        PinInput(modifier = Modifier.background(Color(0xffEFEFEF),
            shape = RoundedCornerShape(4.dp)), value = user.otp,
            length = 4, disableKeypad = false, obscureText = null) {
            loginViewModel.setUserOtp(it)
        }

        Spacer(modifier = Modifier.height(8.dp))
        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            val (wrongOtp, infoIcon, resendOtp, resendTimer) = createRefs()
            if (canShowErrorMsg) {
                Icon(painter = painterResource(id = R.drawable.ic_outline_info_24), contentDescription = "info", modifier = Modifier
                    .size(14.dp)
                    .constrainAs(infoIcon) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }, tint = colorResource(id = R.color.red_primary))

                Text(text = "Incorrect OTP", modifier = Modifier.constrainAs(wrongOtp) {
                    top.linkTo(infoIcon.top)
                    start.linkTo(infoIcon.end, dp_4)
                    bottom.linkTo(infoIcon.bottom)
                }, fontWeight = FontWeight.W500, color = colorResource(id = R.color.red_primary), style = Typography_Montserrat.subtitle1)
            }
            if (!resendOtpTimeLeft.isNullOrEmpty()) {
                Text(text = "Resend OTP in $resendOtpTimeLeft", modifier = Modifier.constrainAs(resendOtp) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }, fontWeight = FontWeight.W500,
                    style = Typography_Montserrat.subtitle1, color = colorResource(id = R.color.new_material_primary), fontSize = 12.sp)
            } else {
                Text(text = "Resend OTP", modifier = Modifier
                    .constrainAs(resendTimer) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        if (!loginViewModel.resendOtpUiState.value.isLoading) {
                            loginViewModel.makeResendOtp(user.mobileNum)
                            FirebaseAnalytics.logEvents(FirebaseAnalytics.RESEND_OTP_CLICK)
                        } else {
                            AppUtility.showToast(context, "Please wait")
                        }
                    }, fontWeight = FontWeight.W500,
                    color = colorResource(id = R.color.new_material_primary),
                    fontSize = 12.sp, style = Typography_Montserrat.subtitle1)
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(onClick = {
            verificationOfUserOtp(user.otp, loginViewModel, context = context)
        }, colors = ButtonDefaults.buttonColors(primaryColor), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.wrapContentHeight()) {
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(text = "Verify OTP", textAlign = TextAlign.Center, style = Typography_Montserrat.body1, color = Color.White, modifier = Modifier
                .padding(6.dp)
                .padding(0.dp))
        }
        if (canShowProgressbar) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(color = colorResource(id = R.color.app_black), modifier = Modifier
                .width(30.dp)
                .align(Alignment.CenterHorizontally)
                .height(30.dp))
        }
    }

}

private fun obscureMobileNumber(mobileNumber: String): String {
    val totalLength = mobileNumber.length
    val obscureLength = (totalLength / 2) + 1
    return try {
        buildString {
            append(mobileNumber.subSequence(0, obscureLength).toString().replace("[a-zA-Z0-9]".toRegex(), "*"))
            append(mobileNumber.subSequence(obscureLength, mobileNumber.length))
        }
    } catch (e: Exception) {
        mobileNumber
    }
}


fun verificationOfUserOtp(enteredOtpText: String?, loginViewModel: LoginViewModel?, context: Context) {
    if (enteredOtpText.isNullOrEmpty()) {
        AppUtility.run { showToast(context, context.getString(R.string.please_enter_otp)) }
        return
    }
    if (enteredOtpText.length == 4) {
        loginViewModel?.userLoginModelFlow?.value?.otp = enteredOtpText
        val mobileNumber = loginViewModel?.userLoginModelFlow?.value?.mobileNum
        if (mobileNumber != null) {
            with(loginViewModel) {
                if (verifyOtpUiState.value.isLoading) {
                    AppUtility.run { showToast(context, "Please wait") }
                    return
                }
                FirebaseAnalytics.logEvents(FirebaseAnalytics.OTP_VERIFICATION_CLICK)
                loginVerifyUserOTP(mobileNumber, enteredOtpText)
            }
        }
    }
}

