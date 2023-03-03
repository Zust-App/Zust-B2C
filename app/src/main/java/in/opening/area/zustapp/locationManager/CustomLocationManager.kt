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
        getLocationSetting().addOnSuccessListener {
            getCurrentLocation()
        }.addOnFailureListener {
            listener.didReceiveException(e = it)
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        if (!isLocationPermissionGranted()) {
            return
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                if (p0.lastLocation?.latitude == 0.0 || p0.lastLocation?.longitude == 0.0) {
                    Log.e("ZERO", "onLocationResult: ")
                } else {
                    listener.receiveLocation(p0.lastLocation)
                    removeLocationUpdates()
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
            locationRequest?.interval = 1000
            locationRequest?.fastestInterval = 500
            locationRequest?.priority = 100
            return locationRequest!!
        }
    }

    override fun onLocationChanged(p0: Location) {
        if (p0.latitude == 0.0 || p0.longitude == 0.0) {
            Log.e("ZERO", "onLocationChanged: ")
        } else {
            listener.receiveLocation(p0)
            removeLocationUpdates()
        }
    }

    private fun removeLocationUpdates() {
        if (locationCallback != null) {
            mFusedLocationClient?.removeLocationUpdates(locationCallback!!)
        }
    }
}