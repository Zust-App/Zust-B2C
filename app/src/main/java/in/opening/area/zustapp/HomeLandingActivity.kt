package `in`.opening.area.zustapp

import `in`.opening.area.zustapp.address.AddressAddSelectActivity
import `in`.opening.area.zustapp.address.AddressBottomSheetV2
import `in`.opening.area.zustapp.address.AddressBtmSheetCallback
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.compose.CustomTopBar
import `in`.opening.area.zustapp.extensions.showBottomSheetIsNotPresent
import `in`.opening.area.zustapp.fcm.CustomFcmService
import `in`.opening.area.zustapp.helper.InAppUpdateManager
import `in`.opening.area.zustapp.helper.SelectLanguageFragment
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.home.HomeMainContainer
import `in`.opening.area.zustapp.offline.ConnectionLiveData
import `in`.opening.area.zustapp.orderDetail.OrderDetailActivity
import `in`.opening.area.zustapp.orderSummary.OrderSummaryActivity
import `in`.opening.area.zustapp.payment.PaymentActivity
import `in`.opening.area.zustapp.payment.models.PaymentActivityReqData
import `in`.opening.area.zustapp.product.model.CreateCartData
import `in`.opening.area.zustapp.profile.ProfileActivity
import `in`.opening.area.zustapp.profile.SuggestProductBtmSheet
import `in`.opening.area.zustapp.search.SearchProductActivity
import `in`.opening.area.zustapp.ui.generic.CustomBottomBarView
import `in`.opening.area.zustapp.uiModels.CreateCartResponseUi
import `in`.opening.area.zustapp.uiModels.VALUE
import `in`.opening.area.zustapp.utility.AppDeepLinkHandler
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.ShowToast
import `in`.opening.area.zustapp.utility.proceedToLoginActivity
import `in`.opening.area.zustapp.viewmodels.HomeViewModel
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update


@AndroidEntryPoint
class HomeLandingActivity : AppCompatActivity(), ShowToast, AddressBtmSheetCallback {

    private val homeViewModel: HomeViewModel by viewModels()
    private var backPressedCount: Int = 0
    private var inAppUpdateManager: InAppUpdateManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                bottomBar = {
                    CustomBottomBarView(viewModel = homeViewModel, VALUE.A, {
                        if (!homeViewModel.isCreateCartOnGoing()) {
                            homeViewModel.createCartOrderWithServer(VALUE.A)
                        } else {
                            AppUtility.showToast(this, "Please wait")
                        }
                    }) {
                        startOrderSummaryActivity(it)
                    }
                },
                topBar = {
                    CustomTopBar(Modifier, homeViewModel) {
                        handleActionIntent(it)
                    }
                },
                backgroundColor = colorResource(id = R.color.screen_surface_color),
                content = { paddingValue ->
                    HomeMainContainer(homeViewModel, paddingValue) {
                        handleActionIntent(it)
                    }
                },
            )
            LaunchedEffect(key1 = Unit, block = {
                getLatestOrderWhichNotDeliver()
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
        inAppUpdateManager = InAppUpdateManager(this, inAppUpdateCallback);
    }

    private fun initialDataManagement() {
        homeViewModel.getUserSavedAddress()
        homeViewModel.getHomePageData(0.0, 0.0)
        lifecycleScope.launchWhenCreated {
            homeViewModel.moveToLoginPage.collectLatest {
                if (it) {
                    homeViewModel.removeUserLocalData()
                    moveToLoginActivity()
                }
            }
        }
    }

    private fun moveToLoginActivity() {
        this.proceedToLoginActivity()
        finish()
    }

    private fun getLatestOrderWhichNotDeliver() {
        homeViewModel.getLatestOrderNotDeliver()
    }


    private fun handleActionIntent(action: ACTION) {
        when (action) {
            ACTION.OPEN_LOCATION -> {
                val bottomSheetV2 = AddressBottomSheetV2.newInstance()
                supportFragmentManager.showBottomSheetIsNotPresent(bottomSheetV2, AddressBottomSheetV2.SHEET_TAG)
            }
            ACTION.OPEN_USER_BOOKING -> {
                startUserProfileActivity()
            }
            ACTION.SEARCH_PRODUCT -> {
                startSearchActivity()
            }
            ACTION.SUGGEST_PRODUCT -> {
                showSuggestProductSheet()
            }
            ACTION.OPEN_PROFILE -> {
                startUserProfileActivity()
            }
            ACTION.ORDER_WA -> {
                openWhatsAppOrderIntent()
            }
            ACTION.LANGUAGE_DIALOG -> {
                showLanguageSelectionDialog()
            }
            ACTION.PHONE_CALL -> {
                AppUtility.openCallIntent(this,"74564062907")
            }
            else -> {}
        }
    }

    private fun startUserProfileActivity() {
        val profileIntent = Intent(this, ProfileActivity::class.java)
        startActivity(profileIntent)
    }

    private fun startSearchActivity() {
        val searchIntent = Intent(this, SearchProductActivity::class.java)
        startActivity(searchIntent)
    }


    private fun proceedToOrderDetails(orderId: Int?) {
        if (orderId != null) {
            val orderDetailIntent = Intent(this, OrderDetailActivity::class.java)
            orderDetailIntent.putExtra(OrderDetailActivity.ORDER_ID, orderId)
            startActivity(orderDetailIntent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 109
        const val ACTION_PERMISSION_GPS = 110
    }


    private fun openWhatsAppOrderIntent() {
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.data = Uri.parse(AppUtility.getWhatsappHelpUrl())
        if (AppUtility.isAppInstalled(AppUtility.WA_PACKAGE_NAME)) {
            startActivity(sendIntent)
        } else if (AppUtility.isAppInstalled(AppUtility.BUSINESS_WA_PACKAGE_NAME)) {
            startActivity(sendIntent)
        }
    }

    private fun showSuggestProductSheet() {
        supportFragmentManager.showBottomSheetIsNotPresent(
            SuggestProductBtmSheet.newInstance(),
            SuggestProductBtmSheet.TAG)
    }

    private fun startOrderSummaryActivity(createCartData: CreateCartData) {
        homeViewModel.createCartUiState.update { CreateCartResponseUi.InitialUi(false) }
        val intent = Intent(this, OrderSummaryActivity::class.java)
        val paymentActivityReqData = PaymentActivityReqData()
        paymentActivityReqData.apply {
            orderId = createCartData.orderId
            itemPrice = createCartData.itemTotalPrice
            deliveryFee = createCartData.deliveryFee
            packagingFee = createCartData.packagingFee
            couponDiscount = createCartData.couponMaxDiscountPrice
            couponString = createCartData.couponCode
            isFreeDelivery = createCartData.isFreeDelivery
            expectedDelivery = createCartData.expectedDelivery
        }
        intent.putExtra(PaymentActivity.PAYMENT_MODEL_KEY, paymentActivityReqData)
        startActivity(intent)
    }

    private fun showLanguageSelectionDialog() {
        SelectLanguageFragment.showDialog(supportFragmentManager, true)
    }

    override fun didTapOnAddAddress(savedAddress: AddressItem) {
        val address = savedAddress.convertToAddress()
        homeViewModel.saveLatestAddress(address)
        homeViewModel.getUserSavedAddress()
    }

    override fun didTapOnAddNewAddress() {
        openNewAddressActivity()
    }

    private fun openNewAddressActivity() {
        val newAddressIntent = Intent(this, AddressAddSelectActivity::class.java)
        startAddNewAddressActivity.launch(newAddressIntent)
    }

    private val startAddNewAddressActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedAddressId = result.data?.getIntExtra(AddressAddSelectActivity.KEY_SELECTED_ADDRESS_ID, -1)
            if (selectedAddressId != null && selectedAddressId != -1) {
                homeViewModel.getUserSavedAddress()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        backPressedCount = 0
        inAppUpdateManager?.onResume()
    }

    override fun onBackPressed() {
        backPressedCount++
        if (backPressedCount >= 2) {
            super.onBackPressed()
        } else {
            AppUtility.showToast(this, "Press again to exit app")
        }
    }

    override fun onPause() {
        super.onPause()
        backPressedCount = 0
    }

    override fun onDestroy() {
        super.onDestroy()
        inAppUpdateManager?.onDestroy()
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
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        inAppUpdateManager?.onActivityResult(requestCode, resultCode, data)
    }

    private val inAppUpdateCallback = fun() {
        AppUtility.showAppUpdateDialog(context = this, false) {
            inAppUpdateManager?.completeUpdate()
        }
    }
}
