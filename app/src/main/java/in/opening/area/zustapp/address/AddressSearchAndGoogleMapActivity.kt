package `in`.opening.area.zustapp.address

import `in`.opening.area.zustapp.BaseActivityWithLocation
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.address.AddNewAddressActivity.Companion.ADDRESS_EDIT_KEY
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.address.model.SearchAddressModel
import `in`.opening.area.zustapp.address.v2.AddressGoogleMapFragment
import `in`.opening.area.zustapp.address.v2.AddressSearchFragment
import `in`.opening.area.zustapp.address.v2.SearchPlaceAndLocationListeners
import `in`.opening.area.zustapp.databinding.ActivityAddressSearchBinding
import `in`.opening.area.zustapp.uiModels.LocationAddressUi
import `in`.opening.area.zustapp.utility.ShowToast
import `in`.opening.area.zustapp.viewmodels.AddressViewModel
import `in`.opening.area.zustapp.viewmodels.OrderSummaryViewModel
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.update


@AndroidEntryPoint
class AddressSearchAndGoogleMapActivity : BaseActivityWithLocation(), SearchPlaceAndLocationListeners {
    private var binding: ActivityAddressSearchBinding? = null

    private val addressViewModel: AddressViewModel by viewModels()

    private val coder by lazy { Geocoder(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressSearchBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        showFragments(1)
    }

    private fun showFragments(type: Int) {
        if (type == 1) {
            val addressSearchFragment = AddressSearchFragment.newInstance()
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, addressSearchFragment).commit()
        }
        if (type == 2) {
            val googleMapFragment = AddressGoogleMapFragment.newInstance()
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, googleMapFragment).commit()
        }
    }

    private fun startAddressInputActivity(searchAddressModel: SearchAddressModel) {
        val inputAddressActivity = Intent(this, AddNewAddressActivity::class.java)
        inputAddressActivity.putExtra(ADDRESS_EDIT_KEY, searchAddressModel)
        startActivity(inputAddressActivity)
    }


    override fun didTapOnSavedAddress(savedAddress: AddressItem) {

    }

    override fun didTapOnCurrentLocation() {
        clickOnUseCurrentLocation()
    }

    override fun didReceivedSearchResult(address: Address?) {
        showFragments(2)
    }


    override fun didReceiveException(e: Exception?) {
        super.didReceiveException(e)
    }

    override fun receiveLocation(location: Location?) {
        if (location != null) {
            addressViewModel.searchedAddress.update {
                LocationAddressUi.Success(false, getAddressFromLatLng(location.latitude, location.longitude))
            }
        }
    }

    private fun getAddressFromLatLng(latitude: Double, longitude: Double): Address? {
        val addresses = coder.getFromLocation(latitude, longitude, 1)
        return addresses!![0]
    }


}