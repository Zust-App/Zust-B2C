package `in`.opening.area.zustapp.payment.adapter

import `in`.opening.area.zustapp.payment.viewholder.ProductShortDescViewHolder
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProductShortDesAdapter : RecyclerView.Adapter<ProductShortDescViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductShortDescViewHolder {
        return ProductShortDescViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ProductShortDescViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10
    }

}