package `in`.opening.area.zustapp.payment.viewholder

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.payment.models.PaymentMethod
import `in`.opening.area.zustapp.databinding.PaymentMethodItemsBinding
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class PaymentMethodViewHolder(val binding: PaymentMethodItemsBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(paymentMethod: PaymentMethod) {
        binding.paymentMethodItemText.text = paymentMethod.name
        when (paymentMethod.key) {
            "upi" -> {
                binding.paymentMethodIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.upi_pay_mode))
            }
            "debit", "credit" -> {
                binding.paymentMethodIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.card_pay_mode))
            }
            "netbanking" -> {
                binding.paymentMethodIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.net_bank_pay_mode))
            }
            "cod" -> {
                binding.paymentMethodIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.card_pay_mode))
            }
        }
        binding.paymentMethodItemRdBtn.isChecked = paymentMethod.isSelected == true
    }

    companion object {
        fun from(parent: ViewGroup): PaymentMethodViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = PaymentMethodItemsBinding.inflate(layoutInflater, parent, false)
            return PaymentMethodViewHolder(binding, parent.context)
        }
    }
}