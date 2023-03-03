package `in`.opening.area.zustapp.payment.viewholder

import `in`.opening.area.zustapp.databinding.ShortDescriptionProductsBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProductShortDescViewHolder(private val binding: ShortDescriptionProductsBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindData() {
        binding.shortDescItemName.text = "Mango"
        binding.itemQuanAndCount.text = "500 X 2"
        binding.totalAmountPerItem.text = "1230"
    }

    companion object {
        fun from(parent: ViewGroup): ProductShortDescViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ShortDescriptionProductsBinding.inflate(layoutInflater, parent, false)
            return ProductShortDescViewHolder(binding)
        }
    }

}