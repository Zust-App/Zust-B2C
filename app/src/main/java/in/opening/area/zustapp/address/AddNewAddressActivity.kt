package `in`.opening.area.zustapp.address

import `in`.opening.area.zustapp.address.compose.*
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.address.model.CustomAddress
import `in`.opening.area.zustapp.address.model.SearchAddressModel
import `in`.opening.area.zustapp.locationManager.models.CustomLocationModel
import `in`.opening.area.zustapp.uiModels.CurrentLocationUi
import `in`.opening.area.zustapp.viewmodels.AddressViewModel
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.update


@AndroidEntryPoint
class AddNewAddressActivity : AppCompatActivity() {
    private val viewModel: AddressViewModel by viewModels()
    private var searchAddressModel: SearchAddressModel? = null
    private val customLocationModel = CustomLocationModel()
    private var intentSource: String? = null


    companion object {
        const val KEY_SELECTED_ADDRESS_ID = "selected_address_id"
        const val ADDRESS_EDIT_KEY = "address_edit_key"
        const val ADDRESS_KEY = "address_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromIntent()
        setContent {
            Scaffold {
                NewAddressAddContainer(it)
            }
        }
    }

    private fun getDataFromIntent() {
        if (intent.hasExtra(ADDRESS_EDIT_KEY)) {
            searchAddressModel = intent.getParcelableExtra(ADDRESS_EDIT_KEY)
        }
        if (intent.hasExtra(GoogleMapsAddressActivity.SOURCE)) {
            intentSource = intent.getStringExtra(GoogleMapsAddressActivity.SOURCE)
        }
    }

    @Composable
    fun NewAddressAddContainer(paddingValues: PaddingValues) {
        if (searchAddressModel == null) {
            AddNewAddressUi(modifier = Modifier.padding(paddingValues), viewModel) {
                handleAction(it)
            }
        }
        if (searchAddressModel != null) {
            customLocationModel.addressLine = searchAddressModel!!.addressDesc
            customLocationModel.pinCode = searchAddressModel!!.postalCode
            customLocationModel.lat = searchAddressModel!!.lat
            customLocationModel.lng = searchAddressModel!!.longitude
            AddNewAddressUiV2(modifier = Modifier.padding(paddingValues), viewModel, customLocationModel) {
                handleAction(it)
            }
        }
    }

    private fun handleAction(data: Any) {
        if (data is CustomAddress) {
            viewModel.saveLatestAddress(data.convertToAddressItem())
            val intent = Intent()
            intent.putExtra(KEY_SELECTED_ADDRESS_ID, data.id)
            if (intentSource != null) {
                intent.putExtra(GoogleMapsAddressActivity.SOURCE,intentSource)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
        if (data is String) {
            when (data) {
                KEY_SAVED_ADDRESS -> {

                }

                KEY_NEW_ADDRESS -> {

                }

                CURRENT_LOCATION -> {
                    viewModel.currentLocationUiState.update {
                        CurrentLocationUi.InitialUi(true)
                    }
                }

                FINISH_PAGE -> {
                    finish()
                }
            }
        }
        if (data is AddressItem) {
            viewModel.saveLatestAddress(data.convertToAddress())
            val intent = Intent()
            intent.putExtra(KEY_SELECTED_ADDRESS_ID, data.id)
            setResult(RESULT_OK, intent)
            finish()
        }
    }


}
