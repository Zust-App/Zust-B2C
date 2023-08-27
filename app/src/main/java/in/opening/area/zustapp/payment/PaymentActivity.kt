package `in`.opening.area.zustapp.payment

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.OrderConfirmationIntermediateActivity
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.coupon.CouponListingActivity
import `in`.opening.area.zustapp.coupon.model.ApplyCouponReqBody
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.payment.models.*
import `in`.opening.area.zustapp.payment.ui.GroceryPaymentPageBottomBar
import `in`.opening.area.zustapp.payment.ui.GroceryPaymentPageMainUi
import `in`.opening.area.zustapp.rapidwallet.RapidWalletActivity
import `in`.opening.area.zustapp.rapidwallet.model.RapidWalletResult
import `in`.opening.area.zustapp.uiModels.CreatePaymentUi
import `in`.opening.area.zustapp.uiModels.PaymentVerificationUi
import `in`.opening.area.zustapp.utility.*
import `in`.opening.area.zustapp.utility.AppUtility.Companion.showToast
import `in`.opening.area.zustapp.viewmodels.PaymentActivityViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONObject
import ui.colorBlack
import ui.colorWhite
import ui.linearGradientGroceryBrush
import zustbase.orderDetail.ui.ORDER_ID


@Suppress("DEPRECATION")
@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {

    private val paymentViewModel: PaymentActivityViewModel by viewModels()
    private val paymentMethodWarningDialog: PaymentMethodWarningDialog by lazy { PaymentMethodWarningDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiveDataFromIntent()
        setContent {
            Scaffold(topBar = {
                ComposeCustomTopAppBar(modifier = Modifier.background(color = colorWhite), titleText = "Review & Pay", color = colorBlack, callback = {
                    finish()
                })
            }, bottomBar = {
                GroceryPaymentPageBottomBar(paymentViewModel) {
                    proceedToPaymentFirstApiCall()
                }
            }) {
                GroceryPaymentPageMainUi(it, paymentViewModel, { paymentMethod ->
                    paymentViewModel.updatePaymentOptions(paymentMethod)
                    proceedToPaymentFirstApiCall()
                }) { firstClickCallback ->
                    proceedAfterCreatePayment(firstClickCallback)
                }
            }
            LaunchedEffect(key1 = Unit, block = {
                paymentViewModel.getPaymentMethodsFromServer()
                setUpObservers()
            })
        }
    }

    private fun receiveDataFromIntent() {
        if (intent != null) {
            if (intent.hasExtra(PAYMENT_MODEL_KEY)) {
                paymentViewModel.paymentActivityReqData = intent.getParcelableExtra(PAYMENT_MODEL_KEY)
            }
            if (intent.hasExtra(TOTAL_ITEMS_IN_CART)) {
                paymentViewModel.cartItemCount = intent.getIntExtra(TOTAL_ITEMS_IN_CART, 0)
            }
        }
    }

    private fun proceedToPaymentFirstApiCall() {
        if (paymentViewModel.paymentActivityReqData?.itemPrice != null && paymentViewModel.paymentActivityReqData?.itemPrice != 0.0) {
            if (paymentViewModel.isCreatePaymentOnGoing()) {
                showToast(this, "Please wait")
                return
            }
            when (paymentViewModel.paymentMethod?.key) {
                "cod" -> {
                    paymentMethodWarningDialog.showDialog(this, {
                        paymentViewModel.invokePaymentToGetId()
                    }, {
                        showToast(this, "Payment Declined or Cancelled")
                    })
                }
                "rapid" -> {
                    openRapidBazaarWallet()
                }
                else -> {
                    paymentViewModel.invokePaymentToGetId()
                }
            }
        } else {
            Toast.makeText(this, "Please select a payment mode", Toast.LENGTH_LONG).show()
        }
    }

    private fun setUpObservers() {
        lifecycleScope.launch {
            launch {
                paymentViewModel.paymentValidationUiState.collect {
                    parsePaymentValidation(it)
                }
            }
            launch {
                paymentViewModel.validateCouponUiState.collect {

                }
            }
        }
    }

    private suspend fun parsePaymentValidation(response: PaymentVerificationUi) {
        when (response) {
            is PaymentVerificationUi.PaymentSuccess -> {
                checkPaymentCapturedStatus(response.data)
            }

            is PaymentVerificationUi.ErrorUi -> {
                if (!response.errorMsg.isNullOrEmpty()) {
                    showToast(this, response.errorMsg)
                } else {
                    showToast(this, response.errors.getTextMsg())
                }
            }

            else -> {

            }
        }
    }

    private suspend fun checkPaymentCapturedStatus(data: JSONObject) {
        if (data.has("success") && data.getBoolean("success")) {
            if (data.has("payment")) {
                val payment = data.getJSONObject("payment")
                if (payment.has("status") && payment.getString("status").equals("captured", ignoreCase = true)) {
                    delay(200)
                    moveToOrderConfIntermediatePage()
                } else {
                    showToast(this, "status not captured")
                }
            }
        }
    }


    private fun startCouponListingActivity() {
        val couponListingIntent = Intent(this, CouponListingActivity::class.java)
        launchIntentionalActivity.launch(couponListingIntent)
    }

    private val launchIntentionalActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result == null) {
                return@registerForActivityResult
            }
            if (result.data == null) {
                return@registerForActivityResult
            }
            if (result.data?.hasExtra(CouponListingActivity.INTENT_KEY_COUPON_VALUE) == true) {
                val couponValue =
                    result.data?.getStringExtra(CouponListingActivity.INTENT_KEY_COUPON_VALUE)
                validateCouponFromIntent(couponValue, false)
            }
            if (result.data?.hasExtra(RapidWalletActivity.RAPID_WALLET_SUCCESS) == true) {
                val rapidWalletResult: RapidWalletResult? =
                    result.data?.getParcelableExtra(RapidWalletActivity.RAPID_WALLET_SUCCESS)
                rapidWalletResult?.let {
                    when (it.status) {
                        1 -> {
                            moveToOrderConfirmIntermediatePage(it.orderId)
                        }

                        -1 -> {
                            showRapidPaymentDeclinedDialog()
                        }

                        else -> {
                            showRapidPaymentDeclinedDialog()
                        }
                    }
                } ?: run {
                    showToast(this@PaymentActivity, "Something went wrong")
                }
            }
        }

    private fun validateCouponFromIntent(couponValue: String?, removeCoupon: Boolean) {
        if (couponValue == null) {
            return
        }
        paymentViewModel.validateCoupon(
            ApplyCouponReqBody(
                couponValue,
                123,
                removeCoupon
            )
        )
    }


    private fun proceedAfterCreatePayment(createPaymentResponseModel: CreatePaymentDataModel) {
        if (createPaymentResponseModel.success == true) {
            if (createPaymentResponseModel.order == null) {
                moveToOrderConfIntermediatePage()
            }
        }
    }


    private fun moveToOrderConfIntermediatePage() {
        if (paymentViewModel.paymentActivityReqData?.orderId != null) {
            paymentViewModel.clearCartItems()
            val orderConfirmationPage: Intent? by lazy {
                Intent(this, OrderConfirmationIntermediateActivity::class.java)
            }
            orderConfirmationPage?.putExtra(ORDER_ID, paymentViewModel.paymentActivityReqData?.orderId)
            orderConfirmationPage?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(orderConfirmationPage)
        }
    }


    private fun moveToOrderConfirmIntermediatePage(orderId: Int) {
        paymentViewModel.clearCartItems()
        val orderConfirmationPage: Intent? by lazy {
            Intent(
                this,
                OrderConfirmationIntermediateActivity::class.java
            )
        }
        orderConfirmationPage?.putExtra(ORDER_ID, orderId)
        orderConfirmationPage?.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(orderConfirmationPage)
    }


    private fun openRapidBazaarWallet() {
        if (paymentViewModel.paymentActivityReqData != null) {
            val intent = Intent(this, RapidWalletActivity::class.java)
            intent.putExtra(PAYMENT_MODEL_KEY, paymentViewModel.paymentActivityReqData)
            intent.putExtra(TOTAL_ITEMS_IN_CART, paymentViewModel.cartItemCount)
            launchIntentionalActivity.launch(intent)
        }
    }

    private fun showRapidPaymentDeclinedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Alert!")
            .setMessage("Rapid wallet payment declined or cancelled. Please try again if amount is not deducted from your wallet") // Specifying a listener allows you to take an action before dismissing the dialog.
            .setPositiveButton(
                "Ok"
            ) { dialog, _ ->
                dialog?.dismiss()
            }
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    companion object {
        const val PAYMENT_MODEL_KEY = "payment_model_key"
        const val TOTAL_ITEMS_IN_CART = "items_in_cart"
    }


}
