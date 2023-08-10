package non_veg.payment

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.OrderConfirmationIntermediateActivity
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import zustbase.orderDetail.OrderDetailActivity
import zustbase.orderDetail.ui.INTENT_SOURCE
import zustbase.orderDetail.ui.INTENT_SOURCE_NON_VEG
import zustbase.orderDetail.ui.ORDER_ID
import `in`.opening.area.zustapp.payment.PaymentActivity
import `in`.opening.area.zustapp.payment.PaymentMethodWarningDialog
import `in`.opening.area.zustapp.payment.models.Payment
import `in`.opening.area.zustapp.payment.models.PaymentActivityReqData
import `in`.opening.area.zustapp.payment.models.PaymentMethod
import `in`.opening.area.zustapp.rapidwallet.RapidWalletActivity
import `in`.opening.area.zustapp.rapidwallet.model.RapidWalletResult
import `in`.opening.area.zustapp.uiModels.PaymentMethodUi
import `in`.opening.area.zustapp.utility.AppUtility
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import non_veg.cart.models.NonVegCartDetailsForPayment
import non_veg.payment.ui.NonVegPaymentPageMainUi
import non_veg.payment.ui.PaymentPageBottomBar
import non_veg.payment.uiModels.NonVegCreateOrderUiState
import non_veg.payment.viewModels.NonVegPaymentViewModel

@AndroidEntryPoint
class NonVegPaymentActivity : AppCompatActivity() {
    private val viewModel: NonVegPaymentViewModel by viewModels()

    companion object {
        const val KEY_CART_INFO = "cart_info"
    }

    private val paymentMethodWarningDialog: PaymentMethodWarningDialog by lazy { PaymentMethodWarningDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromIntent()
        setContent {
            Scaffold(topBar = {
                ComposeCustomTopAppBar(modifier = Modifier, titleText = "Payment Options", callback = {
                    finish()
                })
            }, content = { paddingValues ->
                NonVegPaymentPageMainUi(paddingValues) {
                    updatePaymentMethod(it)
                }
            }, bottomBar = {
                PaymentPageBottomBar {
                    handleSelectedPaymentMethod()
                }
            })
            LaunchedEffect(key1 = Unit, block = {
                viewModel.getPaymentMethodsFromServer()
            })
        }
        attachObservers()
    }

    private fun getDataFromIntent() {
        if (intent.hasExtra(KEY_CART_INFO)) {
            val nonVegPaymentBasicInfo = intent.getParcelableExtra(KEY_CART_INFO) as? NonVegCartDetailsForPayment?
            if (nonVegPaymentBasicInfo == null) {
                finish()
                return
            }
            viewModel.updateNonVegCartDetailsForPayment(nonVegPaymentBasicInfo)
        } else {
            finish()
        }
    }

    private val launchIntentionalActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result == null) {
                return@registerForActivityResult
            }
            if (result.data == null) {
                return@registerForActivityResult
            }
            if (result.data?.hasExtra(RapidWalletActivity.RAPID_WALLET_SUCCESS) == true) {
                val rapidWalletResult: RapidWalletResult? =
                    result.data?.getParcelableExtra(RapidWalletActivity.RAPID_WALLET_SUCCESS)
                rapidWalletResult?.let {
                    when (it.status) {
                        1 -> {
                            moveToOrderConfIntermediatePage(rapidWalletResult.orderId)
                        }

                        -1 -> {
                            showRapidPaymentDeclinedDialog()
                        }

                        else -> {
                            showRapidPaymentDeclinedDialog()
                        }
                    }
                } ?: run {
                    AppUtility.showToast(this@NonVegPaymentActivity, "Something went wrong")
                }
            }
        }


    private fun startWalletPaymentPage() {
        val rapidPaymentIntent = Intent(this, RapidWalletActivity::class.java)
        val paymentSummary = PaymentActivityReqData()
        val totalPayableAmount = ((viewModel.nonVegCartDetailsForPayment?.itemPrice ?: 0.0) +
                (viewModel.nonVegCartDetailsForPayment?.serviceCharge ?: 0.0) +
                (viewModel.nonVegCartDetailsForPayment?.packagingFee ?: 0.0)
                + (viewModel.nonVegCartDetailsForPayment?.deliveryFee ?: 0.0))

        paymentSummary.apply {
            this.couponDiscount = 0.0
            this.deliveryFee = viewModel.nonVegCartDetailsForPayment?.deliveryFee
            this.packagingFee = viewModel.nonVegCartDetailsForPayment?.packagingFee
            this.itemPrice = viewModel.nonVegCartDetailsForPayment?.itemPrice
            this.orderId = viewModel.nonVegCartDetailsForPayment?.cartId
            this.totalAmount = totalPayableAmount
            this.expectedDelivery = viewModel.nonVegCartDetailsForPayment?.expectedDeliveryTime
        }
        rapidPaymentIntent.apply {
            putExtra(INTENT_SOURCE, INTENT_SOURCE_NON_VEG)
            putExtra(PaymentActivity.PAYMENT_MODEL_KEY, paymentSummary)
            putExtra(PaymentActivity.TOTAL_ITEMS_IN_CART, viewModel.nonVegCartDetailsForPayment?.noOfItemsInCart ?: 0)
        }
        launchIntentionalActivity.launch(rapidPaymentIntent)
    }

    private fun showRapidPaymentDeclinedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Alert!")
            .setMessage("Rapid wallet payment declined or cancelled. Please try again if amount is not deducted from your wallet")
            .setPositiveButton(
                "Ok"
            ) { dialog, _ ->
                dialog?.dismiss()
            }
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private fun moveToOrderConfIntermediatePage(orderId: Int?) {
        if (orderId == null) {
            AppUtility.showToast(this, "Something went wrong")
            return
        }
        viewModel.clearAllNonVegCartItems()

        val orderConfirmationPage: Intent? by lazy {
            Intent(this, OrderConfirmationIntermediateActivity::class.java)
        }
        orderConfirmationPage?.putExtra(ORDER_ID, orderId)
        orderConfirmationPage?.putExtra(INTENT_SOURCE, INTENT_SOURCE_NON_VEG)
        orderConfirmationPage?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(orderConfirmationPage)
    }

    private fun attachObservers() {
        lifecycleScope.launch {
            viewModel.nonVegCreateOrderUiState.collectLatest {
                parseCreateOrderResponse(it)
            }
        }
    }

    private fun parseCreateOrderResponse(data: NonVegCreateOrderUiState) {
        if (data is NonVegCreateOrderUiState.Success) {
            moveToOrderConfIntermediatePage(data.orderId)
        }
    }

    private fun showAlertDialogForCOD() {
        paymentMethodWarningDialog.showDialog(this, {
            viewModel.createUserNonVegOrder()
        }, {
            AppUtility.showToast(this, "Payment Declined or Cancelled")
        })
    }

    private fun updatePaymentMethod(paymentMethod: PaymentMethod) {
        viewModel.updatePaymentOptions(paymentMethod)
        handleSelectedPaymentMethod()
    }

    private fun handleSelectedPaymentMethod() {
        viewModel.selectedPaymentKey.let {
            if (it == "cod") {
                showAlertDialogForCOD()
            }
            if (it == "rapid") {
                startWalletPaymentPage()
            }
        }
    }

}


