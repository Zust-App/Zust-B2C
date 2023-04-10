package `in`.opening.area.zustapp.address

import `in`.opening.area.zustapp.BaseActivityWithLocation
import `in`.opening.area.zustapp.address.compose.*
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.address.model.CustomAddress
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.locationManager.LocationUtility
import `in`.opening.area.zustapp.uiModels.CurrentLocationUi
import `in`.opening.area.zustapp.viewmodels.AddressViewModel
import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.update


@AndroidEntryPoint
class AddressAddSelectActivity : BaseActivityWithLocation() {
    private val viewModel: AddressViewModel by viewModels()

    companion object {
        const val KEY_SELECTED_ADDRESS_ID = "selected_address_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold {
                AddressAddSelectContainer(it)
                viewModel.getAllAddress()
            }
        }
    }

    @Composable
    fun AddressAddSelectContainer(paddingValues: PaddingValues) {
        AddNewAddressUi(modifier = Modifier, viewModel) {
            handleAction(it)
        }
    }

    private fun handleAction(data: Any) {
        if (data is CustomAddress) {
            viewModel.saveLatestAddress(data.convertToAddressItem())
            val intent = Intent()
            intent.putExtra(KEY_SELECTED_ADDRESS_ID, data.id)
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
                    clickOnUseCurrentLocation()
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

    override fun receiveLocation(location: Location?) {
        super.receiveLocation(location)
        if (location != null) {
            val customLocationModel = LocationUtility.getAddress(latLng = LatLng(location.latitude, location.longitude), context = this)
            viewModel.currentLocationUiState.update {
                CurrentLocationUi.ReceivedCurrentLocation(false, data = customLocationModel)
            }
        } else {
            viewModel.currentLocationUiState.update {
                CurrentLocationUi.ReceivedCurrentLocation(false, data = null)
            }
        }
    }

    override fun didReceiveError(error: String?) {
        super.didReceiveError(error)
        viewModel.currentLocationUiState.update {
            CurrentLocationUi.ErrorState(false, message = error)
        }
    }

}
