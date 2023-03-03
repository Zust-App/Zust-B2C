package `in`.opening.area.zustapp.address.viewholder

import `in`.opening.area.zustapp.databinding.SearchPlacesItemBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PlacesViewHolder(val binding: SearchPlacesItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(description: String?, mainText: String?) {
        if (description != null) {
            binding.placesTitleTextView.text=mainText
            binding.placesDescriptionTextView.text=description
        }
    }

    companion object {
        fun from(parent: ViewGroup): PlacesViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SearchPlacesItemBinding.inflate(layoutInflater, parent, false)
            return PlacesViewHolder(binding)
        }
    }
}