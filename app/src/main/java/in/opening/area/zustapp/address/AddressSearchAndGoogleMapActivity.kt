package `in`.opening.area.zustapp.address

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.address.AddNewAddressActivity.Companion.ADDRESS_EDIT_KEY
import `in`.opening.area.zustapp.address.model.SearchAddressModel
import `in`.opening.area.zustapp.address.v2.AddressGoogleMapFragment
import `in`.opening.area.zustapp.address.v2.AddressSearchFragment
import `in`.opening.area.zustapp.databinding.ActivityAddressSearchBinding
import `in`.opening.area.zustapp.utility.ShowToast
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddressSearchAndGoogleMapActivity : AppCompatActivity(), ShowToast {
    private var binding: ActivityAddressSearchBinding? = null

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


    companion object {
        const val PLACES_SEARCH_THRESHOLD = 3
        const val ADDRESS_TEXT = "address_text"
    }


    private fun startAddressInputActivity(searchAddressModel: SearchAddressModel) {
        val inputAddressActivity = Intent(this, AddNewAddressActivity::class.java)
        inputAddressActivity.putExtra(ADDRESS_EDIT_KEY, searchAddressModel)
        startActivity(inputAddressActivity)
    }


}