package `in`.opening.area.zustapp.payment.viewholder

import `in`.opening.area.zustapp.payment.adapter.TimeSlotsAdapter
import `in`.opening.area.zustapp.payment.models.TimeSlot
import `in`.opening.area.zustapp.databinding.TimeSlotsItemLayoutBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AvailTimeSlotViewHolder(val binding: TimeSlotsItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(timeSlot: TimeSlot) {
        binding.dateTimeSlotText.text = TimeSlotsAdapter.getDesiredDateString(timeSlot.date)
        binding.timeSlotTextView.text = timeSlot.displayText
    }

    companion object {
        fun from(parent: ViewGroup): AvailTimeSlotViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = TimeSlotsItemLayoutBinding.inflate(layoutInflater, parent, false)
            return AvailTimeSlotViewHolder(binding)
        }
    }
}