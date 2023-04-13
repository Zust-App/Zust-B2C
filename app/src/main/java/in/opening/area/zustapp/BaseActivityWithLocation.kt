package `in`.opening.area.zustapp

import `in`.opening.area.zustapp.HomeLandingActivity.Companion.ACTION_PERMISSION_GPS
import `in`.opening.area.zustapp.HomeLandingActivity.Companion.MY_PERMISSIONS_REQUEST_LOCATION
import `in`.opening.area.zustapp.address.AddressBottomSheetV2
import `in`.opening.area.zustapp.address.AddressBtmSheetCallback
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.extensions.showBottomSheetIsNotPresent
import `in`.opening.area.zustapp.locationManager.CustomLocationListener
import `in`.opening.area.zustapp.locationManager.CustomLocationManager
import `in`.opening.area.zustapp.locationManager.LocationUtility
import `in`.opening.area.zustapp.utility.AppUtility
import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivityWithLocation : AppCompatActivity(), CustomLocationListener, AddressBtmSheetCallback {
    private val locationManager: CustomLocationManager by lazy { CustomLocationManager(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.initiate()
                } else {
                    resolveLocationPermission()
                }
            }
        }
    }

    private fun resolveLocationPermission() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", this.packageName, null)))
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTION_PERMISSION_GPS) {
            locationManager.initiate()
        } else {
            AppUtility.showToast(this, getString(R.string.please_allow_location_permission))
        }
    }

    override fun receiveLocation(location: Location?) {
        if (location != null) {

        } else {
            Log.e("TAG", "LOC NULL")
            AppUtility.showToast(this, getString(R.string.please_retry_current_address))
        }
    }

    override fun didReceiveException(e: Exception?) {
        didReceiveError(e?.message)
        if (e is ResolvableApiException) {
            try {
                e.startResolutionForResult(this, ACTION_PERMISSION_GPS)
            } catch (sendEx: IntentSender.SendIntentException) {
                didReceiveError(sendEx.message)
            }
        }
    }

    override fun didReceiveError(error: String?) {

    }

    private fun requestLocationAccess() {
        if (LocationUtility.checkLocationPermission(this)) {
            locationManager.initiate()
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
        return
    }


    override fun clickOnUseCurrentLocation() {
        requestLocationAccess()
    }

    override fun didTapOnSearchAddress() {

    }

    override fun didTapOnAddAddress(savedAddress: AddressItem) {

    }

}