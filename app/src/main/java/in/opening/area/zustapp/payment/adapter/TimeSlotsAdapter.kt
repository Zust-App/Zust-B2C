package `in`.opening.area.zustapp.payment.adapter

import `in`.opening.area.zustapp.payment.models.TimeSlot
import `in`.opening.area.zustapp.payment.utils.timeSlotDiff
import `in`.opening.area.zustapp.payment.viewholder.AvailTimeSlotViewHolder
import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class TimeSlotsAdapter : RecyclerView.Adapter<AvailTimeSlotViewHolder>() {
    private val timeSlotDiffer = AsyncListDiffer(this, timeSlotDiff)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailTimeSlotViewHolder {
        return AvailTimeSlotViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AvailTimeSlotViewHolder, position: Int) {
        holder.bindData(timeSlotDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return timeSlotDiffer.currentList.size
    }

    fun submitList(list: List<TimeSlot>) {
        timeSlotDiffer.submitList(list)
    }

    companion object {
        private val dateFormat by lazy { SimpleDateFormat("dd-MM-yyyy") }
        private val calendar: Calendar by lazy { Calendar.getInstance() }

        fun getDesiredDateString(inputDateString: String): String {
            try {
                val d = dateFormat.parse(inputDateString)
                val calendarDate = dateFormat.format(calendar.time)
                if (d != null) {
                    val d1 = dateFormat.format(d.time)
                    return if (d1.equals(calendarDate)) {
                        "Today"
                    } else {
                        "Tomorrow"
                    }
                }
            } catch (e: Exception) {
                return inputDateString
            }
            return inputDateString
        }
    }
}