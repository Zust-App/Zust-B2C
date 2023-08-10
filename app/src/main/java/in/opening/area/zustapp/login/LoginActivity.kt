package `in`.opening.area.zustapp.login

import zustbase.HomeLandingActivity
import `in`.opening.area.zustapp.viewmodels.LoginViewModel
import `in`.opening.area.zustapp.databinding.ActivityLoginBinding
import `in`.opening.area.zustapp.services.SMSReceiver
import `in`.opening.area.zustapp.utility.AppUtility.Companion.showToast
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.phone.SmsRetriever
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.locationV2.LocationPermissionActivity

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), FragmentActionListener {
    private val loginViewModel: LoginViewModel by viewModels()
    private var onBoardingFragmentManager: OnBoardingFragmentManager? = null
    private var binding: ActivityLoginBinding? = null
    private var currentFragmentTag = LoginNav.MOVE_TO_PHONE

    private var smsReceiver: SMSReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setUpFragmentManager()
        if (loginViewModel.sharedPrefManager.getUserAuthToken().isNullOrEmpty()) {
            action(LoginNav.MOVE_TO_PHONE)
        } else {
            if (loginViewModel.isProfileCreated()) {
                if (loginViewModel.getSavedAddressFound()) {
                    moveToHomeActivity()
                } else {
                    proceedToLocationPermissionActivity()
                }
            } else {
                action(LoginNav.MOVE_TO_PROFILE)
            }
        }

        initSmsListener()
        initializeSmsBroadcast()
    }

    private fun setUpFragmentManager() {
        onBoardingFragmentManager = object : OnBoardingFragmentManager(supportFragmentManager, binding?.fragmentContainerView?.id) {}
    }

    override fun action(name: String) {
        currentFragmentTag = name
        hideKeyBoard()
        if (name == LoginNav.MOVE_TO_NEXT) {
            if (loginViewModel.getSavedAddressFound()) {
                moveToHomeActivity()
            } else {
                proceedToLocationPermissionActivity()
            }
        } else {
            onBoardingFragmentManager?.setUpFragmentBasedOnId(name)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (supportFragmentManager.fragments.isNotEmpty()) {
            when (currentFragmentTag) {
                LoginNav.MOVE_TO_PHONE -> {
                    finish()
                }

                LoginNav.MOVE_TO_OTP -> {
                    action(LoginNav.MOVE_TO_PHONE)
                }

                LoginNav.MOVE_TO_PROFILE -> {
                    finish()
                }
            }
        } else {
            finish()
        }
    }

    private fun moveToHomeActivity() {
        val intent = Intent(this@LoginActivity, HomeLandingActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun proceedToLocationPermissionActivity() {
        val locationPermissionActivity = Intent(this, LocationPermissionActivity::class.java)
        startActivity(locationPermissionActivity)
        finish()
    }

    private fun hideKeyBoard() {
        try {
            if (binding?.root != null) {
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding?.root?.windowToken, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initializeSmsBroadcast() {
        smsReceiver = SMSReceiver()
        smsReceiver?.setOTPListener(object : SMSReceiver.OTPReceiveListener {
            override fun onOTPReceived(otp: String?) {
                showToast(this@LoginActivity, otp)
                if (otp != null) {
                    loginViewModel.autoFetchOTP.value = otp
                } else {
                    loginViewModel.autoFetchOTP.value = ""
                }
            }

            override fun otpAutoError(error: String) {

            }
        })
    }


    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsReceiver)
    }

    private fun initSmsListener() {
        val client = SmsRetriever.getClient(this)
        client.startSmsRetriever()
    }

    override fun onDestroy() {
        super.onDestroy()
        smsReceiver = null
    }

}

