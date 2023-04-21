package `in`.opening.area.zustapp.address.v2

import `in`.opening.area.zustapp.address.CustomMaterialDialog
import `in`.opening.area.zustapp.address.model.SearchAddressModel
import `in`.opening.area.zustapp.databinding.FragmentAddressGoogleMapBinding
import `in`.opening.area.zustapp.uiModels.AddressValidationUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.SearchAddressViewModel
import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class AddressGoogleMapFragment : Fragment(), OnMapReadyCallback {
    private var binding: FragmentAddressGoogleMapBinding? = null
    private val viewModel: SearchAddressViewModel by activityViewModels()
    private var googleMap: GoogleMap? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var address: Address? = null
    private var latLng: LatLng? = null

    private val customMaterialDialog: CustomMaterialDialog by lazy {
        CustomMaterialDialog {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        setUpObservers()
        binding?.mapView?.onCreate(savedInstanceState)
        binding?.mapView?.getMapAsync(this)
        fusedLocationClient = this.activity?.let { LocationServices.getFusedLocationProviderClient(it) };
        showHideLocationData(false)
        address=viewModel.searchedAddress;
    }

    private fun processWithCurrentLocation() {
        if (googleMap != null && context!=null) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }
    }

    private fun getAddressFromLatLng(latitude: Double, longitude: Double): Address? {
        val addresses = coder.getFromLocation(latitude, longitude, 1)
        return addresses!![0]
    }

    private fun moveMap(latitude: Double, longitude: Double) {
        showHideLocationData(true)
        val latLng = LatLng(latitude, longitude)
        googleMap?.addMarker(MarkerOptions()
            .position(latLng)
            .draggable(true)
            .title("Location"))?.showInfoWindow()
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.zoomTo(18f))
        googleMap?.uiSettings?.isZoomControlsEnabled = true
    }

    private val coder by lazy { Geocoder(requireContext()) }



    private fun showHideLocationData(canShow: Boolean) {
        if (canShow) {
            binding?.progressBar?.visibility = View.GONE
            binding?.shimmerFrameLayout?.visibility = View.GONE
            binding?.locationDetailContainer?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.VISIBLE
            binding?.shimmerFrameLayout?.visibility = View.VISIBLE
            binding?.locationDetailContainer?.visibility = View.GONE
        }
    }

    private fun showHideProgressBar(canShow: Boolean) {
        if (canShow) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        binding?.mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding?.mapView?.onResume()
    }

    private fun setLocationText(location: String?) {
        if (location == null) {
            return
        }
        binding?.locationTextView?.text = location
    }


    private fun setUpObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.validLatLngUiState.collect {
                parseLatLngValidation(it)
            }
        }
    }

    private fun parseLatLngValidation(response: AddressValidationUi) {
        when (response) {
            is AddressValidationUi.AddressValidation -> {
                showHideProgressBar(response.isLoading)
                validateSuccessResponse(response.data)
            }
            is AddressValidationUi.InitialUi -> {
                showHideProgressBar(response.isLoading)
            }
            else -> {
                showHideProgressBar(response.isLoading)
                AppUtility.showToast(context, response.errorMessage)
            }
        }
    }

    private fun validateSuccessResponse(jsonObject: JSONObject) {
        if (jsonObject.has("data")) {
            val dataJson = jsonObject.getJSONObject("data")
            if (dataJson.has("isServiceAvailable") && dataJson.getBoolean("isServiceAvailable")) {
                if (viewModel.searchAddressModel != null) {
//                    startAddressInputActivity(viewModel.searchAddressModel!!)
                }
            } else {
                customMaterialDialog.showDialog(requireContext())
            }
        }
    }

    private fun setUpClickListeners() {
        binding?.proceedBtn?.setOnClickListener {
            if (address != null) {
                if (latLng != null) {
                    val addressLine = address?.getAddressLine(0)
                    val latitude = latLng?.latitude
                    val longitude = latLng?.longitude
                    val postalCode = address?.postalCode
                    val searchAddressModel = SearchAddressModel(latitude, longitude, addressLine, postalCode)
                    viewModel.searchAddressModel = searchAddressModel
                    if (latitude != null && longitude != null) {
                        showHideProgressBar(true)
                        viewModel.checkServiceAvailBasedOnLatLng(latitude, longitude)
                    } else {
                        //start address input activity
                    }
                }
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        val boundsIndia = LatLngBounds(LatLng(23.63936, 68.14712), LatLng(28.20453, 97.34466))
        googleMap?.setLatLngBoundsForCameraTarget(boundsIndia)
        if (address != null) {
            latLng = LatLng(address!!.latitude, address!!.longitude)
            if (latLng?.latitude != null) {
                moveMap(latLng?.latitude!!, latLng?.longitude!!)
            }
        }
    }
    companion object{
        fun newInstance():AddressGoogleMapFragment {
            val args = Bundle()
            val fragment =AddressGoogleMapFragment ()
            fragment.arguments = args
            return fragment
        }
    }


}