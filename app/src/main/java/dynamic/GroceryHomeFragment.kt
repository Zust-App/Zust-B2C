package dynamic

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.address.AddNewAddressActivity
import `in`.opening.area.zustapp.address.AddressSearchActivity
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.address.v2.AddressBottomSheetV2
import `in`.opening.area.zustapp.address.v2.AddressBtmSheetCallback
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics
import `in`.opening.area.zustapp.compose.CustomTopBar
import `in`.opening.area.zustapp.extensions.showBottomSheetIsNotPresent
import `in`.opening.area.zustapp.helper.SelectLanguageFragment
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.home.HomeMainContainer
import `in`.opening.area.zustapp.orderSummary.OrderSummaryActivity
import `in`.opening.area.zustapp.payment.PaymentActivity
import `in`.opening.area.zustapp.payment.models.PaymentActivityReqData
import `in`.opening.area.zustapp.product.model.CreateCartData
import `in`.opening.area.zustapp.profile.SuggestProductBtmSheet
import `in`.opening.area.zustapp.ui.generic.CustomBottomBarView
import `in`.opening.area.zustapp.uiModels.CreateCartResponseUi
import `in`.opening.area.zustapp.uiModels.VALUE
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.openCallIntent
import `in`.opening.area.zustapp.utility.openWhatsAppOrderIntent
import `in`.opening.area.zustapp.utility.proceedToLoginActivity
import `in`.opening.area.zustapp.utility.startSearchActivity
import `in`.opening.area.zustapp.utility.startUserProfileActivity
import `in`.opening.area.zustapp.viewmodels.GroceryHomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroceryHomeFragment : Fragment(), AddressBtmSheetCallback {

    private val groceryHomeViewModel: GroceryHomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                Scaffold(
                    topBar = {
                        CustomTopBar(Modifier) {
                            handleActionIntent(it)
                        }
                    },
                    backgroundColor = colorResource(id = R.color.screen_surface_color),
                    content = { paddingValue ->
                        HomeMainContainer(paddingValues = paddingValue, callback = {
                            handleActionIntent(it)
                        }) {
                            handleActionIntent(ACTION.OPEN_LOCATION)
                        }
                    }, bottomBar = {
                        CustomBottomBarView(viewModel = groceryHomeViewModel, VALUE.A, {
                            if (!groceryHomeViewModel.isCreateCartOnGoing()) {
                                groceryHomeViewModel.createCartOrderWithServer(VALUE.A)
                            } else {
                                AppUtility.showToast(context, "Please wait")
                            }
                        }) {
                            moveToCartPage(it)
                        }
                    })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialDataManagement()
    }

    private fun handleActionIntent(action: ACTION) {
        when (action) {
            ACTION.OPEN_LOCATION -> {
                val bottomSheetV2 = AddressBottomSheetV2.newInstance()
                childFragmentManager.showBottomSheetIsNotPresent(bottomSheetV2, AddressBottomSheetV2.SHEET_TAG)
            }

            ACTION.OPEN_USER_BOOKING -> {
                context?.startUserProfileActivity()
                activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

            ACTION.SEARCH_PRODUCT -> {
                context?.startSearchActivity()
                activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

            ACTION.SUGGEST_PRODUCT -> {
                showSuggestProductSheet()
            }

            ACTION.OPEN_PROFILE -> {
                context?.startUserProfileActivity()
                activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

            ACTION.ORDER_WA -> {
                context?.openWhatsAppOrderIntent()
            }

            ACTION.LANGUAGE_DIALOG -> {
                showLanguageSelectionDialog()
            }

            ACTION.PHONE_CALL -> {
                context?.openCallIntent("74564062907")
            }

            else -> {}
        }
    }

    private fun showSuggestProductSheet() {
        childFragmentManager.showBottomSheetIsNotPresent(
            SuggestProductBtmSheet.newInstance(),
            SuggestProductBtmSheet.TAG)
    }

    private fun initialDataManagement() {
        groceryHomeViewModel.getUserSavedAddress()
        lifecycleScope.launchWhenCreated {
            launch {
                groceryHomeViewModel.moveToLoginPage.collectLatest {
                    if (it) {
                        groceryHomeViewModel.removeUserLocalData()
                        moveToLoginActivity()
                    }
                }
            }
        }
    }

    private fun moveToLoginActivity() {
        context?.proceedToLoginActivity()
        activity?.finish()
    }

    private fun showLanguageSelectionDialog() {
        SelectLanguageFragment.showDialog(childFragmentManager, true)
    }

    override fun onStart() {
        super.onStart()
    }

    companion object {
        fun newInstance() =
            GroceryHomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun didTapOnAddAddress(savedAddress: AddressItem) {
        val address = savedAddress.convertToAddress()
        groceryHomeViewModel.saveLatestAddress(address)
        groceryHomeViewModel.getUserSavedAddress()
    }

    override fun didTapOnAddNewAddress() {
        openAddressSearchActivity()
    }

    private fun openAddressSearchActivity() {
        context?.let {
            val newAddressIntent = Intent(it, AddressSearchActivity::class.java)
            startAddNewAddressActivity.launch(newAddressIntent)
        }
    }

    private val startAddNewAddressActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedAddressId = result.data?.getIntExtra(AddNewAddressActivity.KEY_SELECTED_ADDRESS_ID, -1)
            if (selectedAddressId != null && selectedAddressId != -1) {
                groceryHomeViewModel.getUserSavedAddress()
            }
        }
    }

    private fun moveToCartPage(data: Any?) {
        if (data != null && data is CreateCartData) {
            startOrderSummaryActivity(data)
        }
    }

    private fun startOrderSummaryActivity(createCartData: CreateCartData) {
        if (context == null) {
            return
        }
        groceryHomeViewModel.createCartUiState.update { CreateCartResponseUi.InitialUi(false) }
        FirebaseAnalytics.logEvents(FirebaseAnalytics.CLICK_ON_VIEW_CART_SUCCESS_MOVE)
        val intent = Intent(context, OrderSummaryActivity::class.java)
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
        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


}