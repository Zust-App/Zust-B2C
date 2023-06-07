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

    }
}