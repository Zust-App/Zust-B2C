package `in`.opening.area.zustapp.address.v2

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.address.AddressSearchAndGoogleMapActivity
import `in`.opening.area.zustapp.address.AddressSearchAndGoogleMapActivity.Companion.ADDRESS_TEXT
import `in`.opening.area.zustapp.address.compose.SearchAddressMainUi
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.address.model.SearchPlacesDataModel
import `in`.opening.area.zustapp.address.utils.AddressUtils
import `in`.opening.area.zustapp.viewmodels.AddressViewModel
import android.content.Context
import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddressSearchFragment : Fragment() {
    private val viewModel: AddressViewModel by activityViewModels()

    private var listener: SearchPlaceAndLocationListeners? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                SearchAddressMainUi(viewModel, modifier = Modifier.background(color = colorResource(id = R.color.screen_surface_color))) {
                    processSelectedSearchAddress(it)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SearchPlaceAndLocationListeners) {
            listener = context
        }
    }

    private fun processWhenReceiveLatLng(address: Address?) {
        if (address == null) {
            return
        }
        if (context != null) {
            listener?.didReceivedSearchResult(address)
        }
    }

    private fun processSelectedSearchAddress(searchPlacesDataModel: SearchPlacesDataModel) {
        val address = AddressUtils.getLocationFromAddress(searchPlacesDataModel.description, context)
        processWhenReceiveLatLng(address)
    }

    companion object {
        @JvmStatic
        fun newInstance(): AddressSearchFragment {
            val args = Bundle()
            val fragment = AddressSearchFragment()
            fragment.arguments = args
            return fragment
        }
    }

}

interface SearchPlaceAndLocationListeners {
    fun didTapOnSavedAddress(savedAddress: AddressItem)
    fun didReceivedSearchResult(address: Address?)
}

interface AddressBtmSheetCallback {
    fun clickOnUseCurrentLocation() {}
    fun didTapOnSearchAddress() {}
    fun didTapOnAddAddress(savedAddress: AddressItem)
    fun didTapOnAddNewAddress() {}
}