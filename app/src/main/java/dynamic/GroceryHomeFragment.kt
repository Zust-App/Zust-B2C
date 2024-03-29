package dynamic

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.address.AddNewAddressActivity
import `in`.opening.area.zustapp.address.AddressSearchActivity
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.address.v2.AddressBtmSheetCallback
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics
import `in`.opening.area.zustapp.compose.CustomGroceryTopBar
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.home.HomeMainContainer
import `in`.opening.area.zustapp.orderSummary.OrderSummaryActivity
import `in`.opening.area.zustapp.payment.PaymentActivity
import `in`.opening.area.zustapp.payment.models.PaymentActivityReqData
import `in`.opening.area.zustapp.product.model.CreateCartData
import `in`.opening.area.zustapp.ui.generic.CustomBottomBarView
import `in`.opening.area.zustapp.uiModels.CreateCartResponseUi
import `in`.opening.area.zustapp.uiModels.VALUE
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.proceedToLoginActivity
import `in`.opening.area.zustapp.viewmodels.GroceryHomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ui.colorWhite
import zustbase.utility.handleActionIntent

@AndroidEntryPoint
class GroceryHomeFragment : Fragment(), AddressBtmSheetCallback {

    private val groceryHomeViewModel: GroceryHomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                Scaffold(
                    topBar = {
                        CustomGroceryTopBar(Modifier) {
                            (activity as? AppCompatActivity?)?.handleActionIntent(it, fragmentManager = childFragmentManager)
                        }
                    },
                    containerColor = colorWhite,
                    content = { paddingValue ->
                        HomeMainContainer(paddingValues = paddingValue, callback = {
                            (activity as? AppCompatActivity?)?.handleActionIntent(it, fragmentManager = childFragmentManager)
                        }) {
                            (activity as? AppCompatActivity?)?.handleActionIntent(ACTION.OPEN_LOCATION, fragmentManager = childFragmentManager)
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