package zustbase

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dynamic.GroceryHomeFragment
import dynamic.NonVegHomeFragment
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.ORDER_HISTORY_CLICK_BTM_NAV
import `in`.opening.area.zustapp.compose.CustomBottomNavigation
import `in`.opening.area.zustapp.compose.HomeBottomNavTypes
import `in`.opening.area.zustapp.databinding.HomeLandingLayoutBinding
import `in`.opening.area.zustapp.fcm.CustomFcmService
import `in`.opening.area.zustapp.offline.ConnectionLiveData
import `in`.opening.area.zustapp.rapidwallet.model.ZustServiceType
import zustbase.orderDetail.ui.FragmentTypes
import `in`.opening.area.zustapp.utility.AppDeepLinkHandler
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.proceedToLoginActivity
import `in`.opening.area.zustapp.utility.startFoodEntryActivity
import `in`.opening.area.zustapp.utility.startMyOrders
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import zustbase.services.models.ZustService
import zustbase.services.uiModel.ZustAvailServicesUiModel


@AndroidEntryPoint
class HomeLandingActivity : AppCompatActivity() {
    private var backPressedCount: Int = 0

    private val homeViewModel: HomeViewModel by viewModels()

    private var onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            backPressedCount++
            if (backPressedCount >= 2) {
                finish()
            } else {
                AppUtility.showToast(this@HomeLandingActivity, getString(R.string.press_again_to_exit_app))
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
        binding = HomeLandingLayoutBinding.inflate(layoutInflater)
        setContentView(binding?.root)
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
        homeViewModel.getListOfServiceableItem()
        lifecycleScope.launchWhenCreated {
            launch {
                homeViewModel.isAppUpdateAvail.collectLatest {
                    if (it) {
                        AppUtility.openPlayStore(context = this@HomeLandingActivity)
                    }
                }
            }
            launch {
                homeViewModel.zustServicesUiModel.collectLatest {
                    handleZustServices(it)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLinkIntent(intent)
    }

    private fun handleZustServices(zustAvailServicesUiModel: ZustAvailServicesUiModel) {
        if (zustAvailServicesUiModel.isLoading) {
            binding?.homeProgressBar?.visibility = View.VISIBLE
        } else {
            binding?.homeProgressBar?.visibility = View.INVISIBLE
        }
        if (zustAvailServicesUiModel is ZustAvailServicesUiModel.Success) {
            val availServiceList = zustAvailServicesUiModel.data?.serviceList
            if (!availServiceList.isNullOrEmpty()) {
                inflateBottomNavigationBar(availServiceList)
                val nonVegServices = availServiceList.filter { it.type == ZustServiceType.NON_VEG.name && it.enable }
                if (nonVegServices.isNotEmpty()) {
                    loadDynamicFragments(FragmentTypes.NON_VEG.name)
                } else {
                    loadDynamicFragments(FragmentTypes.GROCERY.name)
                }
            }
        }
    }

    private fun handleDeepLinkIntent(intent: Intent?) {
        if (intent == null) {
            return
        }
        if (intent.hasExtra(CustomFcmService.DEEP_LINK_DATA)) {
            AppDeepLinkHandler.handleDeepLink(intent, this)
        }
        if (intent.data != null) {
            if (homeViewModel.sharedPrefManager.checkIsProfileCreate()) {
                AppDeepLinkHandler.handleWebLink(uri = intent.data, this)
            } else {
                proceedToLoginActivity()
            }
        }
    }


    private fun inflateBottomNavigationBar(availServiceList: List<ZustService>) {
        binding?.homeComposeView?.setContent {
            CustomBottomNavigation(availServiceList) { it, _ ->
                handleBottomNavCallback(it)
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

    private fun loadDynamicFragments(name: String) {
        val existingFragment = supportFragmentManager.findFragmentByTag(name)

        if (existingFragment != null) {
            return
        }

        val fragment = when (name) {
            FragmentTypes.GROCERY.name -> GroceryHomeFragment.newInstance()
            FragmentTypes.NON_VEG.name -> NonVegHomeFragment.newInstance()
            else -> null
        }

        if (fragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment, name).commit()
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
        homeViewModel.getAppMetaData()
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


}

