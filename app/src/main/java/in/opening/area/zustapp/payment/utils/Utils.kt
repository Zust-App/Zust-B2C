package `in`.opening.area.zustapp.payment.utils

import `in`.opening.area.zustapp.payment.models.PaymentMethod
import `in`.opening.area.zustapp.payment.models.TimeSlot
import androidx.recyclerview.widget.DiffUtil

val paymentMethodDiff = object : DiffUtil.ItemCallback<PaymentMethod>() {
    override fun areItemsTheSame(oldItem: PaymentMethod, newItem: PaymentMethod): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(oldItem: PaymentMethod, newItem: PaymentMethod): Boolean {
        return oldItem == newItem
    }
}

val timeSlotDiff = object : DiffUtil.ItemCallback<TimeSlot>() {
    override fun areItemsTheSame(oldItem: TimeSlot, newItem: TimeSlot): Boolean {
        return oldItem.timeSlot == newItem.timeSlot
    }

    override fun areContentsTheSame(oldItem: TimeSlot, newItem: TimeSlot): Boolean {
        return oldItem == newItem
    }

}
