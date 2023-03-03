package `in`.opening.area.zustapp.payment.holder

import `in`.opening.area.zustapp.payment.models.PaymentActivityReqData
import `in`.opening.area.zustapp.databinding.PaymentPageSavingsBinding
import android.view.View
import android.widget.TextView

class TimingSavingHolder(val binding: PaymentPageSavingsBinding?) {
    private val freeDeliveryTag: TextView? = binding?.freeDeliveryTag
    val savingsAmount: TextView? = binding?.totalSavingsTextView
    private val expectedDeliveryTime: TextView? = binding?.expectedDeliveryTimeText

    private fun setFreeDelivery(isFreeDelivery: Boolean) {
        if (isFreeDelivery) {
            freeDeliveryTag?.visibility = View.VISIBLE
        } else {
            freeDeliveryTag?.visibility = View.GONE
        }
    }

    private fun setExpectedDeliveryTime(time: String?) {
        expectedDeliveryTime?.text = time
    }

    internal fun setData(paymentActivityReqData: PaymentActivityReqData?) {
        if (!paymentActivityReqData?.expectedDelivery.isNullOrEmpty()) {
            setExpectedDeliveryTime(paymentActivityReqData?.expectedDelivery)
        } else {
            setExpectedDeliveryTime("Delivery in 45 Mins")
        }
        if (!paymentActivityReqData?.isFreeDelivery.isNullOrEmpty()) {
            val freeDelivery = paymentActivityReqData!!.isFreeDelivery
            if (freeDelivery?.contains("ADD", ignoreCase = true) == true) {
                setFreeDelivery(true)
            }else{
                setFreeDelivery(false)
            }
        }
    }
}