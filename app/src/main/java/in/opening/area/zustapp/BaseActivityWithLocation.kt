package `in`.opening.area.zustapp

import `in`.opening.area.zustapp.address.AddNewAddressActivity
import `in`.opening.area.zustapp.address.v2.AddressBtmSheetCallback
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.locationManager.CustomLocationListener
import `in`.opening.area.zustapp.locationManager.CustomLocationManager
import `in`.opening.area.zustapp.locationManager.LocationUtility
import `in`.opening.area.zustapp.utility.AppUtility
import android.Manifest
import android.app.Activity
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
import `in`.opening.area.zustapp.address.GoogleMapsAddressActivity
import zustbase.HomeLandingActivity

@AndroidEntryPoint
open class BaseActivityWithLocation : AppCompatActivity(), CustomLocationListener, AddressBtmSheetCallback {
    private val locationManager: CustomLocationManager by lazy { CustomLocationManager(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 109
        const val ACTION_PERMISSION_GPS = 110
        const val REQ_CODE_GOOGLE_MAP = 102
        const val REQ_CODE_NEW_ADDRESS = 103
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
            if (resultCode == Activity.RESULT_OK) {
                locationManager.initiate()
            } else {
                AppUtility.showToast(this, getString(R.string.please_allow_location_permission))
            }
        } else if (requestCode == REQ_CODE_NEW_ADDRESS) {
            if (resultCode == Activity.RESULT_OK) {
                val selectedAddressId = data?.getIntExtra(AddNewAddressActivity.KEY_SELECTED_ADDRESS_ID, -1)
                var intentSource: String? = null
                data?.let {
                    if (it.hasExtra(GoogleMapsAddressActivity.SOURCE)) {
                        intentSource = it.getStringExtra(GoogleMapsAddressActivity.SOURCE)
                    }
                }
                if (selectedAddressId != null && selectedAddressId != -1) {
                    setBackDataIfAddressAdded(selectedAddressId, intentSource)
                }
            }
        } else if (requestCode == REQ_CODE_GOOGLE_MAP) {
            if (resultCode == Activity.RESULT_OK) {
                val selectedAddressId = data?.getIntExtra(AddNewAddressActivity.KEY_SELECTED_ADDRESS_ID, -1)
                var intentSource: String? = null
                data?.let {
                    if (it.hasExtra(GoogleMapsAddressActivity.SOURCE)) {
                        intentSource = it.getStringExtra(GoogleMapsAddressActivity.SOURCE)
                    }
                }
                if (selectedAddressId != null && selectedAddressId != -1) {
                    if (intentSource != null && intentSource == GoogleMapsAddressActivity.SOURCE_LOCATION_PERMISSION) {
                        startHomeLandingIntent()
                        return
                    }
                    setBackDataIfAddressAdded(selectedAddressId, intentSource)
                }
            }
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
        if (e is ResolvableApiException) {
            try {
                e.startResolutionForResult(this, ACTION_PERMISSION_GPS)
                didReceiveError(null)
            } catch (sendEx: IntentSender.SendIntentException) {
                didReceiveError(sendEx.message)
            }
        } else {
            didReceiveError(e?.message)
        }
    }

    override fun didReceiveError(error: String?) {
        AppUtility.showToast(this, "Please allow location permission")
    }

    private fun requestLocationAccess() {
        if (LocationUtility.checkLocationPermission(this)) {
            locationManager.initiate()
        } else {
            requestLocationPermission()
        }
    }


    private fun requestLocationPermission() {
        didReceiveError(null)
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

    open fun setBackDataIfAddressAdded(selectedAddressId: Int, intentSource: String?) {
        val intent = Intent()
        intent.putExtra(AddNewAddressActivity.KEY_SELECTED_ADDRESS_ID, selectedAddressId)
        if (intentSource != null) {
            intent.putExtra(GoogleMapsAddressActivity.SOURCE, intentSource)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun startHomeLandingIntent() {
        val homeIntent = Intent(this, HomeLandingActivity::class.java)
        startActivity(homeIntent)
        finish()
    }

}