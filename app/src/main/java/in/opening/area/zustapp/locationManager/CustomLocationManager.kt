package `in`.opening.area.zustapp.locationManager

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import `in`.opening.area.zustapp.utility.AppUtility
import java.util.concurrent.TimeUnit

class CustomLocationManager(private val activity: AppCompatActivity, private val listener: CustomLocationListener) :
    LocationListener {

    private var locationRequest: LocationRequest? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null

    private fun getLocationSetting(): Task<LocationSettingsResponse> {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(prepareLocationRequest())
        val client = LocationServices.getSettingsClient(activity)
        return client.checkLocationSettings(builder.build())
    }

    fun initiate() {
        listener.showHideProgressBar(true)
        getLocationSetting().addOnSuccessListener {
            getCurrentLocation()
        }.addOnFailureListener {
            listener.didReceiveException(e = it)
            listener.showHideProgressBar(false)
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        if (!isLocationPermissionGranted()) {
            AppUtility.showToast(activity, "Please allow location permission")
            listener.showHideProgressBar(false)
            return
        }
        removeLocationUpdates()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                if (p0.lastLocation?.latitude == 0.0 || p0.lastLocation?.longitude == 0.0) {
                    Log.e("ZERO", "onLocationResult: ")
                } else {
                    listener.receiveLocation(p0.lastLocation)
                    removeLocationUpdates()
                    listener.showHideProgressBar(false)
                }
            }
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        val builder = LocationSettingsRequest.Builder()
        if (locationRequest != null) {
            builder.addLocationRequest(locationRequest!!)
        }
        mFusedLocationClient?.requestLocationUpdates(locationRequest!!, locationCallback!!, Looper.myLooper())

    }

    private fun prepareLocationRequest(): LocationRequest {
        return if (locationRequest != null) {
            locationRequest!!
        } else {
            locationRequest = LocationRequest.create()
            locationRequest?.interval = TimeUnit.SECONDS.toMillis(2)
            locationRequest?.fastestInterval = TimeUnit.SECONDS.toMillis(1)
            locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            return locationRequest!!
        }
    }

    override fun onLocationChanged(p0: Location) {
        if (p0.latitude == 0.0 || p0.longitude == 0.0) {
            Log.e("ZERO", "onLocationChanged: ")
        } else {
            listener.receiveLocation(p0)
            removeLocationUpdates()
            listener.showHideProgressBar(false)
        }
    }

    private fun removeLocationUpdates() {
        if (locationCallback != null) {
            mFusedLocationClient?.removeLocationUpdates(locationCallback!!)
            locationCallback = null
        }
    }
}