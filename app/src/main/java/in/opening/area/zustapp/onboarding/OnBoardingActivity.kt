package `in`.opening.area.zustapp.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import zustbase.HomeLandingActivity
import `in`.opening.area.zustapp.locationV2.LocationPermissionActivity
import `in`.opening.area.zustapp.login.LoginActivity
import `in`.opening.area.zustapp.onboarding.compose.LoginClick
import `in`.opening.area.zustapp.onboarding.compose.OnBoardingContainer
import `in`.opening.area.zustapp.viewmodels.OnBoardingViewModel

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {
    private val viewModel: OnBoardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            OnBoardingContainer {
                handleClickAction(it)
            }
        }
    }

    private fun handleClickAction(loginClick: LoginClick) {
        if (loginClick == LoginClick.LOGIN) {
            checkUserLoginOrNot()
        }
    }

    private fun checkUserLoginOrNot() {
        if (viewModel.isAuthTokenFound()) {
            if (viewModel.isProfileCreated()) {
                if (viewModel.getSavedAddressFound()) {
                    proceedToHomePage()
                } else {
                    proceedToLocationPermissionActivity()
                }
            } else {
                proceedToLogin()
            }
        } else {
            proceedToLogin()
        }
    }

    private fun proceedToHomePage() {
        val homeIntent = Intent(this, HomeLandingActivity::class.java)
        startActivity(homeIntent)
        finish()
    }

    private fun proceedToLocationPermissionActivity() {
        val locationPermissionActivity = Intent(this, LocationPermissionActivity::class.java)
        startActivity(locationPermissionActivity)
        finish()
    }

    private fun proceedToLogin() {
        val loginActivity = Intent(this, LoginActivity::class.java)
        startActivity(loginActivity)
        finish()
    }

}