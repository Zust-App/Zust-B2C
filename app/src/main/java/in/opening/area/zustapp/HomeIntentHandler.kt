package `in`.opening.area.zustapp

import `in`.opening.area.zustapp.utility.AppUtility.Companion.showToast
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat

class HomeIntentHandler {

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getLocationPermission(context: Context) {
        val permissions = hasPermissions(context, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        if (!permissions) {
            if (context is `in`.opening.area.zustapp.HomeLandingActivity) {
                registerLocationLauncher(context)
            }
        }
    }

    private fun registerLocationLauncher(context: Context) {
        if (context is `in`.opening.area.zustapp.HomeLandingActivity) {
            val requestPermissionLauncher = context.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { result ->
                if (result) {
                    showToast(context, "Permission Granted")

                } else {
                    showToast(context, "Permission Not Granted")
                }
            }
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}