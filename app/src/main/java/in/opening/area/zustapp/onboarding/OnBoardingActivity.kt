package `in`.opening.area.zustapp.onboarding

import `in`.opening.area.zustapp.login.LoginActivity
import `in`.opening.area.zustapp.onboarding.compose.LoginClick
import `in`.opening.area.zustapp.onboarding.compose.OnBoardingContainer
import `in`.opening.area.zustapp.viewmodels.OnBoardingViewModel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {
    private val viewModel: OnBoardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val doesOnBoardingShown = viewModel.doesOnBoardingShown()
            if (doesOnBoardingShown) {
                proceedToLogin()
            } else {
                OnBoardingContainer() {
                    handleClickAction(it)
                }
            }
        }
    }

    private fun handleClickAction(loginClick: LoginClick) {
        if (loginClick == LoginClick.LOGIN) {
            viewModel.updateOnBoardingShown(true)
            proceedToLogin()
        }
    }

    private fun proceedToLogin() {
        val loginActivity = Intent(this, LoginActivity::class.java)
        startActivity(loginActivity)
        finish()
    }
}