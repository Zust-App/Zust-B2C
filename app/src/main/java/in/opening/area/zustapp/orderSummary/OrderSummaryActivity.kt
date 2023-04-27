package `in`.opening.area.zustapp.orderSummary

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.address.AddNewAddressActivity
import `in`.opening.area.zustapp.address.AddressSearchActivity
import `in`.opening.area.zustapp.address.v2.AddressBottomSheetV2
import `in`.opening.area.zustapp.address.v2.AddressBtmSheetCallback
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.extensions.showBottomSheetIsNotPresent
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.orderSummary.compose.*
import `in`.opening.area.zustapp.orderSummary.model.LockOrderResponseData
import `in`.opening.area.zustapp.payment.PaymentActivity
import `in`.opening.area.zustapp.payment.PaymentActivity.Companion.PAYMENT_MODEL_KEY
import `in`.opening.area.zustapp.payment.PaymentActivity.Companion.TOTAL_ITEMS_IN_CART
import `in`.opening.area.zustapp.payment.models.PaymentActivityReqData
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.uiModels.orderSummary.LockOrderCartUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.OrderSummaryViewModel
import android.app.Activity
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
@AndroidEntryPoint
class OrderSummaryActivity : AppCompatActivity(), OrderItemsClickListeners, AddressBtmSheetCallback {
    private val orderSummaryViewModel: OrderSummaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                bottomBar = {
                    OrderSummaryBottomBarUi(orderSummaryViewModel) {
                        handleActionOfBottomAppBar(it)
                    }
                },
                topBar = {
                    ComposeCustomTopAppBar(Modifier, getString(R.string.my_cart), null, null) {
                        if (it == ACTION.NAV_BACK) {
                            finish()
                        }
                    }
                },
                backgroundColor = colorResource(id = R.color.screen_surface_color),
                content = { paddingValue ->
                    CartMainContainer(orderSummaryViewModel, this, paddingValue)
                },
            )
        }
        receiveDataFromIntent()
        attachObservers()
    }

    private fun receiveDataFromIntent() {
        if (intent != null) {
            if (intent.hasExtra(PAYMENT_MODEL_KEY)) {
                orderSummaryViewModel.paymentActivityReqData = if (SDK_INT >= 33) {
                    intent.getParcelableExtra(PAYMENT_MODEL_KEY, PaymentActivityReqData::class.java)
                } else {
                    intent.getParcelableExtra(PAYMENT_MODEL_KEY)
                }
                orderSummaryViewModel.expectedDeliveryTimeUiState.update { orderSummaryViewModel.paymentActivityReqData?.expectedDelivery }
            } else {
                finish()
            }
        }
    }


    private fun attachObservers() {
        lifecycleScope.launch {
            launch {
                orderSummaryViewModel.lockedCartUiState.collect {
                    parseLockedOrderResponse(it)
                }
            }
        }
        orderSummaryViewModel.getCartItemsFromDb()
        orderSummaryViewModel.getLatestAddress()
        orderSummaryViewModel.setFreeDeliveryBasePrice()
    }

    private fun parseLockedOrderResponse(response: LockOrderCartUi) {
        when (response) {
            is LockOrderCartUi.SuccessLocked -> {
                startPaymentActivity(response.data)
            }
            is LockOrderCartUi.InitialUi -> {

            }
            is LockOrderCartUi.ErrorUi -> {
                if (!response.errorMessage.isNullOrEmpty()) {
                    AppUtility.showToast(this, response.errorMessage)
                } else {
                    AppUtility.showToast(this, response.errors.getTextMsg())
                }
            }
        }
    }

    override fun didTapOnIncreaseProductItemAmount(v: ProductSingleItem) {
        orderSummaryViewModel.increaseItemCount(v)
    }

    override fun didTapOnDecreaseProductItemAmount(v: ProductSingleItem) {
        orderSummaryViewModel.decreaseItemCount(v)
    }

    override fun deleteProductItem(v: ProductSingleItem) {
        orderSummaryViewModel.deleteItem(v)
    }

    override fun clickOnApplyCoupon() {

    }

    private fun startPaymentActivity(data: LockOrderResponseData?) {
        if (data == null) {
            AppUtility.showToast(this, getString(R.string.common_error_message))
            return
        }

        val itemCount = data.items.sumOf {
            it.numberOfItem
        }
        orderSummaryViewModel.lockedCartUiState.update { LockOrderCartUi.InitialUi(false) }
        val reqData = PaymentActivityReqData()
        val bundle = Bundle()
        reqData.apply {
            orderId = data.orderId
            itemPrice = data.itemTotalPrice
            deliveryFee = data.deliveryFee
            packagingFee = data.packagingFee
            couponString = data.couponCode
            deliveryPartnerTip = data.deliveryPartnerTip
            expectedDelivery = data.expectedDelivery
            isFreeDelivery = data.isFreeDelivery
        }
        bundle.putInt("item_count", itemCount)
        bundle.putInt("order_id", data.orderId)
        bundle.putDouble("delivery_fee", data.deliveryFee)
        bundle.putDouble("item_total", data.itemTotalPrice)
        FirebaseAnalytics.logEvents(FirebaseAnalytics.OPEN_PAYMENT_PAGE,bundle)
        val paymentIntent = Intent(this, PaymentActivity::class.java)
        paymentIntent.putExtra(PAYMENT_MODEL_KEY, reqData)
        paymentIntent.putExtra(TOTAL_ITEMS_IN_CART, itemCount)
        startActivity(paymentIntent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


    override fun didTapOnAddAddress(savedAddress: AddressItem) {
        val address = savedAddress.convertToAddress()
        orderSummaryViewModel.updateAddressItem(address)
        orderSummaryViewModel.saveLatestAddress(address)
        updateUserCartWithServer()
    }

    override fun didTapOnAddNewAddress() {
        //openNewAddressActivity()
        openAddressSearchActivity()
    }

    private fun checkAddressThenUpdateCart() {
        orderSummaryViewModel.getLatestAddress()
        if (orderSummaryViewModel.addressItemCache == null) {
            openAddressSelectionBtmSheet()
        } else {
            updateUserCartWithServer()
        }
    }

    private fun updateUserCartWithServer() {
        if (orderSummaryViewModel.paymentActivityReqData?.orderId != null && orderSummaryViewModel.paymentActivityReqData?.orderId != -1) {
            if (!orderSummaryViewModel.isLockedCartOnGoing()) {
                orderSummaryViewModel.updateUserCartWithServer(orderSummaryViewModel.paymentActivityReqData?.orderId!!)
            } else {
                AppUtility.showToast(this, getString(R.string.please_wait))
            }
        } else {
            AppUtility.showToast(this, getString(R.string.common_error_message))
        }
    }

    override fun finishActivity() {
        finish()
    }

    private fun openNewAddressActivity() {
        val newAddressIntent = Intent(this, AddNewAddressActivity::class.java)
        startAddNewAddressActivity.launch(newAddressIntent)
    }

    private fun openAddressSearchActivity() {
        val newAddressIntent = Intent(this, AddressSearchActivity::class.java)
        startAddNewAddressActivity.launch(newAddressIntent)
    }

    private val startAddNewAddressActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedAddressId = result.data?.getIntExtra(AddNewAddressActivity.KEY_SELECTED_ADDRESS_ID, -1)
            if (selectedAddressId != null && selectedAddressId != -1) {
                checkAddressThenUpdateCart()
            }
        }
    }

    private fun handleActionOfBottomAppBar(orderSummaryAction: OrderSummaryAction) {
        if (orderSummaryAction == OrderSummaryAction.UpdateOrder) {
            checkAddressThenUpdateCart()
        } else if (orderSummaryAction == OrderSummaryAction.ChangeAddress) {
            openAddressSelectionBtmSheet()
        }
    }

    private fun openAddressSelectionBtmSheet() {
        val addressBtmSheet = AddressBottomSheetV2.newInstance()
        supportFragmentManager.showBottomSheetIsNotPresent(addressBtmSheet, "address_sheet")
    }


}


