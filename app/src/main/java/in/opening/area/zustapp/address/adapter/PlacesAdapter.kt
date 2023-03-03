package `in`.opening.area.zustapp.address.adapter


import `in`.opening.area.zustapp.address.model.SearchPlacesDataModel
import `in`.opening.area.zustapp.address.viewholder.PlacesViewHolder
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PlacesAdapter(private val callback: (SearchPlacesDataModel) -> Unit) : RecyclerView.Adapter<PlacesViewHolder>() {
    private val list: ArrayList<SearchPlacesDataModel> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesViewHolder {
        return PlacesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        holder.bindData(list[position].description,list[position].mainText)
        holder.binding.root.setOnClickListener {
            callback.invoke(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun submitList(it: ArrayList<SearchPlacesDataModel>) {
        list.clear()
        list.addAll(it)
        notifyDataSetChanged()
    }
}