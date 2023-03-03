package `in`.opening.area.zustapp.payment.adapter

import `in`.opening.area.zustapp.payment.models.PaymentMethod
import `in`.opening.area.zustapp.payment.utils.paymentMethodDiff
import `in`.opening.area.zustapp.payment.viewholder.PaymentMethodViewHolder
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView

class PaymentMethodAdapter(private val listeners: PaymentMethodClickListeners) : RecyclerView.Adapter<PaymentMethodViewHolder>() {
    private val paymentMethodsDiffer = AsyncListDiffer(this, paymentMethodDiff)
    private var lastSelectedPosition: Int = -1
    private var isSelected: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewHolder {
        return PaymentMethodViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PaymentMethodViewHolder, position: Int) {
        holder.bindData(paymentMethodsDiffer.currentList[position])
        if (lastSelectedPosition == holder.absoluteAdapterPosition && !isSelected) {
            listeners.didTapOnPaymentMethods(paymentMethodsDiffer.currentList[position])
        }
        holder.binding.root.setOnClickListener {
            isSelected = true
            if (lastSelectedPosition != -1) {
                paymentMethodsDiffer.currentList[lastSelectedPosition].isSelected = false
                notifyItemChanged(lastSelectedPosition)
                lastSelectedPosition = holder.absoluteAdapterPosition
                paymentMethodsDiffer.currentList[lastSelectedPosition].isSelected = true
                notifyItemChanged(lastSelectedPosition)
            } else {
                lastSelectedPosition = holder.absoluteAdapterPosition
                paymentMethodsDiffer.currentList[lastSelectedPosition].isSelected = true
                notifyItemChanged(lastSelectedPosition)
            }
            listeners.didTapOnPaymentMethods(paymentMethodsDiffer.currentList[position])
        }
    }

    override fun getItemCount(): Int {
        return paymentMethodsDiffer.currentList.size
    }

    fun submitList(paymentMethods: List<PaymentMethod>) {
        if (lastSelectedPosition == -1) {
            lastSelectedPosition = 0
            paymentMethods[lastSelectedPosition].isSelected = true
        }
        paymentMethodsDiffer.submitList(paymentMethods)
    }
}

interface PaymentMethodClickListeners {
    fun didTapOnPaymentMethods(paymentMethod: PaymentMethod)
}