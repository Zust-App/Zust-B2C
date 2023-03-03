package `in`.opening.area.zustapp.address.adapter


import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.address.viewholder.SavedAddressViewHolder
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class SavedAddressAdapter(private val callback:(AddressItem)->Unit) : RecyclerView.Adapter<SavedAddressViewHolder>() {

    private val addressListDiffer = AsyncListDiffer(this, addressDiff)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedAddressViewHolder {
        return SavedAddressViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SavedAddressViewHolder, position: Int) {
        holder.bindData(addressListDiffer.currentList[position])
        holder.itemView.setOnClickListener {
            callback.invoke(addressListDiffer.currentList[position])
        }
    }

    override fun getItemCount(): Int {
        return addressListDiffer.currentList.size
    }

    fun submitList(addresses: List<AddressItem>) {
        addressListDiffer.submitList(addresses)
    }

}

private val addressDiff = object : DiffUtil.ItemCallback<AddressItem>() {
    override fun areItemsTheSame(oldItem: AddressItem, newItem: AddressItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AddressItem, newItem: AddressItem): Boolean {
        return oldItem == newItem
    }

}