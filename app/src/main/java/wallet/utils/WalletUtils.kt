package wallet.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val sdf: SimpleDateFormat by lazy { SimpleDateFormat("d MMM yy", Locale.getDefault()) }

internal fun convertToDisplayDate(timestampInMilliseconds: Long?): String {
    if (timestampInMilliseconds == null) {
        return ""
    }
    val date = Date(timestampInMilliseconds)
    return "Joined on " + sdf.format(date);
}