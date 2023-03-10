package `in`.opening.area.zustapp.address

import `in`.opening.area.zustapp.databinding.ActivityAddressInputBinding
import `in`.opening.area.zustapp.address.model.SaveAddressPostModel
import `in`.opening.area.zustapp.address.model.SearchAddressModel
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.uiModels.SaveUserAddressUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.ShowToast
import `in`.opening.area.zustapp.viewmodels.AddressViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressInputActivity : AppCompatActivity(), ShowToast {
    private var binding: ActivityAddressInputBinding? = null
    private val viewModel: AddressViewModel by viewModels()
    private var searchAddressModel: SearchAddressModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressInputBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        getDataFromIntent()
        setUpClickListeners()
        showUserAddress()
        attachObservers()
    }

    private fun getDataFromIntent() {
        if (intent.hasExtra(ADDRESS_EDIT_KEY)) {
            searchAddressModel = intent.getParcelableExtra(ADDRESS_EDIT_KEY)
        }
    }

    private fun showUserAddress() {
        if (searchAddressModel == null) {
            return
        }
        binding?.userAddressTextView?.text = searchAddressModel?.addressDesc
        if (searchAddressModel?.postalCode == null) {
            binding?.pinCodeTagText?.visibility = View.VISIBLE
            binding?.pinCodeEditText?.visibility = View.VISIBLE
            binding?.postalCodeTextView?.text = LOCATION
        } else {
            binding?.postalCodeTextView?.text = (buildString {
                append("PinCode ")
                append(searchAddressModel?.postalCode)
            })
        }
    }

    private fun setUpClickListeners() {
        binding?.saveAddressButton?.setOnClickListener {
            val saveAddressPostModel = validateInputFields()
            if (saveAddressPostModel != null) {
                saveAddressPostModel.longitude = searchAddressModel?.longitude
                saveAddressPostModel.latitude = searchAddressModel?.lat
                saveAddressPostModel.pinCode = searchAddressModel?.postalCode ?: "800013"
                saveAddressPostModel.description = searchAddressModel?.addressDesc
                showHidePgBar(true)
                viewModel.saveAddressUserAddress(saveAddressPostModel)
            }
        }
        binding?.navBackIcon?.setOnClickListener {
            finish()
        }
    }

    private fun attachObservers() {
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.saveUserAddressUiState.collect {
                    parseSaveAddress(it)
                }
            }
        }
    }

    private fun validateInputFields(): SaveAddressPostModel? {
        if (binding?.houseNoEditText?.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please enter house number", Toast.LENGTH_SHORT).show()
            return null
        }
        if (binding?.landmarkEditText?.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please enter landmark number", Toast.LENGTH_SHORT).show()
            return null
        }
        if (binding?.houseNoEditText?.text?.length!! < 2) {
            Toast.makeText(this, "Please enter house number", Toast.LENGTH_SHORT).show()
            return null
        }
        if (binding?.landmarkEditText?.text?.length!! < 3) {
            Toast.makeText(this, "Please enter landmark number", Toast.LENGTH_SHORT).show()
            return null
        }
        if (searchAddressModel?.postalCode == null) {
            if (binding?.pinCodeEditText?.text.isNullOrEmpty()) {
                AppUtility.showToast(this, "Please enter Pincode")
                return null
            }
            if (binding?.pinCodeEditText?.text?.length != 6) {
                AppUtility.showToast(this, "Please enter valid pincode")
                return null
            }
            val pinCode = binding?.pinCodeEditText?.text
            try {
                if (pinCode == null) {
                    Toast.makeText(this, "Please enter valid pincode", Toast.LENGTH_SHORT).show()
                    return null
                }
                pinCode.toString().toInt()
            } catch (e: Exception) {
                Toast.makeText(this, "Please enter valid pincode", Toast.LENGTH_SHORT).show()
                return null
            }
        }
        val saveAddressPostModel = SaveAddressPostModel()
        saveAddressPostModel.houseNumberAndFloor = binding?.houseNoEditText?.text?.toString()
        saveAddressPostModel.landmark = binding?.landmarkEditText?.text?.toString() ?: ""
        if (searchAddressModel?.postalCode == null) {
            saveAddressPostModel.pinCode = binding?.pinCodeEditText?.text?.toString() ?: ""
        } else {
            saveAddressPostModel.pinCode = searchAddressModel?.postalCode
        }
        return saveAddressPostModel

    }

    private fun showHidePgBar(canShow: Boolean) {
        if (canShow) {
            binding?.pgBar?.visibility = View.VISIBLE
        } else {
            binding?.pgBar?.visibility = View.GONE
        }
    }

    private fun parseSaveAddress(response: SaveUserAddressUi) {
        showHidePgBar(response.isLoading)
        when (response) {
            is SaveUserAddressUi.SaveAddressUi -> {
                if (response.data?.id == -1 || response.data?.id == null) {
                    showToast(this, "Something went wrong")
                } else {
                    showToast(this, "Address Saved successfully")
                }
            }
            is SaveUserAddressUi.InitialUi -> {
                showHidePgBar(response.isLoading)
            }
            is SaveUserAddressUi.ErrorUi -> {
                showToast(this, response.errors.getTextMsg())
            }
        }
    }

    companion object {
        const val LOCATION = "Location"
        const val ADDRESS_EDIT_KEY = "edit_address_key"
    }
}