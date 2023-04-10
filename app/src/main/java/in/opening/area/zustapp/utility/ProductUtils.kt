package `in`.opening.area.zustapp.utility

import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class ProductUtils @Inject constructor(private val sharedPrefManager: SharedPrefManager) {
    companion object {

        fun getNumberDisplayValue(floatValue: Double?): String {
            if (floatValue == null) {
                return ""
            }
            return if (floatValue == floatValue.toInt().toDouble()) floatValue.toInt().toString() else floatValue.toString()
        }

        fun roundTo1DecimalPlaces(doubleValue: Double?): String {
            if (doubleValue==null){
                return ""
            }
            return if (doubleValue == doubleValue.toInt().toDouble()) doubleValue.toInt().toString() else BigDecimal(doubleValue).setScale(1, RoundingMode.HALF_UP).toDouble().toString()
        }

    }
}