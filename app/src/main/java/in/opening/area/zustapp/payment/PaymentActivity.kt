package `in`.opening.area.zustapp.payment

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.MyApplication
import `in`.opening.area.zustapp.coupon.CouponListingActivity
import `in`.opening.area.zustapp.coupon.model.AppliedCouponData
import `in`.opening.area.zustapp.coupon.model.ApplyCouponReqBody
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.orderDetail.OrderDetailActivity
import `in`.opening.area.zustapp.orderDetail.OrderDetailActivity.Companion.JUST_ORDERED
import `in`.opening.area.zustapp.orderDetail.OrderDetailActivity.Companion.ORDER_ID
import `in`.opening.area.zustapp.orderDetail.models.Address
import `in`.opening.area.zustapp.orderDetail.models.convertAsStringText
import `in`.opening.area.zustapp.payment.adapter.PaymentMethodAdapter
import `in`.opening.area.zustapp.payment.adapter.PaymentMethodClickListeners
import `in`.opening.area.zustapp.payment.holder.*
import `in`.opening.area.zustapp.payment.models.*
import `in`.opening.area.zustapp.uiModels.CreatePaymentUi
import `in`.opening.area.zustapp.uiModels.PaymentMethodUi
import `in`.opening.area.zustapp.uiModels.PaymentVerificationUi
import `in`.opening.area.zustapp.uiModels.ValidateCouponUi
import `in`.opening.area.zustapp.utility.CustomDividerItemDecoration
import `in`.opening.area.zustapp.utility.ProductUtils
import `in`.opening.area.zustapp.utility.ShowToast
import `in`.opening.area.zustapp.viewmodels.PaymentActivityViewModel
import `in`.opening.area.zustapp.databinding.ActivityPaymentBinding
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONObject


@Suppress("DEPRECATION")
@AndroidEntryPoint
class PaymentActivity : AppCompatActivity(), PaymentResultWithDataListener, PaymentMethodClickListeners, ShowToast {
    private var binding: ActivityPaymentBinding? = null

    private val paymentViewModel: PaymentActivityViewModel by viewModels()

    private val paymentMethodAdapter by lazy { PaymentMethodAdapter(this) }

    private var paymentBillingHolder: PaymentBillingHolder? = null

    private var checkout: Checkout? = null
    private var paymentActivityReqData: PaymentActivityReqData? = null

    private var paymentMethod: PaymentMethod? = null
    private var couponHolder: CouponHolder? = null
    private var deliveryAddressHolder: DeliveryAddressHolder? = null
    private var timingSavingHolder: TimingSavingHolder? = null
    private var cartItemCount:Int=0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        receiveDataFromIntent()
        setUpViews()
        setUpObservers()
        setUpClickListeners()
        setUpTitleBar()
        Checkout.preload(MyApplication.getApplication())
    }

    @SuppressLint("SetTextI18n")
    private fun setUpPaymentHolder() {
        paymentBillingHolder = PaymentBillingHolder(binding?.paymentBillingContainer, layoutInflater)
        paymentBillingHolder?.setUpData(paymentActivityReqData)
        binding?.paymentPageBottomBar?.totalPayableAmountTv?.text = getString(R.string.ruppes) +
                ProductUtils.roundTo1DecimalPlaces(paymentActivityReqData?.totalAmount)
    }

    private fun receiveDataFromIntent() {
        if (intent != null) {
            if (intent.hasExtra(PAYMENT_MODEL_KEY)) {
                paymentActivityReqData = intent.getParcelableExtra(PAYMENT_MODEL_KEY)
            }
            if (intent.hasExtra(TOTAL_ITEMS_IN_CART)){
                cartItemCount=intent.getIntExtra(TOTAL_ITEMS_IN_CART,0)
            }
        }
    }

    private fun setUpClickListeners() {
        binding?.paymentPageBottomBar?.orderPlaceContainer?.setOnClickListener {
            proceedToPaymentFirstApiCall()
        }
    }

    private fun setUpViews() {
        if (couponHolder == null) {
            couponHolder = CouponHolder(binding) {
                if (it == REMOVE_COUPON) {
                    if (paymentViewModel.appliedCoupon.isNotEmpty()) {
                        validateCouponFromIntent(paymentViewModel.appliedCoupon, true)
                    }
                }
                if (it == APPLY_COUPON) {
                    startCouponListingActivity()
                }
            }
        }
        couponHolder?.initiateViews()

        binding?.paymentMethodContainer?.paymentModeRecyclerView?.apply {
            layoutManager = LinearLayoutManager(this@PaymentActivity)
            adapter = paymentMethodAdapter
            addItemDecoration(CustomDividerItemDecoration(this@PaymentActivity, R.drawable.divider_1dp))
        }
        setUpPaymentHolder()
        paymentViewModel.appliedCoupon = paymentActivityReqData?.couponString ?: ""
        if (paymentViewModel.appliedCoupon.isNotEmpty()) {
            validateCouponFromIntent(paymentViewModel.appliedCoupon, true)
        }

        timingSavingHolder = TimingSavingHolder(binding = binding?.savingsAndTimingContainer)
        deliveryAddressHolder = DeliveryAddressHolder(binding = binding?.deliveryAddressContainer)
        setUpAddressDetails(paymentViewModel.getLatestAddress())
        timingSavingHolder?.setData(paymentActivityReqData)
    }


    private fun proceedToPaymentFirstApiCall() {
        if (paymentMethod?.key != null && paymentActivityReqData?.totalAmount != null) {
            showHidePgBar(true)
            val createPayment = CreatePaymentReqBodyModel(
                paymentActivityReqData?.totalAmount,
                order_id = paymentActivityReqData?.orderId,
                paymentMethod = paymentMethod?.key!!)
            paymentViewModel.invokePaymentToGetId(createPayment)
        } else {
            Toast.makeText(this, "Please select a payment mode", Toast.LENGTH_LONG).show()
        }
    }

    private fun setUpObservers() {
        lifecycleScope.launch {
            launch {
                paymentViewModel.paymentMethodUiState.collectLatest {
                    parsePaymentMethodResponse(it)
                }
            }
            launch {
                paymentViewModel.createPaymentUiState.collectLatest {
                    parseCreatePaymentResponse(it)
                }
            }
            launch {
                paymentViewModel.paymentValidationUiState.collect {
                    parsePaymentValidation(it)
                }
            }
            launch {
                paymentViewModel.validateCouponUiState.collect {
                    parseCouponValidation(it)
                }
            }
        }
        paymentViewModel.getPaymentMethodsFromServer()
    }

    private fun parsePaymentMethodResponse(response: PaymentMethodUi) {
        showHidePgBar(response.isLoading)
        setUpShimmer(response.isLoading)
        when (response) {
            is PaymentMethodUi.InitialUi -> {
                showHidePgBar(response.isLoading)
            }
            is PaymentMethodUi.ErrorUi -> {
                if (!response.errorMsg.isNullOrEmpty()) {
                    showToast(this, response.errorMsg)
                } else {
                    showToast(this, response.errors.getTextMsg())
                }
            }
            is PaymentMethodUi.MethodSuccess -> {
                paymentMethodAdapter.submitList(response.data)
            }
        }
    }

    private suspend fun parsePaymentValidation(response: PaymentVerificationUi) {
        showHidePgBar(response.isLoading)
        when (response) {
            is PaymentVerificationUi.PaymentSuccess -> {
                checkPaymentCapturedStatus(response.data)
            }
            is PaymentVerificationUi.InitialUi -> {
                showHidePgBar(response.isLoading)
            }
            is PaymentVerificationUi.ErrorUi -> {
                if (!response.errorMsg.isNullOrEmpty()) {
                    showToast(this, response.errorMsg)
                } else {
                    showToast(this, response.errors.getTextMsg())
                }
            }
        }
    }

    private suspend fun checkPaymentCapturedStatus(data: JSONObject) {
        if (data.has("success") && data.getBoolean("success")) {
            if (data.has("payment")) {
                val payment = data.getJSONObject("payment")
                if (payment.has("status") && payment.getString("status").equals("captured", ignoreCase = true)) {
                    showToast(this, "Order confirm")
                    delay(1000)
                    moveToOrderDetailsPage()
                } else {
                    showToast(this, "status not captured")
                }
            }
        }
    }

    private fun parseCreatePaymentResponse(response: CreatePaymentUi) {
        showHidePgBar(response.isLoading)
        when (response) {
            is CreatePaymentUi.CreateSuccess -> {
                proceedAfterCreatePayment(response.data)
            }
            is CreatePaymentUi.InitialUi -> {
                showHidePgBar(response.isLoading)
            }
            is CreatePaymentUi.ErrorUi -> {
                if (!response.errorMsg.isNullOrEmpty()) {
                    showToast(this, response.errorMsg)
                } else {
                    showToast(this, response.errors.getTextMsg())
                }
            }
        }
    }

    private fun parseCouponValidation(response: ValidateCouponUi) {
        showHidePgBar(response.isLoading)
        when (response) {
            is ValidateCouponUi.InitialUi -> {
                showHidePgBar(response.isLoading)
            }
            is ValidateCouponUi.ErrorUi -> {
                if (response.errors.isNotEmpty()) {
                    showToast(this, response.errors.getTextMsg())
                } else {
                    showToast(this, response.message)
                }
                paymentActivityReqData?.couponDiscount = 0.0
                couponHolder?.setCouponData(null, null)
                paymentBillingHolder?.setUpData(paymentActivityReqData)
                binding?.paymentPageBottomBar?.totalPayableAmountTv?.text = ProductUtils.roundTo1DecimalPlaces(paymentActivityReqData?.totalAmount)
            }
            is ValidateCouponUi.AppliedSuccessfully -> {
                updateCouponFiled(response.data, response.isCouponRemoved)
            }
        }
    }

    override fun didTapOnPaymentMethods(paymentMethod: PaymentMethod) {
        this.paymentMethod = paymentMethod
    }

    private fun processAfterSuccessfulPayment(paymentData: PaymentData?) {
        if (paymentData == null) {
            return
        }
        val razorPayOrderId = paymentData.orderId
        val signatureId = paymentData.signature
        val paymentId = paymentData.paymentId
        val payment = Payment(razorPayOrderId, signatureId, paymentId)
        paymentViewModel.verifyPaymentWithServer(payment)
    }


    private fun initializePayments(id: String, activity: Activity) {
        if (checkout == null) {
            checkout = Checkout()
        }
        setUpAttributes(id, activity)
    }

    private fun setUpAttributes(id: String, activity: Activity) {
        try {
            val options = JSONObject()
            options.put("name", "Zust")
            options.put("description", "Payment")
            options.put("image", R.drawable.grinzy_black)
            options.put("order_id", id)
            options.put("theme.color", getColor(R.color.new_material_primary))
            options.put("prefill.email", "info@grinzy.in")
            options.put("prefill.contact", paymentViewModel.getUserMobileNumber())
            if (paymentMethod != null) {
                options.put("prefill.method", paymentMethod?.key!!)
            }
            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 2)
            options.put("retry", retryObj)
            checkout?.open(activity, options)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        processAfterSuccessfulPayment(p1)
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        showToast(this, "Payment failed")
    }

    private fun startCouponListingActivity() {
        val couponListingIntent = Intent(this, CouponListingActivity::class.java)
        launchCouponActivity.launch(couponListingIntent)
    }

    private val launchCouponActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result == null) {
            return@registerForActivityResult
        }
        if (result.data == null) {
            return@registerForActivityResult
        }
        if (result.data?.hasExtra(CouponListingActivity.INTENT_KEY_COUPON_VALUE) == true) {
            val couponValue = result.data?.getStringExtra(CouponListingActivity.INTENT_KEY_COUPON_VALUE)
            showHidePgBar(true)
            validateCouponFromIntent(couponValue, false)
        }
    }

    private fun validateCouponFromIntent(couponValue: String?, removeCoupon: Boolean) {
        if (couponValue == null) {
            return
        }
        paymentViewModel.validateCoupon(ApplyCouponReqBody(couponValue, paymentActivityReqData?.orderId, removeCoupon))
    }

    private fun showHidePgBar(canShow: Boolean) {
        if (canShow) {
            binding?.lottieProgressBar?.apply {
                visibility = View.VISIBLE
                playAnimation()
            }
        } else {
            binding?.lottieProgressBar?.apply {
                visibility = View.GONE
                pauseAnimation()
            }
        }
    }

    private fun proceedAfterCreatePayment(createPaymentResponseModel: CreatePaymentDataModel) {
        if (createPaymentResponseModel.success == true) {
            if (createPaymentResponseModel.order == null) {
                moveToOrderDetailsPage()
            } else {
                initializePayments(createPaymentResponseModel.order.rzrPayOrderId, this@PaymentActivity)
            }
        }
    }

    private fun updateCouponFiled(appliedCouponResponse: AppliedCouponData, isCouponRemoved: Boolean) {
        if (isCouponRemoved) {
            paymentActivityReqData?.couponDiscount = 0.0
            couponHolder?.setCouponData(null, null)
            paymentBillingHolder?.setUpData(paymentActivityReqData)
            paymentViewModel.appliedCoupon = ""
            binding?.paymentPageBottomBar?.totalPayableAmountTv?.text = ProductUtils.roundTo1DecimalPlaces(paymentActivityReqData?.totalAmount)
        } else {
            val discountAmount: Double = appliedCouponResponse.discountAmount
            paymentActivityReqData?.couponDiscount = discountAmount
            couponHolder?.setCouponData(discountAmount, appliedCouponResponse.couponCode)
            paymentBillingHolder?.setUpData(paymentActivityReqData)
            paymentViewModel.appliedCoupon = appliedCouponResponse.couponCode
            binding?.paymentPageBottomBar?.totalPayableAmountTv?.text = ProductUtils.roundTo1DecimalPlaces(paymentActivityReqData?.totalAmount)
        }
    }

    private fun moveToOrderDetailsPage() {
        if (paymentActivityReqData?.orderId != null) {
            paymentViewModel.clearCartItems()
            val orderDetailIntent: Intent? by lazy { Intent(this, OrderDetailActivity::class.java) }
            orderDetailIntent?.putExtra(ORDER_ID, paymentActivityReqData?.orderId)
            orderDetailIntent?.putExtra(JUST_ORDERED, true)
            orderDetailIntent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(orderDetailIntent)
            finish()
        }
    }

    private fun setUpAddressDetails(address: Address?) {
        if (address == null) {
            return
        }
        deliveryAddressHolder?.showDeliveryAddress(address.convertAsStringText())
    }

    private fun setUpShimmer(canShowShimmer: Boolean) {
        if (canShowShimmer) {
            binding?.paymentMethodContainer?.paymentModeRecyclerView?.visibility = View.GONE
            binding?.paymentMethodContainer?.shimmerLayout?.shimmerLayout?.apply {
                visibility = View.VISIBLE
                this.startShimmer()
            }
        } else {
            binding?.paymentMethodContainer?.paymentModeRecyclerView?.visibility = View.VISIBLE
            binding?.paymentMethodContainer?.shimmerLayout?.shimmerLayout?.apply {
                visibility = View.GONE
                this.stopShimmer()
            }
        }
    }

    private fun setUpTitleBar() {
        binding?.navTvNumberOfItemsInCart?.text= buildString {
            append(cartItemCount)
            append(" items")
        }
        binding?.navigationBackArrow?.setOnClickListener {
            finish()
        }
    }



    companion object {
        const val PAYMENT_MODEL_KEY = "payment_model_key"
        const val TOTAL_ITEMS_IN_CART = "items_in_cart"
    }

}