package `in`.opening.area.zustapp.payment.holder

import `in`.opening.area.zustapp.databinding.PaymentDetailSavingsItemBinding
import `in`.opening.area.zustapp.payment.models.PaymentActivityReqData
import android.widget.TextView

class TimingSavingHolder(val binding: PaymentDetailSavingsItemBinding?) {
    private val freeDeliveryTag: TextView? = binding?.freeDeliveryTag
    private val expectedDeliveryTime: TextView? = binding?.expectedDeliveryTimeText

    private fun setFreeDelivery(freeDeliveryText: String) {
        freeDeliveryTag?.text = freeDeliveryText
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
        if (paymentActivityReqData?.isFreeDelivery == true) {
            setFreeDelivery("Yay! Eligible for free delivery")
        } else {
            setFreeDelivery("Free delivery above ₹99")
        }
    }
}