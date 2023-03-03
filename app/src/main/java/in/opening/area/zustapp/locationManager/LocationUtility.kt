package `in`.opening.area.zustapp.locationManager

import `in`.opening.area.zustapp.locationManager.models.CustomLocationModel
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.model.LatLng
import java.util.*

open class LocationUtility {
    companion object {
        fun checkLocationPermission(activity: AppCompatActivity): Boolean {
            return hasPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }

        private fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        fun getAddress(latLng: LatLng, context: Context): CustomLocationModel {
            val customLocationModel = CustomLocationModel()
            val geocoder = Geocoder(context, Locale.getDefault())
            val address: Address?
            var fulladdress = ""
            val addresses: List<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            customLocationModel.lat = latLng.latitude
            customLocationModel.lng = latLng.longitude
            if (!addresses.isNullOrEmpty()) {
                address = addresses[0]
                fulladdress = address.getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex
                val city = address.locality;
                val state = address.adminArea;
                val country = address.countryName;
                val postalCode = address.postalCode;
                val knownName = address.featureName;
                customLocationModel.addressLine = fulladdress
                customLocationModel.city = city
                customLocationModel.country = country
                customLocationModel.knownName = knownName
                customLocationModel.pinCode = postalCode
                customLocationModel.state = state
                return customLocationModel
            }
            return customLocationModel
        }
    }
}