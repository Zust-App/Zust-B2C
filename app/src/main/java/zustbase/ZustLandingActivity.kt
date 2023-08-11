package zustbase

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.address.AddNewAddressActivity
import `in`.opening.area.zustapp.address.AddressSearchActivity
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.address.v2.AddressBtmSheetCallback
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.ORDER_HISTORY_CLICK_BTM_NAV
import `in`.opening.area.zustapp.compose.CustomBottomNavigation
import `in`.opening.area.zustapp.compose.HomeBottomNavTypes
import `in`.opening.area.zustapp.databinding.HomeLandingLayoutBinding
import `in`.opening.area.zustapp.fcm.CustomFcmService
import `in`.opening.area.zustapp.offline.ConnectionLiveData
import `in`.opening.area.zustapp.utility.AppDeepLinkHandler
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.proceedToLoginActivity
import `in`.opening.area.zustapp.utility.startFoodEntryActivity
import `in`.opening.area.zustapp.utility.startMyOrders
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import zustbase.basepage.ui.CustomZustTopBar
import zustbase.basepage.ui.ZustBasePageMainUi
import zustbase.utility.handleActionIntent
import zustbase.utility.handleBasicCallbacks


@AndroidEntryPoint
class ZustLandingActivity : AppCompatActivity(), AddressBtmSheetCallback {
    private var backPressedCount: Int = 0

    private val zustLandingViewModel: ZustLandingViewModel by viewModels()

    private var onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            backPressedCount++
            if (backPressedCount >= 2) {
                finish()
            } else {
                AppUtility.showToast(this@ZustLandingActivity, getString(R.string.press_again_to_exit_app))
            }
        }
    }

    private var binding: HomeLandingLayoutBinding? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            AppUtility.showToast(this, "Please allow notification Permission")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(content = { paddingValues ->
                ZustBasePageMainUi(paddingValues = paddingValues, genericCallback = {
                    handleActionIntent(it)
                }) {
                    handleBasicCallbacks(it)
                }
            }, topBar = {
                CustomZustTopBar(modifier = Modifier, callback = {
                    handleActionIntent(it)
                })
            }, bottomBar = {
                CustomBottomNavigation { it, _ ->
                    handleBottomNavCallback(it)
                }
            })
        }
        initialDataManagement()
        ConnectionLiveData(applicationContext).observe(
            this
        ) {
            if (!it) {
                AppUtility.showNoInternetActivity(this)
            }
        }
        handleDeepLinkIntent(intent = intent)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }


    private fun initialDataManagement() {
        zustLandingViewModel.getListOfServiceableItem()
        lifecycleScope.launchWhenCreated {
            launch {
                zustLandingViewModel.isAppUpdateAvail.collectLatest {
                    if (it) {
                        AppUtility.openPlayStore(context = this@ZustLandingActivity)
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLinkIntent(intent)
    }

    private fun handleDeepLinkIntent(intent: Intent?) {
        if (intent == null) {
            return
        }
        if (intent.hasExtra(CustomFcmService.DEEP_LINK_DATA)) {
            AppDeepLinkHandler.handleDeepLink(intent, this)
        }
        if (intent.data != null) {
            if (zustLandingViewModel.sharedPrefManager.checkIsProfileCreate()) {
                AppDeepLinkHandler.handleWebLink(uri = intent.data, this)
            } else {
                proceedToLoginActivity()
            }
        }
    }


    private fun handleBottomNavCallback(homeBottomNavTypes: HomeBottomNavTypes) {
        if (homeBottomNavTypes == HomeBottomNavTypes.Orders) {
            FirebaseAnalytics.logEvents(ORDER_HISTORY_CLICK_BTM_NAV, null)
            this.startMyOrders()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        if (homeBottomNavTypes == HomeBottomNavTypes.Food) {
            startFoodEntryActivity()
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {

                } else {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        checkNotificationPermission()
        zustLandingViewModel.getAppMetaData()
    }

    override fun onResume() {
        super.onResume()
        backPressedCount = 0
    }

    override fun onPause() {
        super.onPause()
        backPressedCount = 0
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


    override fun didTapOnAddAddress(savedAddress: AddressItem) {
        val address = savedAddress.convertToAddress()
        zustLandingViewModel.saveLatestAddress(address)
        zustLandingViewModel.getListOfServiceableItem()
    }

    override fun didTapOnAddNewAddress() {
        openAddressSearchActivity()
    }

    private val startAddNewAddressActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedAddressId = result.data?.getIntExtra(AddNewAddressActivity.KEY_SELECTED_ADDRESS_ID, -1)
            if (selectedAddressId != null && selectedAddressId != -1) {
                zustLandingViewModel.getListOfServiceableItem()
            }
        }
    }

    private fun openAddressSearchActivity() {
        val newAddressIntent = Intent(this, AddressSearchActivity::class.java)
        startAddNewAddressActivity.launch(newAddressIntent)
    }

}


