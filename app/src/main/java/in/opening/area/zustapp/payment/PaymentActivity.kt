package `in`.opening.area.zustapp.payment

import android.content.Context
import android.content.Intent
import android.hardware.BatteryState
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.phonepe.intent.sdk.api.B2BPGRequest
import com.phonepe.intent.sdk.api.PhonePe
import com.phonepe.intent.sdk.api.PhonePeInitException
import com.phonepe.intent.sdk.api.UPIApplicationInfo
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.OrderConfirmationIntermediateActivity
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.coupon.CouponListingActivity
import `in`.opening.area.zustapp.coupon.model.ApplyCouponReqBody
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.payment.models.*
import `in`.opening.area.zustapp.payment.ui.GroceryPaymentPageBottomBar
import `in`.opening.area.zustapp.payment.ui.GroceryPaymentPageMainUi
import `in`.opening.area.zustapp.payment.utils.PG_ApiEndPoint
import `in`.opening.area.zustapp.payment.utils.PG_saltIndex
import `in`.opening.area.zustapp.payment.utils.PG_saltKey
import `in`.opening.area.zustapp.payment.utils.appId
import `in`.opening.area.zustapp.payment.utils.createB2BPaymentReq
import `in`.opening.area.zustapp.payment.utils.createPaymentRequestEncoded
import `in`.opening.area.zustapp.payment.utils.pg_merchant_key
import `in`.opening.area.zustapp.payment.utils.sha256
import `in`.opening.area.zustapp.payment.utils.simulatorPackage
import `in`.opening.area.zustapp.rapidwallet.RapidWalletActivity
import `in`.opening.area.zustapp.rapidwallet.model.RapidWalletResult
import `in`.opening.area.zustapp.uiModels.PaymentVerificationUi
import `in`.opening.area.zustapp.utility.*
import `in`.opening.area.zustapp.utility.AppUtility.Companion.showToast
import `in`.opening.area.zustapp.viewmodels.PaymentActivityViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import ui.colorBlack
import ui.colorWhite
import zustbase.orderDetail.ui.ORDER_ID


@Suppress("DEPRECATION")
@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {
    private val paymentViewModel: PaymentActivityViewModel by viewModels()
    private val paymentMethodWarningDialog: PaymentMethodWarningDialog by lazy { PaymentMethodWarningDialog() }


    private val txnId = System.currentTimeMillis().toString()
    private var targetApp: PaymentMethod? = null

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
                    if (paymentMethod.enable) {
                        paymentViewModel.updatePaymentOptions(paymentMethod)
                        proceedToPaymentFirstApiCall()
                    } else {
                        showToast(this@PaymentActivity, "Please select other payment Option")
                    }
                }) { firstClickCallback ->
                    proceedAfterCreatePayment(firstClickCallback)
                }
            }
            LaunchedEffect(key1 = Unit, block = {
                setUpObservers()
            })
        }

        PhonePe.init(this, PhonePeEnvironment.RELEASE, pg_merchant_key, appId)

        try {
            PhonePe.setFlowId(paymentViewModel.getUserId())
            val upiApps: List<UPIApplicationInfo> = PhonePe.getUpiApps()
            paymentViewModel.getPaymentMethodsFromServer(upiApps)
        } catch (exception: PhonePeInitException) {
            showToast(this, exception.message)
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
                    setUpPaymentProcess()
                    // paymentViewModel.invokePaymentToGetId()
                }
            }
        } else {
            Toast.makeText(this, "Please select a payment mode", Toast.LENGTH_LONG).show()
        }
    }

    private fun setUpPaymentProcess() {
        val callbackUrl = "https://webhook.site/callback-url"
        targetApp = paymentViewModel.paymentMethod
        if (targetApp?.packageName == null) {
            return
        }
        val base64Body = createPaymentRequestEncoded(txnId, pg_merchant_key, paymentViewModel.getUserId(), callbackUrl, "UPI_INTENT", targetApp!!.packageName!!,
            paymentViewModel.paymentActivityReqData?.totalAmount ?: 0.0)
        val checksum = sha256(base64Body + PG_ApiEndPoint + PG_saltKey) + "###$PG_saltIndex";
        val b2BPGRequest = createB2BPaymentReq(base64Body, checksum = checksum)
        startPhonePeTransaction(this, b2BPGRequest, packageName = targetApp!!.packageName!!)
    }

    private fun startPhonePeTransaction(context: Context, b2BPGRequest: B2BPGRequest, packageName: String) {
        try {
            val intent = PhonePe.getImplicitIntent(context, b2BPGRequest, packageName)
            if (intent != null) {
                startActivityForResult(intent, PAYMENT_INTENT_REQ_CODE)
            }
        } catch (e: PhonePeInitException) {
            showToast(this, e.message)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PAYMENT_INTENT_REQ_CODE) {
            if (data != null && data.data != null) {

            }
            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
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
        private const val PAYMENT_INTENT_REQ_CODE = 109
    }

}
