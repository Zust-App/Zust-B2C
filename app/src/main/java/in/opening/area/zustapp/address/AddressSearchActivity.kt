package `in`.opening.area.zustapp.address

import `in`.opening.area.zustapp.BaseActivityWithLocation
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.address.AddNewAddressActivity.Companion.ADDRESS_KEY
import `in`.opening.area.zustapp.address.compose.SearchAddressMainUi
import `in`.opening.area.zustapp.address.model.SearchPlacesDataModel
import `in`.opening.area.zustapp.address.utils.AddressUtils
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.uiModels.LocationAddressUi
import `in`.opening.area.zustapp.viewmodels.AddressViewModel
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.update


@AndroidEntryPoint
class AddressSearchActivity : BaseActivityWithLocation() {

    private val addressViewModel: AddressViewModel by viewModels()
    private val coder by lazy { Geocoder(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                topBar = {
                    ComposeCustomTopAppBar(modifier = Modifier,
                        titleText = "Search Location",
                        subTitleText = null, null) {
                        finish()
                    }
                }
            ) { padding ->
                SearchAddressMainUi(addressViewModel,
                    modifier = Modifier
                        .padding(padding)
                        .background(color = colorResource(id = R.color.white)), {
                        processSelectedSearchAddress(it)
                    }) {
                    handleCurrentLocationClick(it)
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }


    override fun didReceiveError(error: String?) {
        super.didReceiveError(error)
        addressViewModel.searchedAddress.update {
            LocationAddressUi.ErrorUi(false, error)
        }
    }

    override fun receiveLocation(location: Location?) {
        if (location != null) {
            addressViewModel.searchedAddress.update {
                LocationAddressUi.Success(false, getAddressFromLatLng(location.latitude, location.longitude))
            }
        }
    }

    private fun startAddressInputActivity(address: Address) {
        val addressGoogleMapActivity = Intent(this, GoogleMapsAddressActivity::class.java)
        addressGoogleMapActivity.putExtra(ADDRESS_KEY, address)
        startActivityForResult(addressGoogleMapActivity, REQ_CODE_GOOGLE_MAP)
    }

    private fun getAddressFromLatLng(latitude: Double, longitude: Double): Address? {
        val addresses = coder.getFromLocation(latitude, longitude, 1)
        return addresses!![0]
    }

    private var onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    private fun processSelectedSearchAddress(searchPlacesDataModel: SearchPlacesDataModel) {
        val address = AddressUtils.getLocationFromAddress(searchPlacesDataModel.description, this)
        processWhenReceiveLatLng(address)
    }

    private fun handleCurrentLocationClick(data: Any?) {
        when (data) {
            is Address -> {
                addressViewModel.searchedAddress.update {
                    LocationAddressUi.InitialUi(false)
                }
                processWhenReceiveLatLng(data)
            }
            null -> {
                addressViewModel.searchedAddress.update {
                    LocationAddressUi.InitialUi(true)
                }
                clickOnUseCurrentLocation()
            }
        }
    }

    private fun processWhenReceiveLatLng(address: Address?) {
        if (address == null) {
            return
        }
        startAddressInputActivity(address)
    }
}