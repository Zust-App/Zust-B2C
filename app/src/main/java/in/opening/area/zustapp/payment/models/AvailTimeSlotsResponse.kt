package `in`.opening.area.zustapp.payment.models

import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AvailTimeSlotsResponse(
    @SerializedName("data")
    val `data`: TimeSlotData? = null,
    val message: String? = null,
    val statusCode: Int? = -1,
)

@Keep
data class TimeSlotData(
    val timeSlots: List<TimeSlot>
)

@Keep
data class TimeSlot(
    val date: String,
    val timeSlot: String,//afternoon
    val displayText: String,
)