package `in`.opening.area.zustapp.login

import TypewriterText
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics
import `in`.opening.area.zustapp.const.ENTER_MOBILE_NUM
import `in`.opening.area.zustapp.const.INVALID_MOBILE_NUM
import `in`.opening.area.zustapp.const.MOBILE_NUM_THRESHOLD
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.extensions.getNumberKeyboardOptions
import `in`.opening.area.zustapp.login.model.UserLoginModel
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.uiModels.login.GetOtpLoginUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.moveToInAppWebPage
import `in`.opening.area.zustapp.viewmodels.LoginViewModel
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.update


const val APP_TC_URL = "https://zustapp.com/term-and-condition"
const val APP_PRIVACY_URL = "https://zustapp.com/privacy-policy.html"

@Composable
fun LoginMainContainer(loginViewModel: LoginViewModel, navigationAction: (String) -> Unit) {
    val user by loginViewModel.userLoginModelFlow.collectAsState(initial = UserLoginModel())

    val getOtpUiState by loginViewModel.getOtpUiState.collectAsState(initial = GetOtpLoginUi.InitialUi(false))
    val context: Context = LocalContext.current

    var canShowProgressbar by rememberSaveable {
        mutableStateOf(false)
    }

    val response = getOtpUiState
    canShowProgressbar = response.isLoading
    when (response) {
        is GetOtpLoginUi.InitialUi -> {
            canShowProgressbar = response.isLoading
        }
        is GetOtpLoginUi.OtpGetSuccess -> {
            if (response.data.isNotificationSent == true) {
                FirebaseAnalytics.logEvents(FirebaseAnalytics.LOGIN_GET_OTP_SUCCESS)
                canShowProgressbar = false
                navigationAction.invoke(LoginNav.MOVE_TO_OTP)
                loginViewModel.getOtpUiState.update { GetOtpLoginUi.InitialUi(false) }
            } else {
                AppUtility.showToast(context, stringResource(R.string.common_error_message))
            }
        }
        is GetOtpLoginUi.ErrorUi -> {
            val bundle = Bundle()
            bundle.putString("phone_num", user.mobileNum)
            FirebaseAnalytics.logEvents(FirebaseAnalytics.LOGIN_GET_OTP_ERROR, bundle)
            if (!response.errorMsg.isNullOrEmpty()) {
                AppUtility.showToast(context, response.errorMsg)
            } else {
                AppUtility.showToast(context, response.errors?.getTextMsg())
            }
            loginViewModel.getOtpUiState.update { GetOtpLoginUi.InitialUi(false) }
        }
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .verticalScroll(rememberScrollState())
        .background(color = Color.White)
        .padding(horizontal = 20.dp)) {
        Spacer(modifier = Modifier.height(60.dp))
        Image(painter = painterResource(id = R.drawable.zust_app_black_text),
            contentDescription = stringResource(R.string.zust_logo), modifier = Modifier
                .width(140.dp)
                .height(60.dp))
        Spacer(modifier = Modifier.height(8.dp))
        TypewriterText(text = stringResource(R.string.last_minute_delivery_app),
            modifier = Modifier.fillMaxWidth(), textSize = 16.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = stringResource(R.string.last_minute_delivery_app),
            style = ZustTypography.subtitle1,
            color = colorResource(id = R.color.app_black))

        Spacer(modifier = Modifier.height(8.dp))

        TextField(value = user.mobileNum, onValueChange = {
            if (it.length <= MOBILE_NUM_THRESHOLD) {
                loginViewModel.setUserMobNum(it)
            }
        }, keyboardOptions = getNumberKeyboardOptions(),
            visualTransformation = PrefixTransformation(COUNTRY_CODE_INDIA),
            placeholder = { Text(ENTER_MOBILE_NUM) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 0.dp)
                .align(CenterHorizontally)
                .background(color = Color(0xffEFEFEF),
                    shape = RoundedCornerShape(12.dp))
                .padding(0.dp), singleLine = true, leadingIcon = {
                SetDrawableImage(R.drawable.india_flag_icon)
            },
            colors = setTextFiledColors(),
            textStyle = ZustTypography.body2, trailingIcon = {
                if (user.mobileNum.length == MOBILE_NUM_THRESHOLD) {
                    SetDrawableImage(R.drawable.ic_outline_check_circle_outline_24)
                }
            })

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                proceedToGetOtp(user.mobileNum, context, loginViewModel)
            },
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.new_material_primary)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.wrapContentHeight()) {
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(text = "NEXT", textAlign = TextAlign.Center,
                style = ZustTypography.body1,
                color = Color.White, modifier = Modifier
                    .padding(6.dp)
                    .padding(0.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        ClickableText(text = annotatedStringTAndC, style = ZustTypography.subtitle1,
            onClick = { offset ->
                annotatedStringTAndC.getStringAnnotations(tag = "policy",
                    start = offset, end = offset).firstOrNull()?.let {
                    context.moveToInAppWebPage(APP_PRIVACY_URL, "Privacy policy")
                    FirebaseAnalytics.logEvents(FirebaseAnalytics.PRIVACY_POLICY_CLICK)
                }
                annotatedStringTAndC.getStringAnnotations(tag = "term", start = offset, end = offset).firstOrNull()?.let {
                    context.moveToInAppWebPage(APP_TC_URL, "Terms & Conditions")
                    FirebaseAnalytics.logEvents(FirebaseAnalytics.TERM_VIEW)
                }
            })

        if (canShowProgressbar) {
            CircularProgressIndicator(color = colorResource(id = R.color.app_black),
                modifier = Modifier
                    .width(30.dp)
                    .align(CenterHorizontally)
                    .height(30.dp))
        }
    }
}


//function which is used for on clicking on RequestOtp Button
fun proceedToGetOtp(mobileNumber: String?, context: Context?, loginViewModel: LoginViewModel) {
    if (context == null) {
        return
    }
    if (mobileNumber.isNullOrEmpty()) {
        Toast.makeText(context, INVALID_MOBILE_NUM, Toast.LENGTH_SHORT).show()
    } else if (mobileNumber.length != 10) {
        Toast.makeText(context, INVALID_MOBILE_NUM, Toast.LENGTH_SHORT).show()
    } else {
        if (loginViewModel.isGetOtpRequestOnGoing()) {
            Toast.makeText(context, context.getString(R.string.please_wait), Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAnalytics.logEvents(FirebaseAnalytics.LOGIN_PROCEED)
        loginViewModel.userLoginModelFlow.update { UserLoginModel(otp = "", userName = "", userEmail = "", mobileNum = mobileNumber) }
        loginViewModel.makeLoginRequestForGetOtp(mobileNumber)
    }
}


