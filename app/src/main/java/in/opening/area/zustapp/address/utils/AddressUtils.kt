package `in`.opening.area.zustapp.address.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng

class AddressUtils {
    companion object {
         fun getLocationFromAddress(strAddress: String?, context: Context?): Address? {
            if (strAddress == null) {
                return null
            }
            if (context == null) {
                return null
            }
            val coder = Geocoder(context)
            val addressList: List<Address>?
            val locationLatLng: LatLng?
            try {
                addressList = coder.getFromLocationName(strAddress, 5)
                if (addressList == null) {
                    return null
                }
               return addressList[0]
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return null
        }
    }
}