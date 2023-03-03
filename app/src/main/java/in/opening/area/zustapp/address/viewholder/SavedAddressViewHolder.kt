package `in`.opening.area.zustapp.address.viewholder

import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.address.model.getDisplayString
import `in`.opening.area.zustapp.databinding.SavedAddressItemBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SavedAddressViewHolder(private val binding: SavedAddressItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindData(addressItem: AddressItem) {
        binding.savedAddressNameTv.text = addressItem.getDisplayString()
        if (addressItem.addressType != null) {
            binding.addressLabelText.text = addressItem.addressType
        }
    }

    companion object {
        fun from(parent: ViewGroup): SavedAddressViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SavedAddressItemBinding.inflate(layoutInflater, parent, false)
            return SavedAddressViewHolder(binding)
        }
    }

}