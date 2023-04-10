package `in`.opening.area.zustapp.payment.holder

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.extensions.hideViewWithAnimation
import `in`.opening.area.zustapp.extensions.showViewWithAnimation
import `in`.opening.area.zustapp.payment.models.PaymentActivityReqData
import `in`.opening.area.zustapp.utility.ProductUtils
import `in`.opening.area.zustapp.databinding.PaymentBillingDetailsBinding
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.contains

class PaymentBillingHolder(
    private val paymentBillingDetailsBinding: PaymentBillingDetailsBinding? = null,
    private val layoutInflater: LayoutInflater,
) {
    private val paymentContainer: LinearLayout? by lazy { paymentBillingDetailsBinding?.paymentContainerLL }
    private val itemPriceView: View by lazy { layoutInflater.inflate(R.layout.payment_screen_billing_item, null, false) }
    private val packagePriceView: View by lazy { layoutInflater.inflate(R.layout.payment_screen_billing_item, null, false) }
    private val deliveryPriceView: View by lazy { layoutInflater.inflate(R.layout.payment_screen_billing_item, null, false) }
    private val discountPriceView: View by lazy { layoutInflater.inflate(R.layout.payment_screen_billing_item, null, false) }
    private val deliveryPartnerTipView: View by lazy { layoutInflater.inflate(R.layout.payment_screen_billing_item, null, false) }

    private val discountTv: TextView? by lazy { discountPriceView.findViewById(R.id.paymentAmountText) }
    private val deliveryTv: TextView? by lazy { deliveryPriceView.findViewById(R.id.paymentAmountText) }
    private val itemPTv: TextView? by lazy { itemPriceView.findViewById(R.id.paymentAmountText) }
    private val packageTv: TextView? by lazy { packagePriceView.findViewById(R.id.paymentAmountText) }
    private val deliveryPartnerTipTv: TextView? by lazy { deliveryPartnerTipView.findViewById(R.id.paymentAmountText) }

    init {
        inflateItemPrice()
        inflatePackagingPrice()
        inflateDeliveryPrice()
        inflateDiscountPrice()
        inflateDeliveryPartnerTipUi()
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        paymentBillingDetailsBinding?.expandCollapseArrow?.setOnClickListener {
            if (paymentContainer?.visibility == View.VISIBLE) {
                paymentContainer?.hideViewWithAnimation(true)
                paymentBillingDetailsBinding.expandCollapseArrow.rotation =0f
            } else if (paymentContainer?.visibility == View.GONE) {
                paymentBillingDetailsBinding.expandCollapseArrow.rotation =180f
                paymentContainer?.showViewWithAnimation(true)
            }
        }
    }

    private fun inflateItemPrice() {
        itemPriceView.findViewById<TextView>(R.id.paymentNameText).text = buildString {
            append("Items price")
        }
        if (paymentContainer?.contains(itemPriceView) == false) {
            paymentContainer?.addView(itemPriceView)
        }
    }


    private fun inflateDeliveryPartnerTipUi() {
        deliveryPartnerTipView.findViewById<TextView>(R.id.paymentNameText).text = buildString {
            append("Delivery Partner Tip")
        }
        if (paymentContainer?.contains(deliveryPartnerTipView) == false) {
            paymentContainer?.addView(deliveryPartnerTipView)
        }
    }

    private fun inflatePackagingPrice() {
        packagePriceView.findViewById<TextView>(R.id.paymentNameText).text = buildString {
            append("Packaging fee")
        }
        if (paymentContainer?.contains(packagePriceView) == false) {
            paymentContainer?.addView(packagePriceView)
        }
    }

    private fun inflateDeliveryPrice() {
        deliveryPriceView.findViewById<TextView>(R.id.paymentNameText).text = buildString {
            append("Delivery fee")
        }
        if (paymentContainer?.contains(deliveryPriceView) == false) {
            paymentContainer?.addView(deliveryPriceView)
        }
    }

    private fun inflateDiscountPrice() {
        discountPriceView.findViewById<TextView>(R.id.paymentNameText).text = buildString {
            append("Coupon discount")
        }
        if (paymentContainer?.contains(discountPriceView) == false) {
            paymentContainer?.addView(discountPriceView)
        }
    }

    fun setUpData(paymentActivityReqData: PaymentActivityReqData?) {
        if (paymentActivityReqData == null) {
            return
        }
        if (paymentActivityReqData.couponDiscount == null || paymentActivityReqData.couponDiscount!! <= 0.0) {
            discountTv?.text = ""
            discountTv?.visibility = View.GONE
            discountPriceView.findViewById<TextView>(R.id.paymentNameText).visibility = View.GONE
        } else {
            discountTv?.visibility = View.VISIBLE
            discountPriceView.findViewById<TextView>(R.id.paymentNameText).visibility = View.VISIBLE
            discountTv?.text = buildString {
                append("- ₹${ProductUtils.roundTo1DecimalPlaces(paymentActivityReqData.couponDiscount)}")
            }
        }
        deliveryTv?.text = buildString {
            append("₹${ProductUtils.roundTo1DecimalPlaces(paymentActivityReqData.deliveryFee)}")
        }
        packageTv?.text = buildString {
            append("₹${ProductUtils.roundTo1DecimalPlaces(paymentActivityReqData.packagingFee)}")
        }
        deliveryPartnerTipTv?.text = buildString {
            append("₹${ProductUtils.roundTo1DecimalPlaces(paymentActivityReqData.deliveryPartnerTip)}")
        }
        itemPTv?.text = buildString {
            append("₹${ProductUtils.roundTo1DecimalPlaces(paymentActivityReqData.itemPrice)}")
        }
        var totalAmount = 0.0
        if (paymentActivityReqData.couponDiscount != null && paymentActivityReqData.couponDiscount!! >= 0.0) {
            totalAmount -= paymentActivityReqData.couponDiscount!!
        }
        if (paymentActivityReqData.deliveryFee != null) {
            totalAmount += paymentActivityReqData.deliveryFee!!
        }
        if (paymentActivityReqData.packagingFee != null) {
            totalAmount += paymentActivityReqData.packagingFee!!
        }
        if (paymentActivityReqData.itemPrice != null) {
            totalAmount += paymentActivityReqData.itemPrice!!
        }
        if (paymentActivityReqData.deliveryPartnerTip != null) {
            totalAmount += paymentActivityReqData.deliveryPartnerTip!!
        }
        paymentActivityReqData.totalAmount = totalAmount
        paymentBillingDetailsBinding?.totalPriceTitleTag?.text = buildString {
            append("Total amount")
        }
        paymentBillingDetailsBinding?.totalPriceText?.text = buildString {
            append("₹${ProductUtils.roundTo1DecimalPlaces(totalAmount)}")
        }
    }

}