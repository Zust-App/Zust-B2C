package `in`.opening.area.zustapp.address.v2

import `in`.opening.area.zustapp.address.CustomMaterialDialog
import `in`.opening.area.zustapp.address.model.SearchAddressModel
import `in`.opening.area.zustapp.databinding.FragmentAddressGoogleMapBinding
import `in`.opening.area.zustapp.uiModels.AddressValidationUi
import `in`.opening.area.zustapp.uiModels.LocationAddressUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.AddressViewModel
import android.content.Context
import android.location.Address
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
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
    private val viewModel: AddressViewModel by activityViewModels()
    private var googleMap: GoogleMap? = null
    private var address: Address? = null
    private var latLng: LatLng? = null
    private var listener: SearchPlaceAndLocationListeners? = null

    private val customMaterialDialog: CustomMaterialDialog by lazy {
        CustomMaterialDialog {

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SearchPlaceAndLocationListeners) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddressGoogleMapBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        setUpObservers()
        binding?.mapView?.onCreate(savedInstanceState)
        binding?.mapView?.getMapAsync(this)
        binding?.showHideLocationData(false)
        if (viewModel.searchedAddress.value is LocationAddressUi.Success) {
            address = (viewModel.searchedAddress.value as LocationAddressUi.Success).data
        }
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
        binding?.showHideProgressBar(response.isLoading)
        when (response) {
            is AddressValidationUi.AddressValidation -> {
                validateSuccessResponse(response.data)
            }
            is AddressValidationUi.InitialUi -> {

            }
            else -> {
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
                        binding?.showHideProgressBar(true)
                        viewModel.checkServiceAvailBasedOnLatLng(latitude, longitude)
                    } else {

                    }
                }
            }
        }
        binding?.currentLocationIcon?.setOnClickListener {
            listener?.didTapOnCurrentLocation()
        }
    }


    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        val boundsIndia = LatLngBounds(
            LatLng(8.0686, 68.8212),  // Southwest bound
            LatLng(37.0902, 97.3447) // Northeast bound
        )
        googleMap?.setLatLngBoundsForCameraTarget(boundsIndia)
        if (address != null) {
            latLng = LatLng(address!!.latitude, address!!.longitude)
            if (latLng?.latitude != null) {
                moveMap(latLng?.latitude!!, latLng?.longitude!!)
            }
        }
    }

    private fun moveMap(latitude: Double, longitude: Double) {
        binding?.showHideLocationData(true)
        val latLng = LatLng(latitude, longitude)
        googleMap?.addMarker(MarkerOptions()
            .position(latLng)
            .draggable(true)
            .title("Location"))?.showInfoWindow()
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.zoomTo(16f))
        googleMap?.uiSettings?.isZoomControlsEnabled = true
    }


    override fun onStart() {
        super.onStart()
        binding?.mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding?.mapView?.onResume()
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    companion object {
        fun newInstance(): AddressGoogleMapFragment {
            val args = Bundle()
            val fragment = AddressGoogleMapFragment()
            fragment.arguments = args
            return fragment
        }
    }
}


private fun FragmentAddressGoogleMapBinding?.showHideLocationData(canShow: Boolean) {
    if (this == null) {
        return
    }
    if (canShow) {
        progressBar.visibility = View.GONE
        shimmerFrameLayout.visibility = View.GONE
        locationDetailContainer.visibility = View.VISIBLE
    } else {
        progressBar.visibility = View.VISIBLE
        shimmerFrameLayout.visibility = View.VISIBLE
        locationDetailContainer.visibility = View.GONE
    }
}

private fun FragmentAddressGoogleMapBinding.showHideProgressBar(canShow: Boolean) {
    if (canShow) {
        progressBar.visibility = View.VISIBLE
    } else {
        progressBar.visibility = View.GONE
    }
}