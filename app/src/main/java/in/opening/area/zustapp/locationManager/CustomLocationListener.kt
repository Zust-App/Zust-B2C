package `in`.opening.area.zustapp.locationManager

import android.location.Location

interface CustomLocationListener {
    fun receiveLocation(location: Location?)
    fun didReceiveException(e:Exception?)
    fun didReceiveError(error: String?)
}