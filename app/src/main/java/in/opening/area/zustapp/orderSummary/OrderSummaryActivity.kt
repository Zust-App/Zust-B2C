package `in`.opening.area.zustapp.orderSummary

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.address.AddressAddSelectActivity
import `in`.opening.area.zustapp.address.AddressBottomSheetV2
import `in`.opening.area.zustapp.address.AddressBtmSheetCallback
import `in`.opening.area.zustapp.address.model.AddressItem
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

    private var paymentActivityReqData: PaymentActivityReqData? = null

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
                    ComposeCustomTopAppBar(Modifier, "My Cart", null, null) {
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
                paymentActivityReqData = if (SDK_INT >= 33) {
                    intent.getParcelableExtra(PAYMENT_MODEL_KEY, PaymentActivityReqData::class.java)
                } else {
                    intent.getParcelableExtra(PAYMENT_MODEL_KEY)
                }
                orderSummaryViewModel.expectedDeliveryTimeUiState.update { paymentActivityReqData?.expectedDelivery }
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
            AppUtility.showToast(this, "Something went wrong")
            return
        }
        val itemCount = data.items.sumOf {
            it.numberOfItem
        }
        orderSummaryViewModel.lockedCartUiState.update { LockOrderCartUi.InitialUi(false) }
        val reqData = PaymentActivityReqData()
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
        val paymentIntent = Intent(this, PaymentActivity::class.java)
        paymentIntent.putExtra(PAYMENT_MODEL_KEY, reqData)
        paymentIntent.putExtra(TOTAL_ITEMS_IN_CART,itemCount)
        startActivity(paymentIntent)
    }


    override fun didTapOnAddAddress(savedAddress: AddressItem) {
        val address = savedAddress.convertToAddress()
        orderSummaryViewModel.updateAddressItem(address)
        orderSummaryViewModel.saveLatestAddress(address)
        updateUserCartWithServer()
    }

    override fun didTapOnAddNewAddress() {
        openNewAddressActivity()
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
        if (paymentActivityReqData?.orderId != null && paymentActivityReqData?.orderId != -1) {
            if (!orderSummaryViewModel.isLockedCartOnGoing()) {
                orderSummaryViewModel.updateUserCartWithServer(paymentActivityReqData?.orderId!!)
            } else {
                AppUtility.showToast(this, "Please wait")
            }
        } else {
            AppUtility.showToast(this, "Something went wrong")
        }
    }

    override fun finishActivity() {
        finish()
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


