package `in`.opening.area.zustapp.payment.holder

import `in`.opening.area.zustapp.databinding.PaymentPageAddressBinding

class DeliveryAddressHolder(val binding: PaymentPageAddressBinding?) {
    private val deliveryAddressText = binding?.addressTextView

    internal fun showDeliveryAddress(deliveryAddress: String? = null) {
        deliveryAddressText?.text = deliveryAddress
    }
}