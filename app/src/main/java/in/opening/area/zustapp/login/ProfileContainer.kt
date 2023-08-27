package `in`.opening.area.zustapp.login

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.ui.generic.CustomUpBtn
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.uiModels.login.UpdateUserProfileUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.LoginViewModel
import android.content.Context
import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModel

@Composable
fun ProfileContainer(loginViewModel: LoginViewModel, navigationAction: (String) -> Unit) {
    var userName by rememberSaveable { mutableStateOf("") }
    val emailId by rememberSaveable { mutableStateOf("") }
    var referralCode by rememberSaveable { mutableStateOf("") }

    val updateUiState by loginViewModel.userProfileUiState.collectAsState(UpdateUserProfileUi.InitialUi(false))
    val context = LocalContext.current

    var canShowProgressbar by rememberSaveable {
        mutableStateOf(false)
    }
    val response = updateUiState
    canShowProgressbar = response.isLoading

    when (response) {
        is UpdateUserProfileUi.ProfileSuccess -> {
            if (!response.data.id.isNullOrEmpty()) {
                FirebaseAnalytics.logEvents(FirebaseAnalytics.PROFILE_SUCCESSFULLY_NEW_CREATED)
                loginViewModel.saveKeyForProfileCreated(true)
                navigationAction.invoke(LoginNav.MOVE_TO_NEXT)
            } else {
                FirebaseAnalytics.logEvents(FirebaseAnalytics.NEW_PROFILE_CREATE_ERROR)
                loginViewModel.saveKeyForProfileCreated(false)
                AppUtility.showToast(context, "Something went wrong")
            }
        }
        is UpdateUserProfileUi.InitialUi -> {
            canShowProgressbar = response.isLoading
        }
        is UpdateUserProfileUi.ErrorUi -> {
            FirebaseAnalytics.logEvents(FirebaseAnalytics.NEW_PROFILE_CREATE_ERROR)
            loginViewModel.saveKeyForProfileCreated(false)
            referralCode = ""
            if (!response.errors.isNullOrEmpty()) {
                AppUtility.showToast(context, response.errors.getTextMsg())
            } else {
                AppUtility.showToast(context, response.errorMsg)
            }
        }
    }
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .verticalScroll(rememberScrollState())) {
        val (container) = createRefs()
        Column(modifier = Modifier
            .wrapContentHeight()
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .constrainAs(container) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
            Spacer(modifier = Modifier.height(16.dp))
            CustomUpBtn {
                navigationAction.invoke(LoginNav.FINISH)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "What should we call you?",
                style = ZustTypography.bodyMedium,
                color = colorResource(id = R.color.app_black))

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Let us know how to properly address you.",
                style = ZustTypography.bodyMedium,
                fontWeight = FontWeight.W400,
                color = colorResource(id = R.color.app_black))

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Name*",
                style = ZustTypography.bodySmall,
                fontWeight = FontWeight.W500,
                color = colorResource(id = R.color.app_black))
            Spacer(modifier = Modifier.height(dp_8))
            TextField(value = userName, onValueChange = {
                userName = it
            }, textStyle = ZustTypography.bodyMedium, modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
                colors = setTextFiledColors(), shape = RoundedCornerShape(12.dp),
                singleLine = true)

            Spacer(modifier = Modifier.height(dp_20))

            Text(text = "Referral code (if available)",
                style = ZustTypography.bodySmall,
                fontWeight = FontWeight.W500,
                color = colorResource(id = R.color.app_black))
            Spacer(modifier = Modifier.height(dp_8))
            TextField(value = referralCode, onValueChange = {
                referralCode = it
            }, textStyle = ZustTypography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                colors = setTextFiledColors(),
                shape = RoundedCornerShape(8.dp), singleLine = true)
            Spacer(modifier = Modifier.height(dp_24))


            Button(onClick = {
                updateUserProfile(userName, userEmailId = emailId, referralCode = referralCode, loginViewModel, context = context)
            }, modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.new_material_primary))) {
                Text(text = "Complete".uppercase(),
                    modifier = Modifier.padding(8.dp),
                    color = Color.White,
                    style = ZustTypography.bodyMedium)
            }
        }
        val (progressbarRef) = createRefs()
        if (canShowProgressbar) {
            CircularProgressIndicator(
                color = Color.Red,
                modifier = Modifier
                    .width(35.dp)
                    .height(35.dp)
                    .constrainAs(progressbarRef) {
                        top.linkTo(parent.top)
                        bottom.linkTo(container.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
        }
    }


}

private fun updateUserProfile(
    userName: String?, userEmailId: String? = "", referralCode: String? = null,
    loginViewModel: ViewModel, context: Context?,
) {
    if (context == null) {
        return
    }
    if (userName.isNullOrEmpty()) {
        AppUtility.run { showToast(context, context.getString(R.string.please_enter_name)) }
        return
    }
    when (loginViewModel) {
        is LoginViewModel -> {
            val referralCodeByUser = if (referralCode.isNullOrEmpty()) {
                null
            } else {
                referralCode
            }
            if (loginViewModel.userProfileUiState.value.isLoading) {
                AppUtility.showToast(context, "Please wait")
                return
            }
            if (!referralCode.isNullOrEmpty()) {
                val bundle = Bundle()
                bundle.putString("referral_code", referralCode)
                FirebaseAnalytics.logEvents(FirebaseAnalytics.PROFILE_CREATE_CLICK, bundle)
            } else {
                FirebaseAnalytics.logEvents(FirebaseAnalytics.PROFILE_CREATE_CLICK)
            }
            loginViewModel.updateUserProfile(userName = userName, userEmail = userEmailId, referralCodeByUser)
        }

    }
}