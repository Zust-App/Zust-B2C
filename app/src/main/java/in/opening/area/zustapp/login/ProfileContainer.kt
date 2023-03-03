package `in`.opening.area.zustapp.login

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.ui.generic.CustomUpBtn
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.uiModels.login.UpdateUserProfileUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.LoginViewModel
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
                loginViewModel.saveKeyForProfileCreated(true)
                navigationAction.invoke(LoginNav.MOVE_TO_NEXT)
            } else {
                loginViewModel.saveKeyForProfileCreated(false)
                AppUtility.showToast(context, "Something went wrong")
            }
        }
        is UpdateUserProfileUi.InitialUi -> {
            canShowProgressbar = response.isLoading
        }
        is UpdateUserProfileUi.ErrorUi -> {
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
        .fillMaxHeight()) {
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
                style = Typography_Montserrat.body1,
                color = colorResource(id = R.color.app_black))

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Let us know how to properly address you.",
                style = Typography_Montserrat.body2,
                fontWeight = FontWeight.W400,
                color = colorResource(id = R.color.app_black))

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Name*",
                style = Typography_Montserrat.subtitle1,
                fontWeight = FontWeight.W500,
                color = colorResource(id = R.color.app_black))
            Spacer(modifier = Modifier.height(dp_8))
            TextField(value = userName, onValueChange = {
                userName = it
            }, textStyle = Typography_Montserrat.body2, modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
                colors = setTextFiledColors(), shape = RoundedCornerShape(12.dp),
                singleLine = true)

            Spacer(modifier = Modifier.height(dp_32))

            Text(text = "Have a Referral code?",
                style = Typography_Montserrat.body1,
                color = colorResource(id = R.color.app_black))
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Kindly enter your referral code, if available.",
                style = Typography_Montserrat.body2,
                fontWeight = FontWeight.W400,
                color = colorResource(id = R.color.app_black))
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Referral code",
                style = Typography_Montserrat.subtitle1,
                fontWeight = FontWeight.W500,
                color = colorResource(id = R.color.app_black))
            Spacer(modifier = Modifier.height(dp_8))
            TextField(value = referralCode, onValueChange = {
                referralCode = it
            }, textStyle = Typography_Montserrat.body2,
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
                colors = ButtonDefaults.buttonColors(primaryColor)) {
                Text(text = "Complete".uppercase(),
                    modifier = Modifier.padding(8.dp),
                    color = Color.White,
                    style = Typography_Montserrat.body1)
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
            loginViewModel.updateUserProfile(userName = userName, userEmail = userEmailId, referralCodeByUser)
        }

    }
}