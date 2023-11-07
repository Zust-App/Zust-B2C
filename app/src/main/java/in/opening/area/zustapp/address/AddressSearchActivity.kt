package `in`.opening.area.zustapp.address

import android.app.Activity
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
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.locationV2.ApartmentListingBtmSheet
import `in`.opening.area.zustapp.locationV2.models.ApartmentData
import `in`.opening.area.zustapp.locationV2.models.convertToAddressModel
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.AppUtility
import kotlinx.coroutines.flow.update
import ui.colorBlack
import ui.colorWhite


@AndroidEntryPoint
class AddressSearchActivity : BaseActivityWithLocation(), ApartmentListingBtmSheet.ApartmentListingBtmSheetCallback {

    private val addressViewModel: AddressViewModel by viewModels()
    private val coder by lazy { Geocoder(this) }

    private val apartmentAddNewAddressActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedAddressId = result.data?.getIntExtra(AddNewAddressActivity.KEY_SELECTED_ADDRESS_ID, -1)
            if (selectedAddressId != null && selectedAddressId != -1) {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                topBar = {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .shadow(elevation = dp_8)) {
                        ComposeCustomTopAppBar(modifier = Modifier
                            .background(color = colorWhite, shape = RoundedCornerShape(0.dp)),
                            titleText = "Search Location",
                            color = colorBlack,
                            endImageId = null,
                            subTitleText = null) {
                            finish()
                        }
                    }
                }
            ) { padding ->
                SearchAddressMainUi(addressViewModel,
                    modifier = Modifier
                        .padding(padding)
                        .background(color = colorResource(id = R.color.white)), {
                        processSelectedSearchAddress(it)
                    }, {
                        handleCurrentLocationClick(it)
                    }) {
                    openApartmentBtmSheet()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun openApartmentBtmSheet() {
        val apartmentListingBtmSheet: ApartmentListingBtmSheet = ApartmentListingBtmSheet.newInstance()
        apartmentListingBtmSheet.show(supportFragmentManager, "apart_listing")
    }

    override fun didReceiveError(error: String?) {
        super.didReceiveError(error)
        addressViewModel.searchedAddress.update {
            LocationAddressUi.ErrorUi(false, error)
        }
    }

    override fun receiveLocation(location: Location?) {
        if (location != null) {
            val parsedLocation = getAddressFromLatLng(location.latitude, location.longitude)
            if (parsedLocation != null) {
                addressViewModel.searchedAddress.update {
                    LocationAddressUi.Success(false, parsedLocation)
                }
            }
        }
    }

    private fun startAddressInputActivity(address: Address) {
        addressViewModel.searchedAddress.update {
            LocationAddressUi.InitialUi(false)
        }
        val addressGoogleMapActivity = Intent(this, GoogleMapsAddressActivity::class.java)
        addressGoogleMapActivity.putExtra(ADDRESS_KEY, address)
        startActivityForResult(addressGoogleMapActivity, REQ_CODE_GOOGLE_MAP)
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

    override fun didTapOnListedApartment(apartmentData: ApartmentData) {
        val intent = Intent(this, AddNewAddressActivity::class.java)
        intent.putExtra(AddNewAddressActivity.ADDRESS_EDIT_KEY, apartmentData.convertToAddressModel())
        apartmentAddNewAddressActivity.launch(intent)
    }
}