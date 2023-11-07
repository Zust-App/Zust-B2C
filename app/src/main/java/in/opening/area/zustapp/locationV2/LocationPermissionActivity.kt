package `in`.opening.area.zustapp.locationV2

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.BaseActivityWithLocation
import zustbase.ZustLandingActivity
import `in`.opening.area.zustapp.address.AddNewAddressActivity
import `in`.opening.area.zustapp.address.AddNewAddressActivity.Companion.ADDRESS_KEY
import `in`.opening.area.zustapp.address.AddressSearchActivity
import `in`.opening.area.zustapp.address.GoogleMapsAddressActivity
import `in`.opening.area.zustapp.address.compose.AlreadyAddedAddressListUi
import `in`.opening.area.zustapp.databinding.ActivityLocationPermissionBinding
import `in`.opening.area.zustapp.helpline.HelplineBtmSheet
import `in`.opening.area.zustapp.locationV2.dialog.LocationFetchingProgressDialog
import `in`.opening.area.zustapp.locationV2.models.ApartmentData
import `in`.opening.area.zustapp.locationV2.models.convertToAddressModel
import `in`.opening.area.zustapp.locationV2.viewModel.LocationPermissionViewModel
import `in`.opening.area.zustapp.uiModels.locations.CheckDeliverableAddressUiState
import `in`.opening.area.zustapp.utility.AppUtility
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationPermissionActivity : BaseActivityWithLocation(), ApartmentListingBtmSheet.ApartmentListingBtmSheetCallback {
    private val locationPermissionViewModel: LocationPermissionViewModel by viewModels()
    private var binding: ActivityLocationPermissionBinding? = null

    private val coder by lazy { Geocoder(this) }
    private var locationFetchingProgressDialog: LocationFetchingProgressDialog = LocationFetchingProgressDialog()

    private val startAddNewAddressActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedAddressId = result.data?.getIntExtra(AddNewAddressActivity.KEY_SELECTED_ADDRESS_ID, -1)
            if (selectedAddressId != null && selectedAddressId != -1) {
                moveToHomeActivity()
            }
        }
    }

    private val apartmentAddNewAddressActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedAddressId = result.data?.getIntExtra(AddNewAddressActivity.KEY_SELECTED_ADDRESS_ID, -1)
            if (selectedAddressId != null && selectedAddressId != -1) {
                moveToHomeActivity()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (binding == null) {
            binding = ActivityLocationPermissionBinding.inflate(layoutInflater)
        }
        binding?.let {
            setContentView(it.root)
        }
        setUpClickListeners()
        loadAlreadySavedFragment()
        attachObservers()
    }

    private fun setUpClickListeners() {
        binding?.searchLocationManuallyBtn?.setOnClickListener {
            openAddressSearchActivity()
        }
        binding?.currentLocationViewGroup?.setOnClickListener {
            clickOnUseCurrentLocation()
        }
        binding?.helpAndSupportIcon?.setOnClickListener {
            openHelplineBtmSheet()
        }
        binding?.selectApartmentTv?.setOnClickListener {
            openApartmentBtmSheet()
        }
    }

    private fun openHelplineBtmSheet() {
        val helpAndSupportBtmSheet: HelplineBtmSheet = HelplineBtmSheet.newInstance()
        helpAndSupportBtmSheet.show(supportFragmentManager, "help_support")
    }

    private fun openApartmentBtmSheet() {
        val apartmentListingBtmSheet: ApartmentListingBtmSheet = ApartmentListingBtmSheet.newInstance()
        apartmentListingBtmSheet.show(supportFragmentManager, "apart_listing")
    }

    private fun loadAlreadySavedFragment() {
        binding?.composeView?.setContent {
            AlreadyAddedAddressListUi(Modifier, locationPermissionViewModel, PaddingValues(bottom = 16.dp)) {
                locationPermissionViewModel.verifyDeliverableAddress(it)
            }
        }
        locationPermissionViewModel.getAppMetaData()
        locationPermissionViewModel.getAllAddress()
    }

    private fun openAddressSearchActivity() {
        val newAddressIntent = Intent(this, AddressSearchActivity::class.java)
        startAddNewAddressActivity.launch(newAddressIntent)
    }

    override fun didReceiveError(error: String?) {
        super.didReceiveError(error)
        hideProgressBar()
    }

    override fun receiveLocation(location: Location?) {
        hideProgressBar()
        if (location != null) {
            val parsedLocation = getAddressFromLatLng(location.latitude, location.longitude)
            if (parsedLocation != null) {
                moveToMapActivity(parsedLocation)
            }
        }
    }

    private fun getAddressFromLatLng(latitude: Double, longitude: Double): Address? {
        try {
            val addresses = coder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                return addresses[0]
            }
        } catch (e: Exception) {
            AppUtility.showToast(this, "Location not found,Retry again")
        }
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun showProgressBar() {
        if (!this.isFinishing && !this.isDestroyed) {
            locationFetchingProgressDialog.showDialog(this)
        }
    }

    private fun hideProgressBar() {
        locationFetchingProgressDialog.closeDialog()
    }

    private fun moveToHomeActivity() {
        val intent = Intent(this, ZustLandingActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun moveToMapActivity(parsedLocation: Address) {
        val addressGoogleMapActivity = Intent(this, GoogleMapsAddressActivity::class.java)
        addressGoogleMapActivity.putExtra(AddNewAddressActivity.ADDRESS_KEY, parsedLocation)
        addressGoogleMapActivity.putExtra(GoogleMapsAddressActivity.SOURCE, GoogleMapsAddressActivity.SOURCE_LOCATION_PERMISSION)
        startActivityForResult(addressGoogleMapActivity, REQ_CODE_GOOGLE_MAP)
    }

    private fun attachObservers() {
        lifecycleScope.launchWhenStarted {
            launch {
                locationPermissionViewModel.deliverableAddressUiState.collectLatest {
                    parseDeliverableAddress(it)
                }
            }
            launch {
                locationPermissionViewModel.isAppUpdateAvail.collectLatest {
                    if (it) {
                        AppUtility.openPlayStore(this@LocationPermissionActivity)
                    }
                }
            }
        }
    }

    private fun parseDeliverableAddress(checkDeliverableAddressUiState: CheckDeliverableAddressUiState) {
        if (checkDeliverableAddressUiState.isLoading) {
            showProgressBar()
        } else {
            hideProgressBar()
        }
        when (checkDeliverableAddressUiState) {
            is CheckDeliverableAddressUiState.SuccessUiState -> {
                if (checkDeliverableAddressUiState.data != null) {
                    locationPermissionViewModel.saveLatestAddress(checkDeliverableAddressUiState.data.convertToAddress())
                    moveToHomeActivity()
                } else {
                    AppUtility.showToast(this, "Something went wrong")
                }
            }

            is CheckDeliverableAddressUiState.ErrorUiState -> {
                AppUtility.showToast(this, checkDeliverableAddressUiState.message)
            }

            is CheckDeliverableAddressUiState.InitialUiState -> {
                //leave it empty
            }
        }
    }


    override fun showHideProgressBar(canShow: Boolean) {
        if (canShow) {
            binding?.currentLocationPgBar?.visibility = View.VISIBLE
            showProgressBar()
        } else {
            binding?.currentLocationPgBar?.visibility = View.GONE
            hideProgressBar()
        }
    }

    override fun didTapOnListedApartment(apartmentData: ApartmentData) {
        val intent = Intent(this, AddNewAddressActivity::class.java)
        intent.putExtra(AddNewAddressActivity.ADDRESS_EDIT_KEY, apartmentData.convertToAddressModel())
        apartmentAddNewAddressActivity.launch(intent)
    }

}