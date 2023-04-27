package `in`.opening.area.zustapp.address.v2

import `in`.opening.area.zustapp.address.CustomMaterialDialog
import `in`.opening.area.zustapp.address.model.SearchAddressModel
import `in`.opening.area.zustapp.databinding.FragmentAddressGoogleMapBinding
import `in`.opening.area.zustapp.uiModels.AddressValidationUi
import `in`.opening.area.zustapp.uiModels.LocationAddressUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.AddressViewModel
import android.content.Context
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject

//@AndroidEntryPoint
//class AddressGoogleMapFragment : Fragment(), OnMapReadyCallback {
//    private var binding: FragmentAddressGoogleMapBinding? = null
//    private val viewModel: AddressViewModel by activityViewModels()
//    private var googleMap: GoogleMap? = null
//    private var listener: SearchPlaceAndLocationListeners? = null
//    private var latLng: LatLng? = null
//    private val customMaterialDialog: CustomMaterialDialog by lazy {
//        CustomMaterialDialog {
//
//        }
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is SearchPlaceAndLocationListeners) {
//            listener = context
//        }
//    }
//
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        binding = FragmentAddressGoogleMapBinding.inflate(layoutInflater)
//        return binding?.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setUpClickListeners()
//        setUpObservers()
//        binding?.mapView?.onCreate(savedInstanceState)
//        binding?.mapView?.post { binding?.mapView?.getMapAsync(this) }
//        binding?.showHideProgressBar(true)
//        if (viewModel.searchedAddress.value is LocationAddressUi.Success) {
//            with((viewModel.searchedAddress.value as LocationAddressUi.Success).data) {
//                setLocationText(this?.getAddressLine(0))
//            }
//        }
//    }
//
//    private fun setLocationText(location: String?) {
//        if (location == null) {
//            return
//        }
//        binding?.locationTextView?.text = location
//    }
//
//
//    private fun setUpObservers() {
//        lifecycleScope.launchWhenStarted {
//            launch {
//                viewModel.validLatLngUiState.collect {
//                    parseLatLngValidation(it)
//                }
//            }
//            launch {
//                viewModel.searchedAddress.collect {
//                    parseLocationAddress(it)
//                }
//            }
//        }
//    }
//
//
//    private fun parseLatLngValidation(response: AddressValidationUi) {
//        binding?.showHideProgressBar(response.isLoading)
//        when (response) {
//            is AddressValidationUi.AddressValidation -> {
//                validateSuccessResponse(response.data)
//            }
//            is AddressValidationUi.InitialUi -> {
//
//            }
//            else -> {
//                AppUtility.showToast(context, response.errorMessage)
//            }
//        }
//    }
//
//    //it may be current location or it may be searched location
//    //received address
//    private fun parseLocationAddress(locationAddressUi: LocationAddressUi) {
//        binding?.showHideProgressBar(locationAddressUi.isLoading)
//        when (locationAddressUi) {
//            is LocationAddressUi.Success -> {
//                locationAddressUi.data?.let {
//                    latLng = LatLng(it.latitude, it.longitude)
//                }
//                setLocationText(locationAddressUi.data?.getAddressLine(0))
//            }
//            is LocationAddressUi.InitialUi -> {
//
//            }
//            is LocationAddressUi.ErrorUi -> {
//
//            }
//        }
//    }
//
//    private fun validateSuccessResponse(jsonObject: JSONObject) {
//        if (jsonObject.has("data")) {
//            val dataJson = jsonObject.getJSONObject("data")
//            if (dataJson.has("isServiceAvailable") && dataJson.getBoolean("isServiceAvailable")) {
//                if (viewModel.searchAddressModel != null) {
//                    listener?.moveToAddressInputPage()
//                }
//            } else {
//                if (context != null) {
//                    customMaterialDialog.showDialog(context)
//                }
//            }
//        }
//    }
//
//    private fun setUpClickListeners() {
//        binding?.proceedBtn?.setOnClickListener {
//            if (viewModel.searchedAddress.value is LocationAddressUi.Success) {
//                val address = (viewModel.searchedAddress.value as LocationAddressUi.Success).data
//                if (address != null) {
//                    val addressLine = address.getAddressLine(0)
//                    val latitude = address.latitude
//                    val longitude = address.longitude
//                    val postalCode = address.postalCode
//                    val searchAddressModel = SearchAddressModel(latitude, longitude, addressLine, postalCode)
//                    viewModel.searchAddressModel = searchAddressModel
//                    binding?.showHideProgressBar(true)
//                    viewModel.checkServiceAvailBasedOnLatLng(latitude, longitude)
//                }
//            }
//        }
//
//        binding?.currentLocationIcon?.setOnClickListener {
//            binding?.showHideProgressBar(true)
//            viewModel.searchedAddress.update {
//                LocationAddressUi.InitialUi(false)
//            }
//            viewModel.validLatLngUiState.update {
//                AddressValidationUi.InitialUi(false, "")
//            }
//            listener?.didTapOnCurrentLocation()
//        }
//        binding?.closeActivityIcon?.setOnClickListener {
//           listener?.showSearchPage()
//        }
//    }
//
//
//    override fun onMapReady(p0: GoogleMap) {
//        googleMap = p0
//        if (viewModel.searchedAddress.value is LocationAddressUi.Success) {
//            val address = (viewModel.searchedAddress.value as LocationAddressUi.Success).data
//            if (address != null) {
//                latLng = LatLng(address.latitude, address.longitude)
//            }
//        }
//        if (latLng != null) {
//            moveMap(latLng!!.latitude, latLng!!.longitude)
//        }
//    }
//
//    private fun moveMap(latitude: Double, longitude: Double) {
//        binding?.showHideProgressBar(false)
//        val latLng = LatLng(latitude, longitude)
//        val markerBounds = LatLngBounds.Builder().include(latLng).build()
//        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(markerBounds, 100)
//        googleMap?.moveCamera(cameraUpdate)
//        googleMap?.addMarker(MarkerOptions()
//            .position(latLng)
//            .draggable(true)
//            .title("Location"))?.showInfoWindow()
//        googleMap?.animateCamera(CameraUpdateFactory.zoomTo(15f))
//        googleMap?.uiSettings?.isZoomControlsEnabled = true
//    }
//
//    override fun onStart() {
//        super.onStart()
//        binding?.mapView?.onStart()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        binding?.mapView?.onResume()
//    }
//
//
//    override fun onDetach() {
//        super.onDetach()
//        listener = null
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        binding = null
//    }
//
//
//    companion object {
//        fun newInstance(): AddressGoogleMapFragment {
//            val args = Bundle()
//            val fragment = AddressGoogleMapFragment()
//            fragment.arguments = args
//            return fragment
//        }
//    }
//}
//
//
//private fun FragmentAddressGoogleMapBinding.showHideProgressBar(canShow: Boolean) {
//    if (canShow) {
//        progressBar.visibility = View.VISIBLE
//        dataProgressBar.visibility = View.VISIBLE
//    } else {
//        progressBar.visibility = View.GONE
//        dataProgressBar.visibility = View.GONE
//
//    }
//}