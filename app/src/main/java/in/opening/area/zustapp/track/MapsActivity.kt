package `in`.opening.area.zustapp.track

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.track.manager.RealtimeTrackingManager
import `in`.opening.area.zustapp.tracking.track.model.TrackingDataModel
import `in`.opening.area.zustapp.tracking.track.utils.AnimationUtils
import `in`.opening.area.zustapp.tracking.track.utils.MapUtils
import `in`.opening.area.zustapp.tracking.track.utils.PermissionUtils
import `in`.opening.area.zustapp.viewmodels.TrackingViewModel
import `in`.opening.area.zustapp.databinding.ActivityMapsBinding
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject


@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, RealtimeTrackingManager.RealtimeTrackingInterface {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }

    private lateinit var googleMap: GoogleMap
    private var pickUpLatLng: LatLng? = null
    private var dropLatLng: LatLng? = null
    private var currentLocLatLng: LatLng? = null

    private var destinationMarker: Marker? = null
    private var originMarker: Marker? = null
    private var deliveryPartnerMarker: Marker? = null

    private var greyPolyLine: Polyline? = null
    private var blackPolyline: Polyline? = null

    private var binding: ActivityMapsBinding? = null
    private val trackingViewModel: TrackingViewModel by viewModels()
    private val realtimeTrackingManager: RealtimeTrackingManager by lazy { RealtimeTrackingManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        pickUpLatLng = LatLng(25.64386934953221, 85.10279034602493)
        val mapFragment = supportFragmentManager.findFragmentById(`in`.opening.area.zustapp.R.id.trackingMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setUpObservers()
        realtimeTrackingManager.setOrderId("123", "Patna")
    }

    private fun moveCamera(latLng: LatLng?) {
        if (latLng != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        }
    }

    private fun animateCamera(latLng: LatLng?) {
        if (latLng != null) {
            val cameraPosition = CameraPosition.Builder().target(latLng).zoom(15.5f).build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }

    private fun addCarMarkerAndGet(latLng: LatLng): Marker {
        val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(MapUtils.getDestinationBitmap())
        return googleMap.addMarker(MarkerOptions().position(latLng).flat(true).icon(bitmapDescriptor))!!
    }

    private fun addOriginDestinationMarkerAndGet(latLng: LatLng): Marker {
        val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(MapUtils.getDestinationBitmap())
        return googleMap.addMarker(MarkerOptions().position(latLng).flat(true).icon(bitmapDescriptor))!!
    }

    private fun setUpObservers() {
        trackingViewModel.directionsResponse.observe(this) {
            try {
                val jsonResponse = JSONObject(it)
                val routes = jsonResponse.getJSONArray("routes")
                routes.getJSONObject(0).getJSONArray("legs")
                val polyline = routes.getJSONObject(0).getJSONObject("overview_polyline")
                val encodedPolylinePoints = polyline.getString("points")
                val decodedPathList = PolyUtil.decode(encodedPolylinePoints)
                if (decodedPathList != null) {
                    showPath(decodedPathList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        trackingViewModel.wayPointsResponse.observe(this) {
            try {
                val jsonResponse = JSONObject(it)
                val routes = jsonResponse.getJSONArray("routes")
                routes.getJSONObject(0).getJSONArray("legs")
                val polyline = routes.getJSONObject(0).getJSONObject("overview_polyline")
                val encodedPolylinePoints = polyline.getString("points")
                val decodedPathList = PolyUtil.decode(encodedPolylinePoints)
                if (decodedPathList != null) {
                    showPath(decodedPathList)
                }
                updateDeliveryPartnerLocation(currentLocLatLng!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun setUpLocationListener() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                        this, getString(R.string.please_allow_location_permission), Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        realtimeTrackingManager.startObservingPartnerLocation()
    }

    private fun showPath(latLngList: List<LatLng>) {
        val builder = LatLngBounds.Builder()
        for (latLng in latLngList) {
            builder.include(latLng)
        }
        val bounds = builder.build()
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 2))
        val polylineOptions = PolylineOptions()
        polylineOptions.color(Color.BLUE)
        polylineOptions.width(10f)
        polylineOptions.addAll(latLngList)
        greyPolyLine = googleMap.addPolyline(polylineOptions)

        val blackPolylineOptions = PolylineOptions()
        blackPolylineOptions.width(10f)
        blackPolylineOptions.color(Color.BLUE)
        blackPolyline = googleMap.addPolyline(blackPolylineOptions)

        originMarker = addOriginDestinationMarkerAndGet(latLngList[0])
        originMarker?.setAnchor(0.5f, 0.5f)
        destinationMarker = addOriginDestinationMarkerAndGet(latLngList[latLngList.size - 1])
        destinationMarker?.setAnchor(0.5f, 0.5f)

        val polylineAnimator = AnimationUtils.polyLineAnimator()
        polylineAnimator.addUpdateListener { valueAnimator ->
            val percentValue = (valueAnimator.animatedValue as Int)
            val index = (greyPolyLine?.points!!.size * (percentValue / 100.0f)).toInt()
            blackPolyline?.points = greyPolyLine?.points!!.subList(0, index)
        }
        polylineAnimator.start()
    }

    private fun updateDeliveryPartnerLocation(latLng: LatLng) {
        if (deliveryPartnerMarker == null) {
            deliveryPartnerMarker = addCarMarkerAndGet(latLng)
        }
        val valueAnimator = AnimationUtils.cabAnimator()
        valueAnimator.addUpdateListener { va ->
            deliveryPartnerMarker?.setAnchor(0.5f, 0.5f)
        }
        valueAnimator.start()
    }

    override fun getUpdatedLocation(trackingDataModel: TrackingDataModel?) {
        try {
            if (trackingDataModel?.currentLng != null && trackingDataModel.currentLat != null) {
                val currentLat = trackingDataModel.currentLat.toDouble()
                val currentLng = trackingDataModel.currentLng.toDouble()

                val startLat = trackingDataModel.targetLat!!.toDouble()
                val startLng = trackingDataModel.targetLng!!.toDouble()

                dropLatLng = LatLng(startLat, startLng)

                currentLocLatLng = LatLng(currentLat, currentLng)
                moveCamera(currentLocLatLng)
                animateCamera(currentLocLatLng)
                trackingViewModel.getDirectionWithWayPoints(pickUpLatLng!!, dropLatLng!!, currentLocLatLng!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}