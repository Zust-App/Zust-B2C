package `in`.opening.area.zustapp.address

import `in`.opening.area.zustapp.BaseActivityWithLocation
import `in`.opening.area.zustapp.address.AddNewAddressActivity.Companion.ADDRESS_KEY
import `in`.opening.area.zustapp.address.model.SearchAddressModel
import `in`.opening.area.zustapp.databinding.FragmentAddressGoogleMapBinding
import `in`.opening.area.zustapp.uiModels.AddressValidationUi
import `in`.opening.area.zustapp.uiModels.LocationAddressUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.AddressViewModel
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import java.util.Locale

class GoogleMapsAddressActivity : BaseActivityWithLocation(), OnMapReadyCallback {
    private var binding: FragmentAddressGoogleMapBinding? = null
    private val viewModel: AddressViewModel by viewModels()
    private var googleMap: GoogleMap? = null
    private var latLng: LatLng? = null
    private val customMaterialDialog: CustomMaterialDialog by lazy {
        CustomMaterialDialog {

        }
    }

    private val coder by lazy { Geocoder(this, Locale.getDefault()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAddressGoogleMapBinding.inflate(layoutInflater)
        setContentView(binding?.root!!)
        getDataFromIntent()
        setUpClickListeners()
        setUpObservers()
        binding?.mapView?.onCreate(savedInstanceState)
        binding?.mapView?.post { binding?.mapView?.getMapAsync(this) }
        binding?.showHideProgressBar(true)
        if (viewModel.searchedAddress.value is LocationAddressUi.Success) {
            with((viewModel.searchedAddress.value as LocationAddressUi.Success).data) {
                setLocationText(this?.getAddressLine(0))
            }
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
            launch {
                viewModel.validLatLngUiState.collect {
                    parseLatLngValidation(it)
                }
            }
            launch {
                viewModel.searchedAddress.collect {
                    parseLocationAddress(it)
                }
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
                AppUtility.showToast(this, response.errorMessage)
            }
        }
    }

    private fun parseLocationAddress(locationAddressUi: LocationAddressUi) {
        binding?.showHideProgressBar(locationAddressUi.isLoading)
        when (locationAddressUi) {
            is LocationAddressUi.Success -> {
                locationAddressUi.data?.let {
                    latLng = LatLng(it.latitude, it.longitude)
                }
                setLocationText(locationAddressUi.data?.getAddressLine(0))
            }

            is LocationAddressUi.InitialUi -> {

            }

            is LocationAddressUi.ErrorUi -> {

            }
        }
    }

    private fun validateSuccessResponse(jsonObject: JSONObject) {
        if (jsonObject.has("data")) {
            val dataJson = jsonObject.getJSONObject("data")
            if (dataJson.has("isDeliverablePinCode") && dataJson.getBoolean("isDeliverablePinCode")) {
                if (viewModel.searchAddressModel != null) {
                    startAddressInputActivity()
                }
            } else {
                customMaterialDialog.showDialog(this)
            }
        }
    }

    private fun setUpClickListeners() {
        binding?.proceedBtn?.setOnClickListener {
            if (viewModel.searchedAddress.value is LocationAddressUi.Success) {
                val address = (viewModel.searchedAddress.value as LocationAddressUi.Success).data
                if (address != null) {
                    val addressLine = address.getAddressLine(0)
                    val latitude = address.latitude
                    val longitude = address.longitude
                    val postalCode = address.postalCode
                    val searchAddressModel = SearchAddressModel(latitude, longitude, addressLine, postalCode)
                    viewModel.searchAddressModel = searchAddressModel
                    binding?.showHideProgressBar(true)
                    viewModel.checkServiceAvailBasedOnLatLng(latitude, longitude, postalCode)
                }
            }
        }

        binding?.currentLocationIcon?.setOnClickListener {
            binding?.showHideProgressBar(true)
            clickOnUseCurrentLocation()
        }
        binding?.closeActivityIcon?.setOnClickListener {
            finish()
        }
    }


    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        if (viewModel.searchedAddress.value is LocationAddressUi.Success) {
            val address = (viewModel.searchedAddress.value as LocationAddressUi.Success).data
            if (address != null) {
                latLng = LatLng(address.latitude, address.longitude)
            }
        }
        if (latLng != null) {
            moveMap(latLng!!.latitude, latLng!!.longitude)
        }
    }

    private fun moveMap(latitude: Double, longitude: Double) {
        binding?.showHideProgressBar(false)
        val latLng = LatLng(latitude, longitude)
        val markerBounds = LatLngBounds.Builder().include(latLng).build()
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(markerBounds, 100)
        googleMap?.moveCamera(cameraUpdate)
        googleMap?.addMarker(MarkerOptions()
            .position(latLng)
            .draggable(true)
            .title("Location"))?.showInfoWindow()
        googleMap?.animateCamera(CameraUpdateFactory.zoomTo(15f))
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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun getDataFromIntent() {
        if (intent.hasExtra(ADDRESS_KEY)) {
            val address: Address? = intent.getParcelableExtra(ADDRESS_KEY)
            if (address == null) {
                finish()
                return
            }
            viewModel.searchedAddress.update {
                LocationAddressUi.Success(false, address)
            }
        } else {
            finish()
        }
    }


    private fun startAddressInputActivity() {
        if (viewModel.searchAddressModel != null) {
            val addressInputIntent = Intent(this, AddNewAddressActivity::class.java)
            addressInputIntent.putExtra(AddNewAddressActivity.ADDRESS_EDIT_KEY, viewModel.searchAddressModel)
            startActivityForResult(addressInputIntent, REQ_CODE_NEW_ADDRESS)
        } else {
            AppUtility.showToast(this, "Something went wrong")
        }
    }

    override fun receiveLocation(location: Location?) {
        if (location != null) {
            viewModel.searchedAddress.update {
                LocationAddressUi.Success(false, getAddressFromLatLng(location.latitude, location.longitude))
            }
        }
    }

    override fun didReceiveError(error: String?) {
        super.didReceiveError(error)
        viewModel.searchedAddress.update {
            LocationAddressUi.ErrorUi(false)
        }
    }


    private fun getAddressFromLatLng(latitude: Double, longitude: Double): Address? {
        try {
            val addresses = coder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                return addresses[0]
            }
        } catch (e: IOException) {
            AppUtility.showToast(this, "Location Error")
        }
        return null
    }

}

private fun FragmentAddressGoogleMapBinding.showHideProgressBar(canShow: Boolean) {
    if (canShow) {
        progressBar.visibility = View.VISIBLE
        dataProgressBar.visibility = View.VISIBLE
    } else {
        progressBar.visibility = View.GONE
        dataProgressBar.visibility = View.GONE

    }
}