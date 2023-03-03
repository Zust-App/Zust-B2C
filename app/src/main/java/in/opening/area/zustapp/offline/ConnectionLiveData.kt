package `in`.opening.area.zustapp.offline

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData

class ConnectionLiveData(val context: Context) : LiveData<Boolean>() {

    private var connectivityManager: ConnectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    override fun onActive() {
        super.onActive()
        updateConnection()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> connectivityManager.registerDefaultNetworkCallback(getConnectivityManagerCallback())
            else -> lollipopNetworkAvailableRequest()
        }
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
    }

    private fun lollipopNetworkAvailableRequest() {
        val builder = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_VPN)
        connectivityManager.registerNetworkCallback(builder.build(), getConnectivityManagerCallback())
    }

    private fun getConnectivityManagerCallback(): ConnectivityManager.NetworkCallback {

        connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                postValue(true)
            }

            override fun onLost(network: Network) {
                postValue(false)
            }
        }
        return connectivityManagerCallback
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateConnection()
        }
    }

    @Suppress("DEPRECATION")
    private fun updateConnection() {
        val activeNetwork: android.net.NetworkInfo? = connectivityManager.activeNetworkInfo
        postValue(activeNetwork?.isConnected == true)
    }
}